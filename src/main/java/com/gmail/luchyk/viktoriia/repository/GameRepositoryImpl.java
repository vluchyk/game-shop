package com.gmail.luchyk.viktoriia.repository;

import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.model.Game;
import com.gmail.luchyk.viktoriia.model.User;
import com.gmail.luchyk.viktoriia.repository.dao.GameRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameRepositoryImpl implements GameRepository {
    private final Connection connection;

    private static final String CREATE =
            """
                        INSERT INTO public.games(name, release_date, rating, cost, description)
                        VALUES(?,?,?,?,?);
                    """;

    private static final String READ =
            """
                        SELECT * FROM public.games WHERE id = ?;
                    """;

    private static final String UPDATE =
            """
                        UPDATE public.games
                        SET name = ?, release_date = ?, rating = ?, cost = ?, description = ?
                        WHERE id = ?;
                    """;

    private static final String DELETE =
            """
                        DELETE FROM public.games
                        WHERE id = ?;
                    """;

    private static final String READ_ALL =
            """
                        SELECT * FROM public.games;
                    """;

    private static final String READ_BY_NAME =
            """
                        SELECT * FROM public.games WHERE name = ?;
                    """;

    private static final String READ_BY_USER =
            """
                    SELECT * FROM public.aux_user_game u2g
                    INNER JOIN public.games g ON g.id = u2g.game_id
                    WHERE u2g.user_id = ?;
                    """;

    public GameRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Game> create(Game game) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, game.getName());
            preparedStatement.setDate(2, Date.valueOf(game.getReleased()));
            preparedStatement.setInt(3, game.getRating());
            preparedStatement.setDouble(4, game.getCost());
            preparedStatement.setString(5, game.getDescription());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            game.setId(generatedKeys.getInt(1));

        } catch (NullPointerException e) {
            System.out.println(Message.GAME_NOT_CREATED.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.ofNullable(game);
    }

    @Override
    public Optional<Game> read(int id) {
        Game game = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(READ);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                game = Game.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .released(resultSet.getDate("release_date").toLocalDate())
                        .rating(resultSet.getInt("rating"))
                        .cost(resultSet.getDouble("cost"))
                        .description(resultSet.getString("description"))
                        .build();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Optional.ofNullable(game);
    }

    @Override
    public int update(Game game) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, game.getName());
            preparedStatement.setDate(2, Date.valueOf(game.getReleased()));
            preparedStatement.setInt(3, game.getRating());
            preparedStatement.setDouble(4, game.getCost());
            preparedStatement.setString(5, game.getDescription());
            preparedStatement.setInt(6, game.getId());

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

            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Game> readAll() {
        List<Game> games = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(READ_ALL);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                games.add(Game.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .released(resultSet.getDate("release_date").toLocalDate())
                        .rating(resultSet.getInt("rating"))
                        .cost(resultSet.getDouble("cost"))
                        .description(resultSet.getString("description"))
                        .build());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return games;
    }

    @Override
    public List<Game> readBy(User user) {
        List<Game> games = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(READ_BY_USER);
            preparedStatement.setInt(1, user.getId());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                games.add(Game.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .released(resultSet.getDate("release_date").toLocalDate())
                        .rating(resultSet.getInt("rating"))
                        .cost(resultSet.getDouble("cost"))
                        .description(resultSet.getString("description"))
                        .build());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return games;
    }

    @Override
    public Optional<Game> readByName(String name) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(READ_BY_NAME);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(Game.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .released(resultSet.getDate("release_date").toLocalDate())
                        .rating(resultSet.getInt("rating"))
                        .cost(resultSet.getDouble("cost"))
                        .description(resultSet.getString("description"))
                        .build()
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }
}
