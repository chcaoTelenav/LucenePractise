package lucene.database.simpleIndexSearch_v4.bk;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by chcao on 5/10/2017.
 */
public class MyAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		try {
			MyAnalyzer analyzer = new MyAnalyzer();
			TokenStream stream = analyzer.tokenStream(fieldName, reader);
			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);// 相当于一个标记，随着流增加

			stream.reset();

			while (stream.incrementToken()) {
				System.out.println(cta.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		Tokenizer tokenizer = new StandardTokenizer(Version.LUCENE_45, reader);
		return new TokenStreamComponents(tokenizer);
	}

	//Format time into yyyyMMdd
	public String formateTime(String time) throws Exception {
		String formatedTime = "";
		if (null == time || time.length() == 0) {
			System.out.println("OOPS there is something wrong!!");
			return null;
		}

		String[] arr = time.split("[-/]");
		for (String s : arr)
			formatedTime += s;

		return formatedTime;
	}

	@Test
	public void testMethod() throws IOException {
		String testIndexPath = "testIndexPath/databaseIndex/Index_04_01/";
		Document document = new Document();
		document.add(new TextField("stringValue","2017/03/12", Field.Store.YES));

		IndexWriter indexWriter = null;
		try {
			Directory directory = FSDirectory.open(new File(testIndexPath));
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_45, new MyAnalyzer());
			indexWriter = new IndexWriter(directory,iwc);
			indexWriter.addDocument(document);

			System.out.println("Add index: "+document.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				indexWriter.commit();
				indexWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
