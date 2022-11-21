package hotelapp;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ClearCookieServlet extends HttpServlet {

    @Override
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // FILL IN CODE
        // Clear the "username" cookie

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        clearCookies(request, response);
        PrintWriter out = response.getWriter();
        out.println("Cookie cleared");
        response.sendRedirect("/home");
    }

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
