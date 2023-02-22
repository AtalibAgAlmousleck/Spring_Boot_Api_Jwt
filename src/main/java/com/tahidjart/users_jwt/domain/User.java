package com.tahidjart.users_jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_table")
public class User implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column(nullable = false, updatable = false)
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String profileImageUrl;
    private Date lastLoginDate;
    private Date lastLoginDisplay;
    private Date joinDate;
    private String role;
    private String[] authorities;
    private boolean isActive;
    private boolean isNotLocked;
}
