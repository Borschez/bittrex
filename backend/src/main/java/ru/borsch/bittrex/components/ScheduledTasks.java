package ru.borsch.bittrex.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.borsch.bittrex.model.Currency;
import ru.borsch.bittrex.model.Market;
import ru.borsch.bittrex.model.MarketLogEntry;
import ru.borsch.bittrex.model.Ticker;
import ru.borsch.bittrex.model.pojo.MarketSummaryPOJO;
import ru.borsch.bittrex.service.CurrencyService;
import ru.borsch.bittrex.service.MarketLogEntryService;
import ru.borsch.bittrex.service.MarketService;
import ru.borsch.bittrex.service.TickerService;
import ru.borsch.bittrex.util.BittrexRest;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private BittrexRest bittrexRest;

    @Autowired
    private MarketService marketService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private TickerService tickerService;

    @Autowired
    private MarketLogEntryService marketLogEntryService;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private CommonHelper commonHelper;

    @Autowired
    private Settings settings;

    @Scheduled(fixedDelayString = "${scheduler.getmarketsummaries.interval}")
    public void getMarketSummaries(){
        if (!this.settings.getGetMarketSummariesEnabled()) return;
        List<MarketSummaryPOJO> marketSummaries = bittrexRest.getMarketsSummaries();
        marketSummaries.stream().forEach(e->{// add tickers from summary
            if (!e.MarketName.contains(this.settings.getBaseCurrencyFilter())) return;
            List<Market> marketList = marketService.findByName(e.MarketName);
            if (!marketList.isEmpty()){
                Market market = marketList.get(0);
                if (market.getTimeStamp() == null || !market.getTimeStamp().equals(e.TimeStamp)){
                    Ticker ticker = new Ticker(market,e.Last,e.Bid,e.Ask, e.TimeStamp);
                    tickerService.save(ticker);
                    market.setTimeStamp(e.TimeStamp);
                    marketService.save(market);
                }
            }
        });

        tickerService.cleanUp();//clean up old tickers

        marketLogEntryService.cleanUp(4);//clean up old log

        logger.info("Market Summaries got!");

        addMarketHistoryLog(marketService.findAll());
        checkPretendersForSuccess();
        logger.info("Markets History added!");

    }

    private void addMarketHistoryLog(List<Market> markets){
        List<MarketLogEntry> pretentersMarketLogEntries = marketLogEntryService.findByImportanceGreaterThanEqual(7);
        for (Market market : markets){
            List<Ticker> tickers = market.getTickers();
            if (tickers.size() < 1) continue;
            Date fromStamp = tickers.get(0).getTimeStamp();
            Date toStamp = tickers.get(tickers.size() - 1).getTimeStamp();
            if (marketLogEntryService.findByMarketAndFromStampAndToStamp(market, fromStamp,toStamp).size() > 0) continue;

            Double percent = market.getMarketFunction().getPercentDelta();
            Integer importance = (percent >= this.settings.getPercentTopRange())?7:0;

            MarketLogEntry marketLogEntry = new MarketLogEntry(market, tickers.get(tickers.size() - 1).getLast(), fromStamp, toStamp, percent, importance, false);
            List<MarketLogEntry> existMarketLogEntries = pretentersMarketLogEntries.stream().filter(e -> e.getMarket().getId() == market.getId()).collect(Collectors.toList());
            if (existMarketLogEntries.size() > 0){
                MarketLogEntry exist = existMarketLogEntries.get(0);
                percent = (tickers.get(tickers.size() - 1).getLast() - exist.getLast())*100 /exist.getLast();
                marketLogEntry.setPercent(percent);
                marketLogEntryService.update(exist.getId(), marketLogEntry);
            }else {
                marketLogEntryService.save(marketLogEntry);
            }

        }
    }

    private void checkPretendersForSuccess(){
        List<MarketLogEntry> pretentersMarketLogEntries = marketLogEntryService.findByImportanceGreaterThanEqual(7);

        for (MarketLogEntry pretender : pretentersMarketLogEntries) {
            List<Ticker> tickers = pretender.getMarket().getTickers();
            if (tickers.size() < 1 || pretender.getLast() <= 0) continue;
            Ticker lastTicker = tickers.get(tickers.size() - 1);
            Double percent = (lastTicker.getLast() - pretender.getLast())*100 / pretender.getLast();

            if (percent >= this.settings.getPretenderPercentTopRange()){
                pretender.setSuccess(true);
                marketLogEntryService.update(pretender.getId(), pretender);

                Map<String, Object> message = new HashMap<>();

                message.put("market", pretender.getMarket().getName());
                message.put("percent", pretender.getPercent());
                message.put("marketLogoUrl", pretender.getMarket().getLogoUrl());
                message.put("toStamp", pretender.getToStamp());

                template.convertAndSend("/topic/notification", message);

            }
        }
    }


    @PostConstruct
    public void init(){
        List<Currency> bittrexCurrencies = bittrexRest.getBittrexCurrency();

        bittrexCurrencies.stream().forEach(e->{
            List<Currency> currency = currencyService.findByName(e.getName());
            if (!currency.isEmpty()){
                currencyService.update(currency.get(0).getId(), e);
            }else{
                currencyService.save(e);
            }
        });


        List<Market> bittrexMarkets = bittrexRest.getBittrexMarkets();
        bittrexMarkets.stream().forEach(e->{
            List<Market> market = marketService.findByName(e.getName());
            if (!market.isEmpty()){
                marketService.update(market.get(0).getId(), e);
            }else{
                marketService.save(e);
            }
        });
    }
}
