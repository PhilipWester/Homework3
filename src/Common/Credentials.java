package Common;

import java.io.Serializable;

/**
 * A class for containing credentials information to be send from client to server.
 */
public class Credentials implements Serializable {
    private final String username;
    private final String password;

    public Credentials(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
