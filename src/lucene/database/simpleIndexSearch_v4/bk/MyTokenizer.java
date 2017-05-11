package lucene.database.simpleIndexSearch_v4.bk;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by chcao on 5/10/2017.
 */
public class MyTokenizer extends Tokenizer {
	StringBuilder buffer = new StringBuilder(1024);
	private CharTermAttribute termAtt = null;   //词内容

	protected MyTokenizer(Reader input) {
		super(input);
		termAtt = addAttribute(CharTermAttribute.class);
	}

	@Override
	public boolean incrementToken() throws IOException {
		if (buffer.length() > 0) buffer.delete(0, buffer.length());
		int a = 0;
		/*if (-1 != (a = input.read())) {
			buffer.append((char) a);
			termAtt.setEmpty().append(buffer.toString());
		} else {
			return false;
		}*/

		//
		// while(-1 != (a = input.read())) {
		// 	buffer.append((char) a);
		// 	termAtt.append(buffer.toString());
		// 	return true;
		// }

		while (-1 != (a = input.read())) {
			if (Character.isDigit((char) a))
				buffer.append((char) a);
		}
		termAtt.append(buffer.toString());
		return true;

	}
}
