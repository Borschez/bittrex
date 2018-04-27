package ru.borsch.bittrex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.borsch.bittrex.model.pojo.CurrencyPOJO;

import javax.persistence.*;

@Entity
public class Currency {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nameLong;
    private Long minConfirmation;
    private Double txFee;
    private Boolean isActive;
    private String coinType;
    private String baseAddress;
    private String notice;

    Currency(){}

    public Currency(String name, String nameLong, Long minConfirmation, Double txFee, Boolean isActive, String coinType, String baseAddress, String notice) {
        this.name = name;
        this.nameLong = nameLong;
        this.minConfirmation = minConfirmation;
        this.txFee = txFee;
        this.isActive = isActive;
        this.coinType = coinType;
        this.baseAddress = baseAddress;
        this.notice = notice;
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

    public String getNameLong() {
        return nameLong;
    }

    public void setNameLong(String nameLong) {
        this.nameLong = nameLong;
    }

    public Long getMinConfirmation() {
        return minConfirmation;
    }

    public void setMinConfirmation(Long minConfirmation) {
        this.minConfirmation = minConfirmation;
    }

    public Double getTxFee() {
        return txFee;
    }

    public void setTxFee(Double txFee) {
        this.txFee = txFee;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getBaseAddress() {
        return baseAddress;
    }

    public void setBaseAddress(String baseAddress) {
        this.baseAddress = baseAddress;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public static Currency createFromPOJO(CurrencyPOJO pojo){
        return new Currency(pojo.Currency, pojo.CurrencyLong, pojo.MinConfirmation, pojo.TxFee, pojo.IsActive, pojo.CoinType, pojo.BaseAddress, pojo.Notice);
    }
}
