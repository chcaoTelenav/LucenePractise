package lucene.documentField.fields;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


public class IndexUtil {
	/**创建索引写入器
	 * @param indexPath
	 * @param create
	 * @return
	 * @throws IOException
	 */
	public static IndexWriter getIndexWriter(String indexPath,boolean create) throws IOException{
		Directory dir = FSDirectory.open(new File(indexPath));
	    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);
	    IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_45,analyzer);
	    if (create){
	        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
	    }else {
	        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
	    }
	    IndexWriter writer = new IndexWriter(dir, iwc);
	    return writer;
	}
	
}
