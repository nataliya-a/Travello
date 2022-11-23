package hotelapp.utils;

import javax.servlet.http.Cookie;

/**
 * Manages user information.
 */
public class UserManager {

    /**
     * Checks if the user is logged in.
     * @param cookies   the cookies
     * @return         true if the user is logged in, false otherwise
     */
    public static boolean isLoggedIn(Cookie[] cookies) {
        if (cookies == null) {
            return false;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username")) {
                return true;
            }
        }
        return false;
    }


    /**
     * Gets the username of the logged in user.
     * @param cookies   the cookies
     * @return          the username of the logged in user
     */
    public static String getUsername(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
