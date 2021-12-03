package kz.ajs.spring_boot_social_network.config;

import kz.ajs.spring_boot_social_network.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, proxyTargetClass = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
    private final UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling().accessDeniedPage("/403");

        http.authorizeRequests().antMatchers("/", "/css/**", "/js/**").permitAll();

        http.formLogin()
                .loginPage("/login") // -> page -> login.html
                .permitAll() // -> enable all users open this page
                .usernameParameter("user_email") // -> <input type="email" name="user_input">
                .passwordParameter("user_password") // -> <input type="password" name="user_password">
                .loginProcessingUrl("/auth").permitAll() // <form th:action="@{'/auth'}" method="post">
                .failureUrl("/login?error") // page where go user if auth will fail
                .defaultSuccessUrl("/profile"); // page where will go user if auth will be accepted

        http.logout()
                .logoutUrl("/logout") // <form th:action="@{'/logout'}" method="post">
                .permitAll()
                .logoutSuccessUrl("/login");

//        http.csrf().disable();

    }
}
