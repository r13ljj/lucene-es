package com.jonex.search.lucene.attribute;

import org.apache.lucene.util.Attribute;

/**
 * <pre>
 *
 *  File: MyCharAttribute.java
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
public interface MyCharAttribute extends Attribute {

    void setChars(char[] buffer, int length);

    char[] getChars();

    int getLength();

    String getString();

}
