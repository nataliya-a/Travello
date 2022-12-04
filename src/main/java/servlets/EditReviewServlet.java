package servlets;

import hotelapp.DatabaseHandler;
import hotelapp.Review;
import hotelapp.ThreadSafeReviewMapper;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/** Servlet that handles the edit and delete review page.
 */
public class EditReviewServlet extends HttpServlet {

    /**
     * doGet method for EditReviewServlet.
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
        String hotelId = request.getParameter("hotelId");
        System.out.println("hotelId: " + hotelId);
        String reviewId = request.getParameter("reviewId");
        System.out.println("reviewId: " + reviewId);
        String title = request.getParameter("title");
        String reviewText = request.getParameter("reviewText");
        List<Map<String, String>> reviews = dbHandler.getHotelReviews(hotelId);


        if (!UserManager.isLoggedIn(request.getCookies())) {
            response.sendRedirect("/login");
            return;
        }
        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();

        context.put("username", UserManager.getUsername(request.getCookies()));

        String delete = request.getParameter("delete");

         if (delete != null) {
             System.out.println("Delete review");
             dbHandler.deleteReview(reviewId);
             response.sendRedirect("/hotels?hotelId=" + hotelId);
        }

        Template template = ve.getTemplate("templates/editReview.html");

        context.put("hotelId", hotelId);
        context.put("reviewId", reviewId);
        context.put("title", title);
        context.put("reviewText", reviewText);
        context.put("reviews", reviews);


        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer);
    }

    /**
     * Handles POST requests to edit a review.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        Double rating = 0.0;
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        String hotelId = request.getParameter("hotelId");
        String username = UserManager.getUsername(request.getCookies());
        String title = request.getParameter("title");
        String reviewText = request.getParameter("reviewText");

        dbHandler.updateReview(title, reviewText, String.valueOf(rating), hotelId, username);


        response.sendRedirect("/hotels?hotelId=" + hotelId);
    }

}
