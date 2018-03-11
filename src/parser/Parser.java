package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import parser.bean.RecordBean;
import parser.dao.RecordsDao;

public class Parser {
	
	RecordsDao recordsDao = new RecordsDao();

	public static void main(String[] args) {
		Scanner scanner;
		try {
			// Initialize file reader
			scanner = new Scanner(new File("access.log"));
			scanner.useDelimiter("[|]");

			RecordBean recordBean = new RecordBean();

			// Read data from file
			while (scanner.hasNext()) {
				recordBean.setDate(scanner.next());
				recordBean.setIp(scanner.next());
				recordBean.setRequest(scanner.next());
				recordBean.setStatus(scanner.next());
				recordBean.setUserAgent(scanner.next());

				System.out.println(recordBean);

				// TODO : Store values to DB
			}
			scanner.close();

		} catch (

		Exception e) {
			e.printStackTrace();
		}

	}

}
