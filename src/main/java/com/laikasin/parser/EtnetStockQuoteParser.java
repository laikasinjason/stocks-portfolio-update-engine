package com.laikasin.parser;

import com.laikasin.datamodel.StringStockQuote;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EtnetStockQuoteParser {
    protected static final Logger log = LoggerFactory.getLogger(EtnetStockQuoteParser.class);

    private static final String URL = "http://www.etnet.com.hk/www/tc/stocks/realtime/quote.php";

    public static HttpRequest createRequest(String code) {
        return Unirest.get(URL)
                .header("Referer", URL)
                .header("Host", "www.etnet.com.hk")
                .queryString("code", code);
    }

    public static Optional<StringStockQuote> parse(HttpResponse<InputStream> response) {
        try {
            Document doc = Jsoup.parse(response.getRawBody(), "UTF-8", "http://www.etnet.com.hk");

            StringStockQuote q = new StringStockQuote(doc.select("input[id=quotesearch]").attr("value").replaceFirst("^0+(?!$)", ""));
            q.setPrice(doc.select("div[id^=StkDetailMainBox] span[class^=Price ]").text().replaceAll("[\\D]+$", ""));

            String[] changes = doc.select("div[id^=StkDetailMainBox] span[class^=Change]").text().split(" ");
            if (changes.length >= 2) {

                q.setChangeAmount(changes[0]);
                q.setChange(changes[1].substring(1, changes[1].length() - 1));
            }

            q.setHigh(doc.select("div[id^=StkDetailMainBox] tr:eq(0) td:eq(1) span.Number").text());
            q.setLow(doc.select("div[id^=StkDetailMainBox] tr:eq(1) td:eq(0) span.Number").text());

            Optional<String> updateTime = extractText(doc.select("div[id^=StkDetailTime]").text(), "[0-9]*/[0-9]*/[0-9]* [0-9]*:[0-9]*");
            updateTime.ifPresent(q::setLastUpdate);

            q.setPe(doc.select("div[id^=StkList] li:eq(37)").text().split("/")[0].trim());
            q.setYield(doc.select("div[id^=StkList] li:eq(41)").text().split("/")[0].trim() + "%");
            q.setNAV(doc.select("div[id^=StkList] li:eq(49)").text());
            q.setYearHigh(doc.select("div[id^=StkList] li:eq(23)").text());
            q.setYearLow(doc.select("div[id^=StkList] li:eq(27)").text());

            log.debug("parsed quote: {}", q);

            return Optional.of(q);
        } catch (Exception e) {
            log.warn("Cannot get quote from Etnet:" + response.getHeaders(), e);
            return Optional.empty();
        }
    }

    public static Optional<String> extractText(String text, String regex) {
        Pattern p2 = Pattern.compile(regex);
        Matcher m2 = p2.matcher(text);
        if (m2.find()) {
            return Optional.of(m2.group());
        } else {
            return Optional.empty();
        }
    }


}