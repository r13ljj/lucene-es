package com.jonex.search.lucene;


import com.jonex.search.lucene.util.ByteUtil;

/**
 * <pre>
 *
 *  File: ByteTest.java
 *
 *  Copyright (c) 2018, jonex.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/7/19				lijunjun				Initial.
 *
 * </pre>
 */
public class ByteTest {


    public static void main(String[] args) {
        int a = 3;
        byte[] ab = ByteUtil.intToBytes(a);
        System.out.println(ByteUtil.bytesToInt(ab));
        float b = 8.9f;
        byte[] bb = ByteUtil.floatToBytes(b);
        System.out.println(ByteUtil.bytesToFloat(bb));
        double c = 1.3d;
        byte[] cc = ByteUtil.doubleToBytes(c);
        System.out.println(ByteUtil.bytesToDouble(cc));
    }
}
