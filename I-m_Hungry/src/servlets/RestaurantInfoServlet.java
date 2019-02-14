package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RestaurantInfoServlet
 */
@WebServlet("/RestaurantInfo")
public class RestaurantInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public RestaurantInfoServlet() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response)
							throws ServletException, IOException {
		long restaurantID = Long.parseLong(request.getParameter("ID_parameter"));
		System.out.println(restaurantID);
	}
}