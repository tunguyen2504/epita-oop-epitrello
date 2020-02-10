package fr.epita.epitrello.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.epita.epitrello.datamodel.User;

/**
 * @author Anh Tu NGUYEN - Group 2 and Thanh Tung TRINH - Group 1
 *
 */
public class UserJDBCDAO {

	/**
	 * Constructor for UserJDBCDAO when creating an instance of UserJDBCDAO, this
	 * will create the table USER if it does not exist
	 */
	public UserJDBCDAO() {
		try (Connection connection = getConnection()) {
			connection
					.prepareStatement(
							"CREATE TABLE IF NOT EXISTS USER(ID INT AUTO_INCREMENT PRIMARY KEY, USERNAME VARCHAR(50))")
					.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param user the User that needs to be created
	 * @return an integer value which is the row count for SQL statement or 0 if SQL
	 *         statement return nothing
	 */
	public int createUser(User user) {
		PreparedStatement preparedStatement;
		try (Connection connection = getConnection()) {
			preparedStatement = connection.prepareStatement("INSERT INTO USER (USERNAME) VALUES (?)");
			preparedStatement.setString(1, user.getName());
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @param userName the userName of user that need to be found
	 * @return an User if there is a result in database, or else return a null User
	 */
	public User searchUser(String userName) {
		try (Connection connection = getConnection()) {
			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT ID, USERNAME FROM USER WHERE USERNAME = ?");
			prepareStatement.setString(1, userName);
			ResultSet result = prepareStatement.executeQuery();
			User user = new User();
			while (result.next()) {
				String name = result.getString("USERNAME");
				user.setName(name);
			}
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new User();

	}

	/**
	 * @return all User in table USER as an Array List
	 */
	public List<User> getAllUser() {
		try (Connection connection = getConnection()) {
			PreparedStatement prepareStatement = connection.prepareStatement("SELECT USERNAME FROM USER");
			ResultSet result = prepareStatement.executeQuery();
			List<User> users = new ArrayList<>();
			while (result.next()) {
				String name = result.getString("USERNAME");
				User user = new User();
				user.setName(name);
				users.add(user);
			}
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	/**
	 * @param userName the userName of user that need to be found
	 * @return true if user with userName exists in table USER, otherwise return
	 *         false
	 */
	public boolean isUserExists(String userName) {
		try (Connection connection = getConnection()) {
			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT ID, USERNAME FROM USER WHERE USERNAME = ?");
			prepareStatement.setString(1, userName);
			ResultSet result = prepareStatement.executeQuery();
			return (result.next() && result.getString("USERNAME").equals(userName));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * @return a database connection
	 * @throws SQLException
	 */
	private static Connection getConnection() throws SQLException {
		// To connect to the database
		String url = "jdbc:h2:../epitrello";
		String user = "admin";
		String password = "admin";

		return DriverManager.getConnection(url, user, password);
	}
}
