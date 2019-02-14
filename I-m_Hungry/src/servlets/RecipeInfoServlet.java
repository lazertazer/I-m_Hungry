package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		long recipeID = Long.parseLong(request.getParameter("ID_parameter"));
	}
}