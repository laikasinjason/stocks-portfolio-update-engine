package com.laikasin.parser;

import com.laikasin.datamodel.QuoteHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.stock.StockQuote;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

public class StockWebPageYahooAPIParser {
    private static Logger log = LoggerFactory.getLogger(StockWebPageYahooAPIParser.class);
    private String stockCode;

    private QuoteHistory quoteHistory;

    public StockWebPageYahooAPIParser(String stockCode) {
        this.stockCode = stockCode;
        this.quoteHistory = new QuoteHistory(stockCode);
    }

    public QuoteHistory getQuoteHistory() {
        return quoteHistory;
    }

    public BigDecimal parse() {
        Stock stock = null;
        try {
            log.info("YahooAPI - Getting stocks: {}", stockCode);
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.add(Calendar.DATE, -60);

            stock = YahooFinance.get(stockCode);

            try {
                quoteHistory.setHistoricalQuotes(stock.getHistory(from, to, Interval.DAILY));
            } catch (NumberFormatException e) {
                log.warn("NumberFormatException during getting history of {}", stockCode);
            }

            StockQuote stockQuote = stock.getQuote();
            BigDecimal price = stock.getQuote().getPrice();
            BigDecimal change = stock.getQuote().getChangeInPercent();
            BigDecimal peg = stock.getStats().getPeg();
            BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();

            return price;
        } catch (IOException e) {
            log.error("Error while using yahoo API: {}", e.getStackTrace());
            return null;
        }
    }
}
