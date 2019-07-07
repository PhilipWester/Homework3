package Server.Model;

import java.util.HashMap;

public class ActiveUsers {
    private HashMap<Integer, String> cookies;
    public ActiveUsers(){
        cookies = new HashMap<>();
    }

    /**
     * @param username is the user that will log in.
     * @return a cookie related to the user.
     * NOTE: Insecure hash (using java hash code).
     * Yes, using java hash as key is stupid (collisions) but it is not essential for this homework.
     * TODO: Make the key not stupid.
     */
    public int loginUser(String username){
        int cookie = username.hashCode();
        cookies.put(cookie, username);
        System.out.println("User " + username + " has logged in.");
        return cookie;
    }
    /**
     * @param cookie is the cookie related to the user that wishes to log out.
     * @return true if the user was successfully logged out.
     */
    public Boolean logoutUser(int cookie){
        System.out.println("User " + verify(cookie) + " has logged out.");
        return cookies.remove(cookie, cookies.get(cookie));
    }
    /**
     * @param cookie the cookie that is related to the user
     * @return The username related to the cookie if user is logged in and null otherwise.
     */
    public String verify(int cookie){
        return cookies.get(cookie);
    }

    public Boolean isActive(String username){
        return cookies.containsValue(username);

    }

}
