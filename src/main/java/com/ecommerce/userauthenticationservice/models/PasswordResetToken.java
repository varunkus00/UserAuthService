package com.ecommerce.userauthenticationservice.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PasswordResetToken extends BaseModel{
    private String token;
    private Long userId;
    private Long expiryTime;
    private Boolean active;
}
