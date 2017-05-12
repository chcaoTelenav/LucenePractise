package lucene.database.simpleIndexSearch_v5;

import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * Created by chcao on 5/11/2017.
 *
 *  复用 CharTokenizer的功能
 *
 * V5
 */
public class MyTokenizer extends CharTokenizer {

	public MyTokenizer(Version matchVersion, Reader input) {
		super(matchVersion, input);
	}

	// 将所有判断值都定义为true，这样就不会对输入值进行分词。
	// 这里以后需要改进，因为如果输入的是一系列值，用“逗号”或者其他分隔符分开的，不能都返回true
	// 这里暂时满足需求
	@Override
	protected boolean isTokenChar(int c) {
		if(-1!=c)
			return true;
		else
			return false;
	}
}
