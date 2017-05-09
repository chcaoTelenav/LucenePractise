package lucene.database.simpleIndexSearch_v2;

import java.util.Scanner;

/**
 * Created by chcao on 5/9/2017.
 */
public class IndexSearchMenue {
	public static void main(String[] args){
		String indexPath = "testIndexPath/databaseIndex/Index_02/";
		String choice="";

		while(true) {
			System.out.println("----------------------");
			System.out.println("1. Get data from database and create index");
			System.out.println("2. Search words");
			System.out.println("3. Exit");
			System.out.print("Please select your choice: ");

			choice = new Scanner(System.in).next();
			if("1".equals(choice)){
				System.out.println("----------------------");
				DatabaseIndexByLucene index = new DatabaseIndexByLucene();
				index.createIndex(indexPath);
				continue;
			}else if("2".equals(choice)){
				System.out.println("----------------------");
				System.out.println("Searching mode: ");
				System.out.print("Please enter the words your want to search: ");
				choice = new Scanner(System.in).next();
				System.out.println("----------------------");
				SearchDatabaseByLucene search = new SearchDatabaseByLucene();
				search.searchData(indexPath,choice);
			}else if("3".equals(choice)){
				System.exit(0);
			}else{
				System.out.println("Please enter the correct choice!");
				continue;
			}

		}
	}

}
