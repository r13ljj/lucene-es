package com.jonex.search.lucene.attribute;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

import java.io.IOException;

/**
 * <pre>
 *
 *  File: MyLowerCaseTokenFilter.java
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
public class MyLowerCaseTokenFilter extends TokenFilter {

    MyCharAttribute charAttribute = addAttribute(MyCharAttribute.class);

    public MyLowerCaseTokenFilter(TokenStream input) {
        super(input);
    }

    @Override
    public boolean incrementToken() throws IOException {
        boolean next = super.input.incrementToken();
        if (next){
            char[] buffer = charAttribute.getChars();
            int length = charAttribute.getLength();
            if (length > 0){
                for (int i=0; i<length; i++){
                    buffer[i] = Character.toLowerCase(buffer[i]);
                }
            }
        }
        return next;
    }
}
