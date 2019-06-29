package Common;

/*
*
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Filehandler extends Remote {
    String FILEHANDLER_NAME_IN_REGISTRY = "FILEHANDLER_NAME_IN_REGISTRY";

    /**
     * A function to test the availability of the remote methods.
     * @return A test string.
     */

    String testMessage() throws RemoteException;

    Boolean register(Credentials credentials) throws RemoteException;

    Boolean login(String username, String password) throws RemoteException;

    Boolean logout(String username) throws RemoteException;

    String getMetadata(String fileName) throws RemoteException;

    String setMetadata(String fileName, Boolean privateFile, Boolean readOnly) throws RemoteException;

}
