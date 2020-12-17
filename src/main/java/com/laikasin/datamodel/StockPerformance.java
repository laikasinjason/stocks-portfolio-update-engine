package com.laikasin.datamodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockPerformance {
    private static final Logger log = LoggerFactory.getLogger(StockPerformance.class);

    public static String NA = "NA";

    private String stockCode = "";
    private String stockName;
    private Account account;
    private Integer amount;
    private Double boughtPrice;
    private Double currentPrice;
    private Double boughtValue;
    private Double currentValue;
    private Double diff;
    private Double ratio;
    private Double rsi;
    private Double ema10;
    private Double lastDayPrice;
    private Double lastDayEma10;


    public StockPerformance() {
    }

    public StockPerformance(String code) {
        this.stockCode = code;
    }


    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Double getBoughtPrice() {
        return boughtPrice;
    }

    public void setBoughtPrice(Double boughtPrice) {
        this.boughtPrice = boughtPrice;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Double getBoughtValue() {
        return boughtValue;
    }

    public void setBoughtValue(Double boughtValue) {
        this.boughtValue = boughtValue;
    }

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }

    public Double getDiff() {
        return diff;
    }

    public void setDiff(Double diff) {
        this.diff = diff;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Double getRsi() {
        return rsi;
    }

    public void setRsi(Double rsi) {
        this.rsi = rsi;
    }

    public Double getEma10() {
        return ema10;
    }

    public void setEma10(Double ema10) {
        this.ema10 = ema10;
    }

    public Double getLastDayPrice() {
        return lastDayPrice;
    }

    public void setLastDayPrice(Double lastDayPrice) {
        this.lastDayPrice = lastDayPrice;
    }

    public Double getLastDayEma10() {
        return lastDayEma10;
    }

    public void setLastDayEma10(Double lastDayEma10) {
        this.lastDayEma10 = lastDayEma10;
    }
    @Override
    public String toString() {
        final StringBuilder stringBuffer = new StringBuilder(3000);
        try {
            /* @formatter:off */
            stringBuffer.append(this.getClass().getSimpleName());
            stringBuffer.append(" stockCode=").append(stockCode).append(",");
            stringBuffer.append(" amount=").append(amount).append(",");
            stringBuffer.append(" boughtPrice=").append(boughtPrice).append(",");
            stringBuffer.append(" boughtValue=").append(boughtValue).append(",");
            stringBuffer.append(" currentPrice=").append(currentPrice).append(",");
            stringBuffer.append(" currentValue=").append(currentValue).append(",");
            stringBuffer.append(" diff=").append(diff);
            stringBuffer.append(" ratio=").append(ratio);
            stringBuffer.append(" rsi=").append(rsi);
            /* @formatter:on */
            stringBuffer.append("}");
        } catch (Exception ex) {
            log.error("Error while printing {} {}.", this.getClass().getSimpleName(), stockCode, ex);
        }
        return stringBuffer.toString();
    }
}