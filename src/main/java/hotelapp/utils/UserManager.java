package hotelapp.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            System.out.println("No cookies");
            return false;
        }
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName());
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

    /** Updates the last login time for user.
     * @param request request
     * @param response response
     */
    public static void updatePreviousLogIn(HttpServletRequest request, HttpServletResponse response, String username) {
        // we need to see if this is the first time the web server is running. there should be no cookies
        // so we need to add the prev cookie with current time
        if (!hasPrevCookie(request.getCookies())) {
            System.out.println("fjkslkdlk");
            // if not, we need to update the last login time
            Cookie cookie = new Cookie("prev", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss")));
            cookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(cookie);
        }

        // if a user just registered, then there's no last login time for this user
        if (!hasLastTimeForUserCookie(request.getCookies(), username)) {
            Cookie cookie = new Cookie("lastLoginTimeForUser" + username, "Unseen");
            cookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(cookie);

            Cookie updatedPrevCookie = new Cookie("prev", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss")));
            updatedPrevCookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(updatedPrevCookie);
        } else {
            Cookie cookie = new Cookie("lastLoginTimeForUser" + username, getPrevCookie(request.getCookies()));
            cookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(cookie);

            Cookie updatedPrevCookie = new Cookie("prev", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss")));
            updatedPrevCookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(updatedPrevCookie);
        }
    }

    /**
     * Gets the prev time that can be set as the last login time
     */
    private static String getPrevCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("prev")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * Check if the cookies has prev cookie.
     */
    private static boolean hasPrevCookie(Cookie[] cookies) {
        if (cookies == null) {
            return false;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("prev")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the last login time for user.
     * @param cookies  the cookies
     * @param username the username
     * @return        the last login time for user
     */
    private static boolean hasLastTimeForUserCookie(Cookie[] cookies, String username) {
        if (cookies == null) {
            return false;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("lastLoginTimeForUser" + username)) {
                return true;
            }
        }
        return false;
    }

    /** Return the last logged in time.
     * @param cookies the cookies of the user
     * @return String of the last logged in time
     */
    public static String retrieveLastTimeLoginTime(Cookie[] cookies, String username) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("lastLoginTimeForUser" + username)) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
