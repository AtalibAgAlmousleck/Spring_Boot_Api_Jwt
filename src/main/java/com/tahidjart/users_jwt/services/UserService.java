package com.tahidjart.users_jwt.services;

import com.tahidjart.users_jwt.domain.User;
import com.tahidjart.users_jwt.exceptions.domain.EmailExistException;
import com.tahidjart.users_jwt.exceptions.domain.UserNotFoundException;
import com.tahidjart.users_jwt.exceptions.domain.UsernameExistException;

import java.util.List;

public interface UserService {

    User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UsernameExistException;
    List<User> getUsers();
    User findByUsername(String username);
    User findUserByEmail(String email);
}
