package threads;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import utilities.Ingredient;
import utilities.InstructionSet;
import utilities.Recipe;
import utilities.RecipeIngredients;
import utilities.RecipeInstructions;
import utilities.Step;

public class RecipeSearchThread extends APIcall implements Runnable {
	private HttpServletRequest request;
	private String parameters;
	private int numResults;
	public RecipeSearchThread(HttpServletRequest request, String parameters, int numResults) {
		this.request = request;
		this.parameters = parameters;
		this.numResults = numResults;
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
			con.setRequestProperty("X-RapidAPI-Key", "YOUR SPOONACULAR API KEY HERE");	//API key
			
			//Parse response using superclass method
			JsonObject body = readAndParseJSON(con);
			
			//Populate array of Recipe helper class
			JsonArray items = body.getAsJsonArray("results");
			ArrayList<Recipe> recipes = new ArrayList<Recipe>();
			for (JsonElement e : items) {
				JsonObject JSONrecipe = e.getAsJsonObject();
				
				long ID = JSONrecipe.get("id").getAsLong();
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
				
	/*			int agLikes = JSONrecipe.get("aggregateLikes").getAsInt();
				int likes = JSONrecipe.get("likes").getAsInt();
				int score = JSONrecipe.get("spoonacularScore").getAsInt();*/
				
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
						int amount = JSONingredient.get("amount").getAsInt();
						String unit = JSONingredient.get("unit").getAsString();
						String original = JSONingredient.get("originalString").getAsString();
						
						Ingredient ig = new Ingredient(ingredientID, ingredientName, amount, unit, original);
						ingredients.addIngredient(ig);
					}	
				}
				//TODO deal with likes/score?
				Recipe r = new Recipe(ID, name, source, prepMinutes, cookMinutes,
									minutes, servings, image, instructions, ingredients);
				recipes.add(r);
			}
			//Include recipes in request for display on results.jsp
			request.setAttribute("recipeResults", recipes);
		} catch (IOException ioe) {}
	}
}
