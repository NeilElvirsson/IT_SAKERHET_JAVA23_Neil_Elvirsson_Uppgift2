package sqliteuserrepository;

import domain.User;
import userrepository.UserRepository;

import java.sql.*;

public class SqliteUserRepository implements UserRepository {

    private Connection getDatabaseConnection() throws SQLException {
        Connection connection = null;

        String url = "jdbc:sqlite:database.db";
        connection = DriverManager.getConnection(url);
        System.out.println("Connected to database");
        return connection;
    }

    public User getUser(String userName, String password) {

        Connection connection = null;

        try {
            connection = this.getDatabaseConnection();

            String sql = "SELECT user_name FROM users WHERE user_name = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String foundUserName = resultSet.getString("user_name");

                User user = new User(foundUserName);

                return user;


            } else {

                return null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            if (connection != null) {
                try {
                    connection.close();

                } catch (SQLException e) {
                    System.out.println(e.getMessage());

                }
            }
        }

        return null;
    }

    public boolean addUser(User user) {

        Connection connection = null;

        try {
            connection = this.getDatabaseConnection();

            String sql = "INSERT INTO users (user_name, password) VALUES(?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getPassword());

            preparedStatement.execute();

            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());

            }
        }
    }

    public User getUser(String username) {

        Connection connection = null;

        try {
            connection = this.getDatabaseConnection();

            String sql = "SELECT user_name FROM users WHERE user_name = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                String foundUserName = resultSet.getString("user_name");
                User user = new User(foundUserName);
                return user;
            }
            return null;




        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            if(connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {

                    System.out.println(e.getMessage());
                }
        }
        return null;
    }

}
