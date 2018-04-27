package ru.borsch.bittrex.service;

import ru.borsch.bittrex.model.Market;

import java.util.List;

public interface MarketService {
    Market findOne(Long id);

    List<Market> findByName(String name);
    List<Market> findAll();
    Boolean isMarketExist(Market market);
    Market save(Market market);
    Market update(Long id, Market update);
}
