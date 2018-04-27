package ru.borsch.bittrex.service;

import ru.borsch.bittrex.model.Currency;

import java.util.List;

public interface CurrencyService {
    Currency findOne(Long id);

    List<Currency> findByName(String name);
    List<Currency> findAll();
    Boolean isCurrencyExist(Currency currency);
    Currency save(Currency currency);

    Currency update(Long id, Currency update);
}
