package com.laikasin.utils;


import java.text.DecimalFormat;

public final class NumberUtils {
    // private constructor prevents instantiation
    private NumberUtils() {
        throw new UnsupportedOperationException();
    }

    public static double extractDouble(String str) {
        try {
            return Double.parseDouble(extractNumber(str));
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public static String formatDouble(Double doubleValue) {
        if (doubleValue == null) {
            return "NULL";
        } else {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(doubleValue);
        }
    }

    public static String extractNumber(String str) {
        return str.replaceAll("[^\\.\\-0123456789]", "");
    }

    public static double stringToNumber(final String s) {
        return Double.parseDouble(s);
    }

    public static double stringToInteger(final String s) {
        return Integer.parseInt(s);
    }

    public static long stringToLong(final String string) {
        return Long.valueOf(string);
    }
}