package hotelapp;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;

public class HotelServer {

	public static final int PORT = 8090;

	public static void main(String[] args) throws Exception {
//		Server server = new Server(PORT);
		// FILL IN CODE, and add more classes as needed
//		DatabaseHandler dhandler = DatabaseHandler.getInstance();
//		dhandler.createTable();

		Server server = new Server(PORT);
		ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		handler.addServlet(RegistrationServlet.class, "/register");
		handler.addServlet(LoginServlet.class, "/login");
		handler.addServlet(HotelServlet.class, "/hotels");
		handler.addServlet(HomeServlet.class, "/home");
		handler.addServlet(ClearCookieServlet.class, "/clear");
		handler.addServlet(SearchServlet.class, "/search");
//		handler.addServlet(HotelDataServlet.class, "/hoteldata");



		VelocityEngine velocity = new VelocityEngine();
		velocity.init();

		handler.setAttribute("templateEngine", velocity);

		server.setHandler(handler);
		server.start();
		server.join();
	}
}