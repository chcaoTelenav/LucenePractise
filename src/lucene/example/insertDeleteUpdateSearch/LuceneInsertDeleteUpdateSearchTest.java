package lucene.example.insertDeleteUpdateSearch;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 *
 * 这个例子只能做参考，并不能运行。因为其用的IKAnalyzer.jar包太老了，并不支持 Lucene4.5 运行会报错。
 *
 *
 *
 */
public class LuceneInsertDeleteUpdateSearchTest {
	private final static Logger logger = Logger.getLogger(LuceneInsertDeleteUpdateSearchTest.class);
	private static String indexPath = "G://lucene//index";

	public static void main(String[] args) {
		try {
			createIndex();
//           searchIndex("码农");
//           query();
//           deleteIndex();
//            forceDeleteIndex();
//            query();
//            highlighterSearch();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 创建索引
	 */
	public static void createIndex() {
		// 最细粒切分算法--true的话是 智能切分
		Analyzer analyzer = new IKAnalyzer(false);
		Document doc = null;
		IndexWriter indexWriter = null;
		try {
			indexWriter = getIndexWriter(analyzer);
			// 添加索引
			doc = new Document();
			doc.add(new StringField("id", "1", Store.YES));
			doc.add(new TextField("title", "标题：开始", Store.YES));
			doc.add(new TextField("content", "内容：我现在是个码农", Store.YES));
			indexWriter.addDocument(doc);
			doc = new Document();
			doc.add(new StringField("id", "2", Store.YES));
			doc.add(new TextField("title", "标题：结束", Store.YES));
			doc.add(new TextField("content", "内容:我现在是个lucene开发工程师的专家",Store.YES));
			indexWriter.addDocument(doc);
			indexWriter.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("索引器发送异常");
		} finally {
			try {
				destroyWriter(indexWriter);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 搜索文档
	 *
	 * @param keyword
	 */
	@SuppressWarnings("deprecation")
	public static void searchIndex(String keyword) {
		IndexReader indexReader = null;
		IndexSearcher indexSearcher = null;
		try {
			// 1.创建Directory 在硬盘上的G:/luence/index下建立索引
			Directory dir = FSDirectory.open(new File(indexPath));
			// 2.创建IndexReader
			indexReader = IndexReader.open(dir);
			// 实例化搜索器
			indexSearcher = new IndexSearcher(indexReader);

			// 使用QueryParser查询分析器构造Query对象
			QueryParser parse = new QueryParser(Version.LUCENE_45,"content", new IKAnalyzer(false));
			// 搜索包含keyword关键字的文档
			Query query = parse.parse(keyword.trim());

			// 使用lucene构造搜索引擎的时候，如果要针对多个域进行一次性查询
			// 这种方法的好处就是可以加权给字段的控制
			// 在这四个域中检索
			String[] fields = { "phoneType", "name", "category", "price" };
			Query querys = new MultiFieldQueryParser(Version.LUCENE_45, fields,new IKAnalyzer(false)).parse(keyword.trim());

			TopDocs results = indexSearcher.search(query, 1000);
			// 6.根据TopDocs获取ScoreDoc对象
			ScoreDoc[] score = results.scoreDocs;
			if (score.length > 0) {
				logger.info("查询结果数：" + score.length);
				System.out.println("查询结果数：" + score.length);
				for (int i = 0; i < score.length; i++) {
					// 7.根据Seacher和ScoreDoc对象获取具体的Document对象
					Document doc = indexSearcher.doc(score[i].doc);
					// 8.根据Document对象获取需要的值
					System.out.println(doc.toString());
					System.out.println(doc.get("title") + "["+ doc.get("content") + "]");
				}
			} else {
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("查询结果为空！");
		} finally {
			if (indexReader != null) {
				try {
					indexReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 对搜索返回的前n条结果进行分页显示
	 *
	 * @param keyWord
	 *            查询关键词
	 * @param pageSize
	 *            每页显示记录数
	 * @param currentPage
	 *            当前页
	 * @throws ParseException
	 */
	@SuppressWarnings("deprecation")
	public void paginationQuery(String keyWord, int pageSize, int currentPage)
			throws IOException, ParseException {
		String[] fields = { "title", "content" };
		QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_45,fields, new IKAnalyzer());
		Query query = queryParser.parse(keyWord.trim());

		IndexReader indexReader = IndexReader.open(FSDirectory.open(new File(
				indexPath)));
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		// TopDocs 搜索返回的结果
		TopDocs topDocs = indexSearcher.search(query, 100);// 只返回前100条记录
		TopDocs all = indexSearcher.search(new MatchAllDocsQuery(), 100);
		// int totalCount = topDocs.totalHits; // 搜索结果总数量
		ScoreDoc[] scoreDocs = topDocs.scoreDocs; // 搜索返回的结果集合

		// 查询起始记录位置
		int begin = pageSize * (currentPage - 1);
		// 查询终止记录位置
		int end = Math.min(begin + pageSize, scoreDocs.length);

		// 进行分页查询
		for (int i = begin; i < end; i++) {
			int docID = scoreDocs[i].doc;
			System.out.println("docID=" + docID);
			Document doc = indexSearcher.doc(docID);
			String title = doc.get("title");
			System.out.println("title is : " + title);
		}
		indexReader.close();
	}

	@SuppressWarnings("deprecation")
	public static void highlighterSearch() throws IOException, ParseException, InvalidTokenOffsetsException {
		IndexReader reader = IndexReader.open(FSDirectory.open(new File(indexPath)));
		IndexSearcher searcher = new IndexSearcher(reader);

		// String []fields={"title","content"};
		// QueryParser parser=new MultiFieldQueryParser(Version.LATEST, fields,
		// new IKAnalyzer());
		// Query query=parser.parse("");

		Term term = new Term("content", "lucene");
		TermQuery query = new TermQuery(term);

		TopDocs topdocs = searcher.search(query, Integer.MAX_VALUE);
		ScoreDoc[] scoreDoc = topdocs.scoreDocs;
		System.out.println("查询结果总数:" + topdocs.totalHits);
		System.out.println("最大的评分:" + topdocs.getMaxScore());

		for(int i=0;i<scoreDoc.length;i++){
			int docid=scoreDoc[i].doc;
			Document document=searcher.doc(docid);
			System.out.println("检索关键字："+term.toString());
			String  content=document.get("content");
			//高亮展示
			SimpleHTMLFormatter formatter=new SimpleHTMLFormatter("<font color='red'>", "");
			Highlighter highlighter=new Highlighter(formatter, new QueryScorer(query));
			highlighter.setTextFragmenter(new SimpleFragmenter(content.length()));

			if(!"".equals(content)){
				TokenStream tokenstream=new IKAnalyzer().tokenStream(content, new StringReader(content));
				String highLightText = highlighter.getBestFragment(tokenstream,content);
				System.out.println("高亮显示第 " + (i + 1) + " 条检索结果如下所示：");
				System.out.println(highLightText);
                /*End:结束关键字高亮*/
				System.out.println("文件内容:"+content);
				System.out.println("匹配相关度："+scoreDoc[i].score);
			}
		}
	}

	/**
	 * 获取indexWriter对象---获取索引器
	 *
	 * @param
	 * @param
	 * @return
	 * @throws IOException
	 */
	private static IndexWriter getIndexWriter(Analyzer analyzer)
			throws IOException {
		File indexFile = new File(indexPath);
		if (!indexFile.exists())
			indexFile.mkdir();// 索引库不存在 则新建一个
		Directory directory = FSDirectory.open(indexFile);
		// Directory directory = new RAMDirectory(); //在内存中建立索引

		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_45,
				analyzer);
		LogMergePolicy mergePolicy = new LogDocMergePolicy();
		// 索引基本配置
		// 设置segment添加文档(Document)时的合并频率
		// 值较小,建立索引的速度就较慢
		// 值较大,建立索引的速度就较快,>10适合批量建立索引
		mergePolicy.setMergeFactor(30);
		// 设置segment最大合并文档(Document)数
		// 值较小有利于追加索引的速度
		// 值较大,适合批量建立索引和更快的搜索
		mergePolicy.setMaxMergeDocs(5000);
		conf.setMaxBufferedDocs(10000);
		conf.setMergePolicy(mergePolicy);
		conf.setRAMBufferSizeMB(64);

		conf.setOpenMode(OpenMode.CREATE_OR_APPEND);
		if (IndexWriter.isLocked(directory)) {// ?
			IndexWriter.unlock(directory);
		}
		IndexWriter indexWriter = new IndexWriter(directory, conf);
		return indexWriter;
	}

	/**
	 * 销毁writer
	 *
	 * @param
	 * @throws IOException
	 */
	private static void destroyWriter(IndexWriter indexWriter)
			throws IOException {
		if (indexWriter != null) {
			indexWriter.close();
		}
	}

	/**
	 * 批量删除
	 *
	 * @param list
	 * @throws IOException
	 */
	public static void deleteIndexs(List list) throws IOException {
		if (list == null || list.size() > 0) {
			logger.debug("beans is null");
			return;
		}
		for (int i=0 ;i<list.size();i++) {
			deleteIndex();
		}
	}

	/**
	 * 删除单个索引 --不会立刻删除，生成.del文件
	 *
	 * @param
	 * @throws IOException
	 */
	private static void deleteIndex() throws IOException {
		// if(bean==null){
		// logger.debug("Get search bean is empty!");
		// return;
		// }
		IndexWriter indexWriter = getIndexWriter(new IKAnalyzer());
		// 参数是一个选项，可以是一个Query,也可以是一个term,term是一个精确查找的值
		// 这里删除id=1的文档，还会留在”回收站“。xxx.del
		indexWriter.deleteDocuments(new Term("id", "1"));
		destroyWriter(indexWriter);
	}

	/**
	 * 查询文档
	 */
	@SuppressWarnings("deprecation")
	public static void query() {
		// 1.创建Directory 在硬盘上的F:/luence/index下建立索引
		try {
			IndexReader indexReader = IndexReader.open(FSDirectory
					.open(new File(indexPath)));
			System.out.println("存储的文档数:" + indexReader.numDocs());
			System.out.println("总存储量:" + indexReader.maxDoc());
			System.out.println("被删除的文档：" + indexReader.numDeletedDocs());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 回滚回收站
	 *
	 * @throws IOException
	 */
	public void recoveryIndexByIsDelete() throws IOException {
		IndexWriter indexWriter = getIndexWriter(new IKAnalyzer());
		indexWriter.rollback();
		destroyWriter(indexWriter);
	}

	/**
	 * 清空回收站 在版本3.6之后，已经没有了unDeleteAll()方法了
	 *
	 * @throws IOException
	 */
	public static void forceDeleteIndex() throws IOException {
		IndexWriter indexWriter = getIndexWriter(new IKAnalyzer());
		indexWriter.forceMergeDeletes();
		destroyWriter(indexWriter);
	}

	/**
	 * 更新索引
	 *
	 * @throws IOException
	 */
	public void update() throws IOException {
		IndexWriter indexWriter = new IndexWriter(FSDirectory.open(new File(
				indexPath)), new IndexWriterConfig(Version.LUCENE_45,
				new IKAnalyzer(true)));
		Document document = new Document();
		document.add(new Field("id", "10", Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
		document.add(new Field("email", "9481629991", Field.Store.YES,Field.Index.NOT_ANALYZED));
		document.add(new Field("name", "小米", Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
		document.add(new Field("content", "小米好", Field.Store.NO,Field.Index.ANALYZED));

		// 这里的更新，从方法上可以看出，它实际上时将旧的删除，然后添加一个新文档的进去，将匹配到term的文档删除，然后就新的document添加进去
		indexWriter.updateDocument(new Term("id", "1"), document);
		indexWriter.close();
	}


}
