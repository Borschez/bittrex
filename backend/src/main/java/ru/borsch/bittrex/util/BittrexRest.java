package ru.borsch.bittrex.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.borsch.bittrex.components.Settings;
import ru.borsch.bittrex.model.Currency;
import ru.borsch.bittrex.model.Market;
import ru.borsch.bittrex.model.pojo.CurrencyPOJO;
import ru.borsch.bittrex.model.pojo.MarketPOJO;
import ru.borsch.bittrex.model.pojo.MarketSummaryPOJO;
import ru.borsch.bittrex.model.pojo.TickerPOJO;
import ru.borsch.bittrex.service.CurrencyService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BittrexRest {

    private static final String REST_VERSION = "1.1";
    private static final String ALL_CURRENCIES = "https://bittrex.com/api/v{rest.version}/public/getcurrencies";
    private static final String ALL_MARKETS = "https://bittrex.com/api/v{rest.version}/public/getmarkets";
    private static final String GET_TICKER = "https://bittrex.com/api/v{rest.version}/public/getticker?market={market}";
    private static final String GET_MARKET_SUMMARY = "https://bittrex.com/api/v{rest.version}/public/getmarketsummary?market={market}";
    private static final String GET_MARKET_SUMMARIES = "https://bittrex.com/api/v{rest.version}/public/getmarketsummaries";
    private static class QueryResultsCurrency extends QueryResults<CurrencyPOJO> {}
    private static class QueryResultsMarket extends QueryResults<MarketPOJO> {}
    private static class QueryResultsMarketSummary extends QueryResults<MarketSummaryPOJO> {}
    private static class QueryResultTicker extends QueryResult<TickerPOJO> {}

    private final RestTemplate restTemplate;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private Settings settings;


    public BittrexRest(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    public List<Currency> getBittrexCurrency(){
        String url = ALL_CURRENCIES.replace("{rest.version}", REST_VERSION);

        ResponseEntity<QueryResultsCurrency> temp = restTemplate.getForEntity(url, QueryResultsCurrency.class);

        List<Currency> result = new ArrayList<>();
        for(CurrencyPOJO pojo: temp.getBody().result){
            result.add(Currency.createFromPOJO(pojo));
        }

        return result;
    }

    public List<Market> getBittrexMarkets(){
        String url = ALL_MARKETS.replace("{rest.version}", REST_VERSION);

        ResponseEntity<QueryResultsMarket> temp = restTemplate.getForEntity(url, QueryResultsMarket.class);

        List<Market> result = new ArrayList<>();
        for(MarketPOJO pojo: temp.getBody().result){
            if (!pojo.BaseCurrency.contains(this.settings.getBaseCurrencyFilter())) continue;
            List<Currency> currency = currencyService.findByName(pojo.MarketCurrency);
            List<Currency> baseCurrency = currencyService.findByName(pojo.BaseCurrency);

            result.add(Market.createFromPOJO(pojo,
                    (currency != null && currency.size()>0)?currency.get(0):null,
                    (baseCurrency != null && baseCurrency.size()>0)?baseCurrency.get(0):null
            ));
        }

        return result;
    }

    public TickerPOJO getTicker(String marketName){
        String url = GET_TICKER.replace("{rest.version}", REST_VERSION).replace("{market}", marketName);

        ResponseEntity<QueryResultTicker> temp = restTemplate.getForEntity(url, QueryResultTicker.class);

        return temp.getBody().result;
    }

    public List<MarketSummaryPOJO> getMarketsSummaries(){
        String url = GET_MARKET_SUMMARIES.replace("{rest.version}", REST_VERSION);

        ResponseEntity<QueryResultsMarketSummary> temp = restTemplate.getForEntity(url, QueryResultsMarketSummary.class);

        return Arrays.asList(temp.getBody().result);
    }

    public MarketSummaryPOJO getMarketSummary(String marketName){
        String url = GET_MARKET_SUMMARY.replace("{rest.version}", REST_VERSION).replace("{market}", marketName);;

        ResponseEntity<QueryResultsMarketSummary> temp = restTemplate.getForEntity(url, QueryResultsMarketSummary.class);

        return (temp.getBody().result.length >0) ? temp.getBody().result[0] : null;
    }
}
