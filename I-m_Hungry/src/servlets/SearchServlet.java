package servlets;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import threads.CollageThread;
import threads.RecipeSearchThread;
import threads.RestaurantSearchThread;
import utilities.ListContainer;

/**
 * INPUT: User search query and desired number of results
 * FUNCTION:
 * 	   If returning to results from info page,
 * 		   forward results from session. Otherwise:
 *     Build a String of parameters from the query
 *     Use Executor service to run three threads:
 *         CollageThread
 *         RestaurantSearchThread
 *         RecipeSearchThread
 *     Each thread calls an API, parses the response, and sets request attribute
 *     Once all threads are finished, forward to results.jsp
 */
@WebServlet("/Search")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SearchServlet() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response)
						throws ServletException, IOException {
		//Retrieve the session and user saved lists, create them if they don't exist
		HttpSession session = request.getSession();
		ListContainer userListContainer = (ListContainer)session.getAttribute("userListContainer");
		if (userListContainer == null) {
			userListContainer = new ListContainer();
			session.setAttribute("userListContainer", userListContainer);
		}
		//If going back to results page from an info page, retrieve results from session
		if (request.getParameter("back") != null) {
			request.setAttribute("query", session.getAttribute("query"));
			request.setAttribute("restaurantResults", session.getAttribute("restaurantResults"));
			request.setAttribute("recipeResults", session.getAttribute("recipeResults"));
			request.setAttribute("images", session.getAttribute("images"));
		}
		else {
			//Get parameter inputs
			String query = request.getParameter("q");
			int numResults = Integer.parseInt(request.getParameter("num"));
			
			//TODO remove this / handle input better
			if (numResults > 15) {
				return;
			}
			
			request.setAttribute("query", query);
			session.setAttribute("query", query);
			
			//split query into an array of words
			String[] queryArray = (query).split("[ \t&?+_\\/-]");
			
			//Concatenate search query terms with '+'
			String parameters = queryArray[0];
			for (int i = 1; i < queryArray.length; i++) {
				parameters += "+" + queryArray[i];
			}
			
			//Pass user input and lists to each thread to make the API calls and set request attributes
			ExecutorService es = Executors.newCachedThreadPool();
			es.execute(new CollageThread(request, parameters));
			es.execute(new RestaurantSearchThread(request, parameters, numResults, userListContainer));
			es.execute(new RecipeSearchThread(request, parameters, numResults, userListContainer));
			es.shutdown();
			try {
				//Wait for all threads to finish
				boolean finished = es.awaitTermination(20, TimeUnit.SECONDS);
			} catch (InterruptedException ie) {}	
		}
		//Forward to results.jsp
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/results.jsp");
		dispatch.forward(request, response);
	}
}