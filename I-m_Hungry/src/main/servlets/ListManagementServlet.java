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

import main.utilities.ListContainer;
import main.utilities.Recipe;
import main.utilities.Restaurant;
import main.utilities.UserList;

/**
 *	INPUT parameters:
 *		Destination/target list, operation,
 *		recipe/restaurant ID (unless operation is display),
 *		source list (only if operation is to move)
 *	FUNCTION:
 *		add/remove/move a recipe/restaurant from a UserList
 *		OR simply display a list
 *		If there is anything missing from parameters or session,
 *			forward to search.jsp
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
		
		//dstlist only used when moving an item between lists
		String dstListParam = request.getParameter("dstlist");
		
		UserList list = null;
		UserList dstList = null;
		boolean missingInfo = false;
		RequestDispatcher dispatch = null;
		
		if (recipeAttr != null && restaurantAttr != null && listContainerAttr != null
				&& listParam != null && operation != null) {
			
			//Cast session variables now that we know they aren't null
			ListContainer userListContainer = (ListContainer) listContainerAttr;
			@SuppressWarnings("unchecked")
			ArrayList<Recipe> recipes = (ArrayList<Recipe>) recipeAttr;
			@SuppressWarnings("unchecked")
			ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>) restaurantAttr;
			
			//Initialize target list depending on listParam input
			if (listParam.equals("FAV")) {
				list = userListContainer.getFavorites();
			}
			else if (listParam.equals("DNS")) {
				list = userListContainer.getNoShow();
			}
			else if (listParam.equals("XPL")) {
				list = userListContainer.getExplore();
			}
			
			//If performing a movement operation, initialize the source list
			if (operation.equals("moveRecipe") || operation.equals("moveRestaurant")) {
				if (dstListParam != null) {
					if (dstListParam.equals("FAV")) {
						dstList = userListContainer.getFavorites();
					}
					else if (dstListParam.equals("DNS")) {
						dstList = userListContainer.getNoShow();
					}
					else if (dstListParam.equals("XPL")) {
						dstList = userListContainer.getExplore();
					}
					else {
						//unrecognized destination list parameter
						missingInfo = true;
					}
				}
				else {
					//missing destination list parameter when required
					missingInfo = true;
				}
			}
			
			//Check if lists properly initialized
			if (list != null && !missingInfo) {
				if (!operation.equals("display")) {
					//ID is required for every operation except display
					if (id != null) {
						long itemID = Long.parseLong(id);
						
						//Modify list depending on operation
						if (operation.equals("addRecipe")) {
							Recipe r = getRecipeByID(recipes, itemID);
							if (r != null) {
								list.addRecipe(r);
							}
							else {
								//results do not have desired recipe
								missingInfo = true;
							}
						}
						else if (operation.equals("addRestaurant")) {
							Restaurant r = getRestaurantByID(restaurants, itemID);
							if (r != null) {
								list.addRestaurant(r);
							}
							else {
								//results do not have desired restaurant
								missingInfo = true;
							}
						}
						else if (operation.equals("removeRecipe")) {
							list.removeRecipeByID(itemID);
						}
						else if (operation.equals("removeRestaurant")) {
							list.removeRestaurantByID(itemID);
						}
						else if (operation.equals("moveRecipe")) {
							Recipe r = getRecipeByID(recipes, itemID);
							if (r != null) {
								list.removeRecipeByID(itemID);
								dstList.addRecipe(r);
							}
							else {
								missingInfo = true;
							}
						}
						else if (operation.equals("moveRestaurant")) {
							Restaurant r = getRestaurantByID(restaurants, itemID);
							if (r != null) {
								list.removeRestaurantByID(itemID);
								dstList.addRestaurant(r);
							}
							else {
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
			dispatch = request.getRequestDispatcher("/list_management.jsp");
		}
		else {
			dispatch = request.getRequestDispatcher("/search.jsp");
		}
		if(dispatch != null) {
			dispatch.forward(request, response);	
		}
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