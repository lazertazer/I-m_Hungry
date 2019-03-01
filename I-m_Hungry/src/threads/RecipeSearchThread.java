package threads;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import utilities.Ingredient;
import utilities.InstructionSet;
import utilities.ListContainer;
import utilities.Recipe;
import utilities.RecipeIngredients;
import utilities.RecipeInstructions;
import utilities.Step;

public class RecipeSearchThread extends APIcall implements Runnable {
	private HttpServletRequest request;
	private String parameters;
	private int numResults;
	private ListContainer userLists;
	
	public RecipeSearchThread(HttpServletRequest request, String parameters, int numResults, ListContainer userLists) {
		this.request = request;
		this.parameters = parameters;
		this.numResults = numResults;
		this.userLists = userLists;
	}
	public void run() {
		//This "complex search" is the only way to get all the information we need from the recipe
		//but it counts as 3 'requests'
		//Since there is a small fee for going over the free limit of 50 requests per day
		//each team member must get their own Spoonacular rapidapi key for development purposes
		String Spoonacular_url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/searchComplex?";
		Spoonacular_url += "ranking=2";
		Spoonacular_url += "&fillIngredients=true";
		Spoonacular_url += "&instructionsRequired=true";	//Require results with instructions
		Spoonacular_url += "&addRecipeInformation=true";	//Show lots of extra info (including instructions and prep/cook time)
		Spoonacular_url += "&limitLicense=false";
		Spoonacular_url += "&offset=0";
		Spoonacular_url += "&number=" + numResults;
		Spoonacular_url += "&query=" + parameters;
		
		try {
			//Set up connection
			HttpURLConnection con = (HttpURLConnection) (new URL(Spoonacular_url)).openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-RapidAPI-Key", "YOUR SPOONACULAR API KEY");
			
			//Parse response using superclass method
			JsonObject body = readAndParseJSON(con);
			
			//Get the the list of restaurant IDs from the user lists that affect search results
			ArrayList<Long> favoriteIDs = userLists.getFavorites().getRecipeIDs();
			ArrayList<Long> doNotShowIDs = userLists.getNoShow().getRecipeIDs();
			
			//Populate array of Recipe helper class
			JsonArray items = body.getAsJsonArray("results");
			ArrayList<Recipe> recipes = new ArrayList<Recipe>();
			for (JsonElement e : items) {
				JsonObject JSONrecipe = e.getAsJsonObject();
				
				long ID = JSONrecipe.get("id").getAsLong();
				if (doNotShowIDs.contains(ID)) {
					//Skip adding this restaurant because it's on the 'do not show' list
					continue;
				}
				String name = JSONrecipe.get("title").getAsString();
				String source = JSONrecipe.get("sourceUrl").getAsString();
				int prepMinutes = 0;
				if (JSONrecipe.has("preparationMinutes")) {
					prepMinutes = JSONrecipe.get("preparationMinutes").getAsInt();
				}
				int cookMinutes = 0;
				if (JSONrecipe.has("cookingMinutes")) {
					cookMinutes = JSONrecipe.get("cookingMinutes").getAsInt();
				}
				int minutes = JSONrecipe.get("readyInMinutes").getAsInt();
				short servings = JSONrecipe.get("servings").getAsShort();
				String image = JSONrecipe.get("image").getAsString();
				short score = JSONrecipe.get("spoonacularScore").getAsShort();
				
				//Populate RecipeInstructions helper class
				JsonArray JSONinstructions = JSONrecipe.getAsJsonArray("analyzedInstructions");			
				RecipeInstructions instructions = new RecipeInstructions(ID);
				if (JSONinstructions != null && JSONinstructions.size() != 0) {
					for (JsonElement i : JSONinstructions) {
						JsonObject JSONinstructionSet = i.getAsJsonObject();
						String instructionSetName = JSONinstructionSet.get("name").getAsString();
						if (instructionSetName == null) {
							instructionSetName = "";
						}
						InstructionSet instructionSet = new InstructionSet(instructionSetName);
						
						//Fill each step into instructionSet
						JsonArray JSONsteps = JSONinstructionSet.getAsJsonArray("steps");
						for (JsonElement s : JSONsteps) {
							JsonObject stp = s.getAsJsonObject();
							int stepNumber = stp.get("number").getAsInt();
							String desc = stp.get("step").getAsString();
							Step step = new Step(stepNumber, desc);
							instructionSet.addStep(step);
						}
						instructions.addInstructionSet(instructionSet);
					}
				}
				
				//Populate RecipeIngredients helper class
				int numIngredients = JSONrecipe.get("missedIngredientCount").getAsInt();
				JsonArray JSONingredients = JSONrecipe.getAsJsonArray("missedIngredients");
				RecipeIngredients ingredients = new RecipeIngredients(numIngredients);
				if (JSONingredients != null && JSONingredients.size() != 0) {
					for (JsonElement i : JSONingredients) {
						JsonObject JSONingredient = i.getAsJsonObject();
						
						long ingredientID = JSONingredient.get("id").getAsLong();
						String ingredientName = JSONingredient.get("name").getAsString();
						String amount = JSONingredient.get("amount").getAsString();
						String unit = JSONingredient.get("unit").getAsString();
						String original = JSONingredient.get("originalString").getAsString();
						
						Ingredient ig = new Ingredient(ingredientID, ingredientName, amount, unit, original);
						ingredients.addIngredient(ig);
					}	
				}
				//Create recipe and add to results
				Recipe r = new Recipe(ID, name, source, prepMinutes, cookMinutes,
									minutes, servings, image, score, instructions, ingredients);
				recipes.add(r);
			}
			//Sort results using custom comparator
			//Favorites go to the beginning, otherwise sort by prep time
			//Recipes with no prep time are compared using their total time
			Collections.sort(recipes, new Comparator<Recipe>() {
				@Override
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
			//Include recipes in request for display on results.jsp, and save to session
			request.setAttribute("recipeResults", recipes);
			request.getSession().setAttribute("recipeResults", recipes);
		} catch (IOException ioe) {}
	}
}