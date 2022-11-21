package hotelapp;
import hotelapp.DatabaseHandler;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;


@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String username = request.getParameter("username");
//        System.out.println("username: " + username);
//        username = StringEscapeUtils.escapeHtml4(username);
//        System.out.println();
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();


        Cookie[] cookies = request.getCookies();

        if (cookies != null ) { //|| request.getParameter("username") != null
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    response.sendRedirect("/search");
//                    out.println("<h3>Welcome, " + cookie.getValue() + "</h3>");
                }
            }
        } else {
            System.out.println();
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            out.printf("<html>%n%n");
            out.printf("<head><title>%s</title></head>%n", "Form");
            out.printf("<body>%n");

            printForm(request, response);

            out.printf("%n</body>%n");
            out.printf("</html>%n");
        }

//        if (request.getParameter("username") == null) {
//            out.println("Please login below.</p>");
//            System.out.println();
//            response.setContentType("text/html");
//            response.setStatus(HttpServletResponse.SC_OK);
//            out.printf("<html>%n%n");
//            out.printf("<head><title>%s</title></head>%n", "Form");
//            out.printf("<body>%n");
//
//            printForm(request, response);
//
//            out.printf("%n</body>%n");
//            out.printf("</html>%n");
//        }
//        else  {
//            // already logged in
//            System.out.println();
//            response.setContentType("text/html");
//            response.setStatus(HttpServletResponse.SC_OK);
//            PrintWriter pw = response.getWriter();
//            Cookie[] cookies = request.getCookies();
////            for (Cookie cookie : cookies) {
////                System.out.println(cookie.getName() + " " + cookie.getValue());
////
//            if (cookies != null) {
//                for (Cookie cookie : cookies) {
//                    if (cookie.getName().equals("username")) {
//                        pw.println("Welcome, " + cookie.getValue() + "!");
//                    }
//                }
////                out.println("Login successful. Welcome, " + username);
//            }
//
//        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        String username = request.getParameter("username");
        username = StringEscapeUtils.escapeHtml4(username);
        Cookie userCookie = new Cookie("username", username);
        userCookie.setMaxAge(30*60);
        response.addCookie(userCookie);

        String pass = request.getParameter("pass");

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        boolean flag = dbHandler.authenticateUser(username, pass);
        if (flag) {
//            Cookie userCookie = new Cookie("username", username);
//            userCookie.setMaxAge(30*60);
//            response.addCookie(userCookie);

//            response.sendRedirect("/hotels");
//            response.addCookie(new Cookie("username", username));
            response.sendRedirect("/login?username=" + username);
        }
        else
            response.sendRedirect("/login");

    }

    private static void printForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        PrintWriter out = response.getWriter();
//
//        out.printf("<form method=\"post\" action=\"%s\">%n", request.getServletPath());
//        out.printf("Enter your username:<br><input type=\"text\" name=\"username\"><br>");
//        out.printf("Enter your password:<br><input type=\"password\" name=\"pass\"><br>");
//        out.printf("<p><input type=\"submit\" value=\"Enter\"></p>\n%n");
//        out.printf("</form>\n%n");
        PrintWriter out = response.getWriter();
        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/login.html");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer);
    }
}