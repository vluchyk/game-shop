package com.gmail.luchyk.viktoriia.service;

import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.exception.UserException;
import com.gmail.luchyk.viktoriia.model.User;
import com.gmail.luchyk.viktoriia.repository.dao.UserRepository;
import com.gmail.luchyk.viktoriia.service.menu.UserMenuService;
import lombok.Data;

@Data
public class UserService {
    private UserRepository userRepository;
    private UserMenuService userMenuService;
    private User user;

    public UserService(UserRepository userRepository, UserMenuService userMenuService) {
        this.userRepository = userRepository;
        this.userMenuService = userMenuService;
    }

    public void register() {
        User user;
        do {
            user = this.userMenuService.register();
            if (this.userRepository.existLogin(user))
                System.out.println(Message.USERNAME_EXISTS.getMessage());
            else {
                try {
                    this.user = this.userRepository
                            .create(user)
                            .orElseThrow(() -> new UserException(Message.USER_NOT_CREATED.getMessage()));
                    System.out.println(Message.USER_REGISTERED_SUCCESSFULLY.getMessage());
                    break;
                } catch (UserException e) {
                    System.out.println(e);
                    System.out.println(Message.TRY_AGAIN.getMessage());
                }
            }
        } while (this.userRepository.existLogin(user));
    }

    public void login() {
        User user;
        do {
            user = this.userMenuService.signIn();
            if (this.userRepository.exist(user)) {
                System.out.println(Message.WELCOME.getMessage());
                try {
                    this.user = this.userRepository
                            .readByLogin(user.getLogin())
                            .orElseThrow(() -> new UserException(Message.USERNAME_PASSWORD_INCORRECT.getMessage()));
                } catch (UserException e) {
                    System.out.println(e);
                    System.out.println(Message.TRY_AGAIN.getMessage());
                }
            }
            else {
                System.out.println(Message.USERNAME_PASSWORD_INCORRECT.getMessage());
                System.out.println(Message.TRY_AGAIN.getMessage());
            }
        } while (!this.userRepository.exist(user));

    }

    public void exit() {
        System.exit(0);
    }
}