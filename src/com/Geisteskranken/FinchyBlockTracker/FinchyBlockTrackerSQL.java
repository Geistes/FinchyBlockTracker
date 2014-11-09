package com.Geisteskranken.FinchyBlockTracker;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FinchyBlockTrackerSQL {

	Properties prop = new Properties();
	OutputStream output = null;
	
	static FinchyBlockTracker fbt = new FinchyBlockTracker(); 

	public synchronized static Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			FinchyBlockTracker.logger.warning("Disabled");
			FinchyBlockTracker.logger.warning(e.toString());
		}
		try {
			conn = DriverManager.getConnection("jdbc:mysql://"
					+ FinchyBlockTracker.host, FinchyBlockTracker.dbuser,
					FinchyBlockTracker.dbpass);
		} catch (SQLException err) {
			FinchyBlockTracker.logger.warning("Disabled");
			FinchyBlockTracker.logger.warning(err.toString());
			fbt.disable();
		}
		return conn;
	}

	// Returns true if DB exists or if it has been created.
	// Returns false if error.
	// Server should continue to run.
	public static boolean checkDB() {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = getConnection();
			statement = connection.createStatement();
			String sql = "CREATE DATABASE " + FinchyBlockTracker.database + ";";
			statement.executeUpdate(sql);
		} catch (SQLException sqlException) {
			if (sqlException.getErrorCode() == 1007) {
				// Database already exists error
				closeStatement(statement);
				closeConnection(connection);
				FinchyBlockTracker.logger.info("Database: OK");
				return true;
			} else {
				FinchyBlockTracker.logger.warning(sqlException.toString());
				closeStatement(statement);
				closeConnection(connection);
				return false;
			}
		}
		// Database created
		FinchyBlockTracker.logger.info("Database: OK");
		closeStatement(statement);
		closeConnection(connection);
		return true;
	}

	// Returns true if Table exists or if it has been created.
	// Server should continue to run.
	public static boolean checkTable() {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = getConnection();
			statement = connection.createStatement();
			String sql = "USE " + FinchyBlockTracker.database + ";";
			statement.execute(sql);
			// Create Table
			String createTable = "CREATE TABLE IF NOT EXISTS `blocktracker`.`blockbreaks` ("
					+ "`UID` INT NOT NULL AUTO_INCREMENT, "
					+ "`player` VARCHAR(45) NOT NULL, "
					+ "`x` VARCHAR(45) NOT NULL, "
					+ "`y` VARCHAR(45) NOT NULL, "
					+ "`z` VARCHAR(45) NOT NULL, "
					+ "`time` VARCHAR(45) NOT NULL, "
					+ "`block` VARCHAR(255) NOT NULL, "
					+ "`event` VARCHAR(45) NOT NULL, "
					+ "PRIMARY KEY (`UID`));";
			statement.execute(createTable);
		} catch (SQLException e) {
			FinchyBlockTracker.logger.warning("Disabled!");
			FinchyBlockTracker.logger.warning(e.toString());
			closeConnection(connection);
			closeStatement(statement);
			return false;
		}
		// Table exists
		closeConnection(connection);
		closeStatement(statement);
		FinchyBlockTracker.logger.info("Tables: OK");
		return true;
	}

	public static boolean insertBlockBreak(String player, double x, double y,
			double z, String time, String block) {
		Connection connection = null;
		Statement statement = null;
		String event = "BlockBreak";
		try {
			connection = getConnection();
			statement = connection.createStatement();
			String SelectDB = "USE " + FinchyBlockTracker.database + ";";
			String Insert = "INSERT INTO `blockbreaks` (`player`, `x`, `y`, `z`, `time`, `block`, `event`) VALUES ('"
					+ player
					+ "', '"
					+ x
					+ "', '"
					+ y
					+ "', '"
					+ z
					+ "', '"
					+ time + "', '" + block + "', '" + event + "'" + ")" + ";";
			statement.execute(SelectDB);
			statement.execute(Insert);
		} catch (SQLException sqlException) {
			FinchyBlockTracker.logger.warning(sqlException.toString());
			closeStatement(statement);
			closeConnection(connection);
			return false;
		}
		closeStatement(statement);
		closeConnection(connection);
		return true;
	}

	public static boolean insertBlockPlace(String player, double x, double y,
			double z, String time, String block) {
		Connection connection = null;
		Statement statement = null;
		String event = "BlockPlace";
		try {
			connection = getConnection();
			statement = connection.createStatement();
			String SelectDB = "USE " + FinchyBlockTracker.database + ";";
			String Insert = "INSERT INTO `blockbreaks` (`player`, `x`, `y`, `z`, `time`, `block`, `event`) VALUES ('"
					+ player
					+ "', '"
					+ x
					+ "', '"
					+ y
					+ "', '"
					+ z
					+ "', '"
					+ time + "', '" + block + "', '" + event + "'" + ")" + ";";
			statement.execute(SelectDB);
			statement.execute(Insert);
		} catch (SQLException sqlException) {
			FinchyBlockTracker.logger.warning(sqlException.toString());
			closeStatement(statement);
			closeConnection(connection);
			return false;
		}
		closeStatement(statement);
		closeConnection(connection);
		return true;
	}

	public static List<String> getBlockRecord(int X, int Y, int Z, String event) {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs;

		try {
			connection = getConnection();
			statement = connection.createStatement();
			String SelectDB = "USE " + FinchyBlockTracker.database + ";";
			String Fetch = "SELECT * FROM `blockbreaks` WHERE `x`='" + X
					+ "' AND `y`='" + Y + "' AND `z`='" + Z + "';";
			statement.execute(SelectDB);
			rs = statement.executeQuery(Fetch);

			ArrayList<String> list = new ArrayList<String>();
			while (rs.next()) {
				list.add(rs.getString("player"));

				String[] result = new String[list.size()];
				result = list.toArray(result);

				for (int i = 0; i < result.length; i++) {
					FinchyBlockTracker.logger.info(result[i]);
				}
			}

		} catch (SQLException ex) {
			FinchyBlockTracker.logger.warning(ex.toString());
		}

		closeConnection(connection);
		closeStatement(statement);
		return null;

	}

	public static void closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			FinchyBlockTracker.logger
					.warning("mySQL error: Could not close connection");
			FinchyBlockTracker.logger.warning(e.toString());
		}
	}

	public static void closeStatement(Statement statement) {
		try {
			statement.close();
		} catch (SQLException e) {
			FinchyBlockTracker.logger
					.warning("mySQL error: Could not close statement");
			FinchyBlockTracker.logger.warning(e.toString());
		}
	}

}