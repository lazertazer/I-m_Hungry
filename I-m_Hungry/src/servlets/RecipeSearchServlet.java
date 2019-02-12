package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import utilities.Ingredient;
import utilities.InstructionSet;
import utilities.Recipe;
import utilities.RecipeIngredients;
import utilities.RecipeInstructions;
import utilities.Step;

/**
 * INPUT: request attributes 'query' and 'numResults'
 * FUNCTION:
 *     Call Spoonacular recipe API with user query
 *     Max 100 results per search, also rate-limited to 50 requests and 500 results per day
 *     Will require another api request to get recipe instructions
 *     Extract relevant information from json
 *     Put into array of helper class RecipeResult and forward
 */
@WebServlet("/RecipeSearchServlet")
public class RecipeSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public RecipeSearchServlet() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response)
							throws ServletException, IOException {
		//For testing purposes TODO remove
		request.setAttribute("query", "burger egg");
		request.setAttribute("numResults", 5);
		
		//Get user input and split query into an array of words
		String[] queryArray = ((String)request.getAttribute("query")).split("[ \t&?+_\\/-]");
		int numResults = (int)request.getAttribute("numResults");
		
		//Concatenate search query terms with '+'
		String parameters = queryArray[0];
		for (int i = 1; i < queryArray.length; i++) {
			parameters += "+" + queryArray[i];
		}
		
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
				
		//Set up connection
		HttpURLConnection con = (HttpURLConnection) (new URL(Spoonacular_url)).openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("X-RapidAPI-Key", "YOUR SPOONACULAR API KEY HERE");	//API key
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		
		//Read response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer Spoonacular_response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
		    Spoonacular_response.append(inputLine);
		}
		in.close();
		con.disconnect();
		
		//Use Gson to parse the JSON response
		Gson gson = new Gson();
		JsonObject body = gson.fromJson(Spoonacular_response.toString(), JsonObject.class);
		JsonArray items = body.getAsJsonArray("results");
		
		//Populate array of Recipe helper class
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		for (JsonElement e : items) {
			JsonObject obj = e.getAsJsonObject();
			
			long ID = obj.get("id").getAsLong();
			String name = obj.get("title").getAsString();
			String source = obj.get("sourceUrl").getAsString();
			int prepMinutes = 0;
			if (obj.has("preparationMinutes")) {
				prepMinutes = obj.get("preparationMinutes").getAsInt();
			}
			int cookMinutes = 0;
			if (obj.has("cookingMinutes")) {
				cookMinutes = obj.get("cookingMinutes").getAsInt();
			}
			int minutes = obj.get("readyInMinutes").getAsInt();
			short servings = obj.get("servings").getAsShort();
			String image = obj.get("image").getAsString();
			
/*			int agLikes = obj.get("aggregateLikes").getAsInt();
			int likes = obj.get("likes").getAsInt();
			int score = obj.get("spoonacularScore").getAsInt();*/
			

			JsonArray JSONinstructions = obj.getAsJsonArray("analyzedInstructions");			
			//Populate RecipeInstructions helper class
			RecipeInstructions instructions = new RecipeInstructions(ID);
			if (JSONinstructions != null && JSONinstructions.size() != 0) {
				for (JsonElement i : JSONinstructions) {
					JsonObject JSONinstructionSet = i.getAsJsonObject();
					String instructionSetName = JSONinstructionSet.get("name").getAsString();
					if (instructionSetName == null) {
						instructionSetName = "";
					}
					InstructionSet instructionSet = new InstructionSet(instructionSetName);
					JsonArray JSONsteps = JSONinstructionSet.getAsJsonArray("steps");
					//Fill Steps into each InstructionSet
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
			
			int numIngredients = obj.get("missedIngredientCount").getAsInt();
			JsonArray JSONingredients = obj.getAsJsonArray("missedIngredients");
			//Populate RecipeIngredients helper class
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
		
		//Forward to results page
		request.setAttribute("recipeResults", recipes);
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/RestaurantSearchServlet");
		dispatch.forward(request,  response);
	}

}
