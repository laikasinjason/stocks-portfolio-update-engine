package com.laikasin.stockupdate.test;

import com.laikasin.utils.NumberUtils;
import org.junit.Assert;
import org.junit.Test;

public class NumberUtilsTest {

    @Test
    public void doubleFormatReturnExpectedFormat() {
        Assert.assertEquals("1.46", NumberUtils.formatDouble(01.456546));
    }

    @Test
    public void doubleFormatReturnNUllStringIfNumberIsNull() {
        Assert.assertEquals("NULL", NumberUtils.formatDouble(null));
    }
}