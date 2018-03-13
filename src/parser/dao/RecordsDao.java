package parser.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import constant.Constants;
import parser.bean.BlockedIpBean;
import parser.bean.RecordBean;

/**
 * This's the DAO responsible for accessing the LOG_RECORD DB table.
 * 
 * @author lelkadi
 *
 */
public class RecordsDao {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	/**
	 * Inserts the elements of the given RecordBean list to the DB record table
	 * in one DB call.
	 * 
	 * @param recordBeanList
	 */
	public void addRecords(List<RecordBean> recordBeanList) throws Exception {
		try {
			Class.forName(Constants.driver);
			connect = DriverManager.getConnection(Constants.dbString);

			preparedStatement = connect.prepareStatement("INSERT INTO parser.log_record "
					+ "(date, ip, request, status, user_agent) " + "VALUES (?, ?, ?, ?, ?)");

			DateFormat formatter;
			for (RecordBean recordBean : recordBeanList) {
				formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				preparedStatement.setString(1, formatter.format(recordBean.getDate()));
				preparedStatement.setString(2, recordBean.getIp());
				preparedStatement.setString(3, recordBean.getRequest());
				preparedStatement.setInt(4, recordBean.getStatus());
				preparedStatement.setString(5, recordBean.getUserAgent());

				preparedStatement.addBatch();
			}

			preparedStatement.executeBatch();

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
	}

	/**
	 * INSERTs the given list of BlockedIpBeans to the db
	 * 
	 * @param blockedIpBeanList
	 * @throws Exception
	 */
	public void addBlockedIps(List<BlockedIpBean> blockedIpBeanList) throws Exception {
		try {
			Class.forName(Constants.driver);
			connect = DriverManager.getConnection(Constants.dbString);

			preparedStatement = connect
					.prepareStatement("INSERT INTO parser.blocked_ip " + "(ip, comment) " + "VALUES (?, ?)");

			for (BlockedIpBean blockedIpBean : blockedIpBeanList) {
				preparedStatement.setString(1, blockedIpBean.getIp());
				preparedStatement.setString(2, blockedIpBean.getComment());

				preparedStatement.addBatch();
			}

			preparedStatement.executeBatch();

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
	}

	/**
	 * Gets the IPs that exceeded the given threshold starting from the given
	 * startDate for the given duration.
	 * 
	 * @param startDate
	 * @param duration
	 * @param threshold
	 * @return
	 * @throws Exception
	 */
	public Map<String, Integer> getIpsExceedingThreshold(Date startDate, String duration, Integer threshold)
			throws Exception {
		Class.forName(Constants.driver);
		connect = DriverManager.getConnection(Constants.dbString);

		if (startDate == null || duration == null || threshold == null) {
			System.out.println("Invalid parameters passed to getIpsExceedingThreshold()");
			return null;
		}

		Map<String, Integer> ipsMap = new HashMap<String, Integer>();

		// Calculate endDate
		Date endDate;
		switch (duration) {
		case "hourly":
			endDate = new Date(startDate.getTime() + 60 * 60 * 1000);
			break;
		case "daily":
			endDate = new Date(startDate.getTime() + 24 * 60 * 60 * 1000);
			break;
		default:
			endDate = startDate;
			break;
		}

		// Get IPs
		try {
			preparedStatement = connect.prepareStatement(
					"SELECT ip, COUNT(id) AS count FROM log_record WHERE date >= ? AND date <= ? GROUP BY ip");

			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			preparedStatement.setString(1, formatter.format(startDate));
			preparedStatement.setString(2, formatter.format(endDate));
			resultSet = preparedStatement.executeQuery();

			// Return IPs exceeding the threshold
			while (resultSet.next()) {
				if (resultSet.getInt("count") >= 100) {
					ipsMap.put(resultSet.getString("ip"), resultSet.getInt("count"));
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

		return ipsMap;
	}

	// You need to close the resultSet
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}

}
