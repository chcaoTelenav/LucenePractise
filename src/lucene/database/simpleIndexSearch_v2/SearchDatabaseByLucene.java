package lucene.database.simpleIndexSearch_v2;

import java.io.File;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Created by chcao on 5/8/2017.
 *
 * 这个是模拟项目。即数据原先在数据库中，然后对这些数据建立index，存到文件中。
 * 然后当进行search操作的时候，其实就是对那些index进行了查找。
 *
 * 这是对数据库中数据进行搜索，其实是搜索了之前创造的index文件
 *
 * v2
 */
public class SearchDatabaseByLucene {

	private static final int TOP_NUM = 100;//显示数

	//这个方法是从一个field里面搜索一个关键字出来。
	public boolean searchData(String indexpath, String keywords) {
		try {
			Directory directory = FSDirectory.open(new File(indexpath));
			IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
			//普通的查询语句，默认field是schoolInfo
			QueryParser parser = new QueryParser(Version.LUCENE_45, "schoolInfo", new StandardAnalyzer(Version.LUCENE_45));
			Query query = parser.parse(keywords);
			//添加一个collector，用来提供结果的输出数量和排序的功能，这里将排序功能关闭了
			TopScoreDocCollector collector = TopScoreDocCollector.create(TOP_NUM, false);

			long start = new Date().getTime();

			indexSearcher.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			for (int i = 0; i < hits.length; i++) {
				Document document = indexSearcher.doc(hits[i].doc);
				System.out.print("文档编号=" + hits[i].doc + "  文档评分=" + hits[i].score + "   ");
				System.out.println(document.getField("schoolId") + " " + document.getField("schoolName") + " "
						+ document.getField("schoolInfo"));
			}

			long end = new Date().getTime();
			System.out.println("Found " + hits.length + " documents(s) in " + (end - start) + " ms that matched query: " + keywords);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {

		}

	}


}