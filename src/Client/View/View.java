package Client.View;

import Common.Filehandler;

import java.rmi.RemoteException;

/**
*   Might need to change name.
*   View should present the view to the user.
 */

public class View implements Runnable {
    private Filehandler filehandler;

    public void start(Filehandler filehandler){
        this.filehandler = filehandler;

        new Thread(this).start();
    }

    @Override
    public void run() {
        String testString = null;
        try {
            testString = filehandler.testMessage();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println(testString);
    }
}
