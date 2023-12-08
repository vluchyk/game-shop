package com.gmail.luchyk.viktoriia.repository;

import com.gmail.luchyk.viktoriia.model.Account;
import com.gmail.luchyk.viktoriia.model.User;
import com.gmail.luchyk.viktoriia.repository.dao.AccountRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private static final String READ_BY_USER =
            """
                SELECT * FROM public.accounts WHERE user_id = ?;
            """;

    public AccountRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Account> create(Account account) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setDouble(1, account.getAmount());
            preparedStatement.setString(2, account.getType());
            preparedStatement.setInt(3, account.getUser().getId());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            account.setId(generatedKeys.getInt(1));

        } catch (SQLException e) {
            throw new RuntimeException(e); // todo
        }
        return Optional.of(account);
    }

    @Override
    public Optional<Account> read(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(READ);

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            Optional<User> user = new UserRepositoryImpl(connection).read(resultSet.getInt("user_id"));

            Account account = Account.builder()
                    .id(resultSet.getInt("id"))
                    .amount(resultSet.getDouble("amount"))
                    .type(resultSet.getString("type"))
                    .user(user.orElse(null))
                    .build();

            return Optional.ofNullable(account);

        } catch (SQLException e) {
            throw new RuntimeException(e); // todo
        }
    }

    @Override
    public int update(Account account) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);

            preparedStatement.setDouble(1, account.getAmount());
            preparedStatement.setString(2, account.getType());
            preparedStatement.setInt(3, account.getUser().getId());
            preparedStatement.setInt(4, account.getId());

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e); // todo
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setInt(1, id);

            return preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e); // todo
        }
    }

    @Override
    public boolean exist(Account account) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(READ_BY_USER);
            preparedStatement.setInt(1, account.getUser().getId());

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e); // todo
        }
    }

    @Override
    public Optional<Account> readByUser(User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(READ_BY_USER);

            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            Account account = null;
            
            if (resultSet.next()) {
                account = Account.builder()
                        .id(resultSet.getInt("id"))
                        .amount(resultSet.getDouble("amount"))
                        .type(resultSet.getString("type"))
                        .user(user)
                        .build();
            }

            return Optional.ofNullable(account);

        } catch (SQLException e) {
            throw new RuntimeException(e); // todo
        }
    }
}
