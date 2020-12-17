package com.laikasin.indicator;

import com.laikasin.datamodel.QuoteHistory;
import yahoofinance.histquotes.HistoricalQuote;

import java.math.BigDecimal;
import java.util.List;


/**
 * Exponential Moving Average.
 */
public class EMA extends Indicator {
    private final int length;
    private final double multiplier;

    public EMA(final QuoteHistory qh, final int length) {
        super(qh);
        this.length = length;
        multiplier = 2. / (length + 1.);
    }

    @Override
    // day diff means prev days, 1 means last day
    public Double calculate(int dayDiff) {
        List<HistoricalQuote> priceBars = qh.getHistoricalQuotes();
        int lastBar = priceBars.size() - 1 - dayDiff;
        int firstBar = lastBar - 2 * length + 1 - dayDiff;
        Double ema = priceBars.get(firstBar).getClose().doubleValue();

        for (int bar = firstBar; bar <= lastBar; bar++) {
            BigDecimal barClose = priceBars.get(bar).getClose();
            if (barClose!=null) {
                // Just skipping without throwing error at the moment
                ema += (barClose.doubleValue() - ema) * multiplier;
            }
        }

        value = ema;

        return value;
    }
}

