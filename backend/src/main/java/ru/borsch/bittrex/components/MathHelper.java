package ru.borsch.bittrex.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.borsch.bittrex.model.Ticker;
import ru.borsch.bittrex.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MathHelper {

    @Value("${track.interval}")
    private Integer functionInterval;

    public Integer getFunctionInterval() {
        return this.functionInterval;
    }

    public List<Ticker> getExtremas(List<Ticker> tickers, boolean strict){
        List<Ticker> result = new ArrayList<>();

        for (Ticker ticker :tickers) {
            List<Ticker> intervalTickers = getIntervalTickers(ticker, tickers);
            if (intervalTickers.size() > 0 && isLocalMaximum(ticker, intervalTickers, strict)){
                result.add(ticker);
            }else if (intervalTickers.size() > 0 && isLocalMinimum(ticker, intervalTickers, strict)){
                result.add(ticker);
            }
        }

        return result;
    }

    public List<Ticker> getIntervalTickers(Ticker ticker, List<Ticker> tickers){
        List<Ticker> result = new ArrayList<>();
        Date minTimeStamp = DateUtils.addMilliseconds(ticker.getTimeStamp(), -1 * functionInterval);
        Date maxTimeStamp = DateUtils.addMilliseconds(ticker.getTimeStamp(), functionInterval);

        for (Ticker checkItem : tickers) {
            if(!checkItem.equals(ticker) &&
                    (checkItem.getTimeStamp().after(minTimeStamp) || checkItem.getTimeStamp().equals(minTimeStamp)) &&
                    (checkItem.getTimeStamp().before(maxTimeStamp) || checkItem.getTimeStamp().equals(maxTimeStamp))
                    ){
                result.add(checkItem);
            }
        }
        return result;
    }

    public boolean isLocalMaximum(Ticker ticker, List<Ticker> intervalTickers, boolean strict){
        for (Ticker intervalTicker: intervalTickers){
            if (strict && ticker.getLast() <= intervalTicker.getLast()) return false;
            if (!strict && ticker.getLast() < intervalTicker.getLast()) return false;
        }
        return true;
    }

    public boolean isLocalMinimum(Ticker ticker, List<Ticker> intervalTickers, boolean strict){
        for (Ticker intervalTicker: intervalTickers){
            if (strict && ticker.getLast() >= intervalTicker.getLast()) return false;
            if (!strict && ticker.getLast() > intervalTicker.getLast()) return false;
        }
        return true;
    }
}
