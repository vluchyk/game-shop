package com.gmail.luchyk.viktoriia.service;

import com.gmail.luchyk.viktoriia.enums.Message;
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
        do {
            account = this.accountMenuService.create();
            if (this.accountRepository.exist(account))
                System.out.println(Message.ACCOUNT_EXISTS.getMessage());
            else {
                this.account = this.accountRepository.create(account).orElseThrow(); // todo
                System.out.println(Message.ACCOUNT_CREATED_SUCCESSFULLY.getMessage());
            }
        } while (this.accountRepository.exist(account));
    }

    public void view() {
        this.accountMenuService.view();
        this.account = this.accountRepository.readByUser(this.accountMenuService.getUser()).orElseThrow(); // todo
        System.out.println(this.account);
    }

    public void topUp() {
        double increaseBy = this.accountMenuService.topUp();
        this.account = this.accountRepository.readByUser(this.accountMenuService.getUser()).orElseThrow(); // todo
        this.account.setAmount(this.account.getAmount() + increaseBy);
        this.accountRepository.update(this.account);
        System.out.println(Message.ACCOUNT_TOPPED_UP_SUCCESSFULLY.getMessage());
    }

    public void next() {
        this.accountMenuService.next();
        this.account = this.accountRepository.readByUser(this.accountMenuService.getUser()).orElseThrow(); // todo
    }
}