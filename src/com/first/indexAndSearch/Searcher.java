package com.first.indexAndSearch;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * Created by cheng on 2017/4/25.
 */
public class Searcher {

    public static void indexSearch(String keywords){
        // String indexPath = null;
        String indexPath = "D:/lucenetest/index";
        // String term = null;
      /*  for (int i = 0; i < args.length; i++) {
            if ("-index".equals(args[i])) {
                indexPath = args[i + 1];
                i++;
            } else if ("-term".equals(args[i])) {
                term = args[i + 1];
                i++;
            }
        }*/

        System.out.println("Searching " + keywords + " in " + indexPath);
        DirectoryReader reader = null;
        try {
            // 1、打开索引库
            Directory indexDir = FSDirectory.open(new File(indexPath));
            //创建IndexReader
            reader = DirectoryReader.open(indexDir);
            //通过indexReader创建IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);

            // 2、创建搜索的query
            // 创建parse用来确定搜索的内容，第二个参数表示搜索的域
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);
            // QueryParser parser =new QueryParser("contents", analyzer));
            //上面这种方法报错，看了lucene4.5的api文档，new QueryPaser()需要提供一个version号
            // 创建parse用来确定搜索的内容，第二个参数表示搜索的域，content表示搜索的域或者说字段
            QueryParser parser = new QueryParser(Version.LUCENE_45, "contents", analyzer);

            Query query = parser.parse(keywords);// 被搜索的内容

            // 2、根据关键词进行搜索
            // TopDocs docs = searcher.search(new TermQuery(new Term("contents", term)), 20);
            // Searcher返回TopDocs
            TopDocs docs = searcher.search(query, 20);// 查询20条记录

            // 3、遍历结果并处理
            ScoreDoc[] hits = docs.scoreDocs;
            System.out.println(hits.length);
            for (ScoreDoc hit : hits) {
                System.out.println("doc: " + hit.doc + " score: " + hit.score);
            }

        }catch (Exception e){

        }finally {
            // 9、关闭reader
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static void main(String[] args) {

        /*// String indexPath = null;
        String indexPath = "D:/lucenetest/index";
        // String term = null;
        //搜索关键字
        String term = "hello";
      *//*  for (int i = 0; i < args.length; i++) {
            if ("-index".equals(args[i])) {
                indexPath = args[i + 1];
                i++;
            } else if ("-term".equals(args[i])) {
                term = args[i + 1];
                i++;
            }
        }*//*

        System.out.println("Searching " + term + " in " + indexPath);

        // 1、打开索引库
        Directory indexDir = FSDirectory.open(new File(indexPath));
        //创建IndexReader
        IndexReader reader = DirectoryReader.open(indexDir);
        //通过indexReader创建IndexSearcher
        IndexSearcher searcher = new IndexSearcher(reader);

        // 2、创建搜索的query
        // 创建parse用来确定搜索的内容，第二个参数表示搜索的域
        QueryParser parser = new QueryParser("content", new StandardAnalyzer());// content表示搜索的域或者说字段
        Query query = parser.parse(keywords);// 被搜索的内容


        // 2、根据关键词进行搜索
        TopDocs docs = searcher.search(new TermQuery(new Term("contents", term)), 20);

        // 3、遍历结果并处理
        ScoreDoc[] hits = docs.scoreDocs;
        System.out.println(hits.length);
        for (ScoreDoc hit : hits) {
            System.out.println("doc: " + hit.doc + " score: " + hit.score);
        }

        ir.close();
*/
        indexSearch("hello");
    }
}
