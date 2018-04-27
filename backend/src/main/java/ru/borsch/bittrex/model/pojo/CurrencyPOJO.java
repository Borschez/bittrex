package ru.borsch.bittrex.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyPOJO {
    public String Currency;
    public String CurrencyLong;
    public String CoinType;
    public String BaseAddress;
    public String Notice;

    public Long MinConfirmation;
    public Double TxFee;

    public Boolean IsActive;
}
