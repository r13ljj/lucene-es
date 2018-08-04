package org.apache.lucene.demo.custom;

import com.sun.javafx.fxml.expression.ExpressionValue;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.search.FieldValueQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.QueryBuilder;

/**
 * <pre>
 *
 *  File: SearchTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/8/1				lijunjun				Initial.
 *
 * </pre>
 */
public class SearchTest {

    public static void main(String[] args) {
        TermQuery termQuery = new TermQuery(new Term("title", "solr"));

    }

}
