package lucene.database.simpleIndexSearch_v4;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.util.Version;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chcao on 5/10/2017.
 */
public class DateTest {

	public static void main(String[] args) throws Exception{

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

		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);
		TokenStream stream=analyzer.tokenStream("hello","20171231");
		CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);

		stream.reset();
		while (stream.incrementToken()) {
			System.out.print("时间是： " + cta);// 词与词之间的空格

		}


	}

}
