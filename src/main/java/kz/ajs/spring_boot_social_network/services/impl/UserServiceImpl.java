package kz.ajs.spring_boot_social_network.services.impl;

import kz.ajs.spring_boot_social_network.entities.Roles;
import kz.ajs.spring_boot_social_network.entities.User;
import kz.ajs.spring_boot_social_network.repositories.RoleRepository;
import kz.ajs.spring_boot_social_network.repositories.UserRepository;
import kz.ajs.spring_boot_social_network.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User myUser = userRepository.findByEmail(username);
        if(myUser!=null){

            org.springframework.security.core.userdetails.User secUser =
                    new org.springframework.security.core.userdetails.User(
                            myUser.getEmail(),
                            myUser.getPassword(),
                            myUser.getRoles()
                            );

            return secUser;

        }

        throw new UsernameNotFoundException("User Not Found!");

    }

    @Override
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public User createUser(User user){

        User old_user = userRepository.findByEmail(user.getEmail());
        if(old_user==null){
            Roles role = roleRepository.findByRole("ROLE_USER");
            if(role!=null){
                ArrayList<Roles> roles = new ArrayList<Roles>();
                roles.add(role);
                user.setRoles(roles);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                return userRepository.save(user);
            }
        }

        return null;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }
}
