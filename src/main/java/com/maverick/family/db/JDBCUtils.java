package com.maverick.family.db;

import com.maverick.family.entity.Edge;
import com.maverick.family.entity.Gender;
import com.maverick.family.entity.Person;
import com.maverick.family.entity.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.maverick.family.db.Connection.closeConnection;
import static com.maverick.family.db.Connection.closeStatement;
import static com.maverick.family.db.Connection.createConnection;


public final class JDBCUtils {
	private final static Logger logger = LoggerFactory.getLogger(JDBCUtils.class);

	private static final String JDBC_URL = "jdbc:sqlite:family_graph.db";

	/**
	 * Creates the table to hold person and their relations
	 */
	public static void createTables() {
		logger.debug("Creating Tables.");
		Connection con = null;
		try {
			con = createConnection(JDBC_URL);
			final Statement stmt = con.createStatement();
			stmt.executeUpdate("create table IF NOT EXISTS person (name string, gender string, PRIMARY KEY(name))");
			stmt.executeUpdate("create table IF NOT EXISTS relation (from_person string, to_person string, relation string)");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());

		} finally {
			closeConnection(con);
		}
	}

	private static void deleteAllRecords() {
		logger.debug("Deleting All Records.");
		Connection con = null;
		try {
			con = createConnection(JDBC_URL);
			final Statement stmt = con.createStatement();
			stmt.executeUpdate("delete from person");
			stmt.executeUpdate("delete from relation");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());

		} finally {
			closeConnection(con);
		}
	}

	/**
	 * Initializes the tables with family getFamilyMembers and relationship data
	 */
	public static void initSchema(List<Person> personList, List<Edge> edges) {
		deleteAllRecords();
		logger.debug("Inserting initial records.");
		Connection con = null;
		try {
			con = createConnection(JDBC_URL);
			try (PreparedStatement crsStmt = con.prepareStatement("INSERT INTO person VALUES (?, ?)")) {
				for (Person p : personList) {
					crsStmt.setString(1, p.getName());
					crsStmt.setString(2, p.getGender().name());
					crsStmt.executeUpdate();
				}
			}

			try (PreparedStatement preparedStmt = con.prepareStatement("INSERT INTO relation VALUES (?, ?, ? )")) {
				for (Edge e : edges) {
					preparedStmt.setString(1, e.getFromPerson());
					preparedStmt.setString(2, e.getToPerson());
					preparedStmt.setString(3, e.getRelation().name());
					preparedStmt.executeUpdate();
				}
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			closeConnection(con);
		}
	}


	public static void addPerson(String memberName, String gender) {
		logger.debug("Adding member : {}", memberName);
		Connection con = null;
		Statement stmt = null;
		try {
			con = createConnection(JDBC_URL);
			stmt = con.createStatement();
			stmt.executeUpdate(
					"Insert into Person(name, gender) VALUES (\'" + memberName + "\'" + "," + "\'" + gender + "\')"
			);

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			closeStatement(stmt);
			closeConnection(con);
		}
	}

	public static void removePerson(String memberName) {
		logger.debug("Removing member : {}", memberName);
		Connection con = null;
		Statement stmt = null;
		try {
			con = createConnection(JDBC_URL);
			stmt = con.createStatement();
			stmt.executeUpdate(
					"Delete from Person where name= (\'" + memberName + "\')"
			);

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			closeStatement(stmt);
			closeConnection(con);
		}
	}


	public static void createRelation(String from, String to, String relation) {
		logger.debug("Creating relation - {} is {} of {}", from, relation, to);
		Connection con = null;
		Statement stmt = null;
		try {
			con = createConnection(JDBC_URL);
			stmt = con.createStatement();
			stmt.executeUpdate(
					"Insert into Relation VALUES (\'" + from + "\' , \'" + to + "\' , \'" + relation + "\'" + " )"
			);

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			closeStatement(stmt);
			closeConnection(con);
		}
	}

	public static void removeRelation(String from, String to) {
		logger.debug("Removing relation between {} and {} : ", from, to);
		Connection con = null;
		Statement stmt = null;
		try {
			con = createConnection(JDBC_URL);
			stmt = con.createStatement();
			stmt.executeUpdate(
					"Delete from Relation Where  " +
							"from_person =\'" + from + "\'" +
							" and " +
							"to_person =\'" + to + "\'"

			);

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			closeStatement(stmt);
			closeConnection(con);
		}
	}

	public static Person getPerson(String personName) {
		logger.debug("Get person {} details from DB", personName);
		Connection con = null;
		Statement stmt = null;
		try {
			con = createConnection(JDBC_URL);
			stmt = con.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT name, gender FROM person where name=\'" + personName + "\'");
			if (!rs.next()) {
				return null;
			}
			String name = rs.getString(1);
			String gender = rs.getString(2);
			return new Person(name, Gender.valueOf(gender));

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			closeStatement(stmt);
			closeConnection(con);
		}
	}

	public static List<Person> getAllMembers() {
		Connection con = null;
		Statement stmt = null;
		List<Person> personList = new ArrayList<>();
		try {
			con = createConnection(JDBC_URL);
			stmt = con.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT name, gender FROM person");
			while (rs.next()) {
				String name = rs.getString(1);
				String gender = rs.getString(2);
				personList.add(new Person(name, Gender.valueOf(gender)));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			closeStatement(stmt);
			closeConnection(con);
		}
		return personList;
	}

	public static Relation getRelation(String fromMember, String toMember) {
		logger.debug("Get relation between {} and {} : ", fromMember, toMember);
		Connection con = null;
		Statement stmt = null;
		try {
			con = createConnection(JDBC_URL);
			stmt = con.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT relation FROM relation " +
					"Where from_person =\'" + fromMember + "\'" +
					"AND to_person =\'" + toMember + "\'"
			);
			if (!rs.next()) {
				return null;
			}
			String relation = rs.getString(1);
			return Relation.valueOf(relation);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			closeStatement(stmt);
			closeConnection(con);
		}
	}


	public static List<Edge> getRelations(String fromMember) {
		logger.debug("Get all relations of {}  : ", fromMember);
		Connection con = null;
		Statement stmt = null;
		List<Edge> edges = new ArrayList<>();
		try {
			con = createConnection(JDBC_URL);
			stmt = con.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT to_person, relation FROM relation Where from_person =\'" + fromMember + "\'");
			while (rs.next()) {
				String toPerson = rs.getString(1);
				String relation = rs.getString(2);
				edges.add(new Edge(fromMember, toPerson, Relation.valueOf(relation)));
			}


		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			closeStatement(stmt);
			closeConnection(con);
		}
		return edges;
	}


	public static List<Edge> getRelations() {
		logger.debug("Get all relations");
		Connection con = null;
		Statement stmt = null;
		List<Edge> edges = new ArrayList<>();
		try {
			con = createConnection(JDBC_URL);
			stmt = con.createStatement();
			final ResultSet relationRS = stmt.executeQuery("SELECT from_person, to_person, relation FROM relation");

			while (relationRS.next()) {
				String fromPerson = relationRS.getString(1);
				String toPerson = relationRS.getString(2);
				String relation = relationRS.getString(3);
				edges.add(new Edge(fromPerson, toPerson, Relation.valueOf(relation)));


			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			closeStatement(stmt);
			closeConnection(con);
		}
		return edges;
	}

	public static boolean memberExists(String memberName) {
		logger.debug("Checking whether {} exists ", memberName);
		Connection con = null;
		Statement stmt = null;
		try {
			con = createConnection(JDBC_URL);
			stmt = con.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT name FROM person WHERE name =\'" + memberName + "\'");
			final boolean exists = rs.next();
			return exists;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			closeStatement(stmt);
			closeConnection(con);
		}
	}


}