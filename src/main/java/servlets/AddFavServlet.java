package servlets;

import hotelapp.DatabaseHandler;
import hotelapp.utils.UserManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/** A servlet that handles hotel favoring requests.
 */
public class AddFavServlet extends HttpServlet {

    /**
     * This method is called when a POST request is sent to the /addFav URL.
     * This method should handle the favoring request and display the search page.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        if (!UserManager.isLoggedIn(request.getCookies())) {
            response.sendRedirect("/login");
            return;
        }
        String username = UserManager.getUsername(request.getCookies());
        String hotelId = request.getParameter("hotelId");
        dbHandler.insertFavHotel(username, hotelId);
        response.sendRedirect("/hotels?hotelId=" + hotelId);
    }

    /**
     * This method is called when a POST request is sent to the /addfav URL.
     * This method should handle the favoring request and display the hotel page.
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

        String action = request.getParameter("action");
        if (action != null) {
            dbHandler.clearFavHotelHistory(username);
            response.sendRedirect("/myprofile");
        }

    }
}
