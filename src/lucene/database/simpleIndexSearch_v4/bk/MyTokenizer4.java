package lucene.database.simpleIndexSearch_v4.bk;

import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by chcao on 5/11/2017.
 */
public class MyTokenizer4 extends CharTokenizer {


	public MyTokenizer4(Version matchVersion, Reader input) {
		super(matchVersion, input);
		Reader reader = reFormateTime(input);
		try {
			this.setReader(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 改正判断是否是数字
	@Override
	protected boolean isTokenChar(int c) {

		return true;
	}

	public Reader reFormateTime(Reader reader){
		StringBuffer sb = new StringBuffer();
		try {
			int a = 0;
			while(-1 != (a = reader.read())) {
				sb.append((char)a);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return new StringReader(sb.toString());
	}
}
