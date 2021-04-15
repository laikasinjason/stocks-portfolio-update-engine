package com.laikasin.indicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.laikasin.datamodel.QuoteHistory;
import com.laikasin.parser.StockWebPageYahooAPIParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all classes implementing technical indicators.
 */
public abstract class Indicator {
    protected static Logger log = LoggerFactory.getLogger(Indicator.class);

    protected double value;
    protected QuoteHistory qh;
    private final List<IndicatorValue> history;
    protected Indicator parent;

    public abstract Double calculate(int dayDiff);// must be implemented in subclasses.

    public Indicator() {
        history = new ArrayList<>();
    }

    public Indicator(final QuoteHistory qh) {
        this();
        this.qh = qh;
    }

    public Indicator(final Indicator parent) {
        this();
        this.parent = parent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" value: ").append(value);
        return sb.toString();
    }

    public double getValue() {
        return value;
    }

    public Calendar getDate() {
        if (qh != null) {
            return qh.getLastPriceBar(0).getDate();
        } else {
            List<IndicatorValue> parentHistory = parent.getHistory();
            return parentHistory.get(parentHistory.size() - 1).getDate();
        }
    }

    public void addToHistory(final Calendar date, final double value) {
        history.add(new IndicatorValue(date, value));
    }

    public List<IndicatorValue> getHistory() {
        return history;
    }

    public void reset() {

    }

    public String getName() {
        return toString();
    }
}