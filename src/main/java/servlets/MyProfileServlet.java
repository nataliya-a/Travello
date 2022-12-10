package servlets;

import hotelapp.DatabaseHandler;
import hotelapp.Hotel;
import hotelapp.utils.UserManager;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class MyProfileServlet extends HttpServlet {

    /**
     * doGet method for MyProfileServlet.
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        if (!UserManager.isLoggedIn(request.getCookies())) {
            response.sendRedirect("/login");
            return;
        }
        String username = UserManager.getUsername(request.getCookies());

        List<String> links = dbHandler.getExpediaLinksForUser(username);
        List<Hotel> favHotels = dbHandler.getFavHotels(username);

        System.out.println(links);
        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        context.put("username", username);

        Template template = ve.getTemplate("templates/myprofile.html");

        context.put("links", links);
        context.put("favHotels", favHotels);
        context.put("lastTimeLoggedIn", UserManager.retrieveLastTimeLoginTime(request.getCookies(), username));

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        out.println(writer);
    }

    /**
     * doPost method for MyProfileServlet.
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        if (!UserManager.isLoggedIn(request.getCookies())) {
            response.sendRedirect("/login");
            return;
        }
        String username = UserManager.getUsername(request.getCookies());

        dbHandler.clearExpediaHistory(username);
        response.sendRedirect("/myprofile");
    }

}
