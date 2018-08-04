package com.jonex.search.lucene.fieldpayload;

import com.jonex.search.lucene.util.ByteUtil;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *
 *  File: FieldPayloadAnalyzer.java
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
public class FieldPayloadAnalyzer extends Analyzer {

    public Map<String,PayloadData> fieldToData = new HashMap<String,PayloadData>();



    public FieldPayloadAnalyzer() {
        super(PER_FIELD_REUSE_STRATEGY);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer source = new StandardTokenizer();
        PayloadData payloadData = this.fieldToData.get(fieldName);
        TokenStream sink = (payloadData!=null) ? new FieldPayloadTokenFilter(source, fieldName, fieldToData) : new StandardFilter(source);
        sink = new LowerCaseFilter(sink);
        sink = new StopFilter(sink, StandardAnalyzer.ENGLISH_STOP_WORDS_SET);
        return new TokenStreamComponents(source, sink);
    }

    void setPayloadData(String field, byte[] data, int offset, int length) {
        fieldToData.put(field, new PayloadData(data, offset, length));
    }
}
