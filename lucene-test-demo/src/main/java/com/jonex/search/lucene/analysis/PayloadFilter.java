package com.jonex.search.lucene.analysis;

import com.jonex.search.lucene.util.ByteUtil;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.*;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.util.Random;

/**
 * <pre>
 *
 *  File: PayloadFilter.java
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
public class PayloadFilter extends TokenFilter {

    private final static Random RAMDOM = new Random();

    private final String fieldName;
    private final CharTermAttribute termAttribute;
    private final PayloadAttribute payloadAttribute;
    private final OffsetAttribute offsetAttribute;
    private final PositionIncrementAttribute positionIncrementAttribute;
    private final PositionLengthAttribute positionLengthAttribute;
    private final TypeAttribute typeAttribute;

    public PayloadFilter(TokenStream input, String fieldName) {
        super(input);
        this.fieldName = fieldName;
        this.payloadAttribute = addAttribute(PayloadAttribute.class);
        this.termAttribute = addAttribute(CharTermAttribute.class);
        this.offsetAttribute = addAttribute(OffsetAttribute.class);
        this.positionIncrementAttribute = addAttribute(PositionIncrementAttribute.class);
        this.positionLengthAttribute = addAttribute(PositionLengthAttribute.class);
        this.typeAttribute = addAttribute(TypeAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {
        System.out.println("filter fieldName:"+fieldName);
        boolean hasNext = input.incrementToken();
        if (hasNext) {
            if (fieldName.equals("searchWords")) {
                final int termLen = termAttribute.length();
                char[] term = termAttribute.buffer();
                int startOffset = offsetAttribute.startOffset();
                int endOffset = offsetAttribute.endOffset();
                int positionIncrement = positionIncrementAttribute.getPositionIncrement();
                int positionLength = positionLengthAttribute.getPositionLength();
                String type = typeAttribute.type();
                String currentToken = new String(term, 0, termLen);
                System.out.println("currentToken:"+currentToken);
                //payloadAttribute.setPayload(new BytesRef(this.getPayload(currentToken)));
                if ("world".equalsIgnoreCase(currentToken)){
                    payloadAttribute.setPayload(new BytesRef(this.getPayload(currentToken)));
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private byte[] getPayload(String currentToken){
        //TODO generate docid payload
        float payload = RAMDOM.nextInt(100) * 0.1f;
        return ByteUtil.floatToBytes(payload);
    }

}
