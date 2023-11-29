package com.gmail.luchyk.viktoriia.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Account {
    private int id;
    private double amount;
    private String type;
}