package servlets;

import hotelapp.*;
import hotelapp.utils.UserManager;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


/** Servlet that handles hotel page.
 */
public class HotelServlet extends HttpServlet {

        /**
         * doGet method for HotelServlet.
         * @param request
         * @param response
         * @throws IOException
         */
        @Override
        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            DatabaseHandler dbHandler = DatabaseHandler.getInstance();
            Double rating = 0.0;
            String hotelId = request.getParameter("hotelId");
            System.out.println("hotelId: " + hotelId);

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();

            if (!UserManager.isLoggedIn(request.getCookies())) {
                response.sendRedirect("/login");
                return;
            }
            String username = UserManager.getUsername(request.getCookies());

            Map<String, String> hotelDetails= dbHandler.getHotelDetails(hotelId);

            ThreadSafeHotelData hotelData = (ThreadSafeHotelData) request.getServletContext().getAttribute("hotelData");
            ArrayList<LocalDate> myBookedDates = hotelData.getUserBookedDates(hotelId, username);

            List<Map<String, String>> reviews = dbHandler.getHotelReviews(hotelId);

            if (reviews != null) {
                for (Map<String, String> review : reviews) {
                    if (review.get("reviewRating") == "") {
                        rating += 0.0;
                    } else {
                        rating += Double.parseDouble(review.get("reviewRating"));
                    }
                }
                rating = rating / reviews.size();
            }

            String city = hotelDetails.get("city").replace(" ", "-");

            String hotelName = hotelDetails.get("hotelName").replace(" ", "-");
            String expediaLink = "https://www.expedia.com/" + city +
                    "-Hotels-" + hotelName + ".h" + hotelId + ".Hotel-Information";


            VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
            VelocityContext context = new VelocityContext();
            Template template = ve.getTemplate("templates/hotel.html");
            context.put("hotel", hotelDetails);
            context.put("reviews", reviews);
            context.put("isFavoredHotel", dbHandler.favHotelExistsForUser(username, hotelId));
            System.out.println(reviews);
            Formatter formatter = new Formatter();
            formatter.format("%.2f", rating);
            context.put("rating", formatter.toString());
            context.put("expediaLink", expediaLink);
            context.put("username", username);
            context.put("myBookedDates", myBookedDates);
            context.put("currentPage", request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page")));
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            out.println(writer);
        }

        /**
         * Handles POST requests to add a review.
         */
        @Override
        public void doPost (HttpServletRequest request, HttpServletResponse response) throws  IOException {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);

            DatabaseHandler dbHandler = DatabaseHandler.getInstance();
            String username = UserManager.getUsername(request.getCookies());

            String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
            System.out.println(body);
            String[] parameters = body.split("&");
            Map<String, String> params = new HashMap<>();
            for (String param : parameters) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }

            if (params.get("link") != null) {
                dbHandler.insertExpediaTable(username, params.get("link"));
            }


            String ratingString = "0";
            System.out.println("hotel servlet post");

            String hotelId = params.get("hotelId");
            String title =params.get("title");
            String reviewText = params.get("review");
            ratingString = params.get("rating");


            if (title != null && reviewText != null) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
                String date = dtf.format(LocalDate.now());
                date = date.replace("/", "-");
                dbHandler.insertReview(hotelId, UUID.randomUUID().toString(), title, reviewText, username, ratingString, date);
            }


            response.sendRedirect("/hotels?hotelId=" + hotelId);

        }

}
