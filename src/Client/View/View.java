package Client.View;

import Common.Credentials;
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
    private Boolean isLoggedIn = false;

    public void start(Filehandler filehandler){
        this.filehandler = filehandler;
        this.scanner = new Scanner(System.in);

        new Thread(this).start();
    }

    @Override
    public void run() {
        String username = null, password;
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
                    username = scanner.nextLine();
                    System.out.println("Enter your new password: ");
                    password = scanner.nextLine();
                    Credentials credentials = new Credentials(username, password);

                    try {
                        if(filehandler.register(credentials)){
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
                    if(isLoggedIn){
                        //  TODO: Implement log out
                        try {
                            if(isLoggedIn = !filehandler.logout(username))
                            System.out.println("You are already logged in");

                            if(isLoggedIn){
                                System.out.println("Failed to log out.");
                            }else{
                                System.out.println("Successfully logged out,");
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    System.out.println("Enter your username: ");
                    username = scanner.nextLine();
                    System.out.println("Enter your password: ");
                    password = scanner.nextLine();

                    try {
                        if(isLoggedIn = filehandler.login(username, password)){
                            System.out.println("Logged in successfully.");
                        }else{
                            System.out.println("Login Failed.");
                            username = null;
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                //  GET DATA
                case "3":
                    //  TODO: Make sure that accessing a file without being logged in to a (correct) account gives an Error.
                        System.out.println("Specify file by name");
                        if(isLoggedIn){
                            String fileName = scanner.nextLine();
                            try {
                                System.out.println(filehandler.getMetadata(fileName));
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }else{
                            System.out.println("You have to be logged in to use this action.");
                        }
                    break;
                //  SET DATA
                case "4":
                    if(!isLoggedIn){
                        printIllegalCommand();
                    }
                    break;
                //  QUIT
                case "5":
                    takeCommands = false;
                    break;
                default:
                    printIllegalCommand();

            }

        }
    }

    /**
     * An auxiliary function to print the options of the user.
     */
    private void printOptions(){
        System.out.println("Choose an action: ");
        if(isLoggedIn){
            System.out.println("1. Register \n2. Logout \n3. Get data \n4. Set data \n5. Quit");
        }else{
            System.out.println("1. Register \n2. Login \n5. Quit");
        }
    }

    private void printIllegalCommand(){
        System.out.println("Invalid command");
    }
}
