package sqlitetimecapsulerepository;

import application.repositories.TimeCapsuleRepository;
import domain.TimeCapsule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteTimeCapsuleRepository implements TimeCapsuleRepository {

    private Connection getDatabaseConnection() throws SQLException {
        Connection connection = null;

        String url = "jdbc:sqlite:database.db";
        connection = DriverManager.getConnection(url);
        System.out.println("Connected to database");
        return connection;
    }
    public TimeCapsule getTimeCapsuleById(String id, String userName) {

        Connection connection = null;

        try{
            connection = this.getDatabaseConnection();

            String sql = "SELECT id, title, text, user_name, created FROM timecapsules WHERE id = ? AND user_name = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, userName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {

                String timeCapsuleId = resultSet.getString("id");
                String timeCapsuleTitle = resultSet.getString("title");
                String timeCapsuleText = resultSet.getString("text");
                String timeCapsuleUserName = resultSet.getString("user_name");
                int timeCapsuleCreated = resultSet.getInt("created");

                TimeCapsule timeCapsule = new TimeCapsule();
                timeCapsule.setId(timeCapsuleId);
                timeCapsule.setTitle(timeCapsuleTitle);
                timeCapsule.setText(timeCapsuleText);
                timeCapsule.setUserName(timeCapsuleUserName);
                timeCapsule.setCreated(timeCapsuleCreated);

                return timeCapsule;
            }



        } catch (SQLException e) {
            System.out.println("sqlitegTimeCapsuleRepository : getTimeCapsuleById" + e.getMessage());

        } finally {
            if(connection != null) {

                try {
                    connection.close();

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            }
        }

        return null;
    }

    public boolean addTimeCapsule(TimeCapsule timeCapsule) {

        Connection connection = null;

        try {
             connection =  getDatabaseConnection();
            String sql = "INSERT INTO timecapsules (id, title, text, user_name) VALUES (?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, timeCapsule.getId());
            preparedStatement.setString(2, timeCapsule.getTitle());
            preparedStatement.setString(3, timeCapsule.getText());
            preparedStatement.setString(4, timeCapsule.getUserName());

            preparedStatement.execute();
            return true;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if(connection != null )
                connection.close();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
        return false;
    }


}
