package com.laikasin.utils;


import com.laikasin.datamodel.Account;
import com.laikasin.datamodel.PortfolioPnL;
import com.laikasin.datamodel.StockPerformance;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class PortfolioPnLUtils {
    public static Map<Account, Double> getAccountBoughtValueMap(PortfolioPnL portfolioPnL) {
        List<StockPerformance> stockPerformanceList = portfolioPnL.getStockPerformanceList();
        return stockPerformanceList.stream()
                .collect(Collectors.groupingBy(stockPerformance -> stockPerformance.getAccount(),
                        Collectors.summingDouble(stockPerformance -> stockPerformance.getBoughtValue())));
    }

    public static Map<Account, Double> getAccountCurrentValueMap(PortfolioPnL portfolioPnL) {
        List<StockPerformance> stockPerformanceList = portfolioPnL.getStockPerformanceList();
        return stockPerformanceList.stream()
                .collect(Collectors.groupingBy(stockPerformance -> stockPerformance.getAccount(),
                        Collectors.summingDouble(stockPerformance -> stockPerformance.getCurrentValue())));
    }
}