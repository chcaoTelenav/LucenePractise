package lucene.database.simpleIndexSearch_v4.bk;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.util.CharacterUtils;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by chcao on 5/11/2017.
 */
public class MyTokenizer3 extends Tokenizer {

	// private int inputLength=0;
	// private int charCount=0;
	// private StringBuffer outBuffer = new StringBuffer();
	// private CharacterUtils.CharacterBuffer cBuffer = CharacterUtils.newCharacterBuffer(10);


	protected MyTokenizer3(Version matchVersion, Reader input){
		super(input);
		charUtils = CharacterUtils.getInstance(matchVersion);
		// try {
		// 	charUtils.fill(cBuffer, input);
		// }catch (Exception e){
		// 	e.printStackTrace();
		// }
		// inputLength = cBuffer.getLength();
	}

	private int offset = 0, bufferIndex = 0, dataLen = 0, finalOffset = 0;
	private static final int MAX_WORD_LEN = 255;
	private static final int IO_BUFFER_SIZE = 4096;

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);

	private final CharacterUtils charUtils;
	private final CharacterUtils.CharacterBuffer ioBuffer = CharacterUtils.newCharacterBuffer(IO_BUFFER_SIZE);

	protected int normalize(int c) {
		return c;
	}

	@Override
	public boolean incrementToken() throws IOException {

		clearAttributes();
		int length = 0;
		int start = -1; // this variable is always initialized
		int end = -1;
		char[] buffer = termAtt.buffer();

		while (true) {
			//第一次进入，bufferIndex=dataLen=0
			if (bufferIndex >= dataLen) {
				offset += dataLen;//offset=0

				//将数据拷贝进入ioBuffer
				charUtils.fill(ioBuffer, input); // read supplementary char aware with CharacterUtils
				//	这里ioBuffer表示输入的input的数据长度
				if (ioBuffer.getLength() == 0) {
					dataLen = 0; // so next offset += dataLen won't decrement offset
					// 不知道这length是什么的长度
					if (length > 0) {
						break;
					} else {
						finalOffset = correctOffset(offset);
						//返回false，表示分词结束
						return false;
					}
				}

				//重置数据的长度，就是输入input的数据长度
				dataLen = ioBuffer.getLength();

				System.out.println("dataLen =: "+dataLen);

				//重置起始位置，这个bufferIndex用来代表要取出来的具体位置的char
				bufferIndex = 0;
			}

			// use CharacterUtils here to support < 3.1 UTF-16 code unit behavior if the char based methods are gone
			//取得ioBuffer中第bufferIndex位的字符
			final int c = charUtils.codePointAt(ioBuffer.getBuffer(), bufferIndex, ioBuffer.getLength());
			//获得字符长度
			final int charCount = Character.charCount(c);
			System.out.println("charCount = "+charCount);

			//起始位置加charCount
			bufferIndex += charCount;
			//如果输入字符为数字
			if(Character.isDigit((char)c)) {
				if (length == 0) {// start of token
					assert start == -1;
					start = offset + bufferIndex - charCount;
					end = start;
				} else if (length >= buffer.length-1) { // check if a supplementary could run out of bounds
					buffer = termAtt.resizeBuffer(2+length); // make sure a supplementary fits in the buffer
				}
				end += charCount;
				length += Character.toChars(normalize(c), buffer, length); // buffer it, normalized
				if (length >= MAX_WORD_LEN) { // buffer overflow! make sure to check for >= surrogate pair could break == test
					break;
				}
			} else if (length > 0) {//遇到非数字的字符
				//length++;
				//end++;

				if (length == 0) {                // start of token
					assert start == -1;
					start = offset + bufferIndex - charCount;
					end = start;
				} else if (length >= buffer.length-1) { // check if a supplementary could run out of bounds
					buffer = termAtt.resizeBuffer(2+length); // make sure a supplementary fits in the buffer
				}
				end += charCount;
				length += Character.toChars(normalize(c), buffer, length); // buffer it, normalized
				if (length >= MAX_WORD_LEN) { // buffer overflow! make sure to check for >= surrogate pair could break == test
					break;
				}

				break;                           // return 'em
			}
		}
		termAtt.setLength(length);
		assert start != -1;
		offsetAtt.setOffset(correctOffset(start), finalOffset = correctOffset(end));
		return true;


		/*//当countLength == inputLength时候表示 所有input已经被检查过，所以要退出
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
		}*/
	}
}
