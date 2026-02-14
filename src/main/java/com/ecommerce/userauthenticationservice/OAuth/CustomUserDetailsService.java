package com.ecommerce.userauthenticationservice.OAuth;

import com.ecommerce.userauthenticationservice.models.User;
import com.ecommerce.userauthenticationservice.repos.UserAuthRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    public UserAuthRepo userAuthRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> userOp = userAuthRepo.findByEmail(email);
        if( !userOp.isPresent()){
            throw new UsernameNotFoundException("Invalid Username");
        }

        User user = userOp.get();

        return new CustomUserDetails(user);

    }
}
