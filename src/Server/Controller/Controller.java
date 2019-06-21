package Server.Controller;

import Common.Filehandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/*
*   Contains methods that client can use remotely with RMI
 */
public class Controller extends UnicastRemoteObject implements Filehandler {

    public Controller() throws RemoteException {
        super();

    }

    @Override
    public String testMessage() {
        return "Testing Successfull!";
    }
}
