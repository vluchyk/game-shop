package com.gmail.luchyk.viktoriia.repository;

import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.exception.UserException;
import com.gmail.luchyk.viktoriia.model.User;
import com.gmail.luchyk.viktoriia.repository.dao.UserRepository;
import dbConfiguration.H2Connector;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class UserRepositoryImplTest {
    private Connection connection;

    @BeforeEach
    public void init() throws SQLException, IOException, ClassNotFoundException {
        this.connection = H2Connector.get();
    }

    @AfterEach
    public void end() throws SQLException {
        this.connection.close();
    }

    @Test
    public void createTest() throws UserException {
        UserRepository userRepository = new UserRepositoryImpl(this.connection);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        User user = User.builder().fullName("Jane Smith").login("jsmith").password("qwerty").birthDate(LocalDate.parse("15/05/1990", formatter)).build();
        User result = userRepository.create(user).orElseThrow(() -> new UserException(Message.USER_NOT_CREATED.getMessage()));
        User actual = userRepository.read(result.getId()).orElseThrow();

        Assertions.assertEquals(result, actual);
    }
}