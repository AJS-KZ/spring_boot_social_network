package kz.ajs.spring_boot_social_network.services.impl;

import kz.ajs.spring_boot_social_network.entities.User;
import kz.ajs.spring_boot_social_network.repositories.UserRepository;
import kz.ajs.spring_boot_social_network.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

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
}
