package ru.borsch.bittrex.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TickerPOJO {

    public Double Bid;
    public Double Ask;
    public Double Last;
}
