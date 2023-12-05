package com.ecommerce.app.dto;

public class ResponseMessageDto {
    private String message;

    public String getMessage() {
        return message;
    }

    public ResponseMessageDto(String message) {
        this.message = message;
    }
}
