package hotelapp;

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println();
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        printForm(request, response);

    }

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
            response.getWriter().println("Successfully registered the user " + usernameParam);
        }
        else {
            response.sendRedirect("/register");
        }

    }

    private static void printForm(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();
        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/register.html");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer);

    }

    private static boolean checkPassword (String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$");
    }

    private static boolean availableUsername (String username) {
        boolean usernameAvailable = DatabaseHandler.getInstance().checkUser(username);
        return !usernameAvailable;
    }

}