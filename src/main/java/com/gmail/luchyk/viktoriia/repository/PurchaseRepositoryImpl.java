package com.gmail.luchyk.viktoriia.repository;

import com.gmail.luchyk.viktoriia.model.Purchase;
import com.gmail.luchyk.viktoriia.model.User;
import com.gmail.luchyk.viktoriia.repository.dao.PurchaseRepository;
import lombok.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Data
public class PurchaseRepositoryImpl implements PurchaseRepository {
    private AccountRepositoryImpl accountRepository;
    private GameRepositoryImpl gameRepository;
    private final Connection connection;

    private static final String CREATE =
            """
                INSERT INTO public.aux_user_game(user_id, game_id)
                VALUES(?,?);
            """;

    private static final String READ =
            """
                SELECT * FROM public.aux_user_game 
                WHERE user_id = ?;
            """;

    private static final String DELETE =
            """
                DELETE FROM public.aux_user_game 
                WHERE user_id = ? and game_id = ?;
            """;

    public PurchaseRepositoryImpl(AccountRepositoryImpl accountRepository, GameRepositoryImpl gameRepository, Connection connection) {
        this.accountRepository = accountRepository;
        this.gameRepository = gameRepository;
        this.connection = connection;
    }

    @Override
    public Optional<Purchase> create(Purchase purchase) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE);
            preparedStatement.setInt(1, purchase.getUser().getId());
            preparedStatement.setInt(2, purchase.getGame().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e); // todo
        }
        return Optional.empty();
    }

    @Override
    public Optional<Purchase> read(User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(READ);
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            Purchase purchase = Purchase.builder()
                    .user(user)
//                    .game() // todo
                    .build();

            return Optional.ofNullable(purchase);

        } catch (SQLException e) {
            throw new RuntimeException(e); // todo
        }
    }

    @Override
    public boolean delete(Purchase purchase) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setInt(1, purchase.getUser().getId());
            preparedStatement.setInt(2, purchase.getGame().getId());
            return preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e); //todo
        }
    }
}
