package com.jonex.search.lucene.util;

import java.nio.ByteBuffer;

/**
 * <pre>
 *
 *  File: ByteUtil.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
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
public class ByteUtil {

    private static ByteBuffer buffer = ByteBuffer.allocate(8);

    //byte 与 int 的相互转换
    public static byte intToByte(int x) {
        return (byte) x;
    }

    public static int byteToInt(byte b) {
        //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xff 进行二进制与得到它的无符值
        return b & 0xff;
    }

    //byte 数组与 int 的相互转换
    public static int bytesToInt(byte[] b) {
        return   b[3] & 0xff |
                (b[2] & 0xff) << 8 |
                (b[1] & 0xff) << 16 |
                (b[0] & 0xff) << 24;
    }

    public static byte[] intToBytes(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xff),
                (byte) ((a >> 16) & 0xff),
                (byte) ((a >> 8) & 0xff),
                (byte) (a & 0xff)
        };
    }

    //byte 数组与 long 的相互转换
    public static byte[] longToBytes(long x) {
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public static byte[] floatToBytes(float x){
        int intBits = Float.floatToIntBits(x);
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (intBits & 0xff);
        bytes[1] = (byte) ((intBits >> 8) & 0xff);
        bytes[2] = (byte) ((intBits >> 16) & 0xff);
        bytes[3] = (byte) ((intBits >> 24) & 0xff);
        return bytes;
    }

    public static float bytesToFloat(byte[] bytes){
        int bits = (0xff & bytes[0])
                | (0xff00 & bytes[1] << 8)
                | (0xff0000 & bytes[2] << 16)
                | (0xff000000 & bytes[3] << 24);
        return Float.intBitsToFloat(bits);
    }

    public static byte[] doubleToBytes(double x){
        long longBits = Double.doubleToLongBits(x);
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (longBits & 0xff);
        bytes[1] = (byte) ((longBits >> 8) & 0xff);
        bytes[2] = (byte) ((longBits >> 16) & 0xff);
        bytes[3] = (byte) ((longBits >> 24) & 0xff);
        bytes[4] = (byte) ((longBits >> 32) & 0xff);
        bytes[5] = (byte) ((longBits >> 40) & 0xff);
        bytes[6] = (byte) ((longBits >> 48) & 0xff);
        bytes[7] = (byte) ((longBits >> 56) & 0xff);
        return bytes;
    }

    public static double bytesToDouble(byte[] bytes){
        long bits = (0xffL & (long)bytes[0])
                | (0xff00L & ((long)bytes[1] << 8))
                | (0xff0000L & ((long)bytes[2] << 16))
                | (0xff000000L & ((long)bytes[3] << 24))
                | (0xff00000000L & ((long)bytes[4] << 32))
                | (0xff0000000000L & ((long)bytes[5] << 40))
                | (0xff000000000000L & ((long)bytes[6] << 48))
                | (0xff00000000000000L & ((long)bytes[7] << 56));
        return Double.longBitsToDouble(bits);
    }

    public static void main(String[] args) {
        System.out.println(Integer.toHexString(255));
        System.out.println(0xff);
    }


}
