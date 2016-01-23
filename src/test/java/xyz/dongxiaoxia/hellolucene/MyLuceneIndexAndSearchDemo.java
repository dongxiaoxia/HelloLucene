/**
 * IK 涓枃鍒嗚瘝  鐗堟湰 5.0
 * IK Analyzer release 5.0
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 婧愪唬鐮佺敱鏋楄壇鐩�(linliangyi2005@gmail.com)鎻愪緵
 * 鐗堟潈澹版槑 2012锛屼箤榫欒尪宸ヤ綔瀹�
 * provided by Linliangyi and copyright 2012 by Oolong studio
 * 
 * 
 */
package xyz.dongxiaoxia.hellolucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.wltea.analyzer.lucene.IKAnalyzer;
import xyz.dongxiaoxia.hellolucene.util.LuceneUtils;

import java.io.IOException;

/**
 * 使用IKAnalyzer进行Lucene索引和查询的演示 2012-3-2
 *
 * 以下是结合Lucene4.0 API的写法
 *
 */
public class MyLuceneIndexAndSearchDemo {

	/**
	 * 模拟： 创建一个单条记录的索引，并对其进行搜索
	 *
	 * @param args
	 */
	public static void main(String[] args) throws ParseException, IOException {
		// 搜索过程**********************************
			// 实例化搜索器
			IndexSearcher searcher = LuceneUtils.getIndexSearcher(LuceneUtils.getIndexReader(LuceneUtils.openFSDirectory("F:\\Github项目\\HelloLucene\\indexPath")));

			String keyword = "程序员的就业指导";
			// 使用QueryParser查询分析器构造Query对象
			QueryParser qp = new QueryParser("content",new IKAnalyzer(true));
			qp.setDefaultOperator(QueryParser.AND_OPERATOR);
			Query query = qp.parse("java");
			System.out.println("Query = " + query);

			// 搜索相似度最高的5条记录
			TopDocs topDocs = searcher.search(query, 5);
			System.out.println("命中：" + topDocs.totalHits);
			// 输出结果
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (int i = 0; i < topDocs.totalHits; i++) {
				Document targetDoc = searcher.doc(scoreDocs[i].doc);
				Highlighter highlighterParam = LuceneUtils.createHighlighter(query, "<span>", "</span>", 100);

				System.out.println("内容：" + LuceneUtils.highlight(targetDoc, highlighterParam, new IKAnalyzer(true), "content"));
			}

	}
}
