package com.laikasin.stockupdate.test;

import com.google.common.base.Stopwatch;
import com.laikasin.datamodel.StringStockQuote;
import com.laikasin.parser.AastockStockQuoteParser;
import com.laikasin.service.HttpService;
import com.mashape.unirest.request.HttpRequest;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.laikasin.datamodel.StringStockQuote.NA;
import static org.junit.Assert.*;

public class StockWebPageParserTest {
    private Logger log = LoggerFactory.getLogger(StockWebPageParserTest.class);

    HttpService httpService = new HttpService();

    @Test
    public void getStockQuote_Given941_ShouldReturn941StockQuote() {
        Stopwatch timer = Stopwatch.createStarted();

        HttpRequest request = AastockStockQuoteParser.createRequest("941");
        StringStockQuote q = httpService.queryAsync(request::asBinaryAsync, AastockStockQuoteParser::parse).join().get();
        assertEquals("941", q.getStockCode());
        assertEquals("CHINA MOBILE", q.getStockName());
        assertTrue(NumberUtils.isNumber(q.getPrice()));
        assertTrue(NumberUtils.isNumber(q.getChangeAmount().replace("+", "").replace("-", "")));
        assertTrue(q.getChange().endsWith("%"));
        assertNotEquals(NA, q.getLastUpdate());
        assertTrue(NumberUtils.isNumber(q.getPe()));
        assertTrue(q.getYield().endsWith("%"));
        assertNotEquals(NA, q.getNAV());
        assertTrue(NumberUtils.isNumber(q.getYearLow()));
        assertTrue(NumberUtils.isNumber(q.getYearHigh()));

//        if (TestUtils.withIntraDayData()) {
//            assertTrue(NumberUtils.isNumber(q.getLow()));
//            assertTrue(NumberUtils.isNumber(q.getHigh()));
//        }

        log.debug("getStockQuote_Given941_ShouldReturn941StockQuote took: {}", timer.stop());
    }

    @Test
    public void getStockQuote_Given2800_ShouldReturn2800StockQuote() {
        Stopwatch timer = Stopwatch.createStarted();

        HttpRequest request = AastockStockQuoteParser.createRequest("2800");
        StringStockQuote q = httpService.queryAsync(request::asBinaryAsync, AastockStockQuoteParser::parse).join().get();
        assertEquals("2800", q.getStockCode());
        assertEquals("TRACKER FUND", q.getStockName());
        assertTrue(NumberUtils.isNumber(q.getPrice()));
        assertTrue(NumberUtils.isNumber(q.getChangeAmount().replace("+", "").replace("-", "")));
        assertTrue(q.getChange().endsWith("%"));
        assertNotEquals(NA, q.getLastUpdate());
        assertEquals(NA, q.getPe());
        assertTrue(q.getYield().endsWith("%"));
        assertEquals(NA, q.getNAV());
        assertTrue(NumberUtils.isNumber(q.getYearLow()));
        assertTrue(NumberUtils.isNumber(q.getYearHigh()));

//        if (TestUtils.withIntraDayData()) {
//            assertTrue(NumberUtils.isNumber(q.getLow()));
//            assertTrue(NumberUtils.isNumber(q.getHigh()));
//        }

        log.debug("getStockQuote_Given2800_ShouldReturn2800StockQuote took: {}", timer.stop());
    }

}