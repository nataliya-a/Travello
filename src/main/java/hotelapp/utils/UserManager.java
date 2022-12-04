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

    /** Checks the last login time for user.
     * @param request request
     * @param response response
     */
    public static void setLastLoggedIn(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd--HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        if (cookies == null) {
            Cookie currentTime = new Cookie("now", formatter.format(now));
            currentTime.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(currentTime);

            Cookie lastLoggedInCookie = new Cookie("lastLoginTime", formatter.format(now));
            lastLoggedInCookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(lastLoggedInCookie);
            return;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("now")) {
                Cookie lastLoggedInCookie = new Cookie("lastLoginTime", cookie.getValue());
                lastLoggedInCookie.setMaxAge(60 * 60 * 24 * 365);
                response.addCookie(lastLoggedInCookie);

                cookie.setValue(formatter.format(now));
                cookie.setMaxAge(60 * 60 * 24 * 365);
                response.addCookie(cookie);
                return;
            }
        }
        Cookie currentTime = new Cookie("now", formatter.format(now));
        currentTime.setMaxAge(60 * 60 * 24 * 365);
        response.addCookie(currentTime);

        Cookie lastLoggedInCookie = new Cookie("lastLoginTime", formatter.format(now));
        lastLoggedInCookie.setMaxAge(60 * 60 * 24 * 365);
        response.addCookie(lastLoggedInCookie);
    }

    /** Return the last logged in time.
     * @param cookies the cookies of the user
     * @return String of the last logged in time
     */
    public static String getLastLoggedIn(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("lastLoginTime")) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
