package hotelapp;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/** Servlet that handles logout.
 */
public class LogoutServlet extends HttpServlet {
    /**
     * doGet method for LogoutServlet.
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Clear the "username" cookie

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        clearCookies(request, response);
        response.sendRedirect("/home");
    }

    /**
     * Clear the "username" cookie
     * @param request
     * @param response
     */
    public void clearCookies(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue(null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }
}
