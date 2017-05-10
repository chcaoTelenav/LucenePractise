package lucene.database.simpleIndexSearch_v4;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by chcao on 5/10/2017.
 */
public class MyTokenizer extends Tokenizer{

	protected MyTokenizer(Reader input) {
		super(input);
	}

	@Override
	public boolean incrementToken() throws IOException {
		return false;
	}



}
