package ru.borsch.bittrex.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.borsch.bittrex.model.Currency;
import ru.borsch.bittrex.repositories.CurrencyRepository;
import ru.borsch.bittrex.service.CurrencyService;

import java.util.List;

@Service("currencyService")
@Transactional
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public Currency findOne(Long id) {
        return currencyRepository.findOne(id);
    }

    @Override
    public List<Currency> findByName(String name) {
        return currencyRepository.findByName(name);
    }

    @Override
    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    @Override
    public Boolean isCurrencyExist(Currency currency) {
        return findByName(currency.getName()).size() > 0;
    }

    @Override
    public Currency save(Currency currency) {
        return currencyRepository.save(currency);
    }

    @Override
    public Currency update(Long id, Currency update) {
        Currency currency = currencyRepository.findOne(id);

        currency.setName(update.getName());
        currency.setNameLong(update.getNameLong());
        currency.setActive(update.getActive());
        currency.setBaseAddress(update.getBaseAddress());
        currency.setCoinType(update.getCoinType());
        currency.setMinConfirmation(update.getMinConfirmation());
        currency.setNotice(update.getNotice());
        currency.setTxFee(update.getTxFee());

        return currencyRepository.save(currency);
    }
}
