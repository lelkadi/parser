package parser;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import parser.bean.RecordBean;
import parser.dao.RecordsDao;

public class Parser {

	private final static RecordsDao recordsDao = new RecordsDao();

	/**
	 * The apps main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		importRecordsToDb(new File("access.log"));
	}

	/**
	 * Reads the records from the given file, and writes them to the DB
	 * 
	 * @param file
	 */
	private static void importRecordsToDb(File file) {
		Scanner scanner;
		try {
			// Initialize file reader
			scanner = new Scanner(file);
			scanner.useDelimiter("[|\\|\n]");

			List<RecordBean> recordBeanList = new ArrayList<>();
			RecordBean recordBean = new RecordBean();
			DateFormat format;

			// Read data from file
			while (scanner.hasNext()) {
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
				recordBean.setDate(format.parse(scanner.next()));
				recordBean.setIp(scanner.next());
				recordBean.setRequest(scanner.next());
				recordBean.setStatus(scanner.nextInt());
				recordBean.setUserAgent(scanner.next());

				System.out.println(recordBean);

				// Add record to records List
				recordBeanList.add(recordBean);
			}

			// Add records to db
			recordsDao.addRecords(recordBeanList);

			scanner.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
