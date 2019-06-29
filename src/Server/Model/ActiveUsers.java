package Server.Model;

import java.util.ArrayList;

public class ActiveUsers {
    private ArrayList<String> users;
    public ActiveUsers(){
        users = new ArrayList<>();
    }

    /**
     *
     * @param username is the user that has logged in.
     */
    public void addUser(String username){
        users.add(username);
    }

    /**
     * TODO: Test that the function actually works. Maybe we need to find and use index instead.
     * @param username is the user that has logged out.
     */
    public Boolean removeUser(String username){
        return users.remove(username);
    }

    /**
     *
     * @param username is the user which we want to see if they are online
     * @return True if user is online, false otherwise.
     */
    public Boolean isActive(String username){
        return users.contains(username);
    }
}
