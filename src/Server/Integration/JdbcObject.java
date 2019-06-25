package Server.Integration;

import java.sql.*;

public class JdbcObject {
    private String DB_URL = "jdbc:mysql://localhost:3306/fileDB";
    private String USERNAME = "user";
    private String PASSWORD = "user";

    //  TODO: Create pre-defined statements.
    private Statement statement;

    public JdbcObject() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        this.statement = connection.createStatement();

        initDB();

        /*
        Below is just testing
         */
        /*
        ResultSet resultSet = statement.executeQuery("SHOW DATABASES");

        StringBuilder output = new StringBuilder();
        while (resultSet.next()){
            output.append(resultSet.getString(1)).append("\n");

        }
        return output.toString();

         */

    }

    /**
     *  Initializes the tables in the database
     */
    private void initDB() {
        try {

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + "users_table" + "(" +
                    " user_name TEXT, " +
                    " user_password TEXT" +
                    ");"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + "files_table" + "(" +
                            " name TEXT, " +
                            " size INT," +
                            " owner TEXT," +
                            "public_access BOOLEAN," +
                            "write_permission BOOLEAN, " +
                            "read_permisson BOOLEAN" +
                            ");"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
