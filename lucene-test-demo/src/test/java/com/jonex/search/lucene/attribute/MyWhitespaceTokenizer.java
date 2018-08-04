package com.jonex.search.lucene.attribute;

import org.apache.lucene.analysis.Tokenizer;

import java.io.IOException;

/**
 * <pre>
 *
 *  File: MyWhitespaceTokenizer.java
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
public class MyWhitespaceTokenizer extends Tokenizer {

    //记录的属性：term
    private MyCharAttribute charAttribute = this.addAttribute(MyCharAttribute.class);
    private int length;
    private char[] buffer = new char[255];
    int c;

    @Override
    public boolean incrementToken() throws IOException {
        // 清除所有的词项属性
        clearAttributes();
        length = 0;
        while (true) {
            c = this.input.read();

            if (c == -1) {
                if (length > 0) {
                    // 复制到charAttr
                    this.charAttribute.setChars(buffer, length);
                    return true;
                } else {
                    return false;
                }
            }

            if (Character.isWhitespace(c)) {
                if (length > 0) {
                    // 复制到charAttr
                    this.charAttribute.setChars(buffer, length);
                    return true;
                }
            }

            buffer[length++] = (char) c;
        }
    }
}
