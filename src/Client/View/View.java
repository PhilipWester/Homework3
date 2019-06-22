package Client.View;

import Common.Filehandler;

import java.rmi.RemoteException;
import java.util.Scanner;

/**
*   Might need to change name.
*   View should present the view to the user.
 */

public class View implements Runnable {
    private Filehandler filehandler;
    private Scanner scanner;
    private boolean takeCommands = true;

    public void start(Filehandler filehandler){
        this.filehandler = filehandler;
        this.scanner = new Scanner(System.in);

        new Thread(this).start();
    }

    @Override
    public void run() {
        while (takeCommands){
            switch (scanner.nextLine().toUpperCase()){
                case "TEST":
                    try {
                        System.out.println(filehandler.testMessage());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case "QUIT":
                    takeCommands = false;
                    break;

                default:
                    System.out.println("Invalid command");

            }

        }
    }
}
