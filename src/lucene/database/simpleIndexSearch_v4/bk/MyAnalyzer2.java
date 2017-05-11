package lucene.database.simpleIndexSearch_v4.bk;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * Created by chcao on 5/11/2017.
 */
public class MyAnalyzer2 extends Analyzer {


	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {

		MyTokenizer4 tokenizer4 = new MyTokenizer4(Version.LUCENE_45,reader);
		// return new TokenStreamComponents(new MyTokenizer2(Version.LUCENE_45,reader));
		// return new TokenStreamComponents(new MyTokenizer4(Version.LUCENE_45,reader));

		return new TokenStreamComponents(tokenizer4,new MyTokenFilter(tokenizer4));


	}
}
