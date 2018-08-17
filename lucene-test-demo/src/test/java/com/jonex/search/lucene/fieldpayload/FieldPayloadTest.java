package com.jonex.search.lucene.fieldpayload;

import com.jonex.search.lucene.IndexManager;
import com.jonex.search.lucene.analysis.PayloadSimilarity;
import com.jonex.search.lucene.attribute.MyWhitespaceTokenizer;
import com.jonex.search.lucene.util.ByteUtil;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.payloads.MaxPayloadFunction;
import org.apache.lucene.queries.payloads.PayloadScoreQuery;
import org.apache.lucene.search.*;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * <pre>
 *
 *  File: FieldPayloadTest.java
 *
 *  Copyright (c) 2018, jonex.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/7/26				lijunjun				Initial.
 *
 * </pre>
 */
public class FieldPayloadTest {

    public final static String PAYLOAD_INDEX_DIR = "E:\\indices\\fieldpayload";
    public final static String PAYLOAD_INDEX_DIR2 = "E:\\indices\\fieldpayload2";


    public void addDocument(){
        IndexWriter writer = null;
        try {
            FieldPayloadAnalyzer analyzer = new FieldPayloadAnalyzer();
            writer = IndexManager.getInstance().createWriter(PAYLOAD_INDEX_DIR, analyzer);
            Document doc1 = new Document();
            //doc1.add(new Field("goodsId", "111", TextField.TYPE_STORED));
            FieldType idFieldType = new FieldType();
            idFieldType.setStored(true);
            idFieldType.setStoreTermVectors(true);
            idFieldType.setStoreTermVectorOffsets(true);
            idFieldType.setStoreTermVectorPositions(true);
            idFieldType.setStoreTermVectorPayloads(true);
            idFieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
            doc1.add(new Field("goodsId", "111", idFieldType));
            doc1.add(new Field("goodsTitle", "black apple cellphone iphone7 lulu", TextField.TYPE_STORED));
            doc1.add(new SortedNumericDocValuesField("price", Double.doubleToLongBits(4999.0d)));
            analyzer.setPayloadData("goodsTitle", ByteUtil.floatToBytes(1.1f), 0, 1);
            writer.addDocument(doc1);
            Document doc2 = new Document();
            //doc2.add(new Field("goodsId", "222", TextField.TYPE_STORED));
            doc2.add(new Field("goodsId", "222", idFieldType));
            doc2.add(new Field("goodsTitle", "white Himalayan crystalline salt lulu", TextField.TYPE_STORED));
            doc2.add(new SortedNumericDocValuesField("price", Double.doubleToLongBits(0.9d)));
            analyzer.setPayloadData("goodsTitle", ByteUtil.floatToBytes(2.2f), 0, 2);
            writer.addDocument(doc2);
            Document doc3 = new Document();
            //doc3.add(new Field("goodsId", "333", TextField.TYPE_STORED));
            doc3.add(new Field("goodsId", "333", idFieldType));
            doc3.add(new Field("goodsTitle", "tea switch keybord cherry lulu lulu", TextField.TYPE_STORED));
            doc3.add(new SortedNumericDocValuesField("price", Double.doubleToLongBits(628.5d)));
            analyzer.setPayloadData("goodsTitle", ByteUtil.floatToBytes(3.3f), 0, 3);
            writer.addDocument(doc3);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (writer != null){
                try {
                    writer.commit();
                    writer.forceMerge(1);
                    writer.close();
                } catch (IOException e) {
                    //NOOP
                }
            }
        }
    }

    public void hasPayload(){
        DirectoryReader directoryReader = null;
        try {
            directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(PAYLOAD_INDEX_DIR)));
            LeafReader reader = directoryReader.leaves().get(0).reader();
            FieldInfos fieldInfos = reader.getFieldInfos();
            System.out.println("goodsId has payload:"+fieldInfos.fieldInfo("goodsId").hasPayloads());
            System.out.println("goodsTitle has payload:"+fieldInfos.fieldInfo("goodsTitle").hasPayloads());
            System.out.println("price has payload:"+fieldInfos.fieldInfo("price").hasPayloads());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (directoryReader != null){
                try {
                    directoryReader.close();
                } catch (IOException e) {
                    //NOOP
                }
            }
        }

    }

    public void getPaylaod(){
        IndexReader indexReader = null;
        try {
            indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(PAYLOAD_INDEX_DIR)));
            TermsEnum termsEnum = MultiFields.getFields(indexReader).terms("goodsTitle").iterator();
            PostingsEnum payloadPosting = null;
            PostingsEnum docIdPosting = null;
            PostingsEnum allPosting = null;
            int docId = 0;
            int docFreq = 0;
            int position = 0;
            int offset = 0;
            AttributeSource attributeSource = null;
            while (termsEnum.next() != null){
                String termText = termsEnum.term().utf8ToString();
                payloadPosting = termsEnum.postings(payloadPosting, PostingsEnum.PAYLOADS);
                docIdPosting = termsEnum.postings(docIdPosting, PostingsEnum.NONE);
                allPosting = termsEnum.postings(allPosting, PostingsEnum.ALL);
                docFreq = termsEnum.docFreq();
                attributeSource = termsEnum.attributes();
                //System.out.println("next doc:"+docIdPosting.nextDoc());
                while ((docId = payloadPosting.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS){
                    int freq = payloadPosting.freq();
                    for (int i=0; i<freq; i++){
                        payloadPosting.nextPosition();
                        BytesRef payload = payloadPosting.getPayload();
                        System.out.println("term:"+termText+"=doc:"+docId+" docFreq:"+docFreq+" position:"+payloadPosting.nextPosition()+" payload:"+((payload != null) ? ByteUtil.bytesToFloat(payload.bytes) : null));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                indexReader.close();
            } catch (IOException e) {
                //NOOP
            }
        }

    }

    public void mixupMultiValue(){
        IndexWriterConfig writerConfig = new IndexWriterConfig(new StandardAnalyzer());
        writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = null;
        Directory directory = null;
        try {
            /*directory = FSDirectory.open(Paths.get(PAYLOAD_INDEX_DIR2));
            writer = new IndexWriter(directory, writerConfig);
            Document doc = new Document();
            Field field = new TextField("field", "", Field.Store.NO);
            TokenStream stream = new WhitespaceTokenizer();
            //字符流设置源头reader
            ((Tokenizer)stream).setReader(new StringReader("here we go"));
            //设置字段从此字符流获取字符
            field.setTokenStream(stream);
            doc.add(field);

            Field field2 = new TextField("field", "", Field.Store.NO);
            Token payloadToken = new Token("payloadToken", 0, 12);
            payloadToken.setPayload(new BytesRef("test"));
            stream = new CannedTokenStream(payloadToken);
            System.out.println("==========multi field2 hasPayload:"+stream.hasAttribute(PayloadAttribute.class));
            field2.setTokenStream(stream);
            doc.add(field2);

            Field field3 = new TextField("field", "", Field.Store.NO);
            stream = new WhitespaceTokenizer();
            ((Tokenizer)stream).setReader(new StringReader("nopayload"));
            field3.setTokenStream(stream);
            doc.add(field3);

            writer.addDocument(doc);
            writer.forceMerge(1);*/

            DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(PAYLOAD_INDEX_DIR2)));
            LeafReader leafReader = directoryReader.leaves().get(0).reader();
            PostingsEnum postingsEnum = leafReader.postings(new Term("field", "payloadToken"), PostingsEnum.PAYLOADS);
            postingsEnum.nextDoc();
            postingsEnum.nextPosition();
            BytesRef payload = postingsEnum.getPayload();
            System.out.println("===========multi get payload:"+payload.utf8ToString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null){
                try {
                    writer.close();
                    directory.close();
                } catch (IOException e) {
                    //NOOP
                }
            }
        }

    }

    public void search(){
        try {
            IndexSearcher searcher = IndexManager.getInstance().createSearcher(PAYLOAD_INDEX_DIR, new StandardAnalyzer());
            searcher.setSimilarity(new PayloadSimilarity());
            Query query = new PayloadScoreQuery(new SpanTermQuery(new Term("goodsTitle", "himalayan")), new MaxPayloadFunction());
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
                String goodsId = document.get("goodsId");
                String goodsTitle = document.get("goodsTitle");
                BytesRef price = document.getBinaryValue("price");
                System.out.println("docId:"+docID+",goodsId:"+goodsId+",goodsTitle:"+goodsTitle+",price:"+price+",score:"+score);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }



    public static void main(String[] args) {
        FieldPayloadTest test = new FieldPayloadTest();
        //test.addDocument();
        //test.hasPayload();
        //test.getPaylaod();
        //test.mixupMultiValue();
        test.search();
    }

}
