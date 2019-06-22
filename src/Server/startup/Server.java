package Server.startup;

import Common.Filehandler;
import Server.Controller.Controller;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String [] args){
        Server server = new Server();
        try {
            server.setupRegistry();
        } catch (RemoteException | MalformedURLException e) {
            System.err.println("Failed to start server.");
            System.err.println(e);
        }
    }

    private void setupRegistry() throws RemoteException, MalformedURLException {
        try{
            LocateRegistry.getRegistry().list();
        }catch(java.rmi.RemoteException noRegistryRunning){
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
        Controller controller = new Controller();
        Naming.rebind(Filehandler.FILEHANDLER_NAME_IN_REGISTRY, controller);
    }
}
