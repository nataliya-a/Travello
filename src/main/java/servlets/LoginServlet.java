package servlets;
import hotelapp.DatabaseHandler;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


/** A servlet that handles login requests.
 */
public class LoginServlet extends HttpServlet {

    /**
     * This method is called when a GET request is sent to the /login URL.
     * This method should display the login page.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    response.sendRedirect("/search");
                }
            }
        } else {
            System.out.println();
            VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
            VelocityContext context = new VelocityContext();
            Template template = ve.getTemplate("templates/login.html");
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            out.println(writer);

        }

    }


    /**
     * This method is called when a POST request is sent to the /login URL.
     * This method should handle the login request and display the search page.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        String username = request.getParameter("username");

        username = StringEscapeUtils.escapeHtml4(username);

        String pass = request.getParameter("pass");

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        boolean flag = dbHandler.authenticateUser(username, pass);

        if (flag) {
            Cookie userCookie = new Cookie("username", username);
            userCookie.setMaxAge(30*60);
            response.addCookie(userCookie);
            response.sendRedirect("/search?username=" + username);

        }
        else {
            PrintWriter out = response.getWriter();
            VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
            VelocityContext context = new VelocityContext();
            context.put("error", "Invalid username or password. Try again.");
            Template template = ve.getTemplate("templates/login.html");
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            out.println(writer);
        }

    }

}