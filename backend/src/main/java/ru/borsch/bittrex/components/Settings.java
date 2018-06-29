package ru.borsch.bittrex.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.borsch.bittrex.model.pojo.InterfaceSettingsPOJO;

@Component
public class Settings {
    @Value("${track.period.divisions}")
    private Integer periodDivisions;
    @Value("${interface.refresh.period}")
    private Integer refreshPeriod;
    @Value("${track.period}")
    private Integer trackPeriod;
    @Value("${percent.top.range}")
    private Integer percentTopRange;
    @Value("${track.interval}")
    private Integer trackInterval;
    @Value("${base.currency.filter}")
    private String baseCurrencyFilter;
    @Value("${scheduler.getmarketsummaries.enable}")
    private Boolean getMarketSummariesEnabled;
    @Value("${scheduler.getmarketsummaries.interval}")
    private Integer getMarketSumarriesInterval;
    @Value("${pretender.store.period}")
    private Integer pretenderStorePeriod;
    @Value("${pretender.percent.top.range}")
    private Integer pretenderPercentTopRange;

    public Integer getPeriodDivisions() {
        return periodDivisions;
    }

    public void setPeriodDivisions(Integer periodDivisions) {
        this.periodDivisions = periodDivisions;
    }

    public Integer getRefreshPeriod() {
        return refreshPeriod;
    }

    public void setRefreshPeriod(Integer refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
    }

    public Integer getTrackPeriod() {
        return trackPeriod;
    }

    public void setTrackPeriod(Integer trackPeriod) {
        this.trackPeriod = trackPeriod;
    }

    public Integer getPercentTopRange() {
        return percentTopRange;
    }

    public void setPercentTopRange(Integer percentTopRange) {
        this.percentTopRange = percentTopRange;
    }

    public Integer getTrackInterval() {
        return trackInterval;
    }

    public void setTrackInterval(Integer trackInterval) {
        this.trackInterval = trackInterval;
    }

    public String getBaseCurrencyFilter() {
        return baseCurrencyFilter;
    }

    public void setBaseCurrencyFilter(String baseCurrencyFilter) {
        this.baseCurrencyFilter = baseCurrencyFilter;
    }

    public Boolean getGetMarketSummariesEnabled() {
        return getMarketSummariesEnabled;
    }

    public void setGetMarketSummariesEnabled(Boolean getMarketSummariesEnabled) {
        this.getMarketSummariesEnabled = getMarketSummariesEnabled;
    }

    public Integer getGetMarketSumarriesInterval() {
        return getMarketSumarriesInterval;
    }

    public void setGetMarketSumarriesInterval(Integer getMarketSumarriesInterval) {
        this.getMarketSumarriesInterval = getMarketSumarriesInterval;
    }

    public Integer getPretenderStorePeriod() {
        return pretenderStorePeriod;
    }

    public void setPretenderStorePeriod(Integer pretenderStorePeriod) {
        this.pretenderStorePeriod = pretenderStorePeriod;
    }

    public Integer getPretenderPercentTopRange() {
        return pretenderPercentTopRange;
    }

    public void setPretenderPercentTopRange(Integer pretenderPercentTopRange) {
        this.pretenderPercentTopRange = pretenderPercentTopRange;
    }

    public InterfaceSettingsPOJO getSettings(){
        InterfaceSettingsPOJO result = new InterfaceSettingsPOJO();

        result.periodDivisions = getPeriodDivisions();
        result.refreshPeriod = getRefreshPeriod();
        result.baseCurrencyFilter = getBaseCurrencyFilter();
        result.getMarketSummariesEnabled = getGetMarketSummariesEnabled();
        result.percentTopRange = getPercentTopRange();
        result.trackInterval = getTrackInterval();
        result.trackPeriod = getTrackPeriod();
        result.pretenderStorePeriod = getPretenderStorePeriod();
        result.pretenderPercentTopRange = getPretenderPercentTopRange();

        return result;
    }

    public void setSettings(InterfaceSettingsPOJO settings){
        setPeriodDivisions(settings.periodDivisions);
        setRefreshPeriod(settings.refreshPeriod);
        setBaseCurrencyFilter(settings.baseCurrencyFilter);
        setGetMarketSummariesEnabled(settings.getMarketSummariesEnabled);
        setPercentTopRange(settings.percentTopRange);
        setTrackInterval(settings.trackInterval);
        setTrackPeriod(settings.trackPeriod);
        setPretenderStorePeriod(settings.pretenderStorePeriod);
        setPretenderPercentTopRange(settings.pretenderPercentTopRange);
    }
}
