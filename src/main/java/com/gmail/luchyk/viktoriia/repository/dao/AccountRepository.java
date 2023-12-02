package com.gmail.luchyk.viktoriia.repository.dao;

import com.gmail.luchyk.viktoriia.model.Account;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> create(Account account);
    Optional<Account> read(int id);
    int update(Account account);
    boolean delete(int id);
}
