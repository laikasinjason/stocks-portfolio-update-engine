package com.laikasin.datamodel;


public enum Account {
    SIN('S'), FAN('F');

    private Character shortChar;

    Account(Character shortChar) {
        this.shortChar = shortChar;
    }

    public Character toShortChar() {
        return shortChar;
    }

    public static Account fromShortChar(final Character shortChar) {
        if (null != shortChar) {
            for (Account source : Account.values()) {
                if (shortChar.equals(source.toShortChar())) {
                    return source;
                }
            }
        }
        return null;
    }

}
