package com.tahidjart.users_jwt.exceptions.domain;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(String message){
        super(message);
    }
}
