package Client.View;

import Common.Filehandler;

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
        String testString = filehandler.testMessage();
        System.out.println(testString);
    }
}
