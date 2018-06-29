package ru.borsch.bittrex.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.borsch.bittrex.components.CommonHelper;
import ru.borsch.bittrex.components.Settings;
import ru.borsch.bittrex.math.MarketFunction;
import ru.borsch.bittrex.model.Market;
import ru.borsch.bittrex.repositories.MarketRepository;
import ru.borsch.bittrex.service.MarketService;

import java.util.List;

@Service("marketService")
@Transactional
public class MarketServiceImpl implements MarketService {

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private CommonHelper commonHelper;

    @Autowired
    private Settings settings;


    private Market prepareMarket(Market market){
        if (market != null) {
            market.setMarketFunction(new MarketFunction(market.getTickers(), true, this.settings.getPeriodDivisions(), commonHelper.getBaseUrl()));
        }

        return market;
    }

    @Override
    public Market findOne(Long id) {
        return prepareMarket(marketRepository.findOne(id));
    }

    @Override
    public List<Market> findByName(String name) {
        List<Market> result = marketRepository.findByName(name);
        result.forEach(e->prepareMarket(e));
        return result;
    }

    @Override
    public List<Market> findAll() {
        List<Market> result =  marketRepository.findAll();
        result.forEach(e->prepareMarket(e));
        return result;
    }

    @Override
    public Boolean isMarketExist(Market market) {
        return findByName(market.getName()).size() > 0;
    }

    @Override
    public Market save(Market market) {
        return marketRepository.save(market);
    }

    @Override
    public Market update(Long id, Market update) {
        Market market = marketRepository.getOne(id);

        market.setName(update.getName());
        market.setCurrency(update.getCurrency());
        market.setBaseCurrency(update.getBaseCurrency());
        market.setActive(update.getActive());
        market.setCreated(update.getCreated());
        market.setSponsored(update.getSponsored());
        market.setMinTradeSize(update.getMinTradeSize());
        market.setLogoUrl(update.getLogoUrl());
        market.setNotice(update.getNotice());

        return prepareMarket(marketRepository.save(market));
    }
}
