package xyz.dongxiaoxia.hellolucene.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Created by dongxiaoxia on 2016/1/19.
 * <p/>
 * Lucene工具类(基于Lucene5.0封装)
 */
public class LuceneUtils {
    private static final LuceneManager luceneManager = LuceneManager.getInstance();
    private static Analyzer analyzer = new StandardAnalyzer();

    /**
     * 打开索引目录
     * @param luceneDir
     * @return
     */
    public static FSDirectory openFSDirectory(String luceneDir){
        FSDirectory directory = null;
        try {
            directory = FSDirectory.open(Paths.get(luceneDir));
            /**
             * 注意：isLock方法内部会试图去获取Lock，如果获取到Lock，会关闭它，否则return false 表示索引目录没有被锁，
             * 这也就是为什么unlock方法被从IndexWriter类中移除的原因
             */
            IndexWriter.isLocked(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return directory;
    }

    /**
     * 关闭索引目录并销毁
     * @param directory
     * @throws IOException
     */
    public static void closeDirectory(Directory directory) throws IOException{
        if (null != directory){
            directory.close();
            directory = null;
        }
    }

    /**
     * 获取IndexWriter
     * @param dir
     * @param config
     * @return
     */
    public static IndexWriter getIndexWriter(Directory dir, IndexWriterConfig config){
        return luceneManager.getIndexWriter(dir,config);
    }

    /**
     * 获取IndexWriter
     * @param directoryPath
     * @param config
     * @return
     */
    public static IndexWriter getIndexWriter(String directoryPath, IndexWriterConfig config){
        FSDirectory directory = openFSDirectory(directoryPath);
        return luceneManager.getIndexWriter(directory,config);
    }

    /**
     * 获取IndexReader
     * @param dir
     * @param enableNRTReader 是否开启NRTReader
     * @return
     */
    public static IndexReader getIndexReader(Directory dir, boolean enableNRTReader){
        return luceneManager.getIndexReader(dir, enableNRTReader);
    }

    /**
     * 获取IndexReader(默认不开启NRTReader)
     * @param dir
     * @return
     */
    public static IndexReader getIndexReader(Directory dir){
        return luceneManager.getIndexReader(dir);
    }

    /**
     * 获取IndexSearcher
     * @param reader IndexReader对象
     * @param executor 如果你需要开启多线程查询,请提供ExecutorService对象参数
     * @return
     */
    public static IndexSearcher getIndexSearcher(IndexReader reader,ExecutorService executor){
        return luceneManager.getIndexSearcher(reader, executor);
    }

    /**
     * 获取IndexSearcher对象(不支持多线程查询)
     * @param reader
     * @return
     */
    public static IndexSearcher getIndexSearcher(IndexReader reader){
        return luceneManager.getIndexSearcher(reader);
    }

    /**
     * 创建QueryParser对象
     * @param field
     * @param analyzer
     * @return
     */
    public static QueryParser createrQueryParser(String field, Analyzer analyzer){
        return new QueryParser(field,analyzer);
    }

    /**
     * 关闭IndexReader
     * @param reader
     */
    public static void closeIndexReader(IndexReader reader){
        if (null != reader){
            try {
                reader.close();
                reader = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭IndexWriter
     * @param writer
     */
    public static void closeIndexWriter(IndexWriter writer){
        luceneManager.closeIndexWriter(writer);
    }

    /**
     * 关闭IndexReader和IndexWriter
     * @param reader
     * @param writer
     */
    public static void closeAll(IndexReader reader, IndexWriter writer) {
        closeIndexReader(reader);
        closeIndexWriter(writer);
    }

    /**
     * 删除索引(注意：请自己关闭IndexWriter对象)
     * @param writer
     * @param field
     * @param value
     */
    public static void deleteIndex(IndexWriter writer,String field, String value){
        try {
            writer.deleteDocuments(new Term[]{new Term(field,value)});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除索引(注意：请自己关闭IndexWriter对象)
     * @param writer
     * @param query
     */
    public static void deleteIndex(IndexWriter writer, Query query){
        try {
            writer.deleteDocuments(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除索引(注意：请自己关闭IndexWriter对象)
     * @param writer
     * @param terms
     */
    public static void deleteIndexs(IndexWriter writer, Term[] terms){
        try {
            writer.deleteDocuments(terms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除索引(注意：请自己关闭IndexWriter对象)
     * @param writer
     * @param queries
     */
    public static void deleteIndexs(IndexWriter writer, Query[] queries){
        try {
            writer.deleteDocuments(queries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除所有索引
     * @param writer
     */
    public static void deleteAllIndex(IndexWriter writer){
        try {
            writer.deleteAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新索引文档
     * @param writer
     * @param term
     * @param document
     */
    public static void updateIndex(IndexWriter writer, Term term, Document document){
        try {
            writer.updateDocument(term, document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跟新索引文档
     * @param writer
     * @param field
     * @param value
     * @param document
     */
    public static void updateIndex(IndexWriter writer, String field, String value, Document document){
        updateIndex(writer,new Term(field,value),document);
    }

    /**
     * 添加索引文档
     * @param writer
     * @param document
     */
    public static void addIndex(IndexWriter writer, Document document){
        updateIndex(writer,null,document);
    }

    /**
     * 索引文件查询
     * @param searcher
     * @param query
     * @return
     */
    public static List query(IndexSearcher searcher,Query query){
        TopDocs topDocs = null;
        try {
            topDocs = searcher.search(query, Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        int length = scoreDocs.length;
        if (length <= 0){
            return Collections.emptyList();
        }
        List docList = new ArrayList();
        try {
            for (int i = 0; i<length; i++) {
                Document document = searcher.doc(scoreDocs[i].doc);
                docList.add(document);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return docList;
    }

    /**
     * 返回索引文档的总数(注意：请自己手动关闭IndexReader)
     * @param reader
     * @return
     */
    public static int getIndexTotalCount(IndexReader reader){
        return  reader.numDocs();
    }

    /**
     * 返回索引文档中最大文档ID(注意：请手动关闭IndexReader)
     * @param reader
     * @return
     */
    public static int getMaxDocId(IndexReader reader){
        return reader.maxDoc();
    }

    /**
     * 返回已经删除尚未提交的文档总数(注意：请自己手动关闭IndexReader)
     * @param reader
     * @return
     */
    public static int getDeleteDocNum(IndexReader reader){
        return getMaxDocId(reader) - getIndexTotalCount(reader);
    }

    /**
     * 根基docID查询索引文档
     * @param reader IndexReader对象
     * @param docID documentId
     * @param fieldToLoad 需要返回的field
     * @return
     */
    public static Document findDocumentByDocId(IndexReader reader, int docID, Set fieldToLoad){
        try {
            return reader.document(docID,fieldToLoad);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 根据docID查询索引文档
     * @param reader
     * @param docID
     * @return
     */
    public static Document findDocumentByDocId(IndexReader reader,int docID){
        return findDocumentByDocId(reader,docID,null);
    }

    /**
     * @Title: createHighlighter
     * @Description: 创建高亮器
     * @param query             索引查询对象
     * @param prefix            高亮前缀字符串
     * @param stuffix           高亮后缀字符串
     * @param fragmenterLength  摘要最大长度
     * @return
     */
    public static Highlighter createHighlighter(Query query, String prefix, String stuffix, int fragmenterLength) {
        Formatter formatter = new SimpleHTMLFormatter((prefix == null || prefix.trim().length() == 0) ?
                "" : prefix, (stuffix == null || stuffix.trim().length() == 0)?"" : stuffix);
        Scorer fragmentScorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(formatter, fragmentScorer);
        Fragmenter fragmenter = new SimpleFragmenter(fragmenterLength <= 0 ? 50 : fragmenterLength);
        highlighter.setTextFragmenter(fragmenter);
        return highlighter;
    }

    /**
     * @Title: highlight
     * @Description: 生成高亮文本
     * @param document          索引文档对象
     * @param highlighter       高亮器
     * @param analyzer          索引分词器
     * @param field             高亮字段
     * @return
     * @throws IOException
     * @throws InvalidTokenOffsetsException
     */
    public static String highlight(Document document,Highlighter highlighter,Analyzer analyzer,String field) throws IOException {
        List<IndexableField> list = document.getFields();
        for (IndexableField fieldable : list) {
            String fieldValue = fieldable.stringValue();
            if(fieldable.name().equals(field)) {
                try {
                    fieldValue = highlighter.getBestFragment(analyzer, field, fieldValue);
                } catch (InvalidTokenOffsetsException e) {
                    fieldValue = fieldable.stringValue();
                }
                return (fieldValue == null || fieldValue.trim().length() == 0)? fieldable.stringValue() : fieldValue;
            }
        }
        return null;
    }

    /**
     * @Title: searchTotalRecord
     * @Description: 获取符合条件的总记录数
     * @param query
     * @return
     * @throws IOException
     */
    public static int searchTotalRecord(IndexSearcher search,Query query) {
        ScoreDoc[] docs = null;
        try {
            TopDocs topDocs = search.search(query, Integer.MAX_VALUE);
            if(topDocs == null || topDocs.scoreDocs == null || topDocs.scoreDocs.length == 0) {
                return 0;
            }
            docs = topDocs.scoreDocs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return docs.length;
    }

    /**
     * @Title: pageQuery
     * @Description: Lucene分页查询
     * @param searcher
     * @param query
     * @param page
     * @throws IOException
     */
    public static void pageQuery(IndexSearcher searcher,Directory directory,Query query,Page page) {
        int totalRecord = searchTotalRecord(searcher,query);
        //设置总记录数
        page.setTotalRecord(totalRecord);
        TopDocs topDocs = null;
        try {
            topDocs = searcher.searchAfter(page.getAfterDoc(),query, page.getPageSize());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List docList = new ArrayList();
        ScoreDoc[] docs = topDocs.scoreDocs;
        int index = 0;
        for (ScoreDoc scoreDoc : docs) {
            int docID = scoreDoc.doc;
            Document document = null;
            try {
                document = searcher.doc(docID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(index == docs.length - 1) {
                page.setAfterDoc(scoreDoc);
                page.setAfterDocId(docID);
            }
            docList.add(document);
            index++;
        }
        page.setItems(docList);
        closeIndexReader(searcher.getIndexReader());
    }

    /**
     * @Title: pageQuery
     * @Description: 分页查询[如果设置了高亮,则会更新索引文档]
     * @param searcher
     * @param directory
     * @param query
     * @param page
     * @param highlighterParam
     * @param writerConfig
     * @throws IOException
     */
    public static void pageQuery(IndexSearcher searcher,Directory directory,Query query,Page page,HighlighterParam highlighterParam,IndexWriterConfig writerConfig) throws IOException {
        IndexWriter writer = null;
        //若未设置高亮
        if(null == highlighterParam || !highlighterParam.isHighlight()) {
            pageQuery(searcher,directory,query, page);
        } else {
            int totalRecord = searchTotalRecord(searcher,query);
            System.out.println("totalRecord:" + totalRecord);
            //设置总记录数
            page.setTotalRecord(totalRecord);
            TopDocs topDocs = searcher.searchAfter(page.getAfterDoc(),query, page.getPageSize());
            List docList = new ArrayList();
            ScoreDoc[] docs = topDocs.scoreDocs;
            int index = 0;
            writer = getIndexWriter(directory, writerConfig);
            for (ScoreDoc scoreDoc : docs) {
                int docID = scoreDoc.doc;
                Document document = searcher.doc(docID);
                String content = document.get(highlighterParam.getFieldName());
                if(null != content && content.trim().length() > 0) {
                    //创建高亮器
                    Highlighter highlighter = LuceneUtils.createHighlighter(query,
                            highlighterParam.getPrefix(), highlighterParam.getStuffix(),
                            highlighterParam.getFragmenterLength());
                    String text = highlight(document, highlighter, analyzer, highlighterParam.getFieldName());
                    //若高亮后跟原始文本不相同，表示高亮成功
                    if(!text.equals(content)) {
                        Document tempdocument = new Document();
                        List<IndexableField> indexableFieldList = document.getFields();
                        if(null != indexableFieldList && indexableFieldList.size() > 0) {
                            for(IndexableField field : indexableFieldList) {
                                if(field.name().equals(highlighterParam.getFieldName())) {
                                    tempdocument.add(new TextField(field.name(), text, Field.Store.YES));
                                } else {
                                    tempdocument.add(field);
                                }
                            }
                        }
                        updateIndex(writer, new Term(highlighterParam.getFieldName(),content), tempdocument);
                        document = tempdocument;
                    }
                }
                if(index == docs.length - 1) {
                    page.setAfterDoc(scoreDoc);
                    page.setAfterDocId(docID);
                }
                docList.add(document);
                index++;
            }
            page.setItems(docList);
        }
        closeIndexReader(searcher.getIndexReader());
        closeIndexWriter(writer);
    }
}
