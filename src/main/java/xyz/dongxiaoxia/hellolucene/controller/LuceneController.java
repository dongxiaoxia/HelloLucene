package xyz.dongxiaoxia.hellolucene.controller;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.dongxiaoxia.hellolucene.util.LuceneUtils;

import java.io.*;

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
            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
            writer = LuceneUtils.getIndexWriter("F:\\Github项目\\HelloLucene\\indexPath", config);
            File dir = new File("F:\\Github项目\\HelloLucene\\DocPath");
            File[] files = dir.listFiles();
            if (ArrayUtils.isEmpty(files)) {
                throw new IllegalArgumentException("该目录下没有文件：" + dir.getAbsolutePath());
            }
            for (File file : files) {
                Document document = new Document();
                document.add(new StringField("name", file.getName(), Field.Store.YES));
                document.add(new StringField("path", file.getAbsolutePath(), Field.Store.YES));
                document.add(new LongField("size", file.length(), Field.Store.YES));
                document.add(new StringField("type", file.getName().substring(file.getName().lastIndexOf(".")), Field.Store.YES));
                StringBuffer buffer = new StringBuffer();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                char[] c = new char[1024];
                while ((bufferedReader.read(c)) != -1) {
                    buffer.append(c);
                }
                bufferedReader.close();
                document.add(new TextField("content", buffer.toString(), Field.Store.NO));
                LuceneUtils.addIndex(writer, document);
            }
            LuceneUtils.closeIndexWriter(writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            LuceneUtils.closeIndexWriter(writer);
        }
        return "success";
    }

    @RequestMapping(value = "search")
    public String search(@RequestParam(value = "search") String search,ModelMap modelMap){
        IndexReader reader = LuceneUtils.getIndexReader(LuceneUtils.openFSDirectory("F:\\Github项目\\HelloLucene\\indexPath"));
        IndexSearcher searcher = LuceneUtils.getIndexSearcher(reader);
        long start = System.currentTimeMillis();
        int total = LuceneUtils.searchTotalRecord(searcher, new TermQuery(new Term("content", search)));
        long end = System.currentTimeMillis();
       // LuceneUtils.closeIndexReader(reader);
        modelMap.addAttribute("total", total);
        modelMap.addAttribute("time",end -start);
        return "main";
    }
}
