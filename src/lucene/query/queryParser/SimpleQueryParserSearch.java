package lucene.query.queryParser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chcao on 5/3/2017.
 */
public class SimpleQueryParserSearch {

	// using default QueryParser to do the search work
	public static void simpleParserSearch(String keywords){
		// IndexReader indexReader = null;
		String indexPath = "testIndexPath/queryParser/simpleIndex/";
		System.out.println("Searching "+keywords+" . . . ");

		try {
			Directory directory = FSDirectory.open(new File(indexPath));
			// indexReader = DirectoryReader.open(directory);

			IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));

			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);

			//这个是只能对一个field进行查询 还可以使用
			//MultiFieldQueryParser(LuceneUtils.getVersion(),new String[]{"content","title"},analyzer)
			//对多个field进行查询
			// QueryParser parser = new QueryParser(Version.LUCENE_45,"content",analyzer);
			QueryParser queryParser = new QueryParser(Version.LUCENE_45,"name",analyzer);

			Query query = queryParser.parse(keywords);

			// TopDocs docs = indexSearcher.search(query, 10);
			TopDocs topDocs = indexSearcher.search(query, 10);

			ScoreDoc [] scoreDoc = topDocs.scoreDocs;


			/*for(ScoreDoc sd : docs.scoreDocs){
				Document doc = indexSearcher.doc(sd.doc);
				System.out.print("文档编号=" + sd.doc + "  文档评分=" + sd.score + "   ");

				System.out.print("size=" + doc.get("size") + "  date=");
				System.out.print(
						new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date(Long.parseLong(doc.get("date")))));
				System.out.println("  name=" + doc.get("name"));
			}*/

			for(int i=0;i<scoreDoc.length;i++){
				Document doc = indexSearcher.doc(scoreDoc[i].doc);
				System.out.print("文档编号=" + scoreDoc[i].doc + "  文档评分=" + scoreDoc[i].score + "   ");

				System.out.print("size=" + doc.get("size") + "  date=");
				System.out.print(
						new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date(Long.parseLong(doc.get("date")))));
				System.out.println("  name=" + doc.get("name"));
			}

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			// 9、关闭reader
			// try {
			// 	indexReader.close();
			// } catch (IOException e) {
			// 	e.printStackTrace();
			// }
		}
	}

	public static void main(String[] args){

		simpleParserSearch("name:Jadk~");
		simpleParserSearch("name:Ja??er");
		System.out.println("------------------------------------------------------------------------");
		simpleParserSearch("Jade");
		System.out.println("------------------------------------------------------------------------");
		simpleParserSearch("name:[h TO n]");
		System.out.println("------------------------------------------------------------------------");
		simpleParserSearch("size:[20 TO 80]");
		System.out.println("------------------------------------------------------------------------");
		simpleParserSearch("date:[20130407 TO 20130701]");
	}

}
