package com.laikasin.datamodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PortfolioPnL {
    private static final Logger log = LoggerFactory.getLogger(PortfolioPnL.class);

    public static String NA = "NA";

    private String modifiedTime = "";
    private double portfolioBoughtValue;
    private double portfolioCurrentValue;
    private double portfolioLastDayValue;
    private double portfolioCurrentPercent;
    private double portfolioLastDayPercent;
    private double diff;
    private double cash;
    private List<StockPerformance> stockPerformanceList;


    public PortfolioPnL() {
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public double getPortfolioBoughtValue() {
        return portfolioBoughtValue;
    }

    public void setPortfolioBoughtValue(double portfolioBoughtValue) {
        this.portfolioBoughtValue = portfolioBoughtValue;
    }

    public double getPortfolioCurrentValue() {
        return portfolioCurrentValue;
    }

    public void setPortfolioCurrentValue(double portfolioCurrentValue) {
        this.portfolioCurrentValue = portfolioCurrentValue;
    }

    public List<StockPerformance> getStockPerformanceList() {
        return stockPerformanceList;
    }

    public void setStockPerformanceList(List<StockPerformance> stockPerformanceList) {
        this.stockPerformanceList = stockPerformanceList;
    }

    public void setDiff(double diff) {
        this.diff = diff;
    }

    public double getDiff() {
        return diff;
    }

    public double getPortfolioLastDayValue() {
        return portfolioLastDayValue;
    }

    public void setPortfolioLastDayValue(double portfolioLastDayValue) {
        this.portfolioLastDayValue = portfolioLastDayValue;
    }

    public double getPortfolioCurrentPercent() {
        return portfolioCurrentPercent;
    }

    public void setPortfolioCurrentPercent(double portfolioCurrentPercent) {
        this.portfolioCurrentPercent = portfolioCurrentPercent;
    }

    public double getPortfolioLastDayPercent() {
        return portfolioLastDayPercent;
    }

    public void setPortfolioLastDayPercent(double portfolioLastDayPercent) {
        this.portfolioLastDayPercent = portfolioLastDayPercent;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getPecentChange() {
        return (this.portfolioCurrentValue - this.portfolioLastDayValue) / this.portfolioLastDayValue * 100;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuffer = new StringBuilder(3000);
        try {
            /* @formatter:off */
            stringBuffer.append(this.getClass().getSimpleName());
            stringBuffer.append(" modifiedTime=").append(modifiedTime).append(",");
            stringBuffer.append(" portfolioBoughtValue=").append(portfolioBoughtValue).append(",");
            stringBuffer.append(" portfolioLastDayValue=").append(portfolioLastDayValue).append(",");
            stringBuffer.append(" portfolioLastDayPercent=").append(portfolioLastDayPercent).append(",");
            stringBuffer.append(" portfolioCurrentValue=").append(portfolioCurrentValue).append(",");
            stringBuffer.append(" portfolioCurrentPercent=").append(portfolioCurrentPercent).append(",");
            stringBuffer.append(" stockPerformanceList=").append(stockPerformanceList).append(",");
            stringBuffer.append(" diff=").append(diff);
            /* @formatter:on */
            stringBuffer.append("}");
        } catch (Exception ex) {
            log.error("Error while printing portfolio {}.", this.getClass().getSimpleName(), ex);
        }
        return stringBuffer.toString();
    }
}