package lucene.query.queryParser;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chcao on 5/3/2017.
 */
public class SimpleIndex {

	// private Directory directory;
	// private IndexReader reader;

	// set data in to the files
	public static void prepareFile(String indexPath) throws IOException{
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

		IndexWriter indexWriter = null;
		// Document doc = null;
		try {
			dates[0] = sdf.parse("20130407 15:25:30");
			dates[1] = sdf.parse("20130407 16:30:45");
			dates[2] = sdf.parse("20130213 11:15:25");
			dates[3] = sdf.parse("20130808 09:30:55");
			dates[4] = sdf.parse("20130526 13:54:22");
			dates[5] = sdf.parse("20130701 17:35:34");


			Directory directory = FSDirectory.open(new File(indexPath));
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_45, new StandardAnalyzer(Version.LUCENE_45));
			indexWriter = new IndexWriter(directory,iwc);
			//
			//
			//
			//
			// //相对路径好像有问题，改用绝对路径试试看
			// // directory = FSDirectory.open(new File("myExample/01_index/"));
			// directory = FSDirectory.open(new File(indexPath));
			//
			// writer = new IndexWriter(directory,
			// 		new IndexWriterConfig(Version.LUCENE_45, new StandardAnalyzer(Version.LUCENE_45)));

			// writer.deleteAll();

			/*for (int i = 0; i < sizes.length; i++) {
				Document doc = new Document();
				doc.add(new NumericDocValuesField("size", sizes[i]));
				doc.add(new StringField("name", names[i], Field.Store.YES));
				doc.add(new TextField("content", contents[i], Field.Store.NO));
				doc.add(new LongField("date", dates[i].getTime(),Field.Store.YES));
				indexWriter.addDocument(doc);
				System.out.println(doc.toString());
			}*/
			for (int i = 0; i < sizes.length; i++) {
				Document doc = new Document();
				doc.add(new IntField("size", sizes[i],Field.Store.YES));
				doc.add(new Field("name", names[i], Field.Store.YES,Field.Index.ANALYZED ));
				doc.add(new Field("content", contents[i], Field.Store.NO,Field.Index.ANALYZED ));
				doc.add(new LongField("date", dates[i].getTime(),Field.Store.YES));
				indexWriter.addDocument(doc);
				System.out.println(doc.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != indexWriter) {
				try {
					indexWriter.close();
				} catch (IOException ce) {
					ce.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args){
		String indexPath = "D:/lucenetest/myExample/01_index/";
		try{
			prepareFile(indexPath);
		}catch (Exception e){
			e.printStackTrace();
		}

	}
}
