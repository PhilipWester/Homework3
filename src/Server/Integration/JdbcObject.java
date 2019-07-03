package Server.Integration;

import Common.MetaData;

import java.sql.*;

public class JdbcObject {
    private final String USERS_TABLE = "users_table";
    private final String FILES_TABLE = "files_table";
    private final String USER_COL = "user_name";
    private final String PWD_COL = "user_password";

    private final String FILE_NAME_COL = "name";
    private final String FILE_SIZE_COL = "size";
    private final String FILE_OWNER_COL = "owner";
    private final String FILE_PUBLIC_COL = "public_access";
    private final String FILE_WRITE_COL = "write_access";


    public String USER_NOT_FOUND = "USER_NOT_FOUND";

    private Statement statement;
    private PreparedStatement setDataStatement;
    private PreparedStatement existsDataStatement;
    private PreparedStatement getDataStatement;
    /*

    private PreparedStatement getOwnerStatement;
    private PreparedStatement getPublicStatement;
    private PreparedStatement getWriteStatement;

     */
    public JdbcObject() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String DB_URL = "jdbc:mysql://localhost:3306/fileDB";
        String USERNAME = "user";
        String PASSWORD = "user";
        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        this.statement = connection.createStatement();

        setDataStatement = connection.prepareStatement("INSERT INTO " + FILES_TABLE + " VALUES (?,?,?,?,?)");
        getDataStatement = connection.prepareStatement("SELECT * FROM " + FILES_TABLE + " WHERE " + FILE_NAME_COL + " = ?");
        /*
            THESE MIGHT NOT BE NECESSARY
        getSizeStatement = connection.prepareStatement("SELECT " + FILE_SIZE_COL + " FROM " + FILES_TABLE + " WHERE " + FILE_NAME_COL + " = ?");
        getOwnerStatement = connection.prepareStatement("SELECT " + FILE_SIZE_COL + " FROM " + FILES_TABLE + " WHERE " + FILE_NAME_COL + " = ?");
        getPublicStatement = connection.prepareStatement("SELECT " + FILE_SIZE_COL + " FROM " + FILES_TABLE + " WHERE " + FILE_NAME_COL + " = ?");
        getWriteStatement = connection.prepareStatement("SELECT " + FILE_SIZE_COL + " FROM " + FILES_TABLE + " WHERE " + FILE_NAME_COL + " = ?");
        */

        existsDataStatement = connection.prepareStatement("SELECT " + FILE_NAME_COL +" FROM " + FILES_TABLE + " WHERE " + FILE_NAME_COL + " = ?" );
        initDB();
    }

    /**
     * Note: Does not check if the user already exists.
     *
     * @param username is the username that is to be inserted.
     * @param password is the password associated with the username.
     * @throws SQLException if the SQL statement call fails.
     */
    public void insertUser(String username, String password) throws SQLException {
        statement.execute("INSERT INTO " + USERS_TABLE + " VALUES " + "(" + "'" + username + "'" + ", " + "'" + password +"'" + ")");
    }

    /**
     *
     * @param username is the username who's password is returned.
     * @return the password linked to the parameter username or the String USER_NOT_FOUND if the user was not found.
     * @throws SQLException if the SQL statement call fails.
     */
    public String getPwd(String username) throws SQLException {
        ResultSet set = statement.executeQuery("SELECT " + PWD_COL + " FROM " + USERS_TABLE + " WHERE " + USER_COL + " = " + "'" + username + "'");
        if(set.next()){
            return set.getString(1);
        }
        return USER_NOT_FOUND;
    }

    //  TODO: Make implementation
    public void insertMeta(String username, String password){

    }

    /**
     *
     * @param filename is the filename of the file searched for.
     * @return true if the file was found, false otherwise.
     */
    public Boolean findData(String filename){
        try {
            existsDataStatement.setString(1, filename);
            ResultSet resultSet = existsDataStatement.executeQuery();
            existsDataStatement.clearParameters();
            if(resultSet.next()){
                String res = resultSet.getString(1);
                if(res.equals(filename)){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //  TODO: Make implementation
    public MetaData getMeta(String filename){
        try {
            MetaData metaData = new MetaData(null, 0, null, null, null);

            getDataStatement.setString(1, filename);
            ResultSet resultSet = getDataStatement.executeQuery();
            resultSet.next();
            metaData.setFileName(resultSet.getString(1));
            metaData.setSize(resultSet.getInt(2));
            metaData.setOwner(resultSet.getString(3));
            metaData.setPublic_access(resultSet.getBoolean(4));
            metaData.setWrite_access(resultSet.getBoolean(5));

            return metaData;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Inserts new meta data.
     * NOTE: Does not check if metaData already exists in DB.
     *
     * @param metaData is the data that is to be inserted.
     * @return true if successful, false otherwise.
     */
    public Boolean setMeta(MetaData metaData){
        try {
            setDataStatement.setString(1, metaData.getFileName());
            setDataStatement.setInt(2, metaData.getSize());
            setDataStatement.setString(3, metaData.getOwner());
            setDataStatement.setBoolean(4, metaData.getPublic_access());
            setDataStatement.setBoolean(5, metaData.getWrite_access());
            setDataStatement.execute();
            setDataStatement.clearParameters();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param metaData meta data that will be edited in. Attributes with "null" won't change the data.
     * @return true if successful, false otherwise.
     */
    public Boolean editData(MetaData metaData){
        return null;
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
                            FILE_NAME_COL + " TEXT, " +
                            FILE_SIZE_COL+ " INT," +
                            FILE_OWNER_COL + " TEXT," +
                            FILE_PUBLIC_COL + " BOOLEAN," +
                            FILE_WRITE_COL + " BOOLEAN" +
                            ");"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
