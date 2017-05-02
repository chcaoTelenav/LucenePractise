package lucene.analyzer.basicUse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by chcao on 4/28/2017.
 */
public class AnalyzerUtils {

	public static void displayToken(String str, Analyzer analyzer) {
		try {
			// 首先我们使用分词器analyzer将相关数据（这里比如是内容gcontent）进行分词，这样得到一个词汇流
			// 然后我们给这个流做一个标记，可以用来遍历此流
			TokenStream stream = analyzer.tokenStream("content", new StringReader(str));// 这就是一个词汇流
			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);// 相当于一个标记，随着流增加

			stream.reset();

			while (stream.incrementToken()) {
				System.out.print("[" + cta + "]");
			}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void displayAllTokenInfo(String str, Analyzer analyzer) {
		try {
			TokenStream stream = analyzer.tokenStream("content", new StringReader(str));
			PositionIncrementAttribute pia = stream.addAttribute(PositionIncrementAttribute.class);
			OffsetAttribute oa = stream.addAttribute(OffsetAttribute.class);
			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			TypeAttribute ta = stream.addAttribute(TypeAttribute.class);

			stream.reset();

			while (stream.incrementToken()) {
				System.out.print("位置增量： " + pia.getPositionIncrement());// 词与词之间的空格
				System.out.print("，单词： " + cta + "[" + oa.startOffset() + "," + oa.endOffset() + "]");
				System.out.print("，类型： " + ta.type());
				System.out.println();
			}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
