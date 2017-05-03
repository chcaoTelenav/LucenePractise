package lucene.query.queryParser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lucene Demo
 * Author: herui
 * DateTime: 2014-3-31 下午3:39:54
 */
public class MultiFieldQueryParserTest {

	/**
	 * 根据内容，构建索引
	 * @param analyzer
	 * @param directory
	 * @param items
	 * @return
	 */
	private boolean buildIndexer(Analyzer analyzer, Directory directory, List<Item> items) {
		IndexWriter iwriter = null;
		try {
			// 配置索引
			iwriter = new IndexWriter(directory, new IndexWriterConfig(
					Version.LUCENE_45, analyzer));
			// 删除所有document
			iwriter.deleteAll();
			// 将文档信息存入索引
			Document doc[] = new Document[items.size()];
			for (int i = 0; i < items.size(); i++) {
				doc[i] = new Document();

				Item item = items.get(i);
				java.lang.reflect.Field[] fields = item.getClass().getDeclaredFields();
				for (java.lang.reflect.Field field : fields) {
					String fieldName = field.getName();
					String getMethodName = "get"+toFirstLetterUpperCase(fieldName);
					Object obj = item.getClass().getMethod(getMethodName).invoke(item);
					doc[i].add(new Field(fieldName, (String)obj, TextField.TYPE_STORED));
				}

				iwriter.addDocument(doc[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				iwriter.close();
			} catch (IOException e) {
			}
		}
		return true;
	}

	/**
	 * 根据keyword搜索索引
	 * @param analyzer
	 * @param directory
	 * @param keyword
	 * @return
	 */
	public List<Item> searchIndexer(Analyzer analyzer, Directory directory, String keyword) {
		DirectoryReader ireader = null;
		List<Item> result = new ArrayList<Item>();
		try {
			// 设定搜索目录
			ireader = DirectoryReader.open(directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);

			// 对多field进行搜索
			java.lang.reflect.Field[] fields = Item.class.getDeclaredFields();
			int length = fields.length;
			String[] multiFields = new String[length];
			for (int i = 0; i < length; i++) {
				multiFields[i] = fields[i].getName();
			}
			MultiFieldQueryParser parser = new MultiFieldQueryParser(
					Version.LUCENE_45, multiFields, analyzer);

			// 设定具体的搜索词
			Query query = parser.parse(keyword);
			ScoreDoc[] hits = isearcher.search(query, null, 10).scoreDocs;

			for (int i = 0; i < hits.length; i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);
				Item item = new Item();
				for (String field : multiFields) {
					String setMethodName = "set"+toFirstLetterUpperCase(field);
					item.getClass().getMethod(setMethodName, String.class).invoke(item, hitDoc.get(field));
				}
				result.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				ireader.close();
				directory.close();
			} catch (IOException e) {
			}
		}
		return result;
	}

	/**
	 * 首字母转大写
	 * @param str
	 * @return
	 */
	public static String toFirstLetterUpperCase(String str) {
		if(str == null || str.length() < 2){
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
	}

	public static void main(String[] args) throws Exception {
		MultiFieldQueryParserTest demo = new MultiFieldQueryParserTest();
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);

		List<Item> items = new ArrayList<Item>();
		items.add(new Item("1", "first", "This is the text to be greatly indexed."));
		items.add(new Item("2", "second", "This is great"));
		items.add(new Item("3", "third", "I love apple and pear. "));
		items.add(new Item("4", "four", "我是中国人"));
		items.add(new Item("5", "five", "我叫何瑞"));

		// 索引存到内存中的目录
		//Directory directory = new RAMDirectory();
		// 索引存储到硬盘
		File file = new File("D:/lucenetest/myExample/02_index/");
		Directory directory = FSDirectory.open(file);
		demo.buildIndexer(analyzer, directory, items);
		List<Item> result = demo.searchIndexer(analyzer, directory, "中国");

		for (Item item : result) {
			System.out.println(item.toString());
		}
	}
}