package servlets;

import hotelapp.ThreadSafeHotelData;
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

public class BookingServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        Template template = ve.getTemplate("templates/booking.html");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        String username = UserManager.getUsername(request.getCookies());
        String hotelId = request.getParameter("hotelId");
        String checkIn = request.getParameter("checkIn");
        String checkOut = request.getParameter("checkOut");

        LocalDate checkInDate = LocalDate.parse(checkIn);
        LocalDate checkOutDate = LocalDate.parse(checkOut);

        ThreadSafeHotelData hotelData = (ThreadSafeHotelData) request.getServletContext().getAttribute("hotelData");

        if (hotelData.canBook(hotelId, checkInDate, checkOutDate)) {
            hotelData.book(hotelId, checkInDate, checkOutDate, username);
            response.sendRedirect("/hotels?hotelId=" + hotelId);
        } else {
            out.println("<h1>Sorry, the hotel is not available during this period.</h1>");
        }

    }

}
