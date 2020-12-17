package com.laikasin.indicator;

import com.laikasin.datamodel.QuoteHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Relative Strength Index. Implemented up to this specification:
 * http://en.wikipedia.org/wiki/Relative_strength
 */
public class RSI extends Indicator {
    private static Logger log = LoggerFactory.getLogger(RSI.class);
    private final int periodLength;
    private final String stockCode;
    private double gains = 0.0, losses = 0.0;

    public RSI(final QuoteHistory qh, final int periodLength, final String stockCode) {
        super(qh);
        this.periodLength = periodLength;
        this.stockCode = stockCode;
    }

    @Override
    // day diff means prev days, 1 means last day
    public Double calculate(int dayDiff) {
        gains = 0.0;
        losses = 0.0;
        try {
            List<Double> rsiList = new ArrayList();

            rsiList.add(calculateFirst());

            int bar = periodLength + 1;
            while (qh.getPriceBar(bar) != null) {
                BigDecimal barChange = qh.getPriceBar(bar).getClose().subtract(qh.getPriceBar(bar - 1).getClose());
                if (barChange.doubleValue() > 0) {
                    gains = gains * (periodLength - 1) / periodLength + barChange.doubleValue();
                } else {
                    losses = losses * (periodLength - 1) / periodLength - barChange.doubleValue();
                }
                double change = gains + losses;

                value = (change == 0) ? 50 : (100 * gains / change);
                rsiList.add(value);
                bar++;
            }

//            int lastBar = qh.size() - 1 - dayDiff;
//            int firstBar = lastBar - periodLength + 1 - dayDiff;
//
//            double gains = 0, losses = 0;
//
//
//            for (int bar = firstBar; bar <= lastBar; bar++) {
//                BigDecimal change = qh.getPriceBar(bar).getClose().subtract(qh.getPriceBar(bar - 1).getClose());
//                gains += Math.max(0, change.doubleValue());
//                losses += Math.max(0, -change.doubleValue());
//            }
//
//            double change = gains + losses;
//
//            value = (change == 0) ? 50 : (100 * gains / change);
//            return value;
            return rsiList.get(rsiList.size()-1-dayDiff);
        } catch (Exception e) {
            log.error("Exception occur on calculating RSI for {}, {}", stockCode, e);
            return null;
        }
    }

    private double calculateFirst() {
        int lastBar = periodLength;

        for (int bar = 1; bar <= lastBar; bar++) {
            BigDecimal change = qh.getPriceBar(bar).getClose().subtract(qh.getPriceBar(bar - 1).getClose());
            gains += Math.max(0, change.doubleValue());
            losses += Math.max(0, -change.doubleValue());
        }

        double change = gains + losses;

        value = (change == 0) ? 50 : (100 * gains / change);
        return value;
    }
}
