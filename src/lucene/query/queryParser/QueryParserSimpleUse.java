/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package lucene.query.queryParser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;

/**
 * 这里面的 QueryParster可以正常使用，说明我之前的代码有问题。需要借鉴这份code
 *
 *
 */

public class QueryParserSimpleUse {

	public static void main(String []args)throws Exception {

		String []id = {"1","2","3"};
		String []contents = {"java and lucene is good","I had study java and jbpm","I want to study java,hadoop and hbase"};

		// Directory directory = new RAMDirectory();
		Directory directory = FSDirectory.open(new File("D:/lucenetest/myExample/02_index/"));

		IndexWriter indexWriter = new IndexWriter(directory,new IndexWriterConfig(Version.LUCENE_45, new StandardAnalyzer(Version.LUCENE_45)));
		for(int i=0;i<id.length;i++){
			Document document = new Document();
			document.add(new Field("id",id[i],Field.Store.YES,Field.Index.ANALYZED));
			document.add(new Field("contents",contents[i],Field.Store.YES,Field.Index.ANALYZED));
			indexWriter.addDocument(document);
		}
		indexWriter.close();

		System.out.println("String is :java");
		search("java");

		System.out.println("\nString is :lucene");
		search("lucene");

		System.out.println("\nString is :+java +jbpm");
		search("+java +jbpm");

		System.out.println("\nString is :+java -jbpm");
		search("+java -jbpm");

		System.out.println("\nString is :java jbpm");
		search("java jbpm");

		System.out.println("\nString is :java AND jbpm");
		search("java AND jbpm");

		System.out.println("\nString is :java or jbpm");
		search("java or jbpm");
	}

	public static void search(String keyWord)throws Exception {
		Directory directory = FSDirectory.open(new File("D:/lucenetest/myExample/02_index/"));
		IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));

		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);

		QueryParser queryParser = new QueryParser(Version.LUCENE_45,"contents",analyzer);
		Query query = queryParser.parse(keyWord);

		TopDocs topDocs = indexSearcher.search(query, 10);

		ScoreDoc [] scoreDoc = topDocs.scoreDocs;

		for(int i =0;i<scoreDoc.length;i++){
			Document doc = indexSearcher.doc(scoreDoc[i].doc);
			System.out.println(doc.get("id")+" "+doc.get("contents"));
		}
		// indexSearcher.close();
	}
}


