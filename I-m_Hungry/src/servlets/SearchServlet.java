package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import utilities.Recipe;
import utilities.Restaurant;

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
 *     Once all threads are finished, sort results and forward to results.jsp
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
		String back = request.getParameter("back");
		if (back != null && back.equals("true")) {
			request.setAttribute("query", session.getAttribute("query"));
			request.setAttribute("restaurantResults", session.getAttribute("restaurantResults"));
			request.setAttribute("recipeResults", session.getAttribute("recipeResults"));
			request.setAttribute("images", session.getAttribute("images"));
		}
		else {
			//Get parameter inputs
			String query = request.getParameter("q");
			int numResults = Integer.parseInt(request.getParameter("num"));
			if(query.length() == 0 || numResults <= 1) {
				RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/search.jsp");
				dispatch.forward(request, response);
			}
			else {
				
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
		}
		//Get the the list of results and result IDs from the user lists that affect search results
		ArrayList<Long> favoriteRecipeIDs = userListContainer.getFavorites().getRecipeIDs();
		ArrayList<Long> doNotShowRecipeIDs = userListContainer.getNoShow().getRecipeIDs();
		@SuppressWarnings("unchecked")
		ArrayList<Recipe> recipes = (ArrayList<Recipe>) request.getAttribute("recipeResults");
		
		ArrayList<Long> favoriteRestaurantIDs = userListContainer.getFavorites().getRestaurantIDs();
		ArrayList<Long> doNotShowRestaurantIDs = userListContainer.getNoShow().getRestaurantIDs();
		@SuppressWarnings("unchecked")
		ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>) request.getAttribute("restaurantResults");
		
		//Sort recipes using custom comparator
		//Favorites go to the beginning, otherwise sort by prep time
		//Recipes with no prep time are compared using their total time
		Collections.sort(recipes, new Comparator<Recipe>() {
			@Override
			public int compare(Recipe lhs, Recipe rhs) {
				if (favoriteRecipeIDs.contains(lhs.getID())) {
					return -1;
				}
				else if (favoriteRecipeIDs.contains(rhs.getID())) {
					return 1;
				}
				
				int leftTime = lhs.getPrepMinutes();
				leftTime = leftTime != 0 ? leftTime : lhs.getTotalMinutes();

				int rightTime = rhs.getPrepMinutes();
				rightTime = rightTime != 0 ? rightTime : rhs.getTotalMinutes();
				
				return leftTime < rightTime ? -1 : 1;
			}
		});
		
		//Remove do not show recipes
		//TODO concurrent modification exception??
		for (Recipe r : recipes) {
			if (doNotShowRecipeIDs.contains(r.getID())) {
				recipes.remove(r);
			}
		}
		
		//Prioritize favorite restaurants and remove do not shows
		for (Restaurant r : restaurants) {
			if (favoriteRestaurantIDs.contains(r.getID())) {
				restaurants.remove(r);
				restaurants.add(0, r);
			}
			if (doNotShowRestaurantIDs.contains(r.getID())) {
				restaurants.remove(r);
			}
		}
		//Forward to results.jsp
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/results.jsp");
		dispatch.forward(request, response);
	}
}