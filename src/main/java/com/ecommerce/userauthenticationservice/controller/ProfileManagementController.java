package com.ecommerce.userauthenticationservice.controller;

import com.ecommerce.userauthenticationservice.dtos.PasswordUpdateRequestDto;
import com.ecommerce.userauthenticationservice.dtos.ProfileDto;
import com.ecommerce.userauthenticationservice.dtos.ResetPasswordRequestDto;
import com.ecommerce.userauthenticationservice.dtos.SessionInfoDto;
import com.ecommerce.userauthenticationservice.exception.PasswordDoesNotMatchException;
import com.ecommerce.userauthenticationservice.exception.UserNotFoundException;
import com.ecommerce.userauthenticationservice.models.Session;
import com.ecommerce.userauthenticationservice.models.User;

import com.ecommerce.userauthenticationservice.service.ProfileManagementServices;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ott.InvalidOneTimeTokenException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileManagementController {

    @Autowired
    private ProfileManagementServices profileManagementServices;

    @GetMapping("/{email}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable("email") String email){

        try {
            User user = profileManagementServices.getProfile(email);
            List<Session> sessionList = null;
            if( user != null ) {
                sessionList = profileManagementServices.getUserSessionInfo(user);
            }
            return new ResponseEntity<>(from(user, sessionList), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /*@PutMapping("/modifyPassword/{email}")
    public ResponseEntity<ProfileDto> modifyPassword(@PathVariable("email") String email, @RequestBody PasswordUpdateRequestDto requestDto){

        try {
            User user = profileManagementServices.resetProfilePassword(email);
            List<Session> sessionList = null;
            if( user != null ) {
                sessionList = profileManagementServices.getUserSessionInfo(user);
            }
            return new ResponseEntity<>(from(user, sessionList), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch( PasswordDoesNotMatchException passwordDoesNotMatchException ) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }*/

    @PutMapping("/modifyPhoneNumber/{email}")
    public ResponseEntity<ProfileDto> modifyPhoneNumber(@PathVariable("email") String email,  @RequestParam("newPhoneNumber") String phoneNumber){

        try {
            User user = profileManagementServices.modifyPhoneNumber(email, phoneNumber);
            List<Session> sessionList = null;
            if( user != null ) {
                sessionList = profileManagementServices.getUserSessionInfo(user);
            }
            return new ResponseEntity<>(from(user, sessionList), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/modifyName/{email}")
    public ResponseEntity<ProfileDto> modifyName(@PathVariable("email") String email, @RequestParam("newName") String name){

        try {
            User user = profileManagementServices.modifyName(email, name);
            List<Session> sessionList = null;
            if( user != null ) {
                sessionList = profileManagementServices.getUserSessionInfo(user);
            }
            return new ResponseEntity<>(from(user, sessionList), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/resetProfilePassword")
    public ResponseEntity<String> resetProfilePassword(@RequestParam String token, @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        Boolean changePassword = false;
        try {
            changePassword = profileManagementServices.resetProfilePassword(token, resetPasswordRequestDto.getNewPassword());
        } catch (UserNotFoundException | InvalidOneTimeTokenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        if( changePassword ) {
            return new ResponseEntity<>("Reset Password Successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Link Expired! Please retry forget password", HttpStatus.BAD_REQUEST);
        }
    }

    public ProfileDto from(User user, List<Session> sessionList) {
        ProfileDto profileDto = new ProfileDto();

        profileDto.setEmail(user.getEmail());
        profileDto.setName(user.getName());
        profileDto.setPassword(user.getPassword());
        profileDto.setPhoneNumber(user.getPhoneNumber());
        profileDto.setCreatedDate(user.getCreated_date());
        profileDto.setUserStatus(user.getStatus());
        profileDto.setSessionCount(sessionList.size());

        List<SessionInfoDto> sessionInfoDtoList = new ArrayList<>();
        for( Session session : sessionList) {
            SessionInfoDto sessionInfoDto = new SessionInfoDto();
            sessionInfoDto.setToken( session.getToken());
            sessionInfoDto.setStatus(session.getStatus());

            sessionInfoDtoList.add(sessionInfoDto);
        }

        profileDto.setSesInfo(sessionInfoDtoList);
        return profileDto;
    }
}
