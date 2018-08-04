package com.jonex.search.lucene.analysis;

import com.jonex.search.lucene.util.ByteUtil;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.util.BytesRef;

/**
 * <pre>
 *
 *  File: PayloadSimilarity.java
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
public class PayloadSimilarity extends BM25Similarity {

    @Override
    protected float scorePayload(int doc, int start, int end, BytesRef payload) {
        byte[] bytes = payload.bytes;
        if (bytes != null){
            float val = ByteUtil.bytesToFloat(bytes);
            System.out.println("doc:"+doc+"  payload val:"+val);
            return val;
        }
        return super.scorePayload(doc, start, end, payload);
    }
}
