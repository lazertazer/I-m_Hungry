package utilities;

import java.util.ArrayList;

public class UserList {
	private String listName;
	private ArrayList<Restaurant> restaurants;
	private ArrayList<Recipe> recipes;
	
	public UserList(String listName) {
		this.listName = listName;
		this.restaurants = new ArrayList<Restaurant>();
		this.recipes = new ArrayList<Recipe>();
	}
	public String getListName() {
		return listName;
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
	public void addRestaurant(Restaurant r) {
		restaurants.add(r);
	}
	public void addRecipe(Recipe r) {
		recipes.add(r);
	}
	public boolean containsRecipe(Recipe recipe) {
		for (Recipe r : recipes) {
			if (r.getID() == recipe.getID()) {
				return true;
			}
		}
		return false;
	}
	public boolean containsRestaurant(Restaurant restaurant) {
		for (Restaurant r : restaurants) {
			if (r.getID() == restaurant.getID()) {
				return true;
			}
		}
		return false;
	}
}
