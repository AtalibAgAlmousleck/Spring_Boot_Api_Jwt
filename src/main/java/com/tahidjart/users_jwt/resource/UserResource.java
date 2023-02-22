package com.tahidjart.users_jwt.resource;

import com.tahidjart.users_jwt.domain.User;
import com.tahidjart.users_jwt.exceptions.ExceptionHandling;
import com.tahidjart.users_jwt.exceptions.domain.EmailExistException;
import com.tahidjart.users_jwt.exceptions.domain.UserNotFoundException;
import com.tahidjart.users_jwt.exceptions.domain.UsernameExistException;
import com.tahidjart.users_jwt.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserResource extends ExceptionHandling {

    private final UserService userService;

    @GetMapping("/home")
    public String showUser() throws UserNotFoundException {
       // return "Application Running";
        throw new UserNotFoundException("The given user not found.");
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, EmailExistException, UsernameExistException {
       User registerUser = userService.register(user.getFirstName(),
                user.getLastName(), user.getUsername(), user.getEmail());
       return new ResponseEntity<>(registerUser, HttpStatus.OK);
    }
}
