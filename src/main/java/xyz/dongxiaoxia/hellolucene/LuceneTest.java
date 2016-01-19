package xyz.dongxiaoxia.hellolucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 测试lucene jar包
 *
 * @author Administrator
 * @create 2016-01-19 12:46
 */
public class LuceneTest {
    public static void main(String[] args) throws IOException {
        Directory directory = FSDirectory.open(Paths.get("indexPath"));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);
        Document document = new Document();
        document.add(new StringField("name", "lucene", Field.Store.YES));
        indexWriter.addDocument(document);
        indexWriter.close();

        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        System.out.println(isearcher.count(new TermQuery(new Term("name","lucene"))));
    }
}
