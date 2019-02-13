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

import threads.CollageThread;
import threads.RecipeSearchThread;
import threads.RestaurantSearchThread;

/**
 * INPUT: User search query and desired number of results
 * FUNCTION:
 *     Build a String of parameters from the query
 *     Use Executor service to run three threads:
 *         CollageThread
 *         RestaurantSearchThread
 *         RecipeSearchThread
 *     Each thread calls an API, parses the response, and sets request attribute
 *     Once all threads are finished, forward to results.jsp
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SearchServlet() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response)
						throws ServletException, IOException {
		//For testing purposes TODO remove
		request.setAttribute("query", "donald trump");
		request.setAttribute("numResults", 4);
		
		//Get user input and split query into an array of words
		String[] queryArray = ((String)request.getAttribute("query")).split("[ \t&?+_\\/-]");
		int numResults = (int)request.getAttribute("numResults");
		
		//Concatenate search query terms with '+'
		String parameters = queryArray[0];
		for (int i = 1; i < queryArray.length; i++) {
			parameters += "+" + queryArray[i];
		}
		
		//Pass user input to each thread to make the API calls and set request attributes
		ExecutorService es = Executors.newCachedThreadPool();
		es.execute(new CollageThread(request, parameters));
		es.execute(new RestaurantSearchThread(request, parameters, numResults));
		es.execute(new RecipeSearchThread(request, parameters, numResults));
		es.shutdown();
		try {
			//Wait for all threads to finish
			boolean finished = es.awaitTermination(20, TimeUnit.SECONDS);
		} catch (InterruptedException ie) {}
		
		//Forward to results.jsp
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/results.jsp");
		dispatch.forward(request, response);
	}
}