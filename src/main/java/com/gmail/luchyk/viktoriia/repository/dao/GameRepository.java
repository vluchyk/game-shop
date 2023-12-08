package com.gmail.luchyk.viktoriia.repository.dao;

import com.gmail.luchyk.viktoriia.model.Game;
import com.gmail.luchyk.viktoriia.model.User;

import java.util.List;
import java.util.Optional;

public interface GameRepository {
    Optional<Game> create(Game game);
    Optional<Game> read(int id);
    int update(Game game);
    boolean delete(int id);
    List<Game> readAll();
    List<Game> readBy(User user);
    Optional<Game> readByName(String name);
 }
