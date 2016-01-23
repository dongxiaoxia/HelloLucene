package xyz.dongxiaoxia.hellolucene.controller;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.wltea.analyzer.lucene.IKAnalyzer;
import xyz.dongxiaoxia.hellolucene.util.LuceneUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongxiaoxia on 2016/1/20.
 * <p/>
 * Lucene 全文检索控制层
 */
@Controller
@RequestMapping("/lucene/")
public class LuceneController {

    @RequestMapping(value = "index")
    public String createIndex() {
        IndexWriter writer = null;
        try {
//            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
            IndexWriterConfig config = new IndexWriterConfig(new IKAnalyzer(true));
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            writer = LuceneUtils.getIndexWriter("F:\\Github项目\\HelloLucene\\indexPath", config);
            File dir = new File("F:\\Github项目\\HelloLucene\\DocPath");
            File[] files = dir.listFiles();
            if (ArrayUtils.isEmpty(files)) {
                throw new IllegalArgumentException("该目录下没有文件：" + dir.getAbsolutePath());
            }
            long start = System.currentTimeMillis();
            for (File file : files) {
                Document document = new Document();
                document.add(new StringField("name", file.getName(), Field.Store.YES));
                document.add(new StringField("path", file.getAbsolutePath(), Field.Store.YES));
                document.add(new LongField("size", file.length(), Field.Store.YES));
                document.add(new StringField("type", file.getName().substring(file.getName().lastIndexOf(".")+1), Field.Store.YES));
                StringBuffer buffer = new StringBuffer();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
                char[] c = new char[1024];
                while ((bufferedReader.read(c)) != -1) {
                    buffer.append(c);
                }
                bufferedReader.close();
                document.add(new TextField("content", buffer.toString(), Field.Store.YES));
                LuceneUtils.addIndex(writer, document);
            }
            LuceneUtils.closeIndexWriter(writer);
            long end = System.currentTimeMillis();
            System.out.println("索引完成，共耗时" + (end - start) + "ms");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            LuceneUtils.closeIndexWriter(writer);
        }
        return "success";
    }

    @RequestMapping(value = "search")
    public String search(@RequestParam(value = "search") String search, ModelMap modelMap) throws ParseException {
        IndexReader reader = LuceneUtils.getIndexReader(LuceneUtils.openFSDirectory("F:\\Github项目\\HelloLucene\\indexPath"));
        IndexSearcher searcher = LuceneUtils.getIndexSearcher(reader);
        long start = System.currentTimeMillis();

        QueryParser qp = new QueryParser("content", new IKAnalyzer(true));
        qp.setDefaultOperator(QueryParser.OR_OPERATOR);
        Query query = qp.parse(search);
        int total = LuceneUtils.searchTotalRecord(searcher, query);
        List<Document> list = LuceneUtils.query(searcher, query);
        List<xyz.dongxiaoxia.hellolucene.model.Document> documents = new ArrayList<xyz.dongxiaoxia.hellolucene.model.Document>();
        if (list != null && list.size() > 0) {
            for (Document document : list) {
                xyz.dongxiaoxia.hellolucene.model.Document doc = new xyz.dongxiaoxia.hellolucene.model.Document();
                doc.setContent(document.get("content"));
                doc.setName(document.get("name"));
                doc.setPath(document.get("path"));
//                doc.setSize(Long.valueOf(document.get("size")));
                doc.setType(document.get("type"));
                documents.add(doc);
            }
        }
        long end = System.currentTimeMillis();
        // LuceneUtils.closeIndexReader(reader);
        modelMap.addAttribute("document", documents);
        modelMap.addAttribute("total", total);
        modelMap.addAttribute("time", end - start);
        return "main";
    }
}
