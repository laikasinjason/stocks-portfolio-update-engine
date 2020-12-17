package com.laikasin.stockupdate.test;

import com.google.common.base.Stopwatch;
import com.laikasin.parser.StockWebPageYahooAPIParser;
import com.laikasin.service.HttpService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StockWebPageYahooAPITest {
    private Logger log = LoggerFactory.getLogger(StockWebPageYahooAPITest.class);

    HttpService httpService = new HttpService();

    @Test
    public void getStockQuote_Given941_ShouldReturn941StockQuote() {
        Stopwatch timer = Stopwatch.createStarted();

        StockWebPageYahooAPIParser parser = new StockWebPageYahooAPIParser("0941.HK");
        BigDecimal price = parser.parse();
        assertNotNull(price);

        assertNotNull(parser.getQuoteHistory());
        assertTrue(parser.getQuoteHistory().size() > 0);


        log.debug("getStockQuote_Given941_ShouldReturn941StockQuote took: {}", timer.stop());
    }

}