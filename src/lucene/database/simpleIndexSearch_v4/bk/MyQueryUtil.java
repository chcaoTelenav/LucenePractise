package lucene.database.simpleIndexSearch_v4.bk;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by chcao on 5/9/2017.
 */



class MyQueryUtil {

	/**
	 * @param field
	 * @param keyWords
	 * @param queryType 1: TermQuery; 2: PrefixQuery
	 * @return query
	 */
	private static Query chooseQuery(String field, String keyWords, String queryType) {
		Query query = null;
		if ("1".equals(queryType)) {
			query = new TermQuery(new Term(field, keyWords.toLowerCase()));
		} else if ("2".equals(queryType)) {
			query = new PrefixQuery(new Term(field, keyWords.toLowerCase()));
		} else {
			System.out.println("Please choose the correct queryType");
		}
		return query;
	}

	;

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

	public static Query doQuerySearch() {

		Map<Integer,String> fieldsMap = initFields();
		int field=0;
		String keywords;
		String queryType;
		Query query;


		while(true) {
			System.out.println("------ Please choose the searching type:");
			System.out.println("****** 1: Exact search");
			System.out.println("****** 2: Prefix search");
			queryType = new Scanner(System.in).next();
			if (!"1".equals(queryType) && !"2".equals(queryType)) {
				System.out.println("------ Please choose a correct searching type!!");
				continue;
			}

			System.out.println("The number of field you can search: \n*** 1. studentId\n*** 2. firstName\n*** 3. lastName\n*** 4. gender\n*** 5. age\n*** 6. birthday\n*** 7. phoneNumber\n*** 8. email\n*** 9. dormitory\n*** 10. personalInfo");
			System.out.print("Please enter the number of the field you want to search: ");
			field = new Scanner(System.in).nextInt();
			if (!fieldsMap.containsKey(field)){
				System.out.println("------ Please choose a correct field number!!");
				continue;
			}

			System.out.print("\n****** Please enter the words you want to search: ");
			keywords = new Scanner(System.in).next();

			query = chooseQuery(fieldsMap.get(field), keywords, queryType);

			return query;
		}

	}

	private static Map initFields(){
		Map<Integer,String> fieldMap = new HashMap<Integer,String>();

		fieldMap.put(1,"studentId");
		fieldMap.put(2,"firstName");
		fieldMap.put(3,"lastName");
		fieldMap.put(4,"gender");
		fieldMap.put(5,"age");
		fieldMap.put(6,"birthday");
		fieldMap.put(7,"phoneNumber");
		fieldMap.put(8,"email");
		fieldMap.put(9,"dormitory");
		fieldMap.put(10,"personalInfo");

		return fieldMap;
	}
}
