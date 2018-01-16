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
	//����������ĵط�
	static String dir="D:\\solrIndex";
	//�����ִ���
	static Analyzer analyzer = new IKAnalyzer();
    public static void main(String[] args) throws Exception {
    	//write();
    	search();
	}
    
    public static void search() throws IOException, ParseException{
    	Directory directory = FSDirectory.open(new File(dir));
    	//��ȡ������Ĵ洢Ŀ¼
    	DirectoryReader ireader = DirectoryReader.open(directory);
    	//������
    	IndexSearcher isearcher = new IndexSearcher(ireader);
    	//lucenec��ѯ���� ����ָ����ѯ�������ͷִ���
    	QueryParser parser = new QueryParser(Version.LUCENE_47, "userDesc", analyzer);
    	//��ʼ����
    	 Query query = parser.parse("��");
    	 //��ȡ�����Ľ�� ָ������document����
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
    	//������Ĵ洢Ŀ¼
    	Directory directory = FSDirectory.open(new File(dir));
    	//����lucene�汾��ǰ�ִ���
    	IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
    	//���� д��Ŀ¼�ͷִ���
    	IndexWriter iwriter = new IndexWriter(directory, config);
    	//document����field����
    	Document doc = new Document();
    	Field field = new Field("userName","����",TextField.TYPE_STORED);
    	doc.add(field);
    	field = new Field("userDesc","�����������ڣ�ϲ����ˮ��",TextField.TYPE_STORED);
    	doc.add(field);
    	
    	Document doc1 = new Document();
    	Field field1 = new Field("userName","bobi",TextField.TYPE_STORED);
    	doc1.add(field1);
    	field1 = new Field("userDesc","bobi�����½���ϲ�������",TextField.TYPE_STORED);
    	doc1.add(field1);
    	
    	iwriter.addDocument(doc);
    	iwriter.addDocument(doc1);
    	iwriter.commit();
    	iwriter.close();
    }
}
