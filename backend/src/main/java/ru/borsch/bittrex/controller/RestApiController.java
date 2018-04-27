package ru.borsch.bittrex.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.borsch.bittrex.components.CommonHelper;
import ru.borsch.bittrex.components.MathHelper;
import ru.borsch.bittrex.model.Currency;
import ru.borsch.bittrex.model.Market;
import ru.borsch.bittrex.model.MarketLogEntry;
import ru.borsch.bittrex.model.Ticker;
import ru.borsch.bittrex.model.pojo.InterfaceSettingsPOJO;
import ru.borsch.bittrex.model.pojo.MarketSummaryPOJO;
import ru.borsch.bittrex.model.pojo.TickerPOJO;
import ru.borsch.bittrex.service.CurrencyService;
import ru.borsch.bittrex.service.MarketLogEntryService;
import ru.borsch.bittrex.service.MarketService;
import ru.borsch.bittrex.util.BittrexRest;
import ru.borsch.bittrex.util.CustomErrorType;

import java.util.*;

@RestController
@RequestMapping("/api")
public class RestApiController {
    private static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private CommonHelper commonHelper;
    
    @Autowired
    private BittrexRest bittrexRest;

    @Autowired
    private MathHelper mathHelper;

    @Autowired
    private MarketLogEntryService marketLogEntryService;

    @Autowired
    private SimpMessagingTemplate template;

    @RequestMapping(value = "/currency/", method = RequestMethod.POST)
    public ResponseEntity<?> createCurrency(@RequestBody Currency currency, UriComponentsBuilder ucBuilder) {
        logger.info("Creating Currency : {}", currency);

        if (currencyService.isCurrencyExist(currency)){
            logger.error("Unable to create. A Currency with name {} already exist", currency.getName());
            return new ResponseEntity(new CustomErrorType("Unable to create. A Currency with name " +
                    currency.getName() + " already exist."), HttpStatus.CONFLICT);
        }
        currencyService.save(currency);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/currency/{id}").buildAndExpand(currency.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/currency/", method = RequestMethod.GET)
    public ResponseEntity<List<Currency>> listAllCurrencies(){
        logger.info("Retrieving all currencies");
        List<Currency> bittrexCurrencies = bittrexRest.getBittrexCurrency();
        bittrexCurrencies.stream().forEach(e->{
            List<Currency> currency = currencyService.findByName(e.getName());
            if (currency.size()>0){
                currencyService.update(currency.get(0).getId(), e);
            }else {
                currencyService.save(e);
            }
        });


        List<Currency> currencies = currencyService.findAll();
        if (currencies.isEmpty()){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Currency>>(bittrexCurrencies, HttpStatus.OK);
    }

    @RequestMapping(value = "/currency/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCurrency(@PathVariable("id") long id, @RequestBody Currency currency){
        logger.info("Updating Currency with id {}",id);

        if (currencyService.findOne(id) == null){
            logger.error("Unable to update. Currency with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to update. Currency with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Currency>(currencyService.update(id, currency), HttpStatus.OK);
    }

    @RequestMapping(value = "/market/{id}", method = RequestMethod.GET)
    public ResponseEntity<Market>getMarket(@PathVariable("id") long id){
        logger.info("Retrieving Market id="+id);
        Market market = marketService.findOne(id);

        if (market == null){
            logger.error("Market with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Market with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);

        }

        List<Ticker> extremas = mathHelper.getExtremas(market.getTickers(), true);

        return new ResponseEntity<Market>(market, HttpStatus.OK);
    }

    @RequestMapping(value = "/market/count={count}", method = RequestMethod.GET)
    public ResponseEntity<List<Market>> listAllMarkets(@PathVariable("count") int count){
        logger.info("Retrieving all markets");
        List<Market> markets = marketService.findAll();
        markets.sort((c1,c2)->c2.getMarketFunction().getPercentDelta().compareTo(c1.getMarketFunction().getPercentDelta()));

        if (markets.isEmpty()){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Market>>((count > 0)?markets.subList(0, count):markets, HttpStatus.OK);
    }

    @RequestMapping(value = "/market/{id}/tickers", method = RequestMethod.GET)
    public ResponseEntity<List<Ticker>>getMarketTickers(@PathVariable("id") long id){
        logger.info("Retrieving tickers for market id="+id);
        Market market = marketService.findOne(id);

        if (market.getTickers().isEmpty()){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Ticker>>(market.getTickers(), HttpStatus.OK);
    }


    @RequestMapping(value = "/marketsummary/", method = RequestMethod.GET)
    public ResponseEntity<List<MarketSummaryPOJO>> listAllMarketSummaries(){
        logger.info("Retrieving all market summaries");
        List<MarketSummaryPOJO> bittrexMarketSummaries = bittrexRest.getMarketsSummaries();

        if (bittrexMarketSummaries.isEmpty()){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<MarketSummaryPOJO>>(bittrexMarketSummaries, HttpStatus.OK);
    }

    @RequestMapping(value = "/settings/", method = RequestMethod.GET)
    public ResponseEntity<InterfaceSettingsPOJO> getSettings(){
        logger.info("Retrieving interface settings");

        InterfaceSettingsPOJO settings = new InterfaceSettingsPOJO();
        settings.refreshPeriod = commonHelper.getRefreshPeriod();

        return new ResponseEntity<InterfaceSettingsPOJO>(settings, HttpStatus.OK);
    }

    @RequestMapping(value = "/history/?importance={importance}", method = RequestMethod.GET)
    public ResponseEntity<List<MarketLogEntry>> getHistory(@PathVariable("importance") int importance){
        logger.info("Retrieving history with importance="+importance);

        List<MarketLogEntry> marketLogEntries = marketLogEntryService.findByImportanceGreaterThanEqual(importance);

        return new ResponseEntity<List<MarketLogEntry>>(marketLogEntries, HttpStatus.OK);
    }

    @RequestMapping(value = "/notify/", method = RequestMethod.GET)
    public ResponseEntity<MarketLogEntry> sendNotify(){
        logger.info("Sending Test Notify");

        MarketLogEntry marketLogEntry = marketLogEntryService.findOne(6654L);

        Map<String,Object> message = new HashMap<>();

        message.put("market", marketLogEntry.getMarket().getName());
        message.put("percent", marketLogEntry.getPercent());
        message.put("marketLogoUrl",  marketLogEntry.getMarket().getLogoUrl());
        message.put("toStamp",  marketLogEntry.getToStamp());

        template.convertAndSend("/topic/notification", message);

        return new ResponseEntity<MarketLogEntry>(marketLogEntry, HttpStatus.OK);
    }


}
