package Common;

/*
*
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Filehandler extends Remote {
    String FILEHANDLER_NAME_IN_REGISTRY = "ABC";

    /**
     * A function to test the availability of the remote methods.
     * @return A test string.
     */

    String testMessage() throws RemoteException;

}
