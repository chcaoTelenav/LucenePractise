package lucene.database.simpleIndexSearch_v4;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * Created by chcao on 5/11/2017.
 */
public class MyAnalyzer extends Analyzer {


	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {

		MyTokenizer tokenizer = new MyTokenizer(Version.LUCENE_45,reader);

		return new TokenStreamComponents(tokenizer,new MyTokenFilter(tokenizer));


	}
}
