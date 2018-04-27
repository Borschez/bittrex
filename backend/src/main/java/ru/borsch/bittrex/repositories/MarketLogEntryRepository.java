package ru.borsch.bittrex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.borsch.bittrex.model.Market;
import ru.borsch.bittrex.model.MarketLogEntry;

import java.util.Date;
import java.util.List;

public interface MarketLogEntryRepository extends JpaRepository<MarketLogEntry, Long> {
    List<MarketLogEntry> findByMarketId(Long marketId);
    List<MarketLogEntry> findByMarketName(String marketName);
    List<MarketLogEntry> findByMarket(Market market);
    List<MarketLogEntry> findByImportanceGreaterThanEqual(Integer importance);
    List<MarketLogEntry> findByMarketAndFromStampAndToStamp(Market market, Date fromStamp, Date toStamp);

    @Modifying
    @Transactional
    void deleteByFromStampBeforeAndImportanceLessThan(Date expiryDate, Integer lowImportance);
}
