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

import utilities.ListContainer;
import utilities.Recipe;
import utilities.Restaurant;
import utilities.UserList;

/**
 *	TODO put description
 */
@WebServlet("/UserLists")
public class ListManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ListManagementServlet() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get user lists and search results from session
		HttpSession session = request.getSession();
		Object recipeAttr = session.getAttribute("recipeResults");
		Object restaurantAttr = session.getAttribute("restaurantResults");
		Object listContainerAttr = session.getAttribute("userListContainer");
		
		//listParam determines which list is being affected
		String listParam = request.getParameter("list");
		
		//operation determines what to do with the list
		String operation = request.getParameter("operation");
		
		//id specifies a recipe or result (depends on operation)
		String id = request.getParameter("id");
		
		UserList list = null;
		boolean listInit = false;
		boolean missingInfo = false;
		RequestDispatcher dispatch;
		
		if (recipeAttr != null && restaurantAttr != null && listContainerAttr != null
				&& listParam != null && operation != null) {
			
			//Cast session variables now that we know they aren't null
			ListContainer userListContainer = (ListContainer) listContainerAttr;
			@SuppressWarnings("unchecked")
			ArrayList<Recipe> recipes = (ArrayList<Recipe>) recipeAttr;
			@SuppressWarnings("unchecked")
			ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>) restaurantAttr;
			
			//Initialize list depending on listParam input
			if (listParam.equals("FAV")) {
				list = userListContainer.getFavorites();
				listInit = true;
			}
			else if (listParam.equals("DNS")) {
				list = userListContainer.getNoShow();
				listInit = true;
			}
			else if (listParam.equals("XPL")) {
				list = userListContainer.getExplore();
				listInit = true;
			}
			
			if (listInit) {
				//display is the only operation that doesn't use id parameter
				if (!operation.equals("display")) {
					if (id != null) {
						long itemID = Long.parseLong(id);
						
						if (operation.equals("addRecipe")) {
							Recipe r = getRecipeByID(recipes, itemID);
							if (r != null) {
								if (!list.containsRecipe(r)) {
									list.addRecipe(r);
								}
							}
							else {
								//results do not have desired recipe
								missingInfo = true;
							}
						}
						else if (operation.equals("addRestaurant")) {
							Restaurant r = getRestaurantByID(restaurants, itemID);
							if (r != null) {
								if (!list.containsRestaurant(r)) {
									list.addRestaurant(r);
								}
							}
							else {
								//results do not have desired restaurant
								missingInfo = true;
							}
						}
						else {
							//unknown operation
							missingInfo = true;
						}
					}
					else {
						//id is necessary but null
						missingInfo = true;
					}
				}
			}
			else {
				//list not initialized
				missingInfo = true;
			}
		}
		else {
			//required info was null
			missingInfo = true;
		}
		
		//Forward to list_management page if successful, else to search page
		if (!missingInfo) {
			request.setAttribute("currentList", list);
			dispatch = getServletContext().getRequestDispatcher("/list_management.jsp");
		}
		else {
			dispatch = getServletContext().getRequestDispatcher("/search_page.html");
		}
		dispatch.forward(request, response);
	}
	private Recipe getRecipeByID(ArrayList<Recipe> recipes, long recipeID) {
		for (Recipe r : recipes) {
			if (r.getID() == recipeID) {
				return r;
			}
		}
		return null;
	}
	private Restaurant getRestaurantByID(ArrayList<Restaurant> restaurants, long restaurantID) {
		for (Restaurant r : restaurants) {
			if (r.getID() == restaurantID) {
				return r;
			}
		}
		return null;
	}
}
