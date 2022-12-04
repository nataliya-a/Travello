package hotelapp;

import servlets.*;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class HotelServer {

	public static final int PORT = 8090;

	public static void main(String[] args) throws Exception {

		ParseArgs.parseArgs(args);

		Server server = new Server(PORT);
		ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);

		handler.addServlet(RegistrationServlet.class, "/register");
		handler.addServlet(LoginServlet.class, "/login");
		handler.addServlet(HotelServlet.class, "/hotels");
		handler.addServlet(HomeServlet.class, "/home");
		handler.addServlet(LogoutServlet.class, "/logout");
		handler.addServlet(SearchServlet.class, "/search");
		handler.addServlet(EditReviewServlet.class, "/edit");
		handler.addServlet(BookingServlet.class, "/booking");
		handler.addServlet(MyProfileServlet.class, "/myprofile");
		handler.addServlet(AddFavServlet.class, "/addFav");




		VelocityEngine velocity = new VelocityEngine();
		velocity.init();

		handler.setAttribute("templateEngine", velocity);
		handler.setAttribute("hotelData", ParseArgs.getHotelData());
		handler.setAttribute("reviewMapper", ParseArgs.getReviewMapper());

		server.setHandler(handler);
		server.start();
		server.join();
	}
}