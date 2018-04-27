package ru.borsch.bittrex.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketSummaryPOJO {
    public String MarketName;

    public Double High;
    public Double Low;
    public Double Volume;
    public Double Last;
    public Double BaseVolume;
    public Double Bid;
    public Double Ask;
    public Double PrevDay;

    public Long OpenBuyOrders;
    public Long OpenSellOrders;

    public Date Created;
    public Date TimeStamp;
}
