package com.jonex.search.lucene.docvalues;

import com.jonex.search.lucene.IndexManager;
import com.jonex.search.lucene.util.ByteUtil;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *
 *  File: DocValuesTest.java
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
public class DocValuesTest {

    public void addDocValuesDocument(){
        IndexWriter writer = IndexManager.getInstance().createWriter();
        FieldType fieldType = new FieldType();
        fieldType.setStored(true);//存储
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);//设置倒排表存储哪些数据
        fieldType.setDocValuesType(DocValuesType.NUMERIC);//设置DocValues类型

        Document doc = new Document();
        //String field=DocValuesType.SORTED
        doc.add(new SortedDocValuesField("id", new BytesRef("01011")));
        //numeric field=DocValuesType.NUMERIC
        doc.add(new DoubleDocValuesField("price", Double.doubleToRawLongBits(25.258d)));
        try {
            writer.addDocument(doc);
            writer.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer != null){
                try {
                    writer.forceMerge(1);
                    writer.close();
                } catch (IOException e) {
                    //NOOP
                }
            }
        }
    }

    public void readDocValues(){
        DirectoryReader directoryReader = null;
        try {
            directoryReader = DirectoryReader.open(FSDirectory.open(new File(IndexManager.INDEX_DIR).toPath()));
            //读取第一个segment,需要提前merge为1个segment
            SortedDocValues sortedDocValues = DocValues.getSorted(directoryReader.leaves().get(0).reader(), "id");
            NumericDocValues numericDocValues = DocValues.getNumeric(directoryReader.leaves().get(0).reader(), "price");
            System.out.println("id:"+sortedDocValues.get(0).utf8ToString());
            System.out.println("price:"+Double.longBitsToDouble(numericDocValues.get(0)));
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                directoryReader.close();
            } catch (IOException e) {
                //NOOP
            }
        }
    }

    public static void main(String[] args) {
        BytesRef br = new BytesRef("01011");
        int r = ByteUtil.bytesToInt(br.bytes);
        System.out.println(r);
        DocValuesTest test = new DocValuesTest();
        test.addDocValuesDocument();
        test.readDocValues();
    }
}
