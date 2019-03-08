package utilities;

import java.util.ArrayList;
import java.util.ListIterator;

public class UserList {
	private String listName;
	private String listAcronym;
	private ArrayList<Restaurant> restaurants;
	private ArrayList<Recipe> recipes;
	
	public UserList(String listName, String listAcronym) {
		this.listName = listName;
		this.listAcronym = listAcronym;
		this.restaurants = new ArrayList<Restaurant>();
		this.recipes = new ArrayList<Recipe>();
	}
	public String getListName() {
		return listName;
	}
	public String getListAcronym() {
		return listAcronym;
	}
	public ArrayList<Restaurant> getRestaurants() {
		return restaurants;
	}
	public Restaurant getRestaurant(int index) {
		return restaurants.get(index);
	}
	public ArrayList<Long> getRestaurantIDs() {
		ArrayList<Long> restaurantIDs = new ArrayList<Long>();
		for (Restaurant r : restaurants) {
			restaurantIDs.add(r.getID());
		}
		return restaurantIDs;
	}
	public ArrayList<Recipe> getRecipes() {
		return recipes;
	}
	public Recipe getRecipe(int index) {
		return recipes.get(index);
	}
	public ArrayList<Long> getRecipeIDs() {
		ArrayList<Long> recipeIDs = new ArrayList<Long>();
		for (Recipe r : recipes) {
			recipeIDs.add(r.getID());
		}
		return recipeIDs;
	}
	public ArrayList<Item> getAllItems() {
		ArrayList<Item> items = new ArrayList<Item>();
		
		//Add recipes and restaurants alternating until smaller list is exhausted
		int alternateLen = Math.min(recipes.size(), restaurants.size());
		for (int i = 0; i < alternateLen; i++) {
			items.add(recipes.get(i));
			items.add(restaurants.get(i));
		}
		//Add the rest of items to the end if one list is bigger than the other
		int maxLen = Math.max(recipes.size(), restaurants.size());
		boolean recipeOrRestaurant = recipes.size() > restaurants.size();
		for (int i = alternateLen; i < maxLen; i++) {
			if (recipeOrRestaurant) {
				items.add(recipes.get(i));
			}
			else {
				items.add(restaurants.get(i));
			}
		}
		return items;
	}
	public void addRestaurant(Restaurant r) {
		if (!containsRestaurant(r)) {
			restaurants.add(r);
		}
	}
	public void addRecipe(Recipe r) {
		if (!containsRecipe(r)) {
			recipes.add(r);	
		}
	}
	public void removeRestaurantByID(long ID) {
		ListIterator<Restaurant> it = restaurants.listIterator();
		while (it.hasNext()) {
			if (it.next().getID() == ID) {
				it.remove();
			}
		}
	}
	public void removeRecipeByID(long ID) {
		ListIterator<Recipe> it = recipes.listIterator();
		while (it.hasNext()) {
			if (it.next().getID() == ID) {
				it.remove();
			}
		}
	}
	public boolean containsRestaurant(Restaurant restaurant) {
		for (Restaurant r : restaurants) {
			if (r.getID() == restaurant.getID()) {
				return true;
			}
		}
		return false;
	}
	public boolean containsRecipe(Recipe recipe) {
		for (Recipe r : recipes) {
			if (r.getID() == recipe.getID()) {
				return true;
			}
		}
		return false;
	}
}