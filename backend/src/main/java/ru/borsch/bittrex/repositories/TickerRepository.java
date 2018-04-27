package ru.borsch.bittrex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.borsch.bittrex.model.Market;
import ru.borsch.bittrex.model.Ticker;

import java.util.Date;
import java.util.List;

public interface TickerRepository extends JpaRepository<Ticker, Long> {
    List<Ticker> findByMarketName(String marketName);

    @Modifying
    @Transactional
    void deleteByTimeStampBefore(Date expiryDate);
}
