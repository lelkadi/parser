package parser;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import parser.bean.BlockedIpBean;
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

		// Parse options
		CommandLine commandLine = validateAndParseArgs(args);
		if (commandLine != null) {

			if (commandLine.hasOption("import")) {
				// import records from log file to db
				importRecordsToDb(new File(commandLine.getOptionValue("import")));
			}

			String startDate = null, duration = null, threshold = null;
			if (commandLine.hasOption("startDate") && commandLine.hasOption("duration")
					&& commandLine.hasOption("threshold")) {
				startDate = commandLine.getOptionValue("startDate").replace('.', ' ');
				duration = commandLine.getOptionValue("duration");
				threshold = commandLine.getOptionValue("threshold");
			} else {
				System.out.println("Invalid options.");
			}

			try {
				Map<String, Integer> blockedIps = recordsDao.getIpsExceedingThreshold(getDateFromString(startDate),
						duration, Integer.valueOf(threshold));

				List<BlockedIpBean> blockedIpBeanList = new ArrayList<BlockedIpBean>();
				for (Entry<String, Integer> entry : blockedIps.entrySet()) {
					// print IPs to screen
					System.out.println("IP: " + entry.getKey());
					System.out.println("Number of failures: " + entry.getValue() + "\n");

					// Add to IPs list
					BlockedIpBean blockedIpBean = new BlockedIpBean();
					blockedIpBean.setIp(entry.getKey());
					blockedIpBean.setComment("This IP has been blocked after number of failures: " + entry.getValue());
					blockedIpBeanList.add(blockedIpBean);
				}

				recordsDao.addBlockedIps(blockedIpBeanList);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Returns the given String as a Date upon validating against the predefined
	 * format.
	 * 
	 * @return
	 */
	private static Date getDateFromString(String dateString) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		Date date = null;

		try {
			date = format.parse(dateString);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	/**
	 * Validates the given args array against the predefined valid options
	 * 
	 * @param args
	 * @return
	 */
	private static CommandLine validateAndParseArgs(String[] args) {
		// Prepare Valid Options
		Options options = new Options();

		// import
		Option importFromFile = new Option("i", "import", true, "imports the records from access log file to db");
		importFromFile.setRequired(false);
		importFromFile.setArgName("import");
		options.addOption(importFromFile);
		// startDate
		Option startDate = new Option("s", "startDate", true, "startDate to start recording requests");
		startDate.setRequired(true);
		startDate.setArgName("startDate");
		options.addOption(startDate);
		// duration
		Option duration = new Option("d", "duration", true, "duration to be used for recording requests");
		duration.setRequired(true);
		duration.setArgName("duration");
		options.addOption(duration);
		// threshold
		Option threshold = new Option("t", "threshold", true, "threshold to be used for recording requests");
		threshold.setRequired(true);
		threshold.setArgName("threshold");
		options.addOption(threshold);

		// Parse args
		CommandLineParser parser = new DefaultParser();
		CommandLine commandLine = null;

		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return commandLine;
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
			RecordBean recordBean = null;

			// Read data from file
			while (scanner.hasNext()) {
				recordBean = new RecordBean();
				recordBean.setDate(getDateFromString(scanner.next()));
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
