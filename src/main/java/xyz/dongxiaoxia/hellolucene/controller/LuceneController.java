package xyz.dongxiaoxia.hellolucene.controller;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.wltea.analyzer.lucene.IKAnalyzer;
import xyz.dongxiaoxia.hellolucene.util.FileUtils;
import xyz.dongxiaoxia.hellolucene.util.LuceneUtils;

import javax.annotation.PostConstruct;
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
    @PostConstruct
    public String createIndex() {
        IndexWriter writer = null;
        try {
            IndexWriterConfig config = new IndexWriterConfig(new IKAnalyzer(true));
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
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
                document.add(new StringField("type", file.getName().substring(file.getName().lastIndexOf(".") + 1), Field.Store.YES));
                document.add(new StringField("md5", FileUtils.fileMd5(file.getAbsolutePath()), Field.Store.YES));
                StringBuffer buffer = new StringBuffer();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
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
    public String search(@RequestParam(value = "search") String search, ModelMap modelMap) throws ParseException, IOException {
        if (StringUtils.isEmpty(search)) {
            modelMap.addAttribute("total", 0);
            modelMap.addAttribute("time", 0);
            modelMap.addAttribute("search", search);
            return "main";
        }
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
                doc.setContent(LuceneUtils.highlight(document, LuceneUtils.createHighlighter(query, "<span style=\"color:red\">", "</span>", 200), new IKAnalyzer(true), "content"));
                doc.setName(LuceneUtils.highlight(document, LuceneUtils.createHighlighter(query, "<span style=\"color:red\">", "</span>", 200), new IKAnalyzer(true), "name"));
                doc.setPath(document.get("path"));
//                doc.setSize(Long.valueOf(document.get("size")));
                doc.setType(document.get("type"));
                doc.setMd5(document.get("md5"));
                documents.add(doc);
            }
        }
        long end = System.currentTimeMillis();
        // LuceneUtils.closeIndexReader(reader);
        modelMap.addAttribute("document", documents);
        modelMap.addAttribute("total", total);
        modelMap.addAttribute("time", end - start);
        modelMap.addAttribute("search", search);
        return "main";
    }

    @RequestMapping(value = "download/{md5}")
    public ResponseEntity<byte[]> download(@PathVariable("md5") String md5) throws ParseException, IOException {
        IndexReader reader = LuceneUtils.getIndexReader(LuceneUtils.openFSDirectory("F:\\Github项目\\HelloLucene\\indexPath"));
        IndexSearcher searcher = LuceneUtils.getIndexSearcher(reader);
        List<Document> list = LuceneUtils.query(searcher, new TermQuery(new Term("md5", md5)));
        if (list != null && list.size() > 0) {
            Document document = list.get(0);
            String path = document.get("path");
            File file = new File(path);
            HttpHeaders httpHeaders = new HttpHeaders();
            String fileName = new String(file.getName().getBytes("UTF-8"), "iso-8859-1");//解决中文名称乱码问题
            httpHeaders.setContentDispositionFormData("attachment", fileName);
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), httpHeaders, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "detail/{md5}")
    public String detail(@PathVariable("md5") String md5, ModelMap modelMap) throws IOException {
        IndexReader reader = LuceneUtils.getIndexReader(LuceneUtils.openFSDirectory("F:\\Github项目\\HelloLucene\\indexPath"));
        IndexSearcher searcher = LuceneUtils.getIndexSearcher(reader);
        Query query = new TermQuery(new Term("md5", md5));
        List<Document> list = LuceneUtils.query(searcher, query);
        if (list != null && list.size() > 0) {
            Document document = list.get(0);
            String path = document.get("path");
            File file = new File(path);
            xyz.dongxiaoxia.hellolucene.model.Document doc = new xyz.dongxiaoxia.hellolucene.model.Document();
            doc.setName(file.getName());
            doc.setContent("<pre>" + document.get("content")+ "</pre>");
            modelMap.addAttribute("document", doc);
        } else {

        }
        return "detail";
    }
}
