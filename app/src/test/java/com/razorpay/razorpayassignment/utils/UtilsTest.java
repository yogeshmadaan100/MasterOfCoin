package com.razorpay.razorpayassignment.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by yogeshmadaan on 17/04/16.
 */
public class UtilsTest {

    @Test
    public void testFormatDate() throws Exception {
        Assert.assertEquals(Utils.formatDate("2015-09-25T08:25:26.000Z"),"25 Sep 2015 08:25:26 AM");
        Assert.assertEquals(Utils.formatDate("2015-01-31T08:25:26.000Z"),"31 Jan 2015 08:25:26 AM");

    }

    @Test
    public void testConvertDatetoTimestamp() throws Exception {
        Assert.assertEquals(Utils.convertDatetoTimestamp("2015-09-25T08:25:26.000Z"),Long.valueOf("1443149726000"));
        Assert.assertEquals(Utils.convertDatetoTimestamp("2015-01-31T08:25:26.000Z"),Long.valueOf("1422672926000"));

    }

    @Test
    public void testFormatLastUpdatedTimestamp() throws Exception {

        Assert.assertEquals(Utils.formatLastUpdatedTimestamp(1460840962000L),"02:39:22");
        Assert.assertEquals(Utils.formatLastUpdatedTimestamp(0L),"05:30:00");

    }
}