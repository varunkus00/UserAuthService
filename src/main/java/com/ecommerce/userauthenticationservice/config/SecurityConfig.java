package com.ecommerce.userauthenticationservice.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.Mac;
import javax.crypto.MacSpi;
import javax.crypto.SecretKey;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.cors().disable();
//        httpSecurity.csrf().disable();
//        httpSecurity.authorizeHttpRequests(authorize->authorize.anyRequest().permitAll());
//        return httpSecurity.build();
//    }

    @Bean
    public SecretKey generateKey() {
        MacAlgorithm macAlgo = Jwts.SIG.HS256;
        SecretKey secretKey = macAlgo.key().build();
        return secretKey;
    }
}
