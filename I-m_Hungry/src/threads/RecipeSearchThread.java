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
		String SpoonacularURL = buildSpoonacularURL(parameters, numResults);
		try {
			JsonObject body = connectReadParse(SpoonacularURL);
			ArrayList<Long> doNotShowIDs = userLists.getNoShow().getRecipeIDs();
			ArrayList<Recipe> recipes = parseResponseRecipes(body, doNotShowIDs);
			//Include recipes in request for display on results.jsp, and save to session
			if(request != null) {
				request.setAttribute("recipeResults", recipes);
				request.getSession().setAttribute("recipeResults", recipes);
			}
		} catch (IOException ioe) {}
	}
	public ArrayList<Recipe> parseResponseRecipes(JsonObject json, ArrayList<Long> doNotShowIDs) {
		//Populate array of Recipe helper class
		JsonArray items = json.getAsJsonArray("results");
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
		return recipes;
	}
	public JsonObject connectReadParse(String endpoint) throws IOException {
		//Set up connection
		HttpURLConnection con = (HttpURLConnection) (new URL(endpoint)).openConnection();
		con.setRequestMethod("GET");
		//Enter individual Spoonacular key here
		con.setRequestProperty("X-RapidAPI-Key", "YOUR SPOONACULAR API KEY HERE");
		//Parse response using superclass method
		return readAndParseJSON(con);
	}
	public String buildSpoonacularURL(String params, int num) {
		//Complex search counts as 3 requests
		//There is a small fee for going over the free limit of 50 requests per day
		//Each team member must get their own Spoonacular rapidapi key for development purposes
		String SpoonacularURL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/searchComplex?";
		SpoonacularURL += "ranking=2";
		SpoonacularURL += "&fillIngredients=true";
		SpoonacularURL += "&instructionsRequired=true";	//Require results with instructions
		SpoonacularURL += "&addRecipeInformation=true";	//Show lots of extra info (including instructions and prep/cook time)
		SpoonacularURL += "&limitLicense=false";
		SpoonacularURL += "&offset=0";
		SpoonacularURL += "&number=" + num;
		SpoonacularURL += "&query=" + params;
		return SpoonacularURL;
	}
}