package test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;

import main.threads.RecipeSearchThread;
import main.utilities.ListContainer;
import main.utilities.Recipe;

public class RecipeSearchThreadTest {
	private RecipeSearchThread recThread;
	
	@Before
	public void setUp() {
		recThread = new RecipeSearchThread(null, "french+bread", 3, new ListContainer());
	}

	//Make sure API key is in RecipeSearchThread if this tests fails
	@Test
	public void testParseResponseRecipes() {
		try {
			JsonObject json = recThread.connectReadParse(
									recThread.buildSpoonacularURL("french+pastry", 3));
			ArrayList<Recipe> recipes = recThread.parseResponseRecipes(json, new ArrayList<Long>());
			assertTrue(recipes.size() <= 3);
			assertTrue(recipes.size() > 0);
			assertTrue(recipes.get(0).getID() > 0);
		} catch (Exception e) {}
	}

	@Test
	public void testConnectReadParse() {
		try {
			JsonObject json = recThread.connectReadParse(
									recThread.buildSpoonacularURL("french+pastry", 3));
			assertTrue(json.size() > 1);
		} catch (Exception e) {}
	}

	@Test
	public void testBuildSpoonacularURL() {
		String SpoonacularURL = recThread.buildSpoonacularURL("french+pastry", 3);
		assertTrue(SpoonacularURL
				.equals("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/searchComplex?ranking=2&fillIngredients=true&instructionsRequired=true&addRecipeInformation=true&limitLicense=false&offset=0&number=3&query=french+pastry"));
	}
	
	@After
	public void after() {
		recThread.run();
	}
}