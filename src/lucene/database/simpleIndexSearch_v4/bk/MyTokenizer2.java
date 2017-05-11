package lucene.database.simpleIndexSearch_v4.bk;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharacterUtils;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by chcao on 5/11/2017.
 */
public class MyTokenizer2 extends Tokenizer {

	private int inputLength=0;
	private int charCount=0;
	private StringBuffer outBuffer = new StringBuffer();


	private CharacterUtils.CharacterBuffer cBuffer = CharacterUtils.newCharacterBuffer(10);
	private CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private CharacterUtils charUtils;

	protected MyTokenizer2(Version matchVersion, Reader input){
		super(input);
		charUtils = CharacterUtils.getInstance(matchVersion);
		try {
			charUtils.fill(cBuffer, input);
		}catch (Exception e){
			e.printStackTrace();
		}
		inputLength = cBuffer.getLength();
	}

	@Override
	public boolean incrementToken() throws IOException {
		//当countLength == inputLength时候表示 所有input已经被检查过，所以要退出
		if(charCount<inputLength) {
			//清空buffer
			if (outBuffer.length() > 0) {
				outBuffer.delete(0, outBuffer.length());
			}

			System.out.println("input length is: " + cBuffer.getLength());

			for (char c : cBuffer.getBuffer()) {
				if (Character.isDigit(c)) {
					outBuffer.append((c));
				}
				//表示检查了多少个char
				charCount++;
			}
			termAtt.append(outBuffer.toString());
			return true;
		}else{
			return false;
		}
	}
}
