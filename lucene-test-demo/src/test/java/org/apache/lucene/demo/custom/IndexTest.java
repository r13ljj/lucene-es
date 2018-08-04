package org.apache.lucene.demo.custom;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogDocMergePolicy;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * <pre>
 *
 *  File: IndexTest.java
 *
 *  Copyright (c) 2018, jonex.com All Rights Reserved.
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
public class IndexTest {

    public final static String INDEX_DIR = "E:\\indices\\es-lucene";

    public static IndexWriter createWriter(){
        IndexWriter indexWriter = null;
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND)
                .setMaxBufferedDocs(Integer.MAX_VALUE)
                .setCommitOnClose(true)
                .setMaxBufferedDeleteTerms(1000)
                .setMergePolicy(new LogDocMergePolicy());
        try {
            indexWriter = new IndexWriter(FSDirectory.open(Paths.get(INDEX_DIR)), config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexWriter;
    }

    public static void main(String[] args) {
        IndexWriter writer = null;
        try {
            writer = createWriter();
            Document doc1 = new Document();
            doc1.add(new SortedNumericDocValuesField("goodsId", 1001L));
            doc1.add(new StringField("goodsTitle", "black short shoes", Field.Store.YES));
            doc1.add(new TextField("goodsDes", "this is a woman shoes which is black and refinement", Field.Store.NO));
            writer.addDocument(doc1);
            writer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    //NOOP
                }
            }
        }
    }

}
