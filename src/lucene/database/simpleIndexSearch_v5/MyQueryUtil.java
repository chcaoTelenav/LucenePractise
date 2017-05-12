package lucene.database.simpleIndexSearch_v5;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.tartarus.snowball.ext.PorterStemmer;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by chcao on 5/9/2017.
 * <p>
 * 工具类，提供一些方法
 * V5
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
		// term search
		if ("1".equals(queryType)) {
			// 查询age，是int型
			if ("age".equals(field)) {
				query = NumericRangeQuery.newIntRange(field, Integer.valueOf(keyWords), Integer.valueOf(keyWords), true, true);
			} else if("personalInfo".equals(field)){
				// 用这个来将单词还原成 原始单词 如：singing -》 sing
				PorterStemmer ps = new PorterStemmer();
				ps.setCurrent(keyWords.toLowerCase());
				ps.stem();
				query = new TermQuery(new Term(field, ps.getCurrent()));

			}else {
				query = new TermQuery(new Term(field, keyWords.toLowerCase()));
			}

			// prefix search
		} else if ("2".equals(queryType)) {
			query = new PrefixQuery(new Term(field, keyWords.toLowerCase()));
		} else {
			System.out.println("Please choose the correct queryType");
		}
		return query;
	}

	//选择并返回对应的query：Term or prefix
	public static Query doQuerySearch() {

		Map<Integer, String> fieldsMap = initFields();
		String field;
		String keywords;
		String queryType;
		Query query;

		Label1:
		while (true) {
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
			field = new Scanner(System.in).next();
			if (!fieldsMap.containsKey(field)) {
				System.out.println("****** Please choose a correct field number!!");
				continue;
			}

			// 校验错误，跳回这里
			System.out.print("\n****** Please enter the words you want to search: ");
			keywords = new Scanner(System.in).next();

			// 如果是6：birthday的话，需要先format输入的日期格式
			if (keywords.length() > 1 && "6".equals(field)) {
				try {
					keywords = formateTime(keywords);
					System.out.println("+++++ after formatted the keywords are: " + keywords);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if("1".equals(field)||"5".equals(field)){
				// 判断是否是纯数字，如果不是，报错
				for(char c: keywords.toCharArray()) {
					if (!Character.isDigit(c)) {
						System.out.println("****** Please enter the Number !");
						continue Label1;
					}
				}
			}
			query = chooseQuery(fieldsMap.get(field), keywords, queryType);

			return query;
		}

	}

	private static Map initFields() {
		Map<String, String> fieldMap = new HashMap<String, String>();

		fieldMap.put("1", "studentId");
		fieldMap.put("2", "firstName");
		fieldMap.put("3", "lastName");
		fieldMap.put("4", "gender");
		fieldMap.put("5", "age");
		fieldMap.put("6", "birthday");
		fieldMap.put("7", "phoneNumber");
		fieldMap.put("8", "email");
		fieldMap.put("9", "dormitory");
		fieldMap.put("10", "personalInfo");

		return fieldMap;
	}

	// Format time into yyyyMMdd
	public static String formateTime(String time) throws Exception {
		String formatedTime = "";
		if (null == time || time.length() == 0) {
			System.out.println("OOPS there is something wrong!!");
			return null;
		}

		String[] arr = time.split("[-/]");
		for (String s : arr)
			formatedTime += s;

		return formatedTime;
	}
}
