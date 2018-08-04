package com.jonex.search.lucene.boost;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.RAMDirectory;

/**
 * <pre>
 *
 *  File: QueryBoostTest.java
 *
 *  Copyright (c) 2018, jonex.com All Rights Reserved.
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
public class QueryBoostTest {

    public static void main(String[] args) throws Exception{
        RAMDirectory directory = new RAMDirectory();
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(directory, config);
        Document doc1 = new Document();
        Field f1 = new TextField("title", "Java, hello hello!",Field.Store.YES);
        doc1.add(f1);
        writer.addDocument(doc1);

        Document doc2 = new Document();
        Field f2 = new TextField("title", "Python Python Python hello.",Field.Store.YES);
        doc2.add(f2);
        writer.addDocument(doc2);
        writer.close();


        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser("title",analyzer);
        //Query query = parser.parse("java hello");

        //不设置权重之前，Python出现了3次，所以文档2的评分较高
        //增加java关键字的权重，使文档1的评分大于文档2
        Query query = parser.parse("java^2 Python");
        TopDocs topDocs = searcher.search(query, Integer.MAX_VALUE);
        ScoreDoc[] docs = topDocs.scoreDocs;
        if(null == docs || docs.length == 0) {
            System.out.println("No results for this query.");
            return;
        }
        for (ScoreDoc scoreDoc : docs) {
            int docID = scoreDoc.doc;
            float score = scoreDoc.score;
            Document document = searcher.doc(docID);
            String title = document.get("title");
            System.out.println("docId:" + docID);
            System.out.println("title:" + title);
            System.out.println("score:" + score);
            System.out.println("\n");
        }
        reader.close();
        directory.close();
    }

}
