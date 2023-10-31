package com.tahidjart.users_jwt.services.impl;

import com.tahidjart.users_jwt.domain.UserPrincipal;
import com.tahidjart.users_jwt.domain.User;
import com.tahidjart.users_jwt.exception.domain.EmailExistException;
import com.tahidjart.users_jwt.exception.domain.UserNotFoundException;
import com.tahidjart.users_jwt.exception.domain.UsernameExistException;
import com.tahidjart.users_jwt.repository.UserRepository;
import com.tahidjart.users_jwt.services.LoginAttemptService;
import com.tahidjart.users_jwt.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static com.tahidjart.users_jwt.constant.UserImpConstant.*;
import static com.tahidjart.users_jwt.enumeration.Role.*;
@Service
@Slf4j
@Transactional
@Qualifier("userDetailsService")
public class UsersServiceImpl implements UserService, UserDetailsService {
    public static final String GIVEN_DETAILS_NOT_FOUND = "User with the given details not found";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;

    @Autowired
    public UsersServiceImpl(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            log.error("User with the given details not found");
            throw new UsernameNotFoundException(GIVEN_DETAILS_NOT_FOUND + username);
        } else {
           validateLoginAttempt(user);
            user.setLastLoginDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            log.info("Returning found user:" + username);
            return userPrincipal;
        }

    }
    private void validateLoginAttempt(User user) {
        if(user.isNotLocked()) {
            if(loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setNotLocked(false);
            } else {
                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    @Override
    public User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
        User user = new User();
        user.setUserId(generateUserId());
        //String password = passwordEncoder.encode(user.getPassword());
        String password = generatePassword();
        String encodedPassword =  encodePassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl());
        log.info("User saved success");
        userRepository.save(user);
        log.info("Your password is : " + password);
        return user;
    }

    private String getTemporaryProfileImageUrl() {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath().path(IMAGE_PROFILE_TEM).toUriString();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }
    private User validateNewUsernameAndEmail(String currentUsername,
                                             String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByUsername = findByUsername(newUsername);
        User userByEmail = findUserByEmail(newEmail);
        if (StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findByUsername(currentUsername);
            if (currentUser == null) {
                throw new UserNotFoundException(GIVEN_USERNAME);
            }
            //User userByUsername = findByUsername(newUsername);
            if (userByUsername != null && !currentUser.getId()
                    .equals(userByUsername.getId())) {
                throw new UsernameExistException(GIVEN_USERNAME_TAKEN);
            }
            //User userByEmail = findUserByEmail(newEmail);
            if (userByEmail != null && !currentUser.getId().equals(userByEmail.getId())) {
                throw new EmailExistException(GIVEN_EMAIL_TAKEN);
            }
            return currentUser;
        } else {
          // User userByUsername = findByUsername(newUsername);
            if (userByUsername != null) {
                throw new UsernameExistException(GIVEN_USERNAME_TAKEN);
            }
           // User userByEmail = findUserByEmail(newEmail);
            if (userByEmail != null) {
                throw new EmailExistException(GIVEN_EMAIL_TAKEN);
            }
            return null;
        }
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
