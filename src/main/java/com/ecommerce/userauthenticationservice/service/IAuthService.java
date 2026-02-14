package com.ecommerce.userauthenticationservice.service;

import com.ecommerce.userauthenticationservice.dtos.ResetPasswordRequestDto;
import com.ecommerce.userauthenticationservice.models.User;
import org.antlr.v4.runtime.misc.Pair;

public interface IAuthService {

    public Pair<User, String> login(String email, String password);
    public User signUp(User user);
    //public User resetProfilePassword(String email, ResetPasswordRequestDto resetPasswordRequest);
    public User logoutByEmail(String email);
    public Boolean validateToken(String token, Long userId);
    public String forgotPassword(String email);
}
