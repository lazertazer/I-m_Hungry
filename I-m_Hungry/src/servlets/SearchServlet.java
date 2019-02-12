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
 * Servlet implementation class SearchServlet
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
		request.setAttribute("query", "pasta/chicken");
		request.setAttribute("numResults", 8);
		
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
		boolean finished;
		try {
			finished = es.awaitTermination(20, TimeUnit.SECONDS);
		} catch (InterruptedException ie) {}
		
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/results.jsp");
		dispatch.forward(request,  response);
	}
}
