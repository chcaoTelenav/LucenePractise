package lucene.database.simpleIndexSearch_v5;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

/**
 * Created by chcao on 5/11/2017.
 *
 * V5
 */
public class MyTokenFilter extends TokenFilter {

	/**
	 * Construct a token stream filtering the given input.
	 *
	 * @param input
	 */
	protected MyTokenFilter(TokenStream input) {
		super(input);
	}

	@Override
	public boolean incrementToken() throws IOException {
		while(input.incrementToken()){
			CharTermAttribute cta = input.getAttribute(CharTermAttribute.class);
			try {
				String ts = MyQueryUtil.formateTime(cta.toString());
				cta.setEmpty();
				cta.append(ts);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(cta.toString());
			return true;
		}
		return false;
	}


}
