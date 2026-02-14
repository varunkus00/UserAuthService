package com.ecommerce.userauthenticationservice.controller;

import com.ecommerce.userauthenticationservice.dtos.*;
import com.ecommerce.userauthenticationservice.exception.AuthorizationTokenNotFound;
import com.ecommerce.userauthenticationservice.exception.PasswordDoesNotMatchException;
import com.ecommerce.userauthenticationservice.exception.UserAlreadyExistException;
import com.ecommerce.userauthenticationservice.exception.UserNotFoundException;
import com.ecommerce.userauthenticationservice.models.User;
import com.ecommerce.userauthenticationservice.service.IAuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserAuthController {

    @Autowired
    IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto requestDto) {
        try {
            Pair<User, String> pair= authService.login(requestDto.getEmail(), requestDto.getPassword());
            String token = pair.b;

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Bearer ", token);

            return new ResponseEntity<>(from(pair.a), headers, HttpStatus.ACCEPTED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto requestDto) {
        System.out.println("Reaching In UserAuthController SignUp");
       try {
           User newUser = new User();
           System.out.println(requestDto.getEmail());
           System.out.println(requestDto.getName());
           System.out.println(requestDto.getPassword());
           System.out.println(requestDto.getPhoneNumber());

           newUser.setEmail(requestDto.getEmail());
           newUser.setPassword(requestDto.getPassword());
           newUser.setName(requestDto.getName());
           newUser.setPhoneNumber(requestDto.getPhoneNumber());
           User user = authService.signUp(newUser);

           return new ResponseEntity<>(from(user), HttpStatus.CREATED);
       } catch (UserAlreadyExistException e) {
           return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
       }
    }

    @GetMapping("/forgotPassword/{email}")
    public String forgotPassword(@PathVariable String email) {
        String resetLink="";
        try {
            resetLink = authService.forgotPassword(email);
        } catch (UserNotFoundException e) {
            return e.getMessage();
        }

        return resetLink;

    }

    @PostMapping("/logout/{email}")
    public ResponseEntity<UserDto> logout(@PathVariable("email") String email,
                                          @RequestHeader(value = "Authorization") String authHeader) {
        try {
            if( authHeader == null || !authHeader.startsWith("Bearer ") ) {
                System.out.println(authHeader);
                System.out.println("Ohhh! Something wrong...");
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            String token =  authHeader.substring(7);
            User user = authService.logoutByEmail(token, email);

            return new ResponseEntity<>(from(user), HttpStatus.ACCEPTED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch( AuthorizationTokenNotFound e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/validateToken")
    public ResponseEntity<String> validateToken(@RequestBody ValidateTokenRequestDto requestDto) {
        System.out.println(" Inside Validate Token");
        Boolean validateResult = authService.validateToken(requestDto.getToken(), requestDto.getUserId());
        if( validateResult ) {
            return new ResponseEntity<>("Token validated successfully", HttpStatus.OK);
        } else {
            return  new ResponseEntity<>("Token invalid or expired", HttpStatus.UNAUTHORIZED);
        }
    }

    public UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
}
