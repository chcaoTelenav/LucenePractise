package lucene.database.simpleIndexSearch;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

/**
 * Created by chcao on 5/8/2017.
 *
 * 这个是模拟项目。即数据原先在数据库中，然后对这些数据建立index，存到文件中。
 * 然后当进行search操作的时候，其实就是对那些index进行了查找。
 *
 * 这是对数据库中数据进行搜索，其实是搜索了之前创造的index文件
 */
public class SearchDatabaseByLucene {

	private static final int TOP_NUM=100;//显示数
	private static final String INDEXPATH = "testIndexPath/databaseIndex/Index_01/";
	private IndexReader indexReader;

	//这个方法是从一个field里面搜索一个关键字出来。
	@Test
	public void searchData() throws IOException, ParseException{
		String keywords = "normal";
		Directory directory = FSDirectory.open(new File(INDEXPATH));
		indexReader = DirectoryReader.open(directory);
	/*	TermsEnum termEnum = indexReader.terms(); 4.5已经没有这样的写法了.
		4.5里面想要实现获得index里面所有term，只能通过借助Fields
		Fields fields = MultiFields.getFields(indexReader);
		Bits livDocs = MultiFields.getLiveDocs(indexReader);
		for(String field: fields){
			// Terms terms = fields.terms(field);
			// TermsEnum termsEnum = terms.iterator(null);
			TermsEnum termsEnum = MultiFields.getTerms(indexReader,field).iterator(null);
			BytesRef bytesRef;
			while((bytesRef=termsEnum.next())!=null) {
				// System.out.println(termsEnum.term());
				// System.out.println("DocFreq= "+ termsEnum.docFreq());
				if (termsEnum.seekExact(bytesRef)) {
					DocsEnum docsEnum = termsEnum.docs(livDocs, null);
					if (docsEnum != null) {
						int docNum;
						while ((docNum = docsEnum.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
							System.out.println(bytesRef.utf8ToString() + " in doc " + docNum + ": " + docsEnum.freq());
						}

					}
				}
			}
		}
		/////////////////////上面这个Term的词频统计有问题，需要再研究一下*/

		try {
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			QueryParser parser = new QueryParser(Version.LUCENE_45, "schoolinfo", new StandardAnalyzer(Version.LUCENE_45));
			Query query = parser.parse(keywords);
			TopScoreDocCollector collector = TopScoreDocCollector.create(TOP_NUM,false);

			long start = new Date().getTime();

			indexSearcher.search(query,collector);
			ScoreDoc[] hits=collector.topDocs().scoreDocs;
			for(int i=0;i<hits.length;i++){
				Document document=indexSearcher.doc(hits[i].doc);
				System.out.print("文档编号=" + hits[i].doc + "  文档评分=" + hits[i].score + "   ");
				System.out.println(document.getField("schoolid")+" "+document.getField("schoolname")+" "+document.getField("schoolinfo"));
			}

			long end = new Date().getTime();
			System.out.println("Found "+hits.length+" documents(s) in "+(end-start)+" ms that matched query: "+keywords);

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			// indexReader.close();
		}


	}


}
