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
import java.util.Optional;

class UserRepositoryImplTest {
    private Connection connection;
    private UserRepository userRepository;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private User user = User.builder()
            .fullName("Jane Smith")
            .login("jsmith")
            .password("qwerty")
            .birthDate(LocalDate.parse("15/05/1990", formatter))
            .build();

    @BeforeEach
    public void init() throws SQLException, IOException, ClassNotFoundException {
        connection = H2Connector.get();
        userRepository = new UserRepositoryImpl(connection);
    }

    @AfterEach
    public void end() throws SQLException {
        connection.close();
    }

    @Test
    public void createTest() throws UserException {
        User result = userRepository.create(user).orElseThrow(() -> new UserException(Message.USER_NOT_CREATED.getMessage()));
        User actual = userRepository.read(result.getId()).orElseThrow();

        Assertions.assertEquals(result, actual);
    }

    @Test
    public void createNullUserTest() {
        User user = null;

        UserException thrown = Assertions.assertThrows(UserException.class, () -> userRepository.create(user).orElseThrow(() -> new UserException(Message.USER_NOT_CREATED.getMessage())), "UserException was expected.");
        Assertions.assertEquals(Message.USER_NOT_CREATED.getMessage(), thrown.getMessage());
    }

    @Test
    public void readTest() throws UserException {
        User actual = userRepository.create(user).orElseThrow(() -> new UserException(Message.USER_NOT_CREATED.getMessage()));
        User result = userRepository.read(actual.getId()).orElseThrow();

        Assertions.assertEquals(result, actual);
    }

    @Test
    public void readByNonExistentIdTest() {
        int nonExistingId = 999999;
        Optional<User> actual = Optional.empty();
        Optional<User> result = userRepository.read(nonExistingId);

        Assertions.assertEquals(result, actual);
    }

    @Test
    public void updateTest() throws UserException {
        int expected = 1;
        User saved = userRepository.create(user).orElseThrow(() -> new UserException(Message.USER_NOT_CREATED.getMessage()));
        saved.setFullName("Jim Smith");
        saved.setBirthDate(LocalDate.parse("10/05/1990", formatter));
        saved.setPassword("12345");

        int result = userRepository.update(saved);
        User updated = userRepository.read(saved.getId()).orElseThrow();

        Assertions.assertEquals(result, expected);
        Assertions.assertEquals(updated, saved);
    }

    @Test
    public void updateNonExistingUserTest() {
        int expected = 0;
        int result = userRepository.update(user);

        Assertions.assertEquals(result, expected);
    }

    @Test
    public void deleteTest() throws UserException {
        User actual = userRepository.create(user).orElseThrow(() -> new UserException(Message.USER_NOT_CREATED.getMessage()));
        userRepository.delete(actual.getId());

        Assertions.assertNull(userRepository.read(actual.getId()).orElse(null));
    }


    @Test
    public void deleteNonExistingUserTest() {
        int nonExistingId = 999999;
        boolean result = userRepository.delete(nonExistingId);

        Assertions.assertFalse(result);
    }

    @Test
    public void existLoginTest() throws UserException {
        User actual = userRepository.create(user).orElseThrow(() -> new UserException(Message.USER_NOT_CREATED.getMessage()));
        boolean result = userRepository.existLogin(actual);

        Assertions.assertTrue(result);
    }

    @Test
    public void existLoginNoneTest() {
        boolean result = userRepository.existLogin(user);

        Assertions.assertFalse(result);
    }

    @Test
    public void existTest() throws UserException {
        User actual = userRepository.create(user).orElseThrow(() -> new UserException(Message.USER_NOT_CREATED.getMessage()));
        boolean result = userRepository.exist(actual);

        Assertions.assertTrue(result);
    }

    @Test
    public void existNoneTest() {
        boolean result = userRepository.exist(user);

        Assertions.assertFalse(result);
    }

    @Test
    public void readByLoginTest() {
        Optional<User> actual = userRepository.create(user);
        Optional<User> result = userRepository.readByLogin(user.getLogin());

        Assertions.assertEquals(result, actual);
    }

    @Test
    public void readByLoginNoneTest() {
        User result = userRepository.readByLogin(user.getLogin()).orElse(null);

        Assertions.assertNull(result);
    }
}