package Server.Controller;

import Common.Filehandler;
import Server.Integration.JdbcObject;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

/*
*   Contains methods that client can use remotely with RMI
 */
public class Controller extends UnicastRemoteObject implements Filehandler {
    JdbcObject jdbcObject;

    public Controller() throws RemoteException, SQLException {
        super();
        this.jdbcObject = new JdbcObject();

    }

    @Override
    public String testMessage() {
        return "Testing Successfull!";
    }

    @Override
    public Boolean register(String username, String password) {
        try {
            if (jdbcObject.getPwd(username).equals(jdbcObject.USER_NOT_FOUND)){
                try {
                    jdbcObject.insertUser(username, password);
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
            }else return pwd.equals(password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String logout() {
        return null;
    }

    @Override
    public String getMetadata(String fileName) {
        return null;
    }

    @Override
    public String setMetadata(String fileName, Boolean privateFile, Boolean readOnly) {
        return null;
    }
}
