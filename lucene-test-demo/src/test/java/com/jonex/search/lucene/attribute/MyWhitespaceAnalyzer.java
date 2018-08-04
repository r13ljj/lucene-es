package com.jonex.search.lucene.attribute;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

import java.io.IOException;

/**
 * <pre>
 *
 *  File: MyWhitespaceAnalyzer.java
 *
 *  Copyright (c) 2018, jonex.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/7/24				lijunjun				Initial.
 *
 * </pre>
 */
public class MyWhitespaceAnalyzer extends Analyzer {


    @Override
    protected TokenStreamComponents createComponents(String s) {
        Tokenizer source = new MyWhitespaceTokenizer();
        TokenStream sink = new MyLowerCaseTokenFilter(source);
        return new TokenStreamComponents(source, sink);
    }

    public static void main(String[] args) {
        String text = "An AttributeSource contains a list of different AttributeImpls, and methods to add and get them. ";
        try {
            Analyzer analyzer = new MyWhitespaceAnalyzer();
            TokenStream tokenStream = analyzer.tokenStream("aa", text);
            MyCharAttribute charAttribute = tokenStream.getAttribute(MyCharAttribute.class);
            tokenStream.reset();
            while(tokenStream.incrementToken()){
                System.out.print(charAttribute.getString()+"|");
            }
            tokenStream.end();
            analyzer.close();
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
