package lucene.database.simpleIndexSearch;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * Created by chcao on 5/8/2017.
 *
 * 这个是模拟项目。即数据原先在数据库中，然后对这些数据建立index，存到文件中。
 * 然后当进行search操作的时候，其实就是对那些index进行了查找。
 *
 * 这是从数据库中读取数据，并创建相应的index文件
 *
 */
public class DatabaseIndexByLucene {

	public static final String INDEXPATH="testIndexPath/databaseIndex/Index_01/";
	String url = "jdbc:postgresql://127.0.0.1:5432/test";
	String user = "postgres";
	String password = "admin";
	Connection connection=null;
	Statement statement = null;


	@Test
	public void testConnection (){
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("是否成功连接pg数据库" + connection);

			String sql = "select * from schools";
			statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String schoolName = resultSet.getString(2);
				String schoolInfo = resultSet.getString(3);
				System.out.println("School " + schoolName + " is a " + schoolInfo);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try{
				statement.close();
			}catch (Exception e){
				e.printStackTrace();
			}finally {
				try{
					connection.close();
				}catch (Exception e){
					e.printStackTrace();
				}
			}

		}
	}

	@Test
	public void createIndex() throws IOException, SQLException, ClassNotFoundException{

		System.out.println("Create index loading ...");
		// 创建IndexWriter
		File file = new File(INDEXPATH);
		Directory directory = new SimpleFSDirectory(file);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_45, analyzer);
		IndexWriter indexWriter = new IndexWriter(directory,iwc);
		// 调用sql语句
		String sql = "select * from schools";
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(url, user, password);
		PreparedStatement ps = connection.prepareStatement(sql);

		ResultSet rs = ps.executeQuery();

		while(rs.next()){
			Document doc = new Document();
			int id = rs.getInt(1);
			String schoolName = rs.getString(2);
			String schoolInfo = rs.getString(3);

			doc.add(new IntField("SchoolId", id, Field.Store.YES));

			FieldType fieldType = new FieldType();
			fieldType.setIndexed(true); //要做索引
			fieldType.setStored(true); //要存储
			fieldType.setTokenized(true); //要做分词
			doc.add(new Field("SchoolName", schoolName, fieldType));
			//或者可以像下面这样使用
			doc.add(new TextField("SchoolInfo",schoolInfo,Field.Store.YES));
			indexWriter.addDocument(doc);
			System.out.println("Insert doc： "+doc.toString());
		}
		rs.close();
		// indexWriter.commit();
		indexWriter.close();
		//
		// return true;
	}

}
