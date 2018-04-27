package ru.borsch.bittrex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Ticker {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Market market;

    private Double Last;
    private Double Bid;
    private Double Ask;

    private Date timeStamp;

    Ticker(){}

    public Ticker(Market market, Double last, Double bid, Double ask, Date timeStamp) {
        this.market = market;
        Last = last;
        Bid = bid;
        Ask = ask;
        this.timeStamp = timeStamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public Double getLast() {
        return Last;
    }

    public void setLast(Double last) {
        Last = last;
    }

    public Double getBid() {
        return Bid;
    }

    public void setBid(Double bid) {
        Bid = bid;
    }

    public Double getAsk() {
        return Ask;
    }

    public void setAsk(Double ask) {
        Ask = ask;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof Ticker && ((Ticker) object).getTimeStamp().equals(this.timeStamp) ) return true;

        return false;
    }
}
