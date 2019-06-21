package Common;

/*
*
 */

import java.rmi.Remote;

public interface Filehandler extends Remote {
    String FILEHANDLER_NAME_IN_REGISTRY = "ABC";

    String testMessage();
}
