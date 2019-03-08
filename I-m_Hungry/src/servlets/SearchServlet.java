package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.ListIterator;

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
 *     Once all threads are finished, sort/filter results and forward to results.jsp
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
		RequestDispatcher dispatch;
		boolean error = false;
		
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
			String num = request.getParameter("num");

			if (query != null && num != null) {
				int numResults = Integer.parseInt(num);
				if(query.length() == 0 || numResults < 1) {
					//Invalid inputs (most input validation occurring in html)
					error = true;
				}
				else {
					request.setAttribute("query", query);
					session.setAttribute("query", query);
					
					String parameters = splitConcatParams(query);
					
					//Pass user input and lists to each thread to make the API calls and set request attributes
					ExecutorService es = Executors.newCachedThreadPool();
					es.execute(new CollageThread(request, parameters));
					es.execute(new RestaurantSearchThread(request, parameters, numResults, userListContainer));
					es.execute(new RecipeSearchThread(request, parameters, numResults, userListContainer));
					es.shutdown();
					try {
						//Wait for all threads to finish
						boolean finished = es.awaitTermination(20, TimeUnit.SECONDS);
						if (!finished) {
							//API calls timed out
							error = true;
						}
					} catch (InterruptedException ie) {}	
				}	
			}
			else {
				//Missing q or num parameters
				error = true;
			}
		}
		if (!error) {
			//Get the the list of results and result IDs from the user lists that affect search results
			final ArrayList<Long> favoriteRecipeIDs = userListContainer.getFavorites().getRecipeIDs();
			ArrayList<Long> doNotShowRecipeIDs = userListContainer.getNoShow().getRecipeIDs();
			@SuppressWarnings("unchecked")
			ArrayList<Recipe> recipes = (ArrayList<Recipe>) request.getAttribute("recipeResults");
			
			final ArrayList<Long> favoriteRestaurantIDs = userListContainer.getFavorites().getRestaurantIDs();
			ArrayList<Long> doNotShowRestaurantIDs = userListContainer.getNoShow().getRestaurantIDs();
			@SuppressWarnings("unchecked")
			ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>) request.getAttribute("restaurantResults");
			
			sortRecipes(recipes, favoriteRecipeIDs, doNotShowRecipeIDs);
			sortRestaurants(restaurants, favoriteRestaurantIDs, doNotShowRestaurantIDs);
			
			request.setAttribute("restaurantResults", restaurants);
			session.setAttribute("restaurantResults", restaurants);
			request.setAttribute("recipeResults", recipes);
			session.setAttribute("recipeResults", recipes);
			//Forward to results.jsp
			dispatch = request.getRequestDispatcher("/results.jsp");
		}
		else {
			//An error occurred somewhere, go back to search page
			dispatch = request.getRequestDispatcher("/search.jsp");
		}
		if(dispatch != null) {
			dispatch.forward(request, response);	
		}
	}
	public String splitConcatParams(String query) {
		//split query into an array of words
		String[] queryArray = (query).split("[ \t&?+_\\/-]");
		
		//Concatenate search query terms with '+'
		String parameters = queryArray[0];
		for (int i = 1; i < queryArray.length; i++) {
			parameters += "+" + queryArray[i];
		}
		return parameters;
	}
	public void sortRestaurants(ArrayList<Restaurant> rests,
			final ArrayList<Long> favoriteIDs, ArrayList<Long> doNotShowIDs) {
		
		//Sort restaurants using custom comparator
		//Favorites go to the beginning, otherwise sort by driving time
		Collections.sort(rests, new Comparator<Restaurant>() {
			public int compare(Restaurant lhs, Restaurant rhs) {
				if (favoriteIDs.contains(lhs.getID())) {
					return -1;
				}
				else if (favoriteIDs.contains(rhs.getID())) {
					return 1;
				}
				int diff = lhs.getMinutesFromTT() - rhs.getMinutesFromTT();
				if (diff == 0) {
					return 0;
				}
				return diff > 0 ? 1 : -1;
			}
		});
		//Remove do not show restaurants
		ListIterator<Restaurant> it_restaurant = rests.listIterator();
		while (it_restaurant.hasNext()) {
			if (doNotShowIDs.contains(it_restaurant.next().getID())) {
				it_restaurant.remove();
			}
		}
	}
	public void sortRecipes(ArrayList<Recipe> recs,
			final ArrayList<Long> favoriteIDs, ArrayList<Long> doNotShowIDs) {
		
		//Sort recipes using custom comparator
		//Favorites go to the beginning, otherwise sort by prep time
		//Recipes with no prep time are compared using their total time
		Collections.sort(recs, new Comparator<Recipe>() {
			public int compare(Recipe lhs, Recipe rhs) {
				if (favoriteIDs.contains(lhs.getID())) {
					return -1;
				}
				else if (favoriteIDs.contains(rhs.getID())) {
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
		ListIterator<Recipe> it_recipe = recs.listIterator();
		while (it_recipe.hasNext()) {
			if (doNotShowIDs.contains(it_recipe.next().getID())) {
				it_recipe.remove();
			}
		}
	}
}