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
public class Game {
    private int id;
    private String name;
    private LocalDate released;
    private int rating;
    private double cost;
    private String description;
}