package com.gmail.luchyk.viktoriia.repository;

import com.gmail.luchyk.viktoriia.model.Account;
import com.gmail.luchyk.viktoriia.repository.dao.AccountRepository;

import java.sql.Connection;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {
    private final Connection connection;

    private static final String CREATE =
            """
                INSERT INTO public.accounts(amount, type, user_id)
                VALUES(?,?,?);
            """;

    private static final String READ =
            """
                SELECT * FROM public.accounts WHERE id = ?;
            """;

    private static final String UPDATE =
            """
                UPDATE public.accounts
                SET amount = ?, type = ?, user_id = ?
                WHERE id = ?;
            """;

    private static final String DELETE =
            """
                DELETE FROM public.accounts WHERE id = ?;
            """;

    public AccountRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Account> create(Account account) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> read(int id) {
        return Optional.empty();
    }

    @Override
    public int update(Account account) {
        return 0;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
