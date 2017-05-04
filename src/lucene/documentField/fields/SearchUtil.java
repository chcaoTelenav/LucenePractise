package lucene.documentField.fields;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

public class SearchUtil {
	/**获取索引查看器
	 * @param filePath
	 * @param service
	 * @return
	 * @throws IOException
	 */
	public static IndexSearcher getIndexSearcher(String filePath,ExecutorService service) throws IOException{
		IndexReader reader =  DirectoryReader.open(FSDirectory.open(new File(filePath)));
		IndexSearcher searcher = new IndexSearcher(reader,service);
		return searcher;
	}

}
