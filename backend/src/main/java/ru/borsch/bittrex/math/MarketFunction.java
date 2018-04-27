package ru.borsch.bittrex.math;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.borsch.bittrex.components.CommonHelper;
import ru.borsch.bittrex.components.ScheduledTasks;
import ru.borsch.bittrex.model.Ticker;
import ru.borsch.bittrex.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class MarketFunction {

    private Integer periodDivisions;
    private FunctionSignature functionSignature;
    private String baseUrl;
    private List<Ticker> tickers;
    private Boolean isBase;

    public MarketFunction(List<Ticker> tickers, Integer periodDivisions){
       this(tickers, false, periodDivisions,"");
    }

    MarketFunction(){
    }

    public MarketFunction(List<Ticker> tickers, Boolean isBase, Integer periodDivisions, String baseUrl){
        this.tickers = tickers;
        this.isBase = isBase;
        this.periodDivisions = periodDivisions;
        this.baseUrl = baseUrl;
    }

    public FunctionSignature getFunctionSignature() {
        if (!tickers.isEmpty()) {
            if (isBase) {
                FunctionSignature result = FunctionSignature.Base;
                List<List<Ticker>> parts = ListUtils.splitByEqualParts(tickers, periodDivisions);

                for (List<Ticker> part: parts){
                    MarketFunction partFunction = new MarketFunction(part, periodDivisions);
                    result = result.switchDirection(partFunction.getFunctionSignature());
                }
                this.functionSignature = result;
                return this.functionSignature;
            } else {
                Double delta = tickers.get(tickers.size() - 1).getLast() - tickers.get(0).getLast();
                this.functionSignature = FunctionSignature.Base.switchDirection(delta);
                return this.functionSignature;
            }
        }

        this.functionSignature = FunctionSignature.Base;
        return this.functionSignature;
    }

    public Double getPercentDelta(){
        if (!tickers.isEmpty()){
            Double baseLast = tickers.get(0).getLast();
            Double last = tickers.get(tickers.size() - 1).getLast();

            return (last - baseLast)*100 / baseLast;
        }
        return 0.0;
    }

    public String getFunctionSignatureImageUrl(){
        return this.functionSignature.getImageUrl(baseUrl);
    }
}
