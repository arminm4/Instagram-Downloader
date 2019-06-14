
package com.krupizde.persistence;

import java.sql.Statement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;

/**
 * Class represents connection to application's SQLite database.
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class SqliteDatabase {
	private static Connection conn;

	static {
		SqliteDatabase.conn = null;
	}

	/**
	 * Auto-generated constructor
	 */
	public SqliteDatabase() {
		super();
	}

	/**
	 * Method that works similiar to singleton, only for static property Connection.
	 * 
	 * @return Only one instance of connection
	 * @throws SQLException
	 */
	 public static synchronized Connection getConn() throws SQLException {
		if (SqliteDatabase.conn == null) {
			SqliteDatabase.conn = DriverManager.getConnection("jdbc:sqlite:instagram.db");
			conn.setAutoCommit(true);
		}
		return SqliteDatabase.conn;
	}

	/**
	 * Method tries to close connection to SQLite database
	 */
	public static void close() {
		try {
			if (conn != null) {
				conn.commit();
				conn.close();
			}

		} catch (SQLException e) {
		}
	}

	/**
	 * Methods deletes all data from database
	 * 
	 * @throws SQLException
	 */
	public static void resetDatabase() throws SQLException {
		final Statement stm = getConn().createStatement();
		stm.execute("delete from medium;");
		stm.execute("delete from media_type;");
		stm.execute("delete from profile;");
		stm.execute("delete from person;");
		stm.close();
	}
}
