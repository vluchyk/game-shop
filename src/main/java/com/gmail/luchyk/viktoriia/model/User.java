package com.gmail.luchyk.viktoriia.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    private int id;
    private String fullName;
    private String login;
    private LocalDate birthDate;
    private String password;
}