package cn.et;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
/**
 * 
 * @author Administrator
 *
 */
public class IndexDemo {
	//索引所放入的地方
	static String dir="D:\\solrIndex";
	//创建分词器
	static Analyzer analyzer = new IKAnalyzer();
    public static void main(String[] args) throws Exception {
    	//write();
    	search();
	}
    
    public static void search() throws IOException, ParseException{
    	Directory directory = FSDirectory.open(new File(dir));
    	//读取索引库的存储目录
    	DirectoryReader ireader = DirectoryReader.open(directory);
    	//搜索类
    	IndexSearcher isearcher = new IndexSearcher(ireader);
    	//lucenec查询解析 用于指定查询属性名和分词器
    	QueryParser parser = new QueryParser(Version.LUCENE_47, "userDesc", analyzer);
    	//开始搜索
    	 Query query = parser.parse("来");
    	 //获取搜索的结果 指定返回document个数
    	 ScoreDoc[] hits = isearcher.search(query, null, 10).scoreDocs;
    	   // assertEquals(1, hits.length);
    	    // Iterate through the results:
    	    for (int i = 0; i < hits.length; i++) {
    	      Document hitDoc = isearcher.doc(hits[i].doc);
    	      System.out.println(hitDoc.getField("userName").stringValue());
    	      //assertEquals("This is the text to be indexed.", hitDoc.get("fieldname"));
    	    }
    	    ireader.close();
    	    directory.close();
    }
    
    
    
    public static void write() throws IOException{
    	//索引库的存储目录
    	Directory directory = FSDirectory.open(new File(dir));
    	//关联lucene版本当前分词器
    	IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
    	//传入 写入目录和分词器
    	IndexWriter iwriter = new IndexWriter(directory, config);
    	//document对象field属性
    	Document doc = new Document();
    	Field field = new Field("userName","张三",TextField.TYPE_STORED);
    	doc.add(field);
    	field = new Field("userDesc","张三来自深圳，喜欢吃水果",TextField.TYPE_STORED);
    	doc.add(field);
    	
    	Document doc1 = new Document();
    	Field field1 = new Field("userName","bobi",TextField.TYPE_STORED);
    	doc1.add(field1);
    	field1 = new Field("userDesc","bobi来自新疆，喜欢暴力活动",TextField.TYPE_STORED);
    	doc1.add(field1);
    	
    	iwriter.addDocument(doc);
    	iwriter.addDocument(doc1);
    	iwriter.commit();
    	iwriter.close();
    }
}
