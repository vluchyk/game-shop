package com.gmail.luchyk.viktoriia.repository;

import com.gmail.luchyk.viktoriia.model.User;
import com.gmail.luchyk.viktoriia.repository.dao.UserRepository;

import java.sql.*;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final Connection connection;

    private static final String CREATE =
            """
                        INSERT INTO public.users(name, nickname, birthday, password)
                        VALUES(?,?,?,?);
                    """;

    private static final String READ =
            """
                        SELECT * FROM public.users
                        WHERE id = ?;
                    """;

    private static final String UPDATE =
            """
                        UPDATE public.users
                        SET name = ?, nickname = ?, birthday = ?, password = ?
                        WHERE id = ?;
                    """;

    private static final String DELETE =
            """
                        DELETE FROM public.users WHERE id = ?;
                    """;

    private static final String READ_BY_NICKNAME =
            """
                        SELECT * FROM public.users
                        WHERE nickname = ?;
                    """;

    private static final String READ_BY_NICKNAME_PASSWORD =
            """
                        SELECT * FROM public.users
                        WHERE nickname = ? and password = ?;
                    """;

    public UserRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<User> create(User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getFullName());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setDate(3, Date.valueOf(user.getBirthDate()));
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            user.setId(generatedKeys.getInt(1));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> read(int id) {
        User user = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(READ);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = User.builder()
                        .id(resultSet.getInt("id"))
                        .login(resultSet.getString("name"))
                        .fullName(resultSet.getString("nickname"))
                        .birthDate(resultSet.getDate("birthday").toLocalDate())
                        .password(resultSet.getString("password"))
                        .build();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.ofNullable(user);
    }

    @Override
    public int update(User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getFullName());
            preparedStatement.setDate(3, Date.valueOf(user.getBirthDate()));
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setInt(5, user.getId());

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setInt(1, id);

            return preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean existLogin(User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(READ_BY_NICKNAME);
            preparedStatement.setString(1, user.getLogin());

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean exist(User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(READ_BY_NICKNAME_PASSWORD);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<User> readByLogin(String login) {
        User user = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(READ_BY_NICKNAME);
            preparedStatement.setString(1, login);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = User.builder()
                        .id(resultSet.getInt("id"))
                        .login(resultSet.getString("name"))
                        .fullName(resultSet.getString("nickname"))
                        .birthDate(resultSet.getDate("birthday").toLocalDate())
                        .password(resultSet.getString("password"))
                        .build();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Optional.ofNullable(user);
    }
}
