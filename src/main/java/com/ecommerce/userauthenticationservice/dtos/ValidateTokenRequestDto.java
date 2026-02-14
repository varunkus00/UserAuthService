package com.ecommerce.userauthenticationservice.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenRequestDto {
    String token;
    Long userId;
}
