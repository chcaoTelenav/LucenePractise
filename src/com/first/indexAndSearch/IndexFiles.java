package com.first.indexAndSearch;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;


// 1、创建索引库IndexWriter
// 2、根据文件创建文档Document
// 3、向索引库中写入文档内容

public class IndexFiles {
	//创建indexWriter
	private static IndexWriter getIndexWriter(String indexPath)throws IOException {
		//创建Directory
		Directory indexDir = FSDirectory.open(new File(indexPath));
		//创建IndexWriter的配置，包括使用什么版本，以及使用的分析器的版本
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_45, new StandardAnalyzer(Version.LUCENE_45));
		//创建IndexWriter
		IndexWriter writer = new IndexWriter(indexDir, iwc);

		return writer;
	}

	//根据文件，创建Document，并添加到IndexWriter里面去
	private static void index(IndexWriter writer, File file) throws IOException {
		//判断file是否是一个文件夹，如果是的话，那么对这个文件夹里面的所有文件都做index处理
		if (file.isDirectory()) {
			String[] files = file.list();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					index(writer, new File(file, files[i]));
				}
			}
		} else {
			//这个是对于单个文件进行操作.add(Field
			// 2、根据文件创建文档Document
			Document doc = new Document();
			//创建一个新的field，Field.Store.YES表示这个field被存储
			Field pathField = new StringField("path", file.getPath(), Field.Store.YES);
			doc.add(pathField);
			//添加另外一个新的field
			doc.add(new LongField("modified", file.lastModified(), Field.Store.NO));

			doc.add(new TextField("contents", new FileReader(file)));
			System.out.println("Indexing " + file.getName());

			// 3、向索引库中写入文档内容
			writer.addDocument(doc);
		}

	}

	public static void main(String[] args) throws IOException {

		/*String usage = "java IndexFiles"
				+ " [-index INDEX_PATH] [-docs DOCS_PATH] \n\n"
				+ "This indexes the documents in DOCS_PATH, creating a Lucene index"
				+ "in INDEX_PATH that can be searched with SearchFiles";

		String indexPath = null;
		String docsPath = null;
		for (int i = 0; i < args.length; i++) {
			if ("-index".equals(args[i])) {
				indexPath = args[i + 1];
				i++;
			} else if ("-docs".equals(args[i])) {
				docsPath = args[i + 1];
				i++;
			}
		}

		if (docsPath == null) {
			System.err.println("Usage: " + usage);
			System.exit(1);
		}

		final File docDir = new File(docsPath);
		if (!docDir.exists() || !docDir.canRead()) {
			System.out
					.println("Document directory '"
							+ docDir.getAbsolutePath()
							+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}

		IndexWriter writer = null;
		try {
			// 1、创建索引库IndexWriter
			writer = getIndexWriter(indexPath);
			index(writer, docDir);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}*/

		String indexPath="D:/lucenetest/index";
		File files = new File("D:/lucenetest/files");
		IndexWriter writer = null;
		try {
			writer=getIndexWriter(indexPath);
			index(writer,files);
		}catch (IOException e){
			e.printStackTrace();
		}finally {
			writer.close();
		}


	}

}
