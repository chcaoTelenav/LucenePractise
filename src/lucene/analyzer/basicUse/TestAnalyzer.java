package lucene.analyzer.basicUse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.junit.Test;

/**
 * Created by chcao on 4/28/2017.
 */
public class TestAnalyzer {

	@Test
	public void test01() {
		Analyzer analyzer1 = new StandardAnalyzer(Version.LUCENE_45);// 标准分词器
		Analyzer analyzer2 = new StopAnalyzer(Version.LUCENE_45);// 停用分词器
		Analyzer analyzer3 = new SimpleAnalyzer(Version.LUCENE_45);// 简单分词器
		Analyzer analyzer4 = new WhitespaceAnalyzer(Version.LUCENE_45);// 空格分词器
		String text = "this is my house,I am come form Xian,My email is " + "xxx@qq.com,and my qq is 154625554";
		AnalyzerUtils.displayToken(text, analyzer1);
		AnalyzerUtils.displayToken(text, analyzer2);
		AnalyzerUtils.displayToken(text, analyzer3);
		AnalyzerUtils.displayToken(text, analyzer4);
	}

	@Test
	public void test02() { // 对中文分词不适用
		Analyzer analyzer1 = new StandardAnalyzer(Version.LUCENE_45);// 标准分词器
		Analyzer analyzer2 = new StopAnalyzer(Version.LUCENE_45);// 停用分词器
		Analyzer analyzer3 = new SimpleAnalyzer(Version.LUCENE_45);// 简单分词器
		Analyzer analyzer4 = new WhitespaceAnalyzer(Version.LUCENE_45);// 空格分词器
		String text = "西安市雁塔区";
		AnalyzerUtils.displayToken(text, analyzer1);
		AnalyzerUtils.displayToken(text, analyzer2);
		AnalyzerUtils.displayToken(text, analyzer3);
		AnalyzerUtils.displayToken(text, analyzer4);
	}

	@Test
	public void test03() { // 对中文分词不适用
		Analyzer analyzer1 = new StandardAnalyzer(Version.LUCENE_45);// 标准分词器
		Analyzer analyzer2 = new StopAnalyzer(Version.LUCENE_45);// 停用分词器
		Analyzer analyzer3 = new SimpleAnalyzer(Version.LUCENE_45);// 简单分词器
		Analyzer analyzer4 = new WhitespaceAnalyzer(Version.LUCENE_45);// 空格分词器
		String text = "how are you thank you";
		System.out.println("************标准分词器***************");
		AnalyzerUtils.displayAllTokenInfo(text, analyzer1);
		System.out.println("************停用分词器***************");
		AnalyzerUtils.displayAllTokenInfo(text, analyzer2);
		System.out.println("************简单分词器***************");
		AnalyzerUtils.displayAllTokenInfo(text, analyzer3);
		System.out.println("*************空格分词器**************");
		AnalyzerUtils.displayAllTokenInfo(text, analyzer4);
	}
}
