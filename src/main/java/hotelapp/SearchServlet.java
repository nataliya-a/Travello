package hotelapp;

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
        String username = request.getParameter("username");
        PrintWriter out = response.getWriter();
        if (!UserManager.isLoggedIn(request.getCookies())) {
            out.println("<h1>You are not logged in!</h1>");
            return;
        }

        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
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
        PrintWriter out = response.getWriter();
        String search = request.getParameter("search");
        String logout = request.getParameter("logout");
        String username = request.getParameter("username");
        System.out.println("(Search)username:" + username);
        if (logout != null) {
            response.sendRedirect("/logout");
        }

        search = StringEscapeUtils.escapeHtml4(search);

        List<Hotel> hotels = getHotels(search,request);
        List<String> hotelNames = new ArrayList<>();
        for (Hotel hotel : hotels) {
            hotelNames.add(hotel.getHotelName());
        }
        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/getHotels.html");
        context.put("search", search);
        context.put("hotels", hotels);
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
