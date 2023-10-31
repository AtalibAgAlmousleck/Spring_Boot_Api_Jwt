package com.tahidjart.users_jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// todo: What's Brute Force Attack
// an attacker submitting many passwords
// or passphrases with the hope
// of eventually guessing the correct password.
@SpringBootApplication
public class UsersJwtApplication {
    // QSnKMlSvUz
    public static void main(String[] args) {
        SpringApplication.run(UsersJwtApplication.class, args);
    }
}
