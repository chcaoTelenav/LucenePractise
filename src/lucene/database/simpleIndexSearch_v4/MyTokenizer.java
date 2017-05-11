package lucene.database.simpleIndexSearch_v4;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by chcao on 5/11/2017.
 */
public class MyTokenizer extends CharTokenizer {


	public MyTokenizer(Version matchVersion, Reader input) {
		super(matchVersion, input);
	}

	// 改正判断是否是数字
	@Override
	protected boolean isTokenChar(int c) {

		return true;
	}
}
