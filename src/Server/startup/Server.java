package Server.startup;

import Common.Filehandler;
import Server.Controller.Controller;
import Server.Integration.JdbcObject;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class Server {
    public static void main(String [] args){
        Server server = new Server();
        try {
            server.setupRegistry();
        } catch (RemoteException | MalformedURLException e) {
            System.err.println("Failed to start server.");
            System.err.println(e);
        }

        /*
            Test of jdbcObject
            TODO: Remove this test snippet
         */

        JdbcObject jdbcObject = null;
        try {
            jdbcObject = new JdbcObject();
        } catch (SQLException e) {
            e.printStackTrace();
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
