package com.laikasin.stockupdate.test;

import com.laikasin.datamodel.QuoteHistory;
import com.laikasin.indicator.RSI;
import org.junit.Assert;
import org.junit.Test;
import yahoofinance.histquotes.HistoricalQuote;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RSITest {

    @Test
    public void doubleFormatReturnExpectedFormat() {
        QuoteHistory quoteHistory = new QuoteHistory();
        HistoricalQuote hq0 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(8.114079), null, null);
        HistoricalQuote hq1 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(7.994901), null, null);
        HistoricalQuote hq2 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(8.074353), null, null);
        HistoricalQuote hq3 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(8.5), null, null);
        HistoricalQuote hq4 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(8.46), null, null);
        HistoricalQuote hq5 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(8.23), null, null);
        HistoricalQuote hq6 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(8.14), null, null);
        HistoricalQuote hq7 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(8.07), null, null);
        HistoricalQuote hq8 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(7.94), null, null);
        HistoricalQuote hq9 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(7.75), null, null);
        HistoricalQuote hq10 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(7.77), null, null);
        HistoricalQuote hq11 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(7.62), null, null);
        HistoricalQuote hq12 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(7.58), null, null);
        HistoricalQuote hq13 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(7.6), null, null);
        HistoricalQuote hq14 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(7.5), null, null);
        List<HistoricalQuote> hqList = new ArrayList(Arrays.asList(new HistoricalQuote[]{hq0, hq1, hq2, hq3, hq4, hq5, hq6, hq7, hq8, hq9, hq10, hq11, hq12, hq13, hq14}));


        quoteHistory.setHistoricalQuotes(hqList);
        RSI rsiIndicator = new RSI(quoteHistory, 14, "");
        double rsiValue = rsiIndicator.calculate(0);

        Assert.assertEquals(31.9841, rsiValue, 0.1);

        HistoricalQuote hq15 = new HistoricalQuote(null, null, null, null, null, BigDecimal.valueOf(8.0), null, null);
        hqList.add(hq15);
        quoteHistory.setHistoricalQuotes(new ArrayList(Arrays.asList(new HistoricalQuote[]{hq15}))); // set method is actually adding new quotes

        rsiValue = rsiIndicator.calculate(0);
        double lastRsiValue = rsiIndicator.calculate(1);
        Assert.assertEquals(31.9841, lastRsiValue, 0.1);
        Assert.assertEquals(46.4667, rsiValue, 0.1);
    }
}