package ru.borsch.bittrex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class MarketLogEntry {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    private Market market;

    private Date fromStamp;
    private Date toStamp;
    private Double percent;

    private Integer importance;

    MarketLogEntry(){}

    public MarketLogEntry(Market market, Date fromStamp, Date toStamp, Double percent, Integer importance) {
        this.market = market;
        this.fromStamp = fromStamp;
        this.toStamp = toStamp;
        this.percent = percent;
        this.importance = importance;
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

    public Date getFromStamp() {
        return fromStamp;
    }

    public void setFromStamp(Date fromStamp) {
        this.fromStamp = fromStamp;
    }

    public Date getToStamp() {
        return toStamp;
    }

    public void setToStamp(Date toStamp) {
        this.toStamp = toStamp;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Integer getImportance() {
        return importance;
    }

    public void setImportance(Integer importance) {
        this.importance = importance;
    }
}
