package com.laikasin.utils;

public class StockCodeStringUtil {

    private static final String RIC_SUFFIX = ".HK";

    public static String resolveToRicCode(int rawStockCodeNo) {
        String formattedNumber = String.format("%04d", rawStockCodeNo);

        return formattedNumber.concat(RIC_SUFFIX);
    }
}
