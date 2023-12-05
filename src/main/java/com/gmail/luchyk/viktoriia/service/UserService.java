package com.gmail.luchyk.viktoriia.service;

import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.model.User;
import com.gmail.luchyk.viktoriia.repository.dao.UserRepository;
import com.gmail.luchyk.viktoriia.service.menu.UserMenuService;

public class UserService {
    private UserRepository userRepository;
    private UserMenuService userMenuService;

    public UserService(UserRepository userRepository, UserMenuService userMenuService) {
        this.userRepository = userRepository;
        this.userMenuService = userMenuService;
    }

    public void register() {
        User user;
        do {
            user = this.userMenuService.register();
            if (this.userRepository.existNickname(user))
                System.out.println(Message.USERNAME_EXISTS.getMessage());
            else {
                this.userRepository.create(user);
                System.out.println(Message.USER_REGISTERED_SUCCESSFULLY.getMessage());
                break;
            }
        } while (this.userRepository.existNickname(user));
    }

    public void login() {
        User user;
        do {
            user = this.userMenuService.signIn();
            if (this.userRepository.exist(user))
                System.out.println(Message.WELCOME.getMessage());
            else {
                System.out.println(Message.USERNAME_PASSWORD_INCORRECT.getMessage());
                System.out.println(Message.TRY_AGAIN.getMessage());
            }
        } while (!this.userRepository.exist(user));

    }

    public void logout() {

    }

    public void exit() {

    }
}