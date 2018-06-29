package ru.borsch.bittrex.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.borsch.bittrex.components.Settings;
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

    @Autowired
    private MarketLogEntryRepository marketLogEntryRepository;

    @Autowired
    private Settings settings;

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
    public List<MarketLogEntry> findBySuccessAndImportanceGreaterThanEqual(Boolean success, Integer importance) {
        return marketLogEntryRepository.findBySuccessAndImportanceGreaterThanEqual(success, importance);
    }

    @Override
    public MarketLogEntry save(MarketLogEntry marketLogEntry) {
        return marketLogEntryRepository.save(marketLogEntry);
    }

    @Override
    public MarketLogEntry update(Long id, MarketLogEntry update) {
        MarketLogEntry marketLogEntry = marketLogEntryRepository.getOne(id);

        marketLogEntry.setMarket(update.getMarket());
        marketLogEntry.setLast(update.getLast());
        marketLogEntry.setFromStamp(update.getFromStamp());
        marketLogEntry.setToStamp(update.getToStamp());
        marketLogEntry.setPercent(update.getPercent());
        marketLogEntry.setSuccess(update.getSuccess());

        return marketLogEntryRepository.save(marketLogEntry);
    }

    @Override
    public void delete(Long id) {
        marketLogEntryRepository.delete(id);
    }

    @Override
    public void cleanUp(Integer lowImportance) {
        //cleanUp with low importance
        Date range = DateUtils.addMilliseconds(new Date(), -1*this.settings.getTrackPeriod());

        marketLogEntryRepository.deleteByFromStampBeforeAndImportanceLessThan(range, lowImportance);

        //cleanUp pretenders
        range = DateUtils.addMilliseconds(new Date(), -1*this.settings.getPretenderStorePeriod()*3600000);
        marketLogEntryRepository.deleteByFromStampBeforeAndSuccess(range, false);
    }
}
