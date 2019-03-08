package main.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.utilities.Recipe;

/**
 * Servlet to retrieve recipe by ID from the recipeResults in the session
 * After finding the recipe, send it to recipe_info page
 * If it can't be found, or the session attribute is missing, return to home page
 */
@WebServlet("/RecipeInfo")
public class RecipeInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public RecipeInfoServlet() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response)
							throws ServletException, IOException {
		String param = request.getParameter("id");
		RequestDispatcher dispatch;
		boolean noSession = false;
		short scanCount = 0;
		
		if (param != null) {
			//Scan for the recipe in the session up to 3 times
			while (request.getAttribute("recipe") == null && scanCount < 3) {
				HttpSession session = request.getSession();
				Object rec = session.getAttribute("recipeResults");
				
				if (rec != null) {
					long recipeID = Long.parseLong(param);
					@SuppressWarnings("unchecked")
					ArrayList<Recipe> recipes = (ArrayList<Recipe>) rec;
					for (Recipe r : recipes) {
						if (r.getID() == recipeID) {
							request.setAttribute("recipe", r);
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

		if (param == null || noSession || scanCount >= 3) {
			//Info missing, forward to search page
			dispatch = request.getRequestDispatcher("/search.jsp");
		}
		else {
			//Forward to recipe info page
			dispatch = request.getRequestDispatcher("/recipe_info.jsp");
		}
		if(dispatch != null) {
			dispatch.forward(request, response);	
		}
	}
}