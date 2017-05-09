package lucene.database.simpleIndexSearch_v4;

import java.util.Scanner;

/**
 * Created by chcao on 5/9/2017.
 * <p>
 * v4
 */
public class IndexSearchMenue {
	public static void main(String[] args) {
		String indexPath = "testIndexPath/databaseIndex/Index_04/";
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
				System.out.println("----------------------");
				DatabaseIndexByLucene index = new DatabaseIndexByLucene();
				index.createIndex(indexPath);
				continue;
			} else if ("2".equals(choice)) {
				// System.out.println("----------------------");
				// System.out.println("--------- Searching mode: you can only search 'schoolInfo' for now ");
				// System.out.print("--------- Please enter the words your want to search: ");
				// choice = new Scanner(System.in).next();
				// System.out.println("----------------------");
				// SearchDatabaseByLucene search = new SearchDatabaseByLucene();
				// search.searchData(indexPath, choice);

				doSearch(indexPath,choice);
			} else if ("3".equals(choice)) {
				System.exit(0);
			} else {
				System.out.println("--------- Please enter the correct choice!");
				continue;
			}

		}
	}

	private void doIndex() {

	}

	private static void doSearch(String indexPath,String choice) {
		System.out.println("----------------------");
		System.out.println("--------- Searching mode: you can only search 'schoolInfo' for now ");
		System.out.print("--------- Please enter the words your want to search: ");
		choice = new Scanner(System.in).next();
		System.out.println("----------------------");
		SearchDatabaseByLucene search = new SearchDatabaseByLucene();
		search.searchData(indexPath, choice);
	}

	private void doQuerySearch() {
		String queryType = new Scanner(System.in).next();

	}

}
