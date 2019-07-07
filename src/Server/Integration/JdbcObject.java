package Server.Integration;

import Common.MetaData;

import java.sql.*;

/**
 * An object for interacting with the database.
 */
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
    private PreparedStatement editNameStatement;
    private PreparedStatement editSizeStatement;
    private PreparedStatement editOwnerStatement;
    private PreparedStatement editWriteStatement;
    private PreparedStatement editPubStatement;

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
        getDataStatement = connection.prepareStatement("SELECT * FROM " + FILES_TABLE + " WHERE " + FILE_NAME_COL + " = ? AND " + FILE_OWNER_COL +" = ? ");
        existsDataStatement = connection.prepareStatement("SELECT " + FILE_NAME_COL +" FROM " + FILES_TABLE + " WHERE " + FILE_NAME_COL + " = ? AND " + FILE_OWNER_COL +" = ? ");
        //  TODO: switch place on owner col and name col in this statement
        //  1: Which col to edit. 2: New value. 3: Owner (old)  4: File name (old)
        editNameStatement = connection.prepareStatement("UPDATE " + FILES_TABLE + " SET " + FILE_NAME_COL + " = ? WHERE " + FILE_OWNER_COL + " = ? AND " + FILE_NAME_COL + " = ?");
        initDB();
        editSizeStatement = connection.prepareStatement("UPDATE " + FILES_TABLE + " SET " + FILE_SIZE_COL + " = ? WHERE " + FILE_OWNER_COL + " = ? AND " + FILE_NAME_COL + " = ?");
        initDB();
        editOwnerStatement = connection.prepareStatement("UPDATE " + FILES_TABLE + " SET " + FILE_OWNER_COL + " = ? WHERE " + FILE_OWNER_COL + " = ? AND " + FILE_NAME_COL + " = ?");
        initDB();
        editWriteStatement = connection.prepareStatement("UPDATE " + FILES_TABLE + " SET " + FILE_WRITE_COL + " = ? WHERE " + FILE_OWNER_COL + " = ? AND " + FILE_NAME_COL + " = ?");
        initDB();
        editPubStatement = connection.prepareStatement("UPDATE " + FILES_TABLE + " SET " + FILE_PUBLIC_COL + " = ? WHERE " + FILE_OWNER_COL + " = ? AND " + FILE_NAME_COL + " = ?");
        initDB();
    }

    /**
     * @param username is the username that is to be inserted.
     * @param password is the password associated with the username.
     * @throws SQLException if the SQL statement call fails.
     * Note: Does not check if the user already exists.
     */
    public void insertUser(String username, String password) throws SQLException {
        statement.execute("INSERT INTO " + USERS_TABLE + " VALUES " + "(" + "'" + username + "'" + ", " + "'" + password +"'" + ")");
    }

    /**
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

    /**
     * @param filename is the filename of the file searched for.
     * @return true if the file was found, false otherwise.
     */
    public Boolean findData(String filename, String owner){
        try {
            existsDataStatement.setString(1, filename);
            existsDataStatement.setString(2, owner);
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
        System.out.println("Could not find file: " + filename);
        return false;
    }

    //  TODO: Make implementation
    public MetaData getMeta(String filename, String owner){
        try {
            MetaData metaData = new MetaData(null, 0, null, null, null);

            getDataStatement.setString(1, filename);
            getDataStatement.setString(2, owner);
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
     * Edits already inserted meta data.
     * @param metaData meta data that will be edited in. Attributes with "null" won't be edited in.
     * @param filename is the (old) file name of the file.
     * @param owner is the (previous) owner of the file.
     * @return true if successful, false otherwise.
     */
    public Boolean editData(MetaData metaData, String filename, String owner){
        if(metaData.getWrite_access() != null){
            try {
                editWriteStatement.setBoolean(1, metaData.getWrite_access());
                editWriteStatement.setString(2, owner);
                editWriteStatement.setString(3, filename);
                editWriteStatement.execute();
                editWriteStatement.clearParameters();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        if(metaData.getPublic_access() != null){
            try {
                editPubStatement.setBoolean(1, metaData.getPublic_access());
                editPubStatement.setString(2, owner);
                editPubStatement.setString(3, filename);
                editPubStatement.execute();
                editPubStatement.clearParameters();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        if(metaData.getSize() != null){
            try {
                editSizeStatement.setInt(1, metaData.getSize());
                editSizeStatement.setString(2, owner);
                editSizeStatement.setString(3, filename);
                editSizeStatement.execute();
                editSizeStatement.clearParameters();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        boolean newOwner = false;
        if(metaData.getOwner() != null){
            newOwner = true;
            try {
                editOwnerStatement.setString(1, metaData.getOwner());
                editOwnerStatement.setString(2, owner);
                editOwnerStatement.setString(3, filename);
                editOwnerStatement.execute();
                editOwnerStatement.clearParameters();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        if(metaData.getFileName() != null){
            try {
                editNameStatement.setString(1, metaData.getFileName());
                if(newOwner) {
                    editNameStatement.setString(2, metaData.getOwner());
                }else {
                    editNameStatement.setString(2, owner);
                }
                editNameStatement.setString(3, filename);
                editNameStatement.execute();
                editNameStatement.clearParameters();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
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
