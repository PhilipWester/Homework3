package Server.Controller;

import Common.Credentials;
import Common.Filehandler;
import Common.MetaData;
import Server.Integration.JdbcObject;
import Server.Model.ActiveUsers;

import java.beans.JavaBean;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

/**
*   Contains methods that client can use remotely with RMI
 *
 *  NOTE: This implementation is highly insecure.
 *  TODO: See if the clients get added to the same ActiveUsers list. (And not separate for each user).
 */
public class Controller extends UnicastRemoteObject implements Filehandler {
    private JdbcObject jdbcObject;
    private ActiveUsers activeUsers;

    public Controller() throws RemoteException, SQLException {
        super();
        this.jdbcObject = new JdbcObject();
        this.activeUsers = new ActiveUsers();

    }

    @Override
    public Boolean register(Credentials credentials) {
        try {
            if (jdbcObject.getPwd(credentials.getUsername()).equals(jdbcObject.USER_NOT_FOUND)){
                try {
                    jdbcObject.insertUser(credentials.getUsername(), credentials.getPassword());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param username is the String username of the user.
     * @param password is the String password related to the username.
     * @return An Integer cookie if successful, null otherwise.
     */
    //  TODO: Implement the ACTUAL login function.
    @Override
    public Integer login(String username, String password) {
        String pwd;
        try {
            pwd = jdbcObject.getPwd(username);
            if (pwd.equals(jdbcObject.USER_NOT_FOUND)){
                return null;
            }else if(pwd.equals(password)){
                if(!activeUsers.isActive(username)){
                    return activeUsers.loginUser(username);
                }
            return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Logs out a user.
     * @param cookie is the cookie related to the user that wishes to log out.
     * @return true if successfully logged out, false otherwise.
     */
    @Override
    public Boolean logout(int cookie) {
        return activeUsers.logoutUser(cookie);
    }

    /**
     * Collects meta data.
     * @param filename is the name of the file who's data is to be collected.
     * @param owner is the owner of of the file who's data is to be collected.
     * @param cookie is the cookie related to the user who is requesting the data.
     * @return the requested meta data if successful, null otherwise.
     */
    @Override
    public MetaData getMetadata(String filename, String owner, int cookie) {
        if(jdbcObject.findData(filename, owner)){
            MetaData metaData = jdbcObject.getMeta(filename, owner);
            //  If user has permission
            if(metaData.getPublic_access() || metaData.getOwner().equals(activeUsers.verify(cookie))){
                return metaData;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     * Uploads meta data. NOTE: The user must also be the owner of the meta data and that the owners cannot own more than one file of the same name.
     * @param metaData is the meta data that is to be uploaded
     * @param cookie is the cookie related to the user that wishes to upload the meta data
     * @return True if the upload was successful, false otherwise.
     */
    @Override
    public Boolean setMetadata(MetaData metaData, int cookie) {
        if(activeUsers.verify(cookie).equals(metaData.getOwner())) {
            if (!jdbcObject.findData(metaData.getFileName(), activeUsers.verify(cookie))) {
                return jdbcObject.setMeta(metaData);
            }
        }
        return false;
    }

    /**
     * Edits the meta data of a file
     * @param metaData Contains the data that is supposed to be edited. Attributes with value "null" won't have any effect.
     * @param filename The (current) name of the file.
     * @param owner The (current) owner of the file.
     * @param cookie The cookie related to the user making this request.
     * @return True if successful, false otherwise.
     */
    @Override
    public Boolean editMetadata(MetaData metaData, String filename, String owner, int cookie) {
        if(activeUsers.verify(cookie) == null){
            return null;
        }else{
            MetaData oldMetaData = jdbcObject.getMeta(filename, owner);
            if(oldMetaData.getOwner().equals(owner) || metaData.getWrite_access()){
                return jdbcObject.editData(metaData, filename, owner);
            }
        }
        return false;
    }
}
