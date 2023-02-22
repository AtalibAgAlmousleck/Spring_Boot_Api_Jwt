package com.tahidjart.users_jwt.exceptions.domain;

public class EmailExistException extends Exception{

    public EmailExistException(String message) {
        super(message);
    }
}
