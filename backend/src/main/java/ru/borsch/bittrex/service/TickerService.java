package ru.borsch.bittrex.service;

import ru.borsch.bittrex.model.Ticker;

import java.util.List;

public interface TickerService {
    Ticker findOne(Long id);
    List<Ticker> findByMarketName(String marketName);
    Ticker save(Ticker ticker);
    Ticker update(Long id, Ticker update);
    void delete(Long id);
    void cleanUp();
    Integer getPeriodDivisionsCount();
}
