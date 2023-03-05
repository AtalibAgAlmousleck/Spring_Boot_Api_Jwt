package com.tahidjart.users_jwt.controller;

import com.tahidjart.users_jwt.domain.User;
import com.tahidjart.users_jwt.domain.UserPrincipal;
import com.tahidjart.users_jwt.exception.ExceptionHandling;
import com.tahidjart.users_jwt.exception.domain.EmailExistException;
import com.tahidjart.users_jwt.exception.domain.UserNotFoundException;
import com.tahidjart.users_jwt.exception.domain.UsernameExistException;
import com.tahidjart.users_jwt.services.UserService;
import com.tahidjart.users_jwt.utility.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tahidjart.users_jwt.constant.SecurityConstant.JWT_TOKEN_HEADER;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController extends ExceptionHandling {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/home")
    public String showUser() throws UserNotFoundException {
       // return "Application Running";
        throw new UserNotFoundException("The given user not found.");
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser,jwtHeader, HttpStatus.OK);
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));
        return httpHeaders;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, EmailExistException, UsernameExistException {
       User registerUser = userService.register(user.getFirstName(),
                user.getLastName(), user.getUsername(), user.getEmail());
       return new ResponseEntity<>(registerUser, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> listResponseEntity() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }
}
