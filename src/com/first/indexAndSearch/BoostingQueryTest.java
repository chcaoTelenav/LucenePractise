package com.first.indexAndSearch;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.BoostingQuery;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

/**
 * Created by chcao on 4/27/2017.
 */
public class BoostingQueryTest {

	public static void main(String[] args) {

		String positiveKey = "boosting";
		String negativeKey = "query";

		IndexReader indexReader = null;
		String indexPath = "D:/lucenetest/index";
		System.out.println("positiveKey " + positiveKey + "\nnegativeKey "+negativeKey+ indexPath);

		try {
			// 1、打开索引库
			Directory indexDir = FSDirectory.open(new File(indexPath));
			// //创建IndexReader
			indexReader = DirectoryReader.open(indexDir);

			IndexSearcher searcher = new IndexSearcher(indexReader);

			// Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);
			//
			// QueryParser parser = new QueryParser(Version.LUCENE_45, "contents", analyzer);
			Query positiveQuery = new TermQuery(new Term("contents", positiveKey));
			Query negativeQuery = new TermQuery(new Term("contents", negativeKey));
			Query boostQuery = new BoostingQuery(positiveQuery,negativeQuery,0.01f);

			// 2、根据关键词进行搜索
			// Searcher返回TopDocs
			TopDocs docs = searcher.search(boostQuery, 20);// 查询20条记录

			// 3、遍历结果并处理
			ScoreDoc[] hits = docs.scoreDocs;
			System.out.println(hits.length);
			for (ScoreDoc hit : hits) {
				System.out.println("doc: " + hit.doc + " score: " + hit.score);
			}

		} catch (Exception e) {

		} finally {
			// 9、关闭reader
			try {
				indexReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
