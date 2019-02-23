package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.Recipe;
import utilities.Restaurant;

/**
 * Servlet to retrieve restaurant by ID from the restaurantResults in the session
 * After finding the restaurant, send it to restaurant_info page
 * If it can't be found, or the session attribute is missing, return to home page
 */
@WebServlet("/RestaurantInfo")
public class RestaurantInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public RestaurantInfoServlet() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response)
							throws ServletException, IOException {
		String param = request.getParameter("id");
		RequestDispatcher dispatch;
		boolean noSession = false;
		short scanCount = 0;
		
		if (param != null) {
			while (request.getAttribute("restaurant") == null && scanCount < 5) {
				HttpSession session = request.getSession();
				Object res = session.getAttribute("restaurantResults");
				
				if (res != null) {
					long restaurantID = Long.parseLong(param);
					@SuppressWarnings("unchecked")
					ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>) res;
					for (Restaurant r : restaurants) {
						if (r.getID() == restaurantID) {
							request.setAttribute("restaurant", r);
							break;
						}
					}
				}
				else {
					noSession = true;
					break;
				}
				scanCount++;
			}
		}
		if (param == null || noSession || scanCount >= 5) {
			//Info missing, forward to search page
			dispatch = getServletContext().getRequestDispatcher("/search_page.html");
		}
		else {
			//Forward to recipe info page
			dispatch = getServletContext().getRequestDispatcher("/restaurant_info.jsp");	
		}
		dispatch.forward(request, response);
	}
}