package lucene.database.simpleIndexSearch_v3;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Scanner;

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
 * v3
 */
public class DatabaseIndexByLucene {

	private String url = "jdbc:postgresql://127.0.0.1:5432/test";
	private String user = "postgres";
	private String password = "admin";
	private Connection connection = null;

	public boolean createIndex(String indexpath) {

		if(null==indexpath||indexpath.length()==0){
			return false;
		}

		IndexWriter indexWriter = null;
		ResultSet rs = null;

		try {
			System.out.println("------------- Create index loading ...");
			File file = new File(indexpath);
			Directory directory = FSDirectory.open(file);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_45, new StandardAnalyzer(Version.LUCENE_45));

			// String overWrite;
			// boolean stayInside = true;
			// while (file.exists() && file.canRead()&&stayInside) {
			// 	System.out.println("-----------------");
			// 	System.out.println("Index already exist '" + file.getAbsolutePath()	+ "' do you want to overwrite it?");
			// 	System.out.println("1. overwrite");
			// 	System.out.println("2. not-overwrite");
			// 	overWrite = new Scanner(System.in).next();
			// 	if("1".equals(overWrite)){
			// 		// delete the old index and create a new one
			// 		iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			// 		stayInside = false;
			// 	}else if("2".equals(overWrite)){
			// 		// append updated index
			// 		iwc.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
			// 		stayInside = false;
			// 	}else{
			// 		System.out.println("Please make a correct choice! ");
			// 		continue;
			// 	}
			// }

			//设置indexWriter的创建规则，即当存在index时候append，不存在则创建;这个规则是其默认的值，所以下面不需要显示的写出来
			iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			// 创建IndexWriter
			indexWriter = new IndexWriter(directory, iwc);

			long start = new Date().getTime();

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
			System.out.println("------------- Create Index Success ...  Cost " + (end - start)+" ms.");

			return true;

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
		}
	}
}
