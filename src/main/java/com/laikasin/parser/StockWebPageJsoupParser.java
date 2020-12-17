package com.laikasin.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StockWebPageJsoupParser {
    private static Logger log = LoggerFactory.getLogger(StockWebPageJsoupParser.class);

    public void parse() {
        log.info("running jsoup parser...");
        String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.33 (KHTML, like Gecko) Chrome/27.0.1438.7 Safari/537.33";

        Document document;
        try {
            //Get Document object after parsing the html from given url.
            document = Jsoup.connect("https://hk.finance.yahoo.com/quote/0002.HK").userAgent(ua).get();

            String title = document.title(); //Get title
            log.info("  Title: " + title); //Print title.

//            Elements price = document.select("tr"); //Get price

            String price = document.select(".time_rtq_ticker").first().text();
            String name = document.select(".title h2").first().text();

            log.info(String.format("%s [%s] is trading at %s", name, "1", price));


        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("jsoup parser - done");
    }
}
