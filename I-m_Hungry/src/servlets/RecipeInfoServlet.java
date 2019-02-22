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

/**
 * Servlet implementation class RecipeInfoServlet
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
		HttpSession session = request.getSession();
		Object rec = session.getAttribute("recipeResults");
		RequestDispatcher dispatch;
		
		if (param != null && rec != null) {
			long recipeID = Long.parseLong(param);
			@SuppressWarnings("unchecked")
			ArrayList<Recipe> recipes = (ArrayList<Recipe>) rec;
			for (Recipe r : recipes) {
				if (r.getID() == recipeID) {
					request.setAttribute("recipe", r);
					break;
				}
			}
			//Forward to recipe info page
			dispatch = getServletContext().getRequestDispatcher("/recipe_info.jsp");
		}
		else {
			//Info missing, forward to search page
			dispatch = getServletContext().getRequestDispatcher("/search_page.html");
		}
		dispatch.forward(request, response);
	}
}