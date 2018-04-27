package ru.borsch.bittrex.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketPOJO {
    public String MarketCurrency;
    public String BaseCurrency;
    public String MarketCurrencyLong;
    public String BaseCurrencyLong;
    public String MarketName;
    public String Notice;
    public String LogoUrl;

    public Double MinTradeSize;

    public Boolean IsActive;
    public Boolean IsSponsored;

    public Date Created;
}
