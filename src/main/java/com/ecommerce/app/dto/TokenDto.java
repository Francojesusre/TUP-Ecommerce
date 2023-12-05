package com.ecommerce.app.dto;

public class TokenDto {
    private String Token;

    public TokenDto(String token) {
        Token = token;
    }

    public String getToken() {
        return Token;
    }
}
