package com.jonex.search.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <pre>
 *
 *  File: IndexManager.java
 *
 *  Copyright (c) 2018, jonex.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/7/25				lijunjun				Initial.
 *
 * </pre>
 */
public class IndexManager {

    public final static String INDEX_DIR = "E:\\indices\\index2";

    private IndexWriter indexWriter;
    private IndexSearcher indexSearcher;

    private IndexManager(){

    }

    private static class IndexManagerHolder{
        private static IndexManager INSTANCE = new IndexManager();
    }

    public static IndexManager getInstance(){
        return IndexManagerHolder.INSTANCE;
    }


    public IndexWriter createWriter(){
        return createWriter(INDEX_DIR, new StandardAnalyzer());
    }

    public IndexWriter createWriter(String indexDir, Analyzer analyzer){
        if (indexWriter != null){
            return indexWriter;
        }
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND)
            .setMaxBufferedDocs(Integer.MAX_VALUE)
            .setCommitOnClose(true)
            .setMaxBufferedDeleteTerms(1000)
            .setMergePolicy(new LogDocMergePolicy());
        try {
            indexWriter = new IndexWriter(FSDirectory.open(Paths.get(indexDir)), config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexWriter;
    }

    public IndexSearcher createSearcher(){
        return createSearcher(INDEX_DIR, new StandardAnalyzer());
    }

    public IndexSearcher createSearcher(String indexDir, Analyzer analyzer){
        if (indexSearcher != null){
            return indexSearcher;
        }
        try {
            indexSearcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(indexDir))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexSearcher;
    }


    public static void main(String[] args) {
        IndexManager manager = IndexManager.getInstance();
        IndexWriter writer = null;
        try {
            writer = manager.createWriter();
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
