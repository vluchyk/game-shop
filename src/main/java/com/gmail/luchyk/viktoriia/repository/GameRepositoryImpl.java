package com.gmail.luchyk.viktoriia.repository;

import com.gmail.luchyk.viktoriia.model.Game;
import com.gmail.luchyk.viktoriia.repository.dao.GameRepository;

import java.sql.*;
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

        } catch (SQLException e) {
            throw new RuntimeException(e); // todo
        }
        return Optional.ofNullable(game);
    }

    @Override
    public Optional<Game> read(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(READ);

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            Game game = Game.builder()
                    .id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .released(resultSet.getDate("release_date").toLocalDate())
                    .rating(resultSet.getInt("rating"))
                    .cost(resultSet.getDouble("cost"))
                    .description(resultSet.getString("description"))
                    .build();

            return Optional.ofNullable(game);

        } catch (SQLException e) {
            throw new RuntimeException(e); // todo
        }
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

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e); // todo
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            PreparedStatement preparedStatement = preparedStatement = connection.prepareStatement(DELETE);

            preparedStatement.setInt(1, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
