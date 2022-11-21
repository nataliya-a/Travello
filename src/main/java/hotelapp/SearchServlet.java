package hotelapp;

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
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/search.html");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        String search = request.getParameter("search");
        search = StringEscapeUtils.escapeHtml4(search);
        getHotels(search);
//        System.out.println(search);
        List<Hotel> hotels = getHotels(search);
        List<String> hotelNames = new ArrayList<>();
        for (Hotel hotel : hotels) {
            hotelNames.add(hotel.getHotelName());
        }
//        for (Hotel hotel : hotels) {
//            System.out.println(hotel);
//        }
        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/getHotels.html");
        context.put("search", search);
        context.put("hotels", hotelNames);

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        out.println(writer.toString());



    }

    private static List<Hotel> getHotels(String search) {

        HotelData hotelData = new HotelData();
        List<Hotel> lhotel= hotelData.trigerHotelData();

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
//                System.out.println(hotel);
            }
        }

        return hotelList;
    }

}
