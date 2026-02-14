package com.ecommerce.userauthenticationservice.service;

import com.ecommerce.userauthenticationservice.models.Session;
import com.ecommerce.userauthenticationservice.models.User;

import java.util.List;

public interface IProfileServices {

    public List<Session> getUserSessionInfo(User user);
    public User getProfile(String email);
    public Boolean resetProfilePassword(String token, String password);
    public User modifyPhoneNumber(String email, String phoneNumber);
    public User modifyName(String email, String name);

}

