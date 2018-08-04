package com.jonex.search.lucene.lz4;

import com.google.common.primitives.Bytes;
import org.apache.lucene.codecs.compressing.CompressionMode;
import org.apache.lucene.codecs.compressing.Compressor;
import org.apache.lucene.codecs.compressing.Decompressor;
import org.apache.lucene.store.ByteArrayDataInput;
import org.apache.lucene.store.ByteArrayDataOutput;
import org.apache.lucene.util.BytesRef;

import java.util.Arrays;

/**
 * <pre>
 *
 *  File: LZ4Test.java
 *
 *  Copyright (c) 2018, jonex.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/7/30				lijunjun				Initial.
 *
 * </pre>
 */
public class LZ4Test {

    public static void main(String[] args) throws Exception{
        byte[] outputBytes = new byte[1024];
        byte[] inputBytes = "aaaaaaaaaaaa".getBytes("UTF-8");
        ByteArrayDataInput dataInput = new ByteArrayDataInput(outputBytes);
        ByteArrayDataOutput dataOutput = new ByteArrayDataOutput(outputBytes);
        //LZ4 compress
        Compressor compressor = CompressionMode.FAST.newCompressor();
        compressor.compress(inputBytes, 0, inputBytes.length, dataOutput);
        System.out.println(Bytes.asList(outputBytes));
        //LZ4 decompress
        BytesRef bytesRef = new BytesRef();
        Decompressor decompressor = CompressionMode.FAST.newDecompressor();
        decompressor.decompress(dataInput, inputBytes.length, 0, dataOutput.getPosition(), bytesRef);
        System.out.println(bytesRef.utf8ToString());
    }

}
