package com.tahidjart.users_jwt.exceptions.domain;

public class UsernameExistException extends Exception{

    public UsernameExistException(String message) {
        super(message);
    }
}
