package Client.startup;


import Client.View.View;
import Common.Filehandler;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Starts up the client side by fetching the filehandler
 * and start the view state.
 */
public class Client {
    public static void main(String [] args) {
        try {
            Filehandler filehandler = (Filehandler) Naming.lookup(Filehandler.FILEHANDLER_NAME_IN_REGISTRY);
            new View().start(filehandler);

        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            System.err.println("Failed to initialize remote object.");
        }
    }
}
