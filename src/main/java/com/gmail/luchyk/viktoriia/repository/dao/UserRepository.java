package com.gmail.luchyk.viktoriia.repository.dao;

import com.gmail.luchyk.viktoriia.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> create(User user);

    Optional<User> read(int id);

    int update(User user);

    boolean delete(int id);

    boolean existLogin(User user);

    boolean exist(User user);

    Optional<User> readByLogin(String login);
}
