package com.laikasin.datamodel;

import java.util.Calendar;

public class Quote {
    public enum Type {
        Yield, Price, Future, Spread, Average
    }

    protected Type type;
    protected double value = 0;
    protected Calendar date = Calendar.getInstance();

    public Quote(final Type type, final double price) {
        this.type = type;
        this.value = price;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(final Calendar date) {
        this.date = date;
    }

    public void setValue(final double value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(final float value) {
        this.value = value;
    }
}