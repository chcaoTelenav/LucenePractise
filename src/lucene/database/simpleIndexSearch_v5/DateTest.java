package lucene.database.simpleIndexSearch_v5;

import lucene.database.simpleIndexSearch_v4.MyAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.tartarus.snowball.ext.PorterStemmer;

import java.io.File;
import java.util.Date;

/**
 * Created by chcao on 5/10/2017.
 */
public class DateTest {

	static String indexdir = "testIndexPath/databaseIndex/Index_05_01/";
	public static void main(String[] args) throws Exception {
/*

		Date firstTime = new Date();
		System.out.println("First time is: "+firstTime);

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("First time after format1: "+format1.format(firstTime));

		SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd");
		System.out.println("First time after format2: "+format2.format(firstTime));

		SimpleDateFormat format3 = new SimpleDateFormat("yyyyMMdd");
		System.out.println("First time after format3: "+format3.format(firstTime));

		//////////////

		System.out.println("===========================");

		System.out.println("After.... Format1: "+ new MyAnalyzer().formateTime(format1.format(firstTime)));
		System.out.println("After.... Format2: "+ new MyAnalyzer().formateTime(format2.format(firstTime)));
		System.out.println("After.... Format3: "+ new MyAnalyzer().formateTime(format3.format(firstTime)));
*/
	/*	String time = "2017-12-31";
		System.out.println("Original time is "+time);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);
		TokenStream stream=analyzer.tokenStream("birthday",time);
		CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);

		stream.reset();
		while (stream.incrementToken()) {
			System.out.println("StandardAnalyzer: " + cta);

		}
		System.out.println("========================");

		Analyzer analyzer2 = new MyAnalyzer2();
		TokenStream stream2 = analyzer2.tokenStream("birthday",time);
		CharTermAttribute cta2 = stream2.addAttribute(CharTermAttribute.class);
		stream2.reset();
		while(stream2.incrementToken()) {
			System.out.println("MyAnalyzer2: " + cta2);
		}
*/

		System.out.println("=========== Doing index test ==============");


		Directory directory = FSDirectory.open(new File(indexdir));
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_45, new StandardAnalyzer(Version.LUCENE_45));
		// IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_45,new SimpleAnalyzer(Version.LUCENE_45));
		iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		IndexWriter indexWriter = new IndexWriter(directory, iwc);


		//定义一个fieldType，索引，存储，分词都需要
		FieldType fieldType = new FieldType();
		fieldType.setIndexed(true); //要做索引
		fieldType.setStored(true); //要存储
		fieldType.setTokenized(true); //要做分词

/*		Document d1 = new Document();
		d1.add(new Field("birthday","2017-06-05",fieldType));
		indexWriter.addDocument(d1);

		Document d2 = new Document();
		d2.add(new Field("birthday","2000-12-25",fieldType));
		indexWriter.addDocument(d2);

		Document d3 = new Document();
		d3.add(new Field("birthday","1987-10-12",fieldType));
		indexWriter.addDocument(d3);*/


		fieldType.setNumericType(FieldType.NumericType.INT);
		Document d1 = new Document();
		d1.add(new IntField("age", 31, fieldType));
		indexWriter.addDocument(d1);

		Document d2 = new Document();
		d2.add(new IntField("age", 28, fieldType));
		indexWriter.addDocument(d2);

		Document d3 = new Document();
		d3.add(new IntField("age", 19, fieldType));
		indexWriter.addDocument(d3);

		indexWriter.close();

	}

	@Test
	public void searchTest() throws Exception{

		int keywords = 31;

		Directory directory = FSDirectory.open(new File(indexdir));
		IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));

		// Query query = new TermQuery(new Term("age", keywords));
		Query query = NumericRangeQuery.newIntRange("age", keywords, keywords, true, true);

		TopDocs docs = indexSearcher.search(query,20);
		ScoreDoc[] hits = docs.scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			Document document = indexSearcher.doc(hits[i].doc);
			System.out.print("文档编号=" + hits[i].doc + "  文档评分=" + hits[i].score + "   ");
			System.out.println(document.getFields().toString());
		}
	}

	@Test
	public void testPorterStemer(){
		PorterStemmer ps = new PorterStemmer();
		ps.setCurrent("singing");
		ps.stem();
		System.out.println("current is: "+ps.getCurrent());
	}

}
