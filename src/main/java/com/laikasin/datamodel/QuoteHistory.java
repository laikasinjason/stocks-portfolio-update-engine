package com.laikasin.datamodel;

import yahoofinance.histquotes.HistoricalQuote;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class QuoteHistory {

    private static final long CONTINUITY_THRESHOLD = 15 * 60 * 1000;// 15

    private final List<HistoricalQuote> historicalQuotes;
    private final List<String> validationMessages;
    private final String stockName;
    private boolean isHistRequestCompleted;
    private HistoricalQuote nextBar;
    private boolean isForex;
    private Snapshot snapshot;
    private final LinkedList<QuoteHistoryEvent> events;

    public QuoteHistory(final String stockName) {
        this.stockName = stockName;
        historicalQuotes = new ArrayList<>();
        validationMessages = new ArrayList<>();
        events = new LinkedList<>();
    }

    public QuoteHistory() {
        this("BackDataDownloader");
    }

    public LinkedList<QuoteHistoryEvent> getEvents() {
        return events;
    }

    public Snapshot getSnapshot() {
        return snapshot;
    }

    public void setIsForex(final boolean isForex) {
        this.isForex = isForex;
    }

    public boolean getIsForex() {
        return isForex;
    }

    public List<HistoricalQuote> getHistoricalQuotes() {
        return historicalQuotes;
    }

    public void setHistoricalQuotes(List<HistoricalQuote> historicalQuoteList) {
        historicalQuoteList.forEach(historicalQuote -> this.historicalQuotes.add(historicalQuote));
    }


    public synchronized void onBar(final Calendar nextBarTime) {
        if (nextBar == null) {
            // The just completed bar never opened, so we assign its OHLC values
            // to the last bar's close
            nextBar = new HistoricalQuote();
            nextBar.setClose(getLastPriceBar(0).getClose());
        }

        nextBar.setDate(nextBarTime);
        historicalQuotes.add(nextBar);
        nextBar = null;// initialize next bar

        // Send a notification to waiting threads informing them that a new bar
        // has closed
        QuoteHistoryEvent quoteHistoryEvent = new QuoteHistoryEvent(
                QuoteHistoryEvent.EventType.NEW_BAR);
        synchronized (events) {
            events.add(quoteHistoryEvent);
            events.notifyAll();
        }
    }

    public synchronized void update(final BigDecimal open, final BigDecimal high,
                                    final BigDecimal low, final BigDecimal close, final BigDecimal adjClose, long volume) {
        if (isForex) {
            volume = 0;// volume is not reported for Forex
        }

        if (nextBar == null) {

            nextBar = new HistoricalQuote(stockName, Calendar.getInstance(), open, low, high, close, adjClose, volume);
        } else {
            nextBar.setClose(close);
            nextBar.setLow(new BigDecimal(Math.min(low.doubleValue(), nextBar.getLow().doubleValue())));
            nextBar.setHigh(new BigDecimal(Math.max(high.doubleValue(), nextBar.getHigh().doubleValue())));
            nextBar.setVolume(nextBar.getVolume() + volume);
        }

        snapshot = new Snapshot(open.doubleValue(), high.doubleValue(), low.doubleValue(), close.doubleValue(), volume);

        QuoteHistoryEvent quoteHistoryEvent = new QuoteHistoryEvent(
                QuoteHistoryEvent.EventType.MARKET_CHANGE);
        synchronized (events) {
            events.add(quoteHistoryEvent);
            events.notifyAll();
        }

    }

    public String getStockName() {
        return stockName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (HistoricalQuote priceBar : historicalQuotes) {
            sb.append(priceBar).append("\n");
        }

        return sb.toString();
    }

    public boolean isValid() {
        // TODO: validate quote history
        boolean isValid = true;
        validationMessages.clear();
        return isValid;
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }

    public int size() {
        return historicalQuotes.size();
    }

    public void addHistoricalPriceBar(final HistoricalQuote priceBar) {
        historicalQuotes.add(priceBar);
    }

    public HistoricalQuote getPriceBar(final int index) {
        if (index > historicalQuotes.size() - 1) {
            return null;
        } else {
            return historicalQuotes.get(index);
        }
    }

    public int getSize() {
        return historicalQuotes.size();
    }

    public void setIsHistRequestCompleted(final boolean isHistRequestCompleted) {
        this.isHistRequestCompleted = isHistRequestCompleted;
        if (isHistRequestCompleted) {
            if (!historicalQuotes.isEmpty()) {
                long timeDifference = System.currentTimeMillis() - getLastPriceBar(0).getDate().getTimeInMillis();
                if (timeDifference < CONTINUITY_THRESHOLD) {
                    nextBar = getLastPriceBar(0);
                    historicalQuotes.remove(historicalQuotes.size() - 1);
                }
            }
        }
    }

    public boolean getIsHistRequestCompleted() {
        return isHistRequestCompleted;
    }

    public HistoricalQuote getLastPriceBar(int dayDiff) {
        try {
            return historicalQuotes.get(historicalQuotes.size() - dayDiff - 1);
        } catch (IndexOutOfBoundsException ex) {
            return new HistoricalQuote(stockName, null, new BigDecimal("0.0"), new BigDecimal("0.0"),
                    new BigDecimal("0.0"), new BigDecimal("0.0"), new BigDecimal("0.0"), 0L);
        }
    }

    public HistoricalQuote getFirstPriceBar() {
        return historicalQuotes.get(0);
    }
}
