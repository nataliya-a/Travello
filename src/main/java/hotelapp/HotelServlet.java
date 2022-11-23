package hotelapp;

import hotelapp.utils.UserManager;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
        public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            Double rating = 0.0;
            String hotelId = request.getParameter("hotelId");

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();

            if (!UserManager.isLoggedIn(request.getCookies())) {
                out.println("<h1>You are not logged in!</h1>");
                response.sendRedirect("/login");
                return;
            }
            String username = UserManager.getUsername(request.getCookies());

            ThreadSafeHotelData hotelData = (ThreadSafeHotelData) request.getServletContext().getAttribute("hotelData");
            Hotel hotel = hotelData.getHotelObjectNew(hotelId);


            ThreadSafeReviewMapper reviewMapper = (ThreadSafeReviewMapper) request.getServletContext().getAttribute("reviewMapper");
            TreeSet<Review> reviews = reviewMapper.getReviewObject(hotelId);


            if (reviews != null) {
                for (Review review : reviews) {
                    if (review.getRatingOverall() == "") {
                        rating += 0.0;
                    } else {
                        rating += Double.parseDouble(review.getRatingOverall());
                    }
                }
                rating = rating / reviews.size();
            }

            String city = hotel.getCity().replace(" ", "-");
            String hotelName = hotel.getHotelName().replace(" ", "-");
            String expediaLink = "https://www.expedia.com/" + city +
                    "-Hotels-" + hotelName + ".h" + hotelId + ".Hotel-Information";


            VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
            VelocityContext context = new VelocityContext();
            Template template = ve.getTemplate("templates/hotel.html");
            context.put("hotel", hotel);
            context.put("reviews", reviews);
            context.put("rating", rating);
            context.put("expediaLink", expediaLink);
            context.put("username", username);
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            out.println(writer);
        }

        /**
         * Handles POST requests to add a review.
         */
        @Override
        public void doPost (HttpServletRequest request, HttpServletResponse response) throws  IOException {
            System.out.println("hotel servlet post");
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            String hotelId = request.getParameter("hotelId");
            String title = request.getParameter("title");
            String reviewText = request.getParameter("review");
            String username = UserManager.getUsername(request.getCookies());

            if (title != null && reviewText != null) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
                String date = dtf.format(LocalDate.now());
                date = date.replace("/", "-");
                date = date + "T00:00:00Z";
                Review newReview = new Review(hotelId, reviewText,  username, title, UUID.randomUUID().toString(), "", date);
                System.out.println(newReview);
                ThreadSafeReviewMapper reviewMapper = (ThreadSafeReviewMapper) request.getServletContext().getAttribute("reviewMapper");
                TreeSet<Review> reviews = reviewMapper.getReviewObject(hotelId);
                if (reviews == null) {
                    List<Review> reviewList = new ArrayList<>();
                    reviewList.add(newReview);
                    reviewMapper.addReviews(reviewList);
                } else {
                    reviewMapper.addNewReview(hotelId,newReview);
                }
            }
            response.sendRedirect("/hotels?hotelId=" + hotelId);
        }
}
