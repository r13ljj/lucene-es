package com.jonex.search.lucene.payload;

import com.jonex.search.lucene.analysis.PayloadAnalyzer;
import com.jonex.search.lucene.analysis.PayloadSimilarity;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.payloads.MaxPayloadFunction;
import org.apache.lucene.queries.payloads.PayloadScoreQuery;
import org.apache.lucene.search.*;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;

/**
 * <pre>
 *
 *  File: PayloadTest.java
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
public class PayloadTest {

    public static void main(String[] args) throws Exception{
        RAMDirectory directory = new RAMDirectory();
        Analyzer analyzer = new PayloadAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(directory, config);
        Document doc1 = new Document();
        Field id1 = new SortedDocValuesField("id", new BytesRef("001"));
        Field f1 = new TextField("searchWords", "Java hello world",Field.Store.YES);
        Field ff1 = new TextField("title", "first lesson",Field.Store.YES);
        doc1.add(f1);
        doc1.add(ff1);
        doc1.add(id1);
        writer.addDocument(doc1);

        Document doc2 = new Document();
        Field id2 = new SortedDocValuesField("id", new BytesRef("002"));
        Field f2 = new TextField("searchWords", "Java ,I like it.",Field.Store.YES);
        Field ff2 = new TextField("title", "second lesson",Field.Store.YES);
        doc2.add(f2);
        doc2.add(ff2);
        doc2.add(id2);
        writer.addDocument(doc2);
        writer.close();


        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new PayloadSimilarity());
        /*SpanTermQuery queryStart = new SpanTermQuery(new Term("title","java"));
        SpanTermQuery queryEnd = new SpanTermQuery(new Term("title","world"));
        //Query query = new PayloadScoreQuery(new SpanQuery[] {
        //        queryStart,queryEnd},new MaxPayloadFunction(), true);
        SpanQuery[] ss = new SpanTermQuery[]{queryStart,queryEnd};
        Query query = new PayloadScoreQuery(queryEnd, new MaxPayloadFunction(), true);*/
        //Query query = new TermQuery(new Term("searchWords", "world"));
        Query query = new PayloadScoreQuery(new SpanTermQuery(new Term("searchWords", "world")), new MaxPayloadFunction());
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
            BytesRef id = document.getBinaryValue("id");
            String title = document.get("searchWords");
            //System.out.println("docId:" + docID);
            //System.out.println("title:" + title);
            //System.out.println("score:" + score);
            System.out.println("docId:"+docID+",id:"+id+",title:"+title+",score:"+score);
            System.out.println("\n");
        }
        reader.close();
        directory.close();
    }

}
