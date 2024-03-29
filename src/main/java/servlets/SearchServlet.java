package servlets;

import hotelapp.DatabaseHandler;
import hotelapp.Hotel;
import hotelapp.ThreadSafeHotelData;
import hotelapp.utils.UserManager;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** Servlet that handles the search page.
 */
public class SearchServlet extends HttpServlet {

    /**
     * This method is called when a GET request is sent to the /search URL.
     * This method should display the search page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        if (!UserManager.isLoggedIn(request.getCookies())) {
            System.out.println("User not logged in");
            response.sendRedirect("/login");
            return;
        }

        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        String username = UserManager.getUsername(request.getCookies());
        context.put("username", username);
        Template template = ve.getTemplate("templates/search.html");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer);
    }


    /**
     * This method is called when a POST request is sent to the /search URL.
     * This method should handle the search request and display the search results.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        DatabaseHandler db = DatabaseHandler.getInstance();
        PrintWriter out = response.getWriter();
        String search = request.getParameter("search");
        String logout = request.getParameter("logout");
        String username = request.getParameter("username");
        if (logout != null) {
            response.sendRedirect("/logout");
        }

        search = StringEscapeUtils.escapeHtml4(search);

        Map<String, String> hotelsWithID = db.getHotelNames(search);


        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/getHotels.html");
        context.put("search", search);
        context.put("hotels", hotelsWithID);
        context.put("username", username);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        out.println(writer);
    }

    /**
     * This method gets the list of hotels that match the search query.
     *
     */
    private static List<Hotel> getHotels(String search, HttpServletRequest request) {

        ThreadSafeHotelData hotelData = (ThreadSafeHotelData) request.getServletContext().getAttribute("hotelData");

        List<Hotel> lhotel= hotelData.getHotels();
        System.out.println("lhotel size:"+lhotel.size());

        if (search == null || search.isEmpty()) {
            return lhotel;
        }

        List<Hotel> hotelList = new ArrayList<>();
        Pattern p = Pattern.compile(search, Pattern.CASE_INSENSITIVE);

        for (Hotel hotel: lhotel) {

            String name = hotel.getHotelName();
            Matcher m = p.matcher(name);
            boolean matchFound = m.find();
            if (matchFound) {
                hotelList.add(hotel);
            }
        }

        return hotelList;
    }

}
