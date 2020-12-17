package com.laikasin.parser;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StockWebPageHtmlUnitParser {
    private static Logger log = LoggerFactory.getLogger(StockWebPageHtmlUnitParser.class);
    private WebClient client = new WebClient();
    private String stockCode;

    public StockWebPageHtmlUnitParser(String stockCode) {
        this.stockCode = stockCode;
    }

    public void parse() {
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        log.info("htmlunit - client browser version: {}", client.getBrowserVersion().toString());

        try {
            log.info("htmlunit - getting stocks: {}", stockCode);
//            String searchUrl = "http://www.aastocks.com/tc/ltp/rtquote.aspx?symbol=" + URLEncoder.encode(stockCode, "UTF-8");
            String searchUrl = "https://hk.finance.yahoo.com/quote/0002.HK";

            HtmlPage page = client.getPage(searchUrl);
            String title = page.getTitleText();
            log.info("Title: {}", title);
//            DomElement domElement= page.getElementById("StkTabsBox");


            List<HtmlElement> items = page.getByXPath("//*[@id=\"aspnetForm\"]/div[4]/div/div[2]/div[1]/div[5]/div[1]/div/table[1]/tbody/tr[1]/td[1]/div/div[1]/div[2]/span/span/span/span/span");
//            List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//div[@class='C font28 C bold']/span");
//            and @style='display:none;']
            HtmlElement lastPrice = items.get(0).getFirstByXPath(".//span");
            for (HtmlElement item : items) {

                log.info("a: ");
                log.info(item.getAttribute("class"));
            }

//            if (items.isEmpty()) {
//                log.warn("No items found !");
//            } else {
//                for (HtmlElement htmlItem : items) {
//                    HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//a"));
//
//                    String itemName = itemAnchor.asText();
//                    String itemUrl = itemAnchor.getHrefAttribute();
//                    HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//span[@class='result-price']"));
//                    // It is possible that an item doesn't have any price
//                    String itemPrice = spanPrice == null ? "no price" : spanPrice.asText();
//
//                    log.info(String.format("Name : %s Url : %s Price : %s", itemName, itemPrice, itemUrl));
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
