package com.laikasin.indicator;

import java.util.Calendar;

public class IndicatorValue {
    private final Calendar date;
    private final double value;

    public IndicatorValue(final Calendar date, final double value) {
        this.date = date;
        this.value = value;
    }

    public Calendar getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }

}
