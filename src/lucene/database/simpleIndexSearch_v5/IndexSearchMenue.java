package lucene.database.simpleIndexSearch_v5;

import org.apache.lucene.search.Query;

import java.util.Scanner;

/**
 * Created by chcao on 5/9/2017.
 * <p>
 * V5
 */
public class IndexSearchMenue {
	public static void main(String[] args) {
		String indexPath = "testIndexPath/databaseIndex/Index_05/";
		String choice;

		System.out.println(" *********** Lucene practise with database V4 ************");
		System.out.println("=============================================================");

		while (true) {
			System.out.println("----------------------");
			System.out.println("1. Get data from database and create index");
			System.out.println("2. Search words");
			System.out.println("3. Exit");
			System.out.print("Please select your choice: ");

			choice = new Scanner(System.in).next();
			if ("1".equals(choice)) {
				doIndex(indexPath);
			} else if ("2".equals(choice)) {
				doSearch(indexPath);
			} else if ("3".equals(choice)) {
				System.exit(0);
			} else {
				System.out.println("--------- Please enter the correct choice!");
				continue;
			}
		}
	}

	private static void doIndex(String indexPath) {
		System.out.println("----------------------");
		DatabaseIndexByLucene index = new DatabaseIndexByLucene();
		index.createIndex(indexPath);
	}

	private static void doSearch(String indexPath) {
		System.out.println("----------------------");
		Query query = MyQueryUtil.doQuerySearch();
		SearchDatabaseByLucene search = new SearchDatabaseByLucene();
		search.searchData(indexPath, query);
	}
}
