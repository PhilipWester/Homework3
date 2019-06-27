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
    private Boolean loggedIn = false;

    public void start(Filehandler filehandler){
        this.filehandler = filehandler;
        this.scanner = new Scanner(System.in);

        new Thread(this).start();
    }

    @Override
    public void run() {
        while (takeCommands){
            printOptions();
            switch (scanner.nextLine()){
                case "TEST":
                    try {
                        System.out.println(filehandler.testMessage());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                //  REGISTER
                case "1":
                    System.out.println("Enter your new username: ");
                    String username = scanner.nextLine();
                    System.out.println("Enter your new password: ");
                    String password = scanner.nextLine();

                    try {
                        if(filehandler.register(username, password)){
                            System.out.println("Successfully registered " + username);
                        }else{
                            System.out.println("Were unable to register " + username + ".\nPlease try a different username");
                        }

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                //  LOGIN
                case "2":
                    break;
                //  GET DATA
                case "3":
                    break;
                //  SET DATA
                case "4":
                    break;
                //  QUIT
                case "5":
                    takeCommands = false;
                    break;
                default:
                    System.out.println("Invalid command");

            }

        }
    }

    /**
     * An auxiallary function to print the options of the user.
     */
    private void printOptions(){
        System.out.println("Choose an action: ");
        if(loggedIn){
            System.out.println("1. Register \n2. Logout \n3. Get data \n4. Set data \n5. Quit");
        }else{
            System.out.println("1. Register \n2. Login \n3. Get data \n4. Set data \n5. Quit");
        }
    }
}
