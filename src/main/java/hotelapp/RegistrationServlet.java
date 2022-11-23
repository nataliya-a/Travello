package hotelapp;

import hotelapp.utils.UserManager;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Pattern;


/** An example that demonstrates how to process HTML forms with servlets.
 */
@SuppressWarnings("serial")
public class RegistrationServlet extends HttpServlet {
    /**
     * This method is called when a GET request is sent to the /register URL.
     * This method should display the registration page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println();
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        if (UserManager.isLoggedIn(request.getCookies())) {
            out.println("<h1>You are already logged in!</h1>");
            response.sendRedirect("/search");
            return;
        }

        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/register.html");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer);

    }

    /**
     * This method is called when a POST request is sent to the /register URL.
     * This method should handle the registration request and display the search page.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        String usernameParam = request.getParameter("name");
        usernameParam = StringEscapeUtils.escapeHtml4(usernameParam);
        String password = request.getParameter("pass");
        password = StringEscapeUtils.escapeHtml4(password);
        if (checkPassword(password) && availableUsername(usernameParam)) {
            DatabaseHandler dbHandler = DatabaseHandler.getInstance();
            dbHandler.registerUser(usernameParam, password);
//            response.getWriter().println("Successfully registered the user " + usernameParam);
            PrintWriter out = response.getWriter();
            VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
            VelocityContext context = new VelocityContext();
            Template template = ve.getTemplate("templates/register.html");
            context.put("message", "Successfully registered the user " + usernameParam);
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            out.println(writer);
        }
        else if (!checkPassword(password) && availableUsername(usernameParam)) {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);

            PrintWriter out = response.getWriter();
            VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
            VelocityContext context = new VelocityContext();
            Template template = ve.getTemplate("templates/register.html");
            context.put("error", "Invalid password. Password must be at least 8 characters long and contain " +
                    "at least one digit, one uppercase letter, and one lowercase letter.");
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            out.println(writer);

        }
        else if (checkPassword(password) && !availableUsername(usernameParam)) {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);

            PrintWriter out = response.getWriter();
            VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
            VelocityContext context = new VelocityContext();
            Template template = ve.getTemplate("templates/register.html");
            context.put("error", "Username already taken");
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            out.println(writer);
        }

    }

    /**
     * This method checks if the password is valid.
     * @param password
     * @return true if the password is valid, false otherwise.
     */
    private static boolean checkPassword (String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$");
    }

    /**
     * This method checks if the username is available.
     * @param username
     * @return true if the username is available, false otherwise.
     */
    private static boolean availableUsername (String username) {
        boolean usernameAvailable = DatabaseHandler.getInstance().checkUser(username);
        return !usernameAvailable;
    }



}