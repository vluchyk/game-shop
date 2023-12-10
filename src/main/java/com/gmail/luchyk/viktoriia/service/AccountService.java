package com.gmail.luchyk.viktoriia.service;

import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.exception.AccountException;
import com.gmail.luchyk.viktoriia.model.Account;
import com.gmail.luchyk.viktoriia.repository.dao.AccountRepository;
import com.gmail.luchyk.viktoriia.service.menu.AccountMenuService;
import lombok.Data;

@Data
public class AccountService {
    private AccountRepository accountRepository;
    private AccountMenuService accountMenuService;
    private Account account;

    public AccountService(AccountRepository accountRepository, AccountMenuService accountMenuService) {
        this.accountRepository = accountRepository;
        this.accountMenuService = accountMenuService;
    }

    public void create() {
        Account account;
        account = this.accountMenuService.create();
        if (this.accountRepository.exist(account)) {
            System.out.println(Message.ACCOUNT_EXISTS.getMessage());
        } else {
            try {
                this.account = this.accountRepository
                        .create(account)
                        .orElseThrow(() -> new AccountException(Message.ACCOUNT_NOT_CREATED.getMessage()));
                System.out.println(Message.ACCOUNT_CREATED_SUCCESSFULLY.getMessage());
            } catch (AccountException e) {
                System.out.println(e);
                System.out.println(Message.TRY_AGAIN.getMessage());
            }
        }
    }

    public void view() {
        this.accountMenuService.view();
        try {
            this.account = this.accountRepository
                    .readByUser(this.accountMenuService.getUser())
                    .orElseThrow(() -> new AccountException(Message.ACCOUNT_DOES_NOT_EXIST.getMessage()));
            System.out.println(this.account);
        } catch (AccountException e) {
            System.out.println(e);
        }
    }

    public void topUp() {
        try {
            this.account = this.accountRepository
                    .readByUser(this.accountMenuService.getUser())
                    .orElseThrow(() -> new AccountException(Message.ACCOUNT_DOES_NOT_EXIST.getMessage()));
            double increaseBy = this.accountMenuService.topUp();
            this.account.setAmount(this.account.getAmount() + increaseBy);
            if (this.accountRepository.update(this.account) != 0)
                System.out.println(Message.ACCOUNT_TOPPED_UP_SUCCESSFULLY.getMessage());
        } catch (AccountException e) {
            System.out.println(e);
        }
    }
}