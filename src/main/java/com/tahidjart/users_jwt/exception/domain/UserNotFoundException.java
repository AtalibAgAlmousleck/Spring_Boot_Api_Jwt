package com.tahidjart.users_jwt.exception.domain;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(String message){
        super(message);
    }
}
