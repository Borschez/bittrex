package ru.borsch.bittrex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.hibernate.annotations.LazyCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.borsch.bittrex.components.CommonHelper;
import ru.borsch.bittrex.components.ScheduledTasks;
import ru.borsch.bittrex.math.MarketFunction;
import ru.borsch.bittrex.model.pojo.MarketPOJO;
import ru.borsch.bittrex.service.TickerService;

import javax.inject.Inject;
import javax.persistence.*;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

@Entity
public class Market {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "market")
    @OrderBy("timeStamp ASC")
    private List<Ticker> tickers;

    @JsonIgnore
    @ManyToOne
    private Currency currency;
    @JsonIgnore
    @ManyToOne
    private Currency baseCurrency;
    private Double minTradeSize;
    private Boolean isActive;
    private Date created;
    private Date timeStamp;
    private String notice;
    private Boolean isSponsored;
    @Column(length = 2000)
    private String logoUrl;

    @Transient
    private MarketFunction marketFunction;
    @Transient
    private Double delta;

    Market(){}

    public Market(String name, Currency currency, Currency baseCurrency, Double minTradeSize,
                  Boolean isActive, Date created, Date timeStamp, String notice, Boolean isSponsored, String logoUrl) {
        this.name = name;
        this.currency = currency;
        this.baseCurrency = baseCurrency;
        this.minTradeSize = minTradeSize;
        this.isActive = isActive;
        this.created = created;
        this.timeStamp = timeStamp;
        this.notice = notice;
        this.isSponsored = isSponsored;
        this.logoUrl = logoUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Double getMinTradeSize() {
        return minTradeSize;
    }

    public void setMinTradeSize(Double minTradeSize) {
        this.minTradeSize = minTradeSize;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Boolean getSponsored() {
        return isSponsored;
    }

    public void setSponsored(Boolean sponsored) {
        isSponsored = sponsored;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public List<Ticker> getTickers() {
        return tickers;
    }

    public void setTickers(List<Ticker> tickers) {
        this.tickers = tickers;
    }

    public String getCurrencyName(){
        return currency.getName();
    }

    public String getBaseCurrencyName(){
        return baseCurrency.getName();
    }

    public void setMarketFunction(MarketFunction marketFunction) {
        this.marketFunction = marketFunction;
        this.delta = marketFunction.getPercentDelta();
    }

    public MarketFunction getMarketFunction(){
        return this.marketFunction;
    }

    public Double getLast(){
        if (!this.tickers.isEmpty()){
            return this.tickers.get(this.tickers.size()-1).getLast();
        }
        return 0.0;
    }

    public Double getDelta() {
        return this.delta;
    }

    public static Market createFromPOJO(MarketPOJO pojo, Currency currency, Currency baseCurrency){
        return new Market(pojo.MarketName, currency, baseCurrency, pojo.MinTradeSize, pojo.IsActive, pojo.Created, null, pojo.Notice, pojo.IsSponsored, pojo.LogoUrl);
    }
}
