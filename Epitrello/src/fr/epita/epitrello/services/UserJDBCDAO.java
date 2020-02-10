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
 * @author Anh Tu NGUYEN & Thanh Tung TRINH - Group 2
 *
 */
public class UserJDBCDAO {

	public UserJDBCDAO() {
		try (Connection connection = getConnection()) {
			connection.prepareStatement(
					"CREATE TABLE IF NOT EXISTS USER(ID INT AUTO_INCREMENT PRIMARY KEY, USERNAME VARCHAR(50))")
					.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int createUser(User user) {
		PreparedStatement preparedStatement;
		try (Connection connection = getConnection()) {
			preparedStatement = connection
					.prepareStatement("INSERT INTO USER (USERNAME) VALUES (?)");
			preparedStatement.setString(1, user.getName());
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public User searchUser(String userName) {
		try (Connection connection = getConnection()) {
			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT ID, USERNAME FROM USER WHERE USERNAME = ?");
			prepareStatement.setString(1, userName);
			ResultSet result = prepareStatement.executeQuery();
			User user = new User();
			while (result.next()) {
				int id = result.getInt("ID");
				String name = result.getString("USERNAME");
				user.setName(name);
			}
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new User();

	}
	
	public List<User> getAllUser() {
		try (Connection connection = getConnection()) {
			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT USERNAME FROM USER");
			ResultSet result = prepareStatement.executeQuery();
			List<User> users = new ArrayList<User>();
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
		return new ArrayList<User>();
	}

	public boolean isUserExists(String userName) {
		try (Connection connection = getConnection()) {
			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT ID, USERNAME FROM USER WHERE USERNAME = ?");
			prepareStatement.setString(1, userName);
			ResultSet result = prepareStatement.executeQuery();
			if (result.next() && result.getString("USERNAME").equals(userName)) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}	
	
	private static Connection getConnection() throws SQLException {
		// To connect to the database
		String url = "jdbc:h2:../epitrello";
		String user = "admin";
		String password = "admin";

		return DriverManager.getConnection(url, user, password);
	}
}
