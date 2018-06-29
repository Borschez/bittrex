package ru.borsch.bittrex.service;

import ru.borsch.bittrex.model.Market;
import ru.borsch.bittrex.model.MarketLogEntry;

import java.util.Date;
import java.util.List;

public interface MarketLogEntryService {
    MarketLogEntry findOne(Long id);

    List<MarketLogEntry> findAll();
    List<MarketLogEntry> findByMarketId(Long marketId);
    List<MarketLogEntry> findByMarketName(String marketName);
    List<MarketLogEntry> findByMarket(Market market);
    List<MarketLogEntry> findByImportanceGreaterThanEqual(Integer importance);
    List<MarketLogEntry> findByMarketAndFromStampAndToStamp(Market market, Date fromStamp, Date toStamp);
    List<MarketLogEntry> findBySuccessAndImportanceGreaterThanEqual(Boolean success, Integer importance);


    MarketLogEntry save(MarketLogEntry marketLogEntry);
    MarketLogEntry update(Long id, MarketLogEntry marketLogEntry);
    void delete(Long id);

    void cleanUp(Integer lowImportance);

}
