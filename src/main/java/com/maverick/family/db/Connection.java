package com.maverick.family.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class Connection {
	private final static Logger logger = LoggerFactory.getLogger(Connection.class);

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * Opens a connection to the in-memory SQLite instance.
	 * If the underlying connection is closed, it creates a new connection. Otherwise, the current instance is returned
	 *
	 * @throws SQLException JDBC {@link DriverManager} exception
	 */
	static java.sql.Connection createConnection(String url) throws SQLException {
		return DriverManager.getConnection(url);

	}

	/**
	 * Closes the underlying connection to the in-memory SQLite instance.
	 * It's a good practice to free up resources after you're done with them.
	 */
	static void closeConnection(java.sql.Connection con) {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	static void closeStatement(Statement stmt) {
		try {
			if (stmt != null && !stmt.isClosed()) {
				stmt.close();
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
}
