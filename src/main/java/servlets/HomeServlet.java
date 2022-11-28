package servlets;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/** Servlet that handles the home page.
 */
public class HomeServlet extends HttpServlet {

    /**
     * doGet method for HomeServlet.
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doGet (HttpServletRequest request, HttpServletResponse response ) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/home.html");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer);
    }

    /**
     * doPost method for HomeServlet.
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();


        String login = request.getParameter("login");
        String register = request.getParameter("register");
        if (login != null) {
            response.sendRedirect("/login");
        } else if (register != null) {
            response.sendRedirect("/register");
        }
    }

}
