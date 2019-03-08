package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import main.servlets.SearchServlet;
import main.utilities.Recipe;
import main.utilities.Restaurant;

public class SearchServletTest {
	private SearchServlet ss;
	
	@Before
	public void setUp() {
		ss = new SearchServlet();
	}

	@Test
	public void testSortRestaurants() {
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		restaurants.add(GenerateTestUtil.getTestRestaurant(5));
		restaurants.add(GenerateTestUtil.getTestRestaurantLatLon(15, 30, 30));
		restaurants.add(GenerateTestUtil.getTestRestaurant(10));
		restaurants.add(GenerateTestUtil.getTestRestaurantLatLon(20, 5, 5));
		
		ArrayList<Long> favoriteIDs = new ArrayList<Long>();
		favoriteIDs.add((long)15);
		ArrayList<Long> doNotShowIDs = new ArrayList<Long>();
		doNotShowIDs.add((long)5);
		
		assertEquals(4, restaurants.size());
		ss.sortRestaurants(restaurants, favoriteIDs, doNotShowIDs);
		assertEquals(3, restaurants.size());
		assertEquals(15, restaurants.get(0).getID());
		assertEquals(20, restaurants.get(1).getID());
		assertEquals(10, restaurants.get(2).getID());
	}

	@Test
	public void testSortRecipes() {
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		recipes.add(GenerateTestUtil.getTestRecipe(5));
		recipes.add(GenerateTestUtil.getTestRecipePrepCook(10, 0, 15));
		recipes.add(GenerateTestUtil.getTestRecipePrepCook(15, 0, 30));
		recipes.add(GenerateTestUtil.getTestRecipe(25));
		recipes.add(GenerateTestUtil.getTestRecipePrepCook(20, 3, 1));
		
		ArrayList<Long> favoriteIDs = new ArrayList<Long>();
		favoriteIDs.add((long)10);
		ArrayList<Long> doNotShowIDs = new ArrayList<Long>();
		doNotShowIDs.add((long)25);
		
		assertEquals(5, recipes.size());
		ss.sortRecipes(recipes, favoriteIDs, doNotShowIDs);
		assertEquals(4, recipes.size());
		assertEquals(10, recipes.get(0).getID());
		assertEquals(20, recipes.get(1).getID());
		assertEquals(5, recipes.get(2).getID());
		assertEquals(15, recipes.get(3).getID());
	}
	
	@Test
	public void testSplitConcatParams() {
		assertTrue(ss.splitConcatParams("bread pudding")
					.equals("bread+pudding"));
		assertTrue(ss.splitConcatParams("too&many/wombats on-fire")
					.equals("too+many+wombats+on+fire"));
	}
}