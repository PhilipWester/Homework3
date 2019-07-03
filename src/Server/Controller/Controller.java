package Server.Controller;

import Common.Credentials;
import Common.Filehandler;
import Common.MetaData;
import Server.Integration.JdbcObject;
import Server.Model.ActiveUsers;

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
    public String testMessage() {
        return "Testing Successful!";
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

    //  TODO: Implement the ACTUAL login function.
    @Override
    public Boolean login(String username, String password) {
        String pwd;
        try {
            pwd = jdbcObject.getPwd(username);
            if (pwd.equals(jdbcObject.USER_NOT_FOUND)){
                return false;
            }else if(pwd.equals(password)){
                if(!activeUsers.isActive(username)){
                    activeUsers.addUser(username);
                    //  TODO: Remove this testprint
                    System.out.println("User " + username + " has logged in.");
                }else{
                    System.out.println("User " + username + " is already logged in.");
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
     * @param username is the username of the user that wishes to log out.
     * @return true if successfully logged out, false otherwise.
     */
    @Override
    public Boolean logout(String username) {
        return activeUsers.removeUser(username);
    }

    @Override
    public MetaData getMetadata(String filename) {
        if(jdbcObject.findData(filename)){
            return jdbcObject.getMeta(filename);
        }else{
            System.out.println("Could not find file: " + filename);
            return null;
        }
    }

    @Override
    public Boolean setMetadata(MetaData metaData) {
        System.out.println("Setting data...");
        return jdbcObject.setMeta(metaData);
    }
}
