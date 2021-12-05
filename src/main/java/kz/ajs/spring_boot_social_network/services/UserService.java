package kz.ajs.spring_boot_social_network.services;

import kz.ajs.spring_boot_social_network.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {

    User getUserByEmail(String email);
    User createUser(User user);
    User saveUser(User user);

}
