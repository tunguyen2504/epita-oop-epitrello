package fr.epita.epitrello.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import fr.epita.epitrello.datamodel.User;
import fr.epita.epitrello.services.UserJDBCDAO;

/**
 * @author Anh Tu NGUYEN & Thanh Tung TRINH - Group 2
 *
 */
public class TestJDBC {

	public static void main(String[] args) throws SQLException {
		testConnection();
		testCreate();
		testSearch();
	}
	
	private static void testSearch() throws SQLException {
		Connection connection = getConnection();
		UserJDBCDAO dao = new UserJDBCDAO();
		boolean isUserExists = dao.isUserExists("Anh Tu");
		System.out.println(isUserExists);
	}

	private static void testCreate() throws SQLException {

		Connection connection = getConnection();
		connection.prepareStatement(
				"CREATE TABLE IF NOT EXISTS USER(ID INT AUTO_INCREMENT PRIMARY KEY, USERNAME VARCHAR(50))")
				.execute();

		User user = new User();
		user.setName("Anh Tu");

		UserJDBCDAO dao = new UserJDBCDAO();
		
		int id = dao.createUser(user);
		boolean success = id != 0;

		System.out.println("Success ? " + success);
	}

	private static void testConnection() throws SQLException {

		Connection connection = getConnection();
		String schema = connection.getSchema();
		boolean success = "PUBLIC".equals(schema);

		System.out.println("Success ? " + success);
		connection.close();
	}

	private static Connection getConnection() throws SQLException {
		String url = "jdbc:h2:../epitrello";
		String user = "admin";
		String password = "admin";

		Connection connection = DriverManager.getConnection(url, user, password);
		return connection;
	}
	
}
