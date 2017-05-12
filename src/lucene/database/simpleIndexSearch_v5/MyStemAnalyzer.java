package lucene.database.simpleIndexSearch_v5;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * Created by chcao on 5/12/2017.
 */
public class MyStemAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {

		Tokenizer tokenizer = new StandardTokenizer(Version.LUCENE_45,reader);
		return new TokenStreamComponents(tokenizer,new PorterStemFilter(tokenizer));
	}
}
