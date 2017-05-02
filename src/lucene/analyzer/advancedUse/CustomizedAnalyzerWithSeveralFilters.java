package lucene.analyzer.advancedUse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Set;

/**
 * This is a test for customized Analyzer with several filter inside.
 * 很有代表性
 *
 */
public class CustomizedAnalyzerWithSeveralFilters extends Analyzer {

	public static final String[] stopWordsString = { "and", "of", "the", "to", "is", "their", "can", "all", "i", "in" };

	private Set stopWords = null;

	public CustomizedAnalyzerWithSeveralFilters() {

		//自动将字符串数组转换为set
		stopWords = StopFilter.makeStopSet(Version.LUCENE_45,stopWordsString,true);
		//将原有的停用词加入到现在的停用词中
		stopWords.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
	}

	public static void main(String[] args) throws IOException {
		Analyzer analyzer = new CustomizedAnalyzerWithSeveralFilters(); // 这个类是一个自定义的analyzer类
		String text = "Holmes, who first appeared in publication in 1887, was featured in four novels and 56 short stories. The first story, A Study in Scarlet, appeared in Beeton's Christmas Annual in 1887 and the second, The Sign of the Four, in Lippincott's Monthly Magazine in 1890. The character grew tremendously in popularity with the beginning of the first series of short stories in Strand Magazine in 1891; further series of short stories and two novels published in serial form appeared between then and 1927. The stories cover a period from around 1880 up to 1914.";

		Reader reader = new StringReader(text);
		TokenStream stream = analyzer.tokenStream("field",reader);
		CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);// 相当于一个标记，随着流增加

		stream.reset();

		while(stream.incrementToken()){
			System.out.println(cta.toString());
		}
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		//主要负责接收reader，将reader进行分词操作
		Tokenizer tokenizer = new StandardTokenizer(Version.LUCENE_45,reader);

		TokenFilter lowerCaseFilter = new LowerCaseFilter(Version.LUCENE_45,tokenizer);
		//创建停用词的set对象
		CharArraySet charArraySet = CharArraySet.copy(Version.LUCENE_45,stopWords);
		// 所有的Filter都是返回一个tokenStream的对象，这里对于上面获取的lowerCaseFilter过滤之后的
		//tokenStream再次进行“停用词”的过滤操作。使用的是上面自定义的停用词set =》 charArraySet
		TokenFilter stopFilter = new StopFilter(Version.LUCENE_45,lowerCaseFilter,charArraySet);
		//这个是将上面经过 停用词过滤后生成的 tokenStream。再放到PorterStemFilter里面，将里面单词还原成原型
		TokenFilter stemFilter = new PorterStemFilter(stopFilter);

		//分词器做好处理之后获得一个流 stemFilter <- 这其实就是个TokenStream对象。所有的TokenFilter都是TokenStream
		// 这个流中存储了分词的信息，将其返回
		return new TokenStreamComponents(tokenizer,stemFilter);
	}


}
