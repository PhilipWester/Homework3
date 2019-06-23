package Server.Integration;

import java.sql.*;

public class JdbcObject {
    public String connectToDB() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "user", "user");
        Statement statement = connection.createStatement();


        /*
        Below is just testing
         */
        ResultSet resultSet = statement.executeQuery("SHOW DATABASES");

        StringBuilder output = new StringBuilder();
        while (resultSet.next()){
            output.append(resultSet.getString(1)).append("\n");

        }
        return output.toString();

    }
}
