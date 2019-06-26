package Server.Integration;

import java.sql.*;

public class JdbcObject {
    private String DB_URL = "jdbc:mysql://localhost:3306/fileDB";
    private String USERNAME = "user";
    private String PASSWORD = "user";
    private String USERS_TABLE = "users_table";
    private String FILES_TABLE = "files_table";
    private String USER_COL = "user_name";
    private String PWD_COL = "user_password";

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
     * Note: Does not check if the user already exists.
     *
     * @param username is the username that is to be inserted.
     * @param password is the password associated with the username.
     * @throws SQLException if the SQL statement call fails.
     */
    //  TODO: Make implementation
    public void insertUser(String username, String password) throws SQLException {
        statement.execute("INSERT INTO " + USERS_TABLE + " VALUES " + "(" + "'" + username + "'" + ", " + "'" + password +"'" + ")");
    }

    /**
     *
     * @param username is the username who's password is returned.
     * @return the password linked to the parameter username.
     * @throws SQLException if the SQL statement call fails.
     */
    //  TODO: Make implementation
    public String getPwd(String username) throws SQLException {
        ResultSet set = statement.executeQuery("SELECT " + PWD_COL + " FROM " + USERS_TABLE + " WHERE " + USER_COL + " = " + "'" + username + "'");
        set.next();
        return set.getString(1);
    }

    //  TODO: Make implementation
    public void insertMeta(String username, String password){

    }

    //  TODO: Make implementation
    public String[] getMeta(String filename){
        return new String[]{" ", " "};
    }

    /**
     *  Initializes the tables in the database
     */
    private void initDB() {
        try {

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + USERS_TABLE + "(" +
                     USER_COL + " TEXT, " +
                     PWD_COL + " TEXT" +
                    ");"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + FILES_TABLE + "(" +
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
