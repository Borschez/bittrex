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

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Value("${scheduler.getmarketsummaries.enable}")
    private Boolean getMarketSummariesEnabled;

    @Value("${base.currency.filter}")
    private String baseCurrencyFilter;

    @Value("${percent.top.range}")
    private Double percentTopRange;

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

    @Scheduled(fixedDelayString = "${scheduler.getmarketsummaries.fixedrate}")
    public void getMarketSummaries(){
        if (!getMarketSummariesEnabled) return;
        List<MarketSummaryPOJO> marketSummaries = bittrexRest.getMarketsSummaries();
        marketSummaries.stream().forEach(e->{// add tickers from summary
            if (!e.MarketName.contains(baseCurrencyFilter)) return;
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

        logger.info("Markets History added!");

    }

    private void addMarketHistoryLog(List<Market> markets){
        for (Market market : markets){
            List<Ticker> tickers = market.getTickers();
            if (tickers.size() < 1) continue;
            Date fromStamp = tickers.get(0).getTimeStamp();
            Date toStamp = tickers.get(tickers.size() - 1).getTimeStamp();
            if (marketLogEntryService.findByMarketAndFromStampAndToStamp(market, fromStamp,toStamp).size() > 0) continue;

            Double percent = market.getMarketFunction().getPercentDelta();
            Integer importance = (percent >= percentTopRange)?7:0;

            MarketLogEntry marketLogEntry = new MarketLogEntry(market, fromStamp, toStamp, percent, importance);

            marketLogEntryService.save(marketLogEntry);

            if (percent >= percentTopRange){

                Map<String,Object> message = new HashMap<>();

                message.put("market", marketLogEntry.getMarket().getName());
                message.put("percent", marketLogEntry.getPercent());
                message.put("marketLogoUrl",  marketLogEntry.getMarket().getLogoUrl());
                message.put("toStamp",  marketLogEntry.getToStamp());

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
