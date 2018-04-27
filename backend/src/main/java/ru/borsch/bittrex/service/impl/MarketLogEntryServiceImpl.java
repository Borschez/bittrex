package ru.borsch.bittrex.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.borsch.bittrex.model.Market;
import ru.borsch.bittrex.model.MarketLogEntry;
import ru.borsch.bittrex.repositories.MarketLogEntryRepository;
import ru.borsch.bittrex.service.MarketLogEntryService;
import ru.borsch.bittrex.util.DateUtils;

import java.util.Date;
import java.util.List;

@Service("marketLogEntryService")
@Transactional
public class MarketLogEntryServiceImpl implements MarketLogEntryService {

    @Value("${track.period}")
    private Integer trackPeriod;

    @Autowired
    private MarketLogEntryRepository marketLogEntryRepository;

    @Override
    public MarketLogEntry findOne(Long id) {
        return marketLogEntryRepository.findOne(id);
    }

    @Override
    public List<MarketLogEntry> findAll() {
        return marketLogEntryRepository.findAll();
    }

    @Override
    public List<MarketLogEntry> findByMarketId(Long marketId) {
        return marketLogEntryRepository.findByMarketId(marketId);
    }

    @Override
    public List<MarketLogEntry> findByMarketName(String marketName) {
        return marketLogEntryRepository.findByMarketName(marketName);
    }

    @Override
    public List<MarketLogEntry> findByMarket(Market market) {
        return marketLogEntryRepository.findByMarket(market);
    }

    @Override
    public List<MarketLogEntry> findByImportanceGreaterThanEqual(Integer importance) {
        return marketLogEntryRepository.findByImportanceGreaterThanEqual(importance);
    }

    @Override
    public List<MarketLogEntry> findByMarketAndFromStampAndToStamp(Market market, Date fromStamp, Date toStamp) {
        return  marketLogEntryRepository.findByMarketAndFromStampAndToStamp(market, fromStamp, toStamp);
    }

    @Override
    public MarketLogEntry save(MarketLogEntry marketLogEntry) {
        return marketLogEntryRepository.save(marketLogEntry);
    }

    @Override
    public MarketLogEntry update(Long id, MarketLogEntry update) {
        MarketLogEntry marketLogEntry = marketLogEntryRepository.getOne(id);

        marketLogEntry.setMarket(update.getMarket());
        marketLogEntry.setFromStamp(update.getFromStamp());
        marketLogEntry.setToStamp(update.getToStamp());
        marketLogEntry.setPercent(update.getPercent());

        return marketLogEntryRepository.save(marketLogEntry);
    }

    @Override
    public void delete(Long id) {
        marketLogEntryRepository.delete(id);
    }

    @Override
    public void cleanUp(Integer lowImportance) {
        Date range = DateUtils.addMilliseconds(new Date(), -1*trackPeriod);

        marketLogEntryRepository.deleteByFromStampBeforeAndImportanceLessThan(range, lowImportance);
    }
}
