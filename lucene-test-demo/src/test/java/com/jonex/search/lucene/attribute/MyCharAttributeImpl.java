package com.jonex.search.lucene.attribute;

import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.AttributeReflector;

/**
 * <pre>
 *
 *  File: MyCharAttributeImpl.java
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
public class MyCharAttributeImpl extends AttributeImpl implements MyCharAttribute {

    private char[] chars = new char[255];
    private int length;

    @Override
    public void setChars(char[] buffer, int length) {
        this.length = length;
        if (length > 0){
            System.arraycopy(buffer, 0, chars, 0, length);
        }
    }

    @Override
    public char[] getChars() {
        return chars;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public String getString() {
        if (length > 0){
            return new String(this.chars, 0, length);
        }
        return null;
    }

    @Override
    public void clear() {
        this.length = 0;
    }

    @Override
    public void reflectWith(AttributeReflector attributeReflector) {

    }

    @Override
    public void copyTo(AttributeImpl attribute) {

    }
}
