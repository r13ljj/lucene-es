package com.jonex.search.lucene.analysis;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * <pre>
 *
 *  File: PayloadAnalyzer.java
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
public class PayloadAnalyzer extends Analyzer {

    public PayloadAnalyzer() {
        super(PER_FIELD_REUSE_STRATEGY);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        System.out.println("analyzer fieldName:"+fieldName);
        Tokenizer tokenizer = new StandardTokenizer();
        TokenStream tokenStream = new PayloadFilter(tokenizer, fieldName);
        tokenStream = new LowerCaseFilter(tokenStream);
        tokenStream = new StopFilter(tokenStream, StandardAnalyzer.ENGLISH_STOP_WORDS_SET);
        return new TokenStreamComponents(tokenizer, tokenStream);
    }


}
