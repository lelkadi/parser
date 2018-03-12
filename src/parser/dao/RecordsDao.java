package parser.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

	public RecordsDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/parser?" + "user=root&password=root");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inserts the elements of the given RecordBean list to the DB record table
	 * in one DB call.
	 * 
	 * @param recordBeanList
	 */
	public void addRecords(List<RecordBean> recordBeanList) throws Exception {
		try {
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

	public List<RecordBean> getRecords() throws Exception {
		List<RecordBean> recordBeanList = new ArrayList<>();
		try {
			preparedStatement = connect
					.prepareStatement("SELECT id, date, ip, request, status, user_agent FROM log_records");
			resultSet = preparedStatement.executeQuery();

			RecordBean recordBean;
			while (resultSet.next()) {
				recordBean = new RecordBean();

				recordBean.setId(resultSet.getLong("id"));
				recordBean.setDate(resultSet.getDate("date"));
				recordBean.setIp(resultSet.getString("ip"));
				recordBean.setRequest(resultSet.getString("request"));
				recordBean.setStatus(resultSet.getInt("status"));
				recordBean.setUserAgent(resultSet.getString("user_agent"));

				recordBeanList.add(recordBean);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

		return recordBeanList;
	}

	public void deleteRecords(List<RecordBean> recordBeanList) throws Exception {
		try {
			preparedStatement = connect.prepareStatement("DELETE FROM log_record WHERE id= ?;");

			for (RecordBean recordBean : recordBeanList) {
				preparedStatement.setLong(1, recordBean.getId());
				preparedStatement.addBatch();
			}

			preparedStatement.executeBatch();

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
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
