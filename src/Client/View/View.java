package Client.View;

import Common.Credentials;
import Common.Filehandler;
import Common.MetaData;

import java.rmi.RemoteException;
import java.util.Scanner;

public class View implements Runnable {
    private Filehandler filehandler;
    private Scanner scanner;
    private boolean takeCommands = true;
    private Integer cookie = null;

    public void start(Filehandler filehandler){
        this.filehandler = filehandler;
        this.scanner = new Scanner(System.in);

        new Thread(this).start();
    }

    @Override
    public void run() {
        String username, password;
        while (takeCommands){
            printOptions();
            String nextLine = scanner.nextLine();
            switch (nextLine){
                //  REGISTER
                case "1":
                    register();
                    break;
                //  LOGIN/LOG OUT
                case "2":
                    if(cookie != null){
                        logout();

                    }else{
                        login();
                    }
                    break;
                //  GET DATA
                case "3":
                    //  TODO: Make sure that accessing a file without being logged in to a (correct) account gives an Error.
                    getData();
                    break;
                //  SET DATA
                case "4":
                    setData();
                    break;
                //  EDIT DATA
                case "5":
                    editData();
                    break;
                //  QUIT
                case "6":
                    takeCommands = false;
                    if(cookie != null){
                        logout();
                    }
                    break;
                default:
                    printIllegalCommand(nextLine);

            }

        }
    }

    /**
     * An auxiliary function to print the options of the user.
     */
    private void printOptions(){
        System.out.println("Choose an action: ");
        if(cookie == null){
            System.out.println("1. Register \n2. Login \n6. Quit");
        }else{
            System.out.println("1. Register \n2. Logout \n3. Get data \n4. Set data \n5. Edit data \n6. Quit");
        }
    }

    private void printIllegalCommand(String string){
        System.out.println("Invalid command: " + string);
    }

    private void login(){
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        try {
            cookie = filehandler.login(username, password);
            if(cookie != null){
                System.out.println("Logged in successfully.");
            }else{
                System.out.println("Failed to log in.");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void logout(){
        try {
            if(filehandler.logout(cookie)){
                cookie = null;
                System.out.println("Successfully logged out,");
            }else{
                System.out.println("Failed to log out.");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setData(){
        if(cookie != null){
            System.out.println("What is the name of the file?");
            String name = scanner.nextLine();

            System.out.println("Should there be public read access? (y/n)");
            Boolean readAcc = scanner.nextLine().toUpperCase().equals("Y");

            System.out.println("Should there be public write access? (y/n)");
            Boolean writeAcc = scanner.nextLine().toUpperCase().equals("Y");

            System.out.println("Who is the new owner?");
            String owner = scanner.nextLine();

            System.out.println("What is the size of the file?");
            Integer size;

            //  Make sure that the size is an integer.
            while(true){
                if(scanner.hasNextInt()){
                    size = scanner.nextInt();
                    scanner.nextLine();
                    break;
                }else{
                    String next = scanner.nextLine();
                    System.out.println(next + " is not an integer. Try again");
                }
            }

            MetaData metaData = new MetaData(name, size, owner, readAcc, writeAcc);

            try {
                Boolean res = filehandler.setMetadata(metaData, cookie);
                if(res){
                    System.out.println("Successfully set new meta data.");
                }else{
                    System.out.println("Failed to set new meta data.");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("You have to be logged in to use this action.");
        }

    }

    private void getData(){
        if(cookie != null){
            System.out.println("Specify file by name:");
            String fileName = scanner.nextLine();
            System.out.println("And owner:");
            String owner = scanner.nextLine();
            try {
                MetaData metaData = filehandler.getMetadata(fileName, owner, cookie);
                if(metaData != null){
                    metaData.printData();
                }else{
                    System.out.println("Failed to get data");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("You have to be logged in to use this action.");
        }
    }

    private void register(){
        System.out.println("Enter your new username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your new password: ");
        String password = scanner.nextLine();
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
    }

    private void editData(){

        Integer size;

        System.out.println("What is the name of the file?");
        String oldName = scanner.nextLine();

        System.out.println("Who is the current owner of the file? ");
        String oldOwner = scanner.nextLine();

        System.out.println("What is the new name of the file?");
        String name = scanner.nextLine();
        if(name.equals("")){
            name = null;
        }

        System.out.println("Should there be public read access? (y/n)");
        String tmp = scanner.nextLine().toUpperCase();
        Boolean readAcc;
        if(tmp.equals("Y")){
            readAcc = true;
        }else if(tmp.equals("N")){
            readAcc = false;
        }else{
            readAcc = null;
        }

        System.out.println("Should there be public write access? (y/n)");
        tmp = scanner.nextLine().toUpperCase();
        Boolean writeAcc ;
        if(tmp.equals("Y")){
            writeAcc = true;
        }else if(tmp.equals("N")){
            writeAcc = false;
        }else{
            writeAcc = null;
        }

        System.out.println("Who is the new owner of the file?");
        String owner = scanner.nextLine();
        if(owner.equals("")){
            owner = null;
        }

        System.out.println("What is the size of the file?");

        if(scanner.hasNextInt()){
            size = scanner.nextInt();
        }else{
            System.out.println("You will keep the current size.");
            size = null;
        }
        scanner.nextLine();

        MetaData metaData = new MetaData(name, size, owner, readAcc, writeAcc);

        try {
            filehandler.editMetadata(metaData, oldName, oldOwner, cookie);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
