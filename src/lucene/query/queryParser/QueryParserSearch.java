package lucene.query.queryParser;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chcao on 5/3/2017. 通过自定义QueryParser的方式实现 1.禁用模糊和通配符搜索，
 * 2.以及扩展基于数字和日期的搜索等功能
 *
 */
public class QueryParserSearch {
	private Directory directory;
	private IndexReader reader;

	public QueryParserSearch() {
		/** 文件大小 */
		int[] sizes = { 90, 10, 20, 10, 60, 50 };
		/** 文件名 */
		String[] names = { "Michael.java", "Scofield.ini", "Tbag.txt", "Jack", "Jade", "Jadyer" };
		/** 文件内容 */
		String[] contents = { "my blog is https://jadyer.github.io/",
							"my github is https://github.com/jadyer",
							"my name is jadyer",
							"I am a Java Developer",
							"I am from Haerbin",
							"I like java of Lucene" };
		/** 文件日期 */
		Date[] dates = new Date[sizes.length];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		IndexWriter writer = null;
		Document doc = null;
		try {
			dates[0] = sdf.parse("20130407 15:25:30");
			dates[1] = sdf.parse("20130407 16:30:45");
			dates[2] = sdf.parse("20130213 11:15:25");
			dates[3] = sdf.parse("20130808 09:30:55");
			dates[4] = sdf.parse("20130526 13:54:22");
			dates[5] = sdf.parse("20130701 17:35:34");
			//相对路径好像有问题，改用绝对路径试试看
			// directory = FSDirectory.open(new File("myExample/01_index/"));
			directory = FSDirectory.open(new File("D:/lucenetest/myExample/01_index/"));

			writer = new IndexWriter(directory,
					new IndexWriterConfig(Version.LUCENE_45, new StandardAnalyzer(Version.LUCENE_45)));
			writer.deleteAll();
			for (int i = 0; i < sizes.length; i++) {
				doc = new Document();
				doc.add(new NumericDocValuesField("size", sizes[i]));
				doc.add(new StringField("name", names[i], Field.Store.YES));
				doc.add(new StringField("content", contents[i], Field.Store.NO));
				doc.add(new NumericDocValuesField("date", dates[i].getTime()));
				writer.addDocument(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException ce) {
					ce.printStackTrace();
				}
			}
		}
	}

	/**
	 * 测试一下搜索效果
	 */
	public static void main(String[] args) {
		QueryParserSearch queryParserSearch = new QueryParserSearch();
		queryParserSearch.searchByCustomQueryParser("name:Jadk~");
		queryParserSearch.searchByCustomQueryParser("name:Ja??er");
		System.out.println("------------------------------------------------------------------------");
		queryParserSearch.searchByCustomQueryParser("name:Jade");
		System.out.println("------------------------------------------------------------------------");
		queryParserSearch.searchByCustomQueryParser("name:[h TO n]");
		System.out.println("------------------------------------------------------------------------");
		queryParserSearch.searchByCustomQueryParser("size:[20 TO 80]");
		System.out.println("------------------------------------------------------------------------");
		queryParserSearch.searchByCustomQueryParser("date:[20130407 TO 20130701]");
	}

	/**
	 * 获取IndexReader实例
	 */
	private IndexReader getIndexReader() {
		try {
			if (reader == null) {
				reader = DirectoryReader.open(directory);
			} else {
				// if the index was changed since the provided reader was
				// opened, open and return a new reader; else,return null
				// 如果当前reader在打开期间index发生改变，则打开并返回一个新的IndexReader，否则返回null
				IndexReader ir = DirectoryReader.openIfChanged((DirectoryReader)reader);
				if (ir != null) {
					reader.close(); // 关闭原reader
					reader = ir; // 赋予新reader
				}
			}
			return reader;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; // 发生异常则返回null
	}

	/**
	 * 自定义QueryParser的搜索
	 *
	 * @param expr
	 *            搜索的表达式
	 */
	public void searchByCustomQueryParser(String expr) {
		IndexSearcher searcher = new IndexSearcher(this.getIndexReader());
		QueryParser parser = new MyQueryParser(Version.LUCENE_45, "content", new StandardAnalyzer(Version.LUCENE_45));
		try {
			Query query = parser.parse(expr);
			TopDocs tds = searcher.search(query, 10);
			for (ScoreDoc sd : tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				/*
				 * System.out.print("文档编号=" + sd.doc + "  文档权值=" +
				 * doc.getBoost() + "  文档评分=" + sd.score + "   ");
				 * 貌似4.5之后文档没有boost的相关方法了。权值只能加在field里面
				 */

				System.out.print("文档编号=" + sd.doc + "  文档评分=" + sd.score + "   ");

				System.out.print("size=" + doc.get("size") + "  date=");
				System.out.print(
						new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date(Long.parseLong(doc.get("date")))));
				System.out.println("  name=" + doc.get("name"));
			}
		} catch (ParseException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
