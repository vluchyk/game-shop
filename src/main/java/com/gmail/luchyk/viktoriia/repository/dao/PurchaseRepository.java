package com.gmail.luchyk.viktoriia.repository.dao;

import com.gmail.luchyk.viktoriia.model.Purchase;
import com.gmail.luchyk.viktoriia.model.User;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository {
    Optional<Purchase> create(Purchase purchase);

    List<Purchase> read(User user);

    boolean delete(Purchase purchase);
}
