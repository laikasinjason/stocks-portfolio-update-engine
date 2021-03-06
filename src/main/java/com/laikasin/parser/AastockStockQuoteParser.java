package com.laikasin.parser;

import com.laikasin.datamodel.StringStockQuote;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Supplier;

import static com.laikasin.utils.NumberUtils.extractNumber;

public class AastockStockQuoteParser {
    protected static final Logger log = LoggerFactory.getLogger(AastockStockQuoteParser.class);

    public static String URL = "http://www.aastocks.com/en/stocks/quote/detail-quote.aspx?symbol=";

    public static HttpRequest createRequest(String code) {
        return Unirest.get(URL + StringUtils.leftPad(code, 5, '0'))
                .header("Referer", "http://www.aastocks.com/en/stocks/quote/detail-quote.aspx")
                .header("Host", "www.aastocks.com");
    }

    public static Optional<StringStockQuote> parse(HttpResponse<InputStream> response) {
        try {
            Document doc = Jsoup.parse(response.getRawBody(), "UTF-8", URL);
            StringStockQuote quote = new StringStockQuote(extractNumber(doc.select("title").first().text()).replace(".-", "").replaceFirst("^0+(?!$)", ""));

            // price
            quote.setPrice(doc.select("div[id=labelLast] span").first().text().substring(1));

            // stock name
            quote.setStockName(doc.select("span[id=cp_ucStockBar_litInd_StockName]").first().text());

            // change
            quote.setChangeAmount(doc.select("div:containsOwn(Change)").first().nextElementSibling().nextElementSibling().text());
            quote.setChange(doc.select("div:containsOwn(Change(%))").first().nextElementSibling().nextElementSibling().text());

            // day high day low
            String[] range = doc.select("div:containsOwn(Range)").first().nextElementSibling().nextElementSibling().text().split(" ");
            if (range.length >= 3) {
                if (StringUtils.isNumeric(range[0])) quote.setHigh(range[0]);
                if (StringUtils.isNumeric(range[2])) quote.setLow(range[2]);
            }

            // PE
            quote.setPe(parseValue(() -> doc.select("div[id=tbPERatio]").first().child(1).text().split(" / ")[0].substring(1)));
            // yield
            quote.setYield(parseValue(() -> doc.select("div:containsOwn(Yield/)").first().parent().parent().child(1).text().split(" / ")[0].substring(1)));
            // NAV
            quote.setNAV(parseValue(() -> doc.select("div[id=tbPBRatio]").first().child(1).text().split(" / ")[1]));

            // last update
            quote.setLastUpdate(doc.select("span:containsOwn(Updated:)").first().child(0).text());

            // 52 high low
            String[] yearHighLow = doc.select("td:containsOwn(52 Week)").first().nextElementSibling().text().split(" - ");
            quote.setYearLow(yearHighLow[0]);
            quote.setYearHigh(yearHighLow[1]);
            return Optional.of(quote);
        } catch (Exception e) {
            log.error("Cannot parse stock code: {}", response.getHeaders(), e);
        }
        return Optional.empty();
    }

    private static String parseValue(Supplier<String> f) {
        try {
            return f.get();
        } catch (Exception e) {
            return StringStockQuote.NA;
        }
    }

}