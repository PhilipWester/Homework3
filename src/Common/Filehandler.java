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

    Boolean register(Credentials credentials) throws RemoteException;

    Integer login(String username, String password) throws RemoteException;

    Boolean logout(int cookie) throws RemoteException;

    MetaData getMetadata(String filename, String fileOwner, int cookie) throws RemoteException;

    Boolean setMetadata(MetaData metaData, int cookie) throws RemoteException;

    Boolean editMetadata(MetaData metaData, String filename, String owner, int cookie) throws RemoteException;

}
