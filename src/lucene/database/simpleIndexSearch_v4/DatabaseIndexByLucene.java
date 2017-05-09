package lucene.database.simpleIndexSearch_v4;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
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
 * v4
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

			//设置indexWriter的创建规则，即当存在index时候append，不存在则创建;这个规则是其默认的值，所以下面不需要显示的写出来
			iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			// 创建IndexWriter
			indexWriter = new IndexWriter(directory, iwc);

			long start = new Date().getTime();

			// 调用sql语句,获取students表数据
			String sql = "select * from students";
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user, password);
			PreparedStatement ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				Document doc = new Document();
				int studentId = rs.getInt(1);
				String firstName = rs.getString(2);
				String lastName = rs.getString(3);
				String gender = rs.getString(4);
				int age = rs.getInt(5);
				Date birthday = rs.getDate(6);
				String phoneNumber = rs.getString(7);
				String email = rs.getString(8);
				String dormitory = rs.getString(9);
				String personalInfo = rs.getString(10);

				//定义一个fieldType，索引，存储，分词都需要
				FieldType fieldType = new FieldType();
				fieldType.setIndexed(true); //要做索引
				fieldType.setStored(true); //要存储
				fieldType.setTokenized(true); //要做分词

				doc.add(new IntField("studentId", studentId, Field.Store.YES));
				doc.add(new Field("firstName",firstName,fieldType));
				doc.add(new Field("lastName",lastName,fieldType));
				doc.add(new Field("gender",gender,fieldType));
				doc.add(new IntField("age",age,Field.Store.YES));
				doc.add(new LongField("date",birthday.getTime(),Field.Store.YES));
				doc.add(new Field("phoneNumber",phoneNumber,fieldType));
				doc.add(new Field("email",email,fieldType));
				doc.add(new Field("dormitory",dormitory,fieldType));
				doc.add(new Field("personalInfo",personalInfo,fieldType));

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
