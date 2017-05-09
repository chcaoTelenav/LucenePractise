package lucene.database.simpleIndexSearch_v4;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

/**
 * Created by chcao on 5/9/2017.
 */
class MyQueryUtil{

	// private String queryOption = "term";
	// private String keyWords;
	// private Query query=null;
	//
	// public MyQuery(String queryOption, String keyWords) {
	// 	this.queryOption = queryOption;
	// 	this.keyWords = keyWords;
	// }

	/*private Query useTermQuery(String field,String keyWords){
		Query query = new TermQuery(new Term(field,keyWords));
		return query;
	}

	private Query usePrefixQuery(String field,String keyWords){

	}*/

	//选并返回对应的query

	/**
	 *
	 * @param field
	 * @param keyWords
	 * @param queryType  1: TermQuery; 2: PrefixQuery
	 * @return query
	 */
	public static Query chooseQuery(String field,String keyWords,String queryType){
		Query query=null;

		if("1".equals(queryType)){
			query = new TermQuery(new Term(field,keyWords));
		}else if("2".equals(queryType)){
			query = new PrefixQuery(new Term(field,keyWords));
		}else{
			System.out.println("Please choose the correct queryType");
		}
		return query;
	}

}
