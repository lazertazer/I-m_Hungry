package utilities;

import java.util.ArrayList;

public class RecipeIngredients {
	private int numIngredients;
	private ArrayList<Ingredient> ingredients;
	public RecipeIngredients(int numIngredients, ArrayList<Ingredient> ingredients) {
		this.numIngredients = numIngredients;
		this.ingredients = ingredients;
	}
	public RecipeIngredients(int numIngredients) {
		this.numIngredients = numIngredients;
		this.ingredients = new ArrayList<Ingredient>();
	}
	public int getNumIngredients() {
		return numIngredients;
	}
	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}
	public Ingredient getIngredient(int ingredientIndex) {
		return ingredients.get(ingredientIndex);
	}
	public void addIngredient(Ingredient ig) {
		ingredients.add(ig);
	}
}