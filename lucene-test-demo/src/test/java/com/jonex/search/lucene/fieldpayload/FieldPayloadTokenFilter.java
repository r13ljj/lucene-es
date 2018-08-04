package com.jonex.search.lucene.fieldpayload;

import com.jonex.search.lucene.util.ByteUtil;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *
 *  File: FieldPayloadTokenFilter.java
 *
 *  Copyright (c) 2018, jonex.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/7/26				lijunjun				Initial.
 *
 * </pre>
 */
public class FieldPayloadTokenFilter extends TokenFilter {

    private String fieldName;
    private PayloadData payloadData;
    private int offset;
    private Map<String,PayloadData> fieldToData;
    private CharTermAttribute termAttribute = addAttribute(CharTermAttribute.class);
    private PayloadAttribute payloadAttribute = addAttribute(PayloadAttribute.class);



    public FieldPayloadTokenFilter(TokenStream input, String fieldName, Map<String,PayloadData> fieldToData) {
        super(input);
        this.fieldName = fieldName;
        this.fieldToData = fieldToData;
    }

    @Override
    public boolean incrementToken() throws IOException {
        boolean hasNext = super.input.incrementToken();
        if (!hasNext){
            return false;
        }
        int termLength = termAttribute.length();
        char[] buffer = termAttribute.buffer();
        String term = new String(buffer, 0, termLength);
        System.out.println("======FieldPayloadTokenFilter term:"+term);
        if (offset + payloadData.length <= payloadData.data.length) {
            BytesRef p = new BytesRef(payloadData.data, offset, payloadData.length);
            System.out.println("===FieldPayloadTokenFilter payload:"+ByteUtil.bytesToFloat(p.bytes));
            payloadAttribute.setPayload(p);
            offset += payloadData.length;
        } else {
            payloadAttribute.setPayload(null);
        }
        return true;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        payloadData = this.fieldToData.get(fieldName);
        offset = (payloadData != null) ? payloadData.offset : 0;
    }

}
