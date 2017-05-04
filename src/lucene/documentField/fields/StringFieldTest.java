package lucene.documentField.fields;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.*;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;

/**
 * Created by chcao on 5/4/2017.
 */
public class StringFieldTest {

	/**
	 * 保存一个StringField
	 */
	@Test
	public void testIndexLongFieldStored() {
		Document document = new Document();
		document.add(new StringField("stringValue","12445", Field.Store.YES));
		document.add(new SortedDocValuesField("stringValue", new BytesRef("12445".getBytes())));
		Document document1 = new Document();
		document1.add(new StringField("stringValue","23456", Field.Store.YES));
		document1.add(new SortedDocValuesField("stringValue", new BytesRef("23456".getBytes())));
		IndexWriter writer = null;
		try {
			writer = IndexUtil.getIndexWriter("D:/lucenetest/fields", false);
			writer.addDocument(document);
			writer.addDocument(document1);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				writer.commit();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 测试StringField排序
	 */
	@Test
	public void testStringFieldSort(){
		try {
			IndexSearcher searcher = SearchUtil.getIndexSearcher("D:/lucenetest/fields", null);
			//构建排序字段
			SortField[] sortField = new SortField[1];
			sortField[0] = new SortField("stringVal",SortField.Type.STRING,true);
			Sort sort = new Sort(sortField);
			//查询所有结果
			Query query = new MatchAllDocsQuery();
			TopFieldDocs docs = searcher.search(query, 2, sort);
			ScoreDoc[] scores = docs.scoreDocs;
			//遍历结果
			for (ScoreDoc scoreDoc : scores) {
				//System.out.println(searcher.doc(scoreDoc.doc));;
				Document doc = searcher.doc(scoreDoc.doc);
				System.out.println(doc);
				//System.out.println(doc.getField("binaryValue").numericValue());
			}
			//searcher.search(query, results);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
