package hotelapp;

import hotelapp.utils.UserManager;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        String hotelId = request.getParameter("hotelId");
        String reviewId = request.getParameter("reviewId");
        String title = request.getParameter("title");
        String reviewText = request.getParameter("reviewText");
        if (!UserManager.isLoggedIn(request.getCookies())) {
            out.println("<h1>You are not logged in!</h1>");
            return;
        }

        String delete = request.getParameter("delete");
        ThreadSafeReviewMapper reviewMapper = (ThreadSafeReviewMapper) request.getServletContext().getAttribute("reviewMapper");

         if (delete != null) {
            reviewMapper.deleteReview(hotelId, reviewId);
            response.sendRedirect("/hotels?hotelId=" + hotelId);
        }
        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/editReview.html");

        context.put("hotelId", hotelId);
        context.put("reviewId", reviewId);
        context.put("title", title);
        context.put("reviewText", reviewText);


        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer);
    }

    /**
     * Handles POST requests to edit a review.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        String hotelId = request.getParameter("hotelId");
        String reviewId = request.getParameter("reviewId");
        String title = request.getParameter("title");
        String reviewText = request.getParameter("reviewText");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        String date = dtf.format(LocalDate.now());
        date = date.replace("/", "-");
        date = date + "T00:00:00Z";
        Review newReview = new Review(hotelId, reviewText, UserManager.getUsername(request.getCookies()), title, UUID.randomUUID().toString(), "", date);
        ThreadSafeReviewMapper reviewMapper = (ThreadSafeReviewMapper) request.getServletContext().getAttribute("reviewMapper");
        System.out.println(hotelId);
        System.out.println(reviewId);
        reviewMapper.deleteReview(hotelId, reviewId);
        reviewMapper.addNewReview(hotelId, newReview);
        response.sendRedirect("/hotels?hotelId=" + hotelId);
    }

}
