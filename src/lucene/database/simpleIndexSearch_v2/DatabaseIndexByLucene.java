package lucene.database.simpleIndexSearch_v2;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Created by chcao on 5/8/2017.
 *
 * 这个是模拟项目。即数据原先在数据库中，然后对这些数据建立index，存到文件中。
 * 然后当进行search操作的时候，其实就是对那些index进行了查找。
 *
 * 这是从数据库中读取数据，并创建相应的index文件
 *
 * v2
 */
public class DatabaseIndexByLucene {

	private String url = "jdbc:postgresql://127.0.0.1:5432/test";
	private String user = "postgres";
	private String password = "admin";
	private Connection connection = null;

	public boolean createIndex(String indexpath) {
		IndexWriter indexWriter = null;
		ResultSet rs = null;

		try {
			System.out.println("Create index loading ...");

			long start = new Date().getTime();

			// 创建IndexWriter
			Directory directory = FSDirectory.open(new File(indexpath));
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_45, new StandardAnalyzer(Version.LUCENE_45));
			indexWriter = new IndexWriter(directory, iwc);
			// 调用sql语句
			String sql = "select * from schools";
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user, password);
			PreparedStatement ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				Document doc = new Document();
				int id = rs.getInt(1);
				String schoolName = rs.getString(2);
				String schoolInfo = rs.getString(3);

				doc.add(new IntField("schoolId", id, Field.Store.YES));
				FieldType fieldType = new FieldType();
				fieldType.setIndexed(true); //要做索引
				fieldType.setStored(true); //要存储
				fieldType.setTokenized(true); //要做分词
				doc.add(new Field("schoolName", schoolName, fieldType));
				doc.add(new Field("schoolInfo", schoolInfo, fieldType));
				indexWriter.addDocument(doc);
				System.out.println("Insert doc： " + doc.toString());
			}

			long end = new Date().getTime();
			System.out.println("Create Index Success ...  Cost " + (end - start)+" ms.");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				rs.close();
				indexWriter.commit();
				indexWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			return true;
		}
	}
}
