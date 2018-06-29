package ru.borsch.bittrex.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.borsch.bittrex.components.Settings;
import ru.borsch.bittrex.model.Ticker;
import ru.borsch.bittrex.repositories.TickerRepository;
import ru.borsch.bittrex.service.TickerService;
import ru.borsch.bittrex.util.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("tickerService")
@Transactional
public class TickerServiceImpl implements TickerService {

    @Autowired
    private Settings settings;

    @Autowired
    private TickerRepository tickerRepository;

    @Override
    public Ticker findOne(Long id) {
        return tickerRepository.findOne(id);
    }

    @Override
    public List<Ticker> findByMarketName(String marketName) {
        return tickerRepository.findByMarketName(marketName);
    }

    @Override
    public Ticker save(Ticker ticker) {
        return tickerRepository.save(ticker);
    }

    @Override
    public Ticker update(Long id, Ticker update) {
        Ticker ticker = tickerRepository.getOne(id);

        ticker.setAsk(update.getAsk());
        ticker.setBid(update.getBid());
        ticker.setLast(update.getLast());
        ticker.setMarket(update.getMarket());
        ticker.setTimeStamp(update.getTimeStamp());

        return tickerRepository.save(ticker);
    }

    @Override
    public void delete(Long id) {
        tickerRepository.delete(id);
    }

    @Override
    public void cleanUp() {
        Date range = DateUtils.addMilliseconds(new Date(), -1*this.settings.getTrackPeriod());

        tickerRepository.deleteByTimeStampBefore(range);
    }
}
