package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.utilities.UserList;

public class UserListTest {
	private UserList fav;
	private UserList dns;
	private UserList xpl;
	
	@Before
	public void setUp() {
		fav = new UserList("Favorites", "FAV");
		dns = new UserList("Do Not Show", "DNS");
		xpl = new UserList("To Explore", "XPL");
	}

	@Test
	public void testGetListName() {
		assertTrue(fav.getListName().equals("Favorites"));
		assertTrue(dns.getListName().equals("Do Not Show"));
		assertTrue(xpl.getListName().equals("To Explore"));
	}

	@Test
	public void testGetListAcronym() {
		assertTrue(fav.getListAcronym().equals("FAV"));
		assertTrue(dns.getListAcronym().equals("DNS"));
		assertTrue(xpl.getListAcronym().equals("XPL"));
	}

	@Test
	public void testGetRestaurants() {
		fav.addRestaurant(GenerateTestUtil.getTestRestaurant(1));
		dns.addRestaurant(GenerateTestUtil.getTestRestaurant(2));
		xpl.addRestaurant(GenerateTestUtil.getTestRestaurant(3));
		
		assertTrue(fav.getRestaurants().size() == 1);
		assertTrue(dns.getRestaurants().size() == 1);
		assertTrue(xpl.getRestaurants().size() == 1);
	}

	@Test
	public void testGetRestaurantIDs() {
		fav.addRestaurant(GenerateTestUtil.getTestRestaurant(1));
		dns.addRestaurant(GenerateTestUtil.getTestRestaurant(2));
		xpl.addRestaurant(GenerateTestUtil.getTestRestaurant(3));
		
		assertTrue(fav.getRestaurantIDs().get(0) == 1);
		assertTrue(dns.getRestaurantIDs().get(0) == 2);
		assertTrue(xpl.getRestaurantIDs().get(0) == 3);
	}

	@Test
	public void testGetRecipes() {
		fav.addRecipe(GenerateTestUtil.getTestRecipe(1));
		dns.addRecipe(GenerateTestUtil.getTestRecipe(2));
		xpl.addRecipe(GenerateTestUtil.getTestRecipe(3));
		
		assertTrue(fav.getRecipes().size() == 1);
		assertTrue(dns.getRecipes().size() == 1);
		assertTrue(xpl.getRecipes().size() == 1);
	}

	@Test
	public void testGetRecipeIDs() {
		fav.addRecipe(GenerateTestUtil.getTestRecipe(1));
		dns.addRecipe(GenerateTestUtil.getTestRecipe(2));
		xpl.addRecipe(GenerateTestUtil.getTestRecipe(3));
		
		assertTrue(fav.getRecipeIDs().get(0) == 1);
		assertTrue(dns.getRecipeIDs().get(0) == 2);
		assertTrue(xpl.getRecipeIDs().get(0) == 3);
	}

	@Test
	public void testGetAllItems() {
		fav.addRecipe(GenerateTestUtil.getTestRecipe(1));
		dns.addRecipe(GenerateTestUtil.getTestRecipe(2));
		fav.addRestaurant(GenerateTestUtil.getTestRestaurant(1));
		xpl.addRestaurant(GenerateTestUtil.getTestRestaurant(3));
		
		assertTrue(fav.getAllItems().get(0).getID() == 1);
		assertTrue(fav.getAllItems().get(1).getID() == 1);
		assertTrue(dns.getAllItems().get(0).getID() == 2);
		assertTrue(xpl.getAllItems().get(0).getID() == 3);
	}

	@Test
	public void testRemoveRestaurantByID() {
		fav.addRestaurant(GenerateTestUtil.getTestRestaurant(1));
		dns.addRestaurant(GenerateTestUtil.getTestRestaurant(2));
		xpl.addRestaurant(GenerateTestUtil.getTestRestaurant(3));
		
		fav.removeRestaurantByID(0);
		assertTrue(fav.getRestaurants().size() == 1);
		fav.removeRestaurantByID(1);
		assertTrue(fav.getRestaurants().size() == 0);
		
		dns.removeRestaurantByID(0);
		assertTrue(dns.getRestaurants().size() == 1);
		dns.removeRestaurantByID(2);
		assertTrue(dns.getRestaurants().size() == 0);
		
		xpl.removeRestaurantByID(0);
		assertTrue(xpl.getRestaurants().size() == 1);
		xpl.removeRestaurantByID(3);
		assertTrue(xpl.getRestaurants().size() == 0);
	}

	@Test
	public void testRemoveRecipeByID() {
		fav.addRecipe(GenerateTestUtil.getTestRecipe(1));
		dns.addRecipe(GenerateTestUtil.getTestRecipe(2));
		xpl.addRecipe(GenerateTestUtil.getTestRecipe(3));
		
		fav.removeRecipeByID(0);
		assertTrue(fav.getRecipes().size() == 1);
		fav.removeRecipeByID(1);
		assertTrue(fav.getRecipes().size() == 0);
		
		dns.removeRecipeByID(0);
		assertTrue(dns.getRecipes().size() == 1);
		dns.removeRecipeByID(2);
		assertTrue(dns.getRecipes().size() == 0);
		
		xpl.removeRecipeByID(0);
		assertTrue(xpl.getRecipes().size() == 1);
		xpl.removeRecipeByID(3);
		assertTrue(xpl.getRecipes().size() == 0);
	}

	@Test
	public void testContainsRestaurant() {
		fav.addRestaurant(GenerateTestUtil.getTestRestaurant(1));
		dns.addRestaurant(GenerateTestUtil.getTestRestaurant(2));
		xpl.addRestaurant(GenerateTestUtil.getTestRestaurant(3));
		
		assertTrue(fav.containsRestaurant(GenerateTestUtil.getTestRestaurant(1)));
		assertFalse(fav.containsRestaurant(GenerateTestUtil.getTestRestaurant(2)));
		
		assertTrue(dns.containsRestaurant(GenerateTestUtil.getTestRestaurant(2)));
		assertFalse(dns.containsRestaurant(GenerateTestUtil.getTestRestaurant(3)));
		
		assertTrue(xpl.containsRestaurant(GenerateTestUtil.getTestRestaurant(3)));
		assertFalse(xpl.containsRestaurant(GenerateTestUtil.getTestRestaurant(4)));
	}

	@Test
	public void testContainsRecipe() {
		fav.addRecipe(GenerateTestUtil.getTestRecipe(1));
		dns.addRecipe(GenerateTestUtil.getTestRecipe(2));
		xpl.addRecipe(GenerateTestUtil.getTestRecipe(3));
		
		assertTrue(fav.containsRecipe(GenerateTestUtil.getTestRecipe(1)));
		assertFalse(fav.containsRecipe(GenerateTestUtil.getTestRecipe(2)));
		
		assertTrue(dns.containsRecipe(GenerateTestUtil.getTestRecipe(2)));
		assertFalse(dns.containsRecipe(GenerateTestUtil.getTestRecipe(3)));
		
		assertTrue(xpl.containsRecipe(GenerateTestUtil.getTestRecipe(3)));
		assertFalse(xpl.containsRecipe(GenerateTestUtil.getTestRecipe(4)));
	}
	
	@Test
	public void testAddRecipe() {
		fav.addRecipe(GenerateTestUtil.getTestRecipe(1));
		fav.addRecipe(GenerateTestUtil.getTestRecipe(1));
		assertTrue(fav.getRecipe(0).getID() == 1);
		assertTrue(fav.getRecipes().size() == 1);
		
		dns.addRecipe(GenerateTestUtil.getTestRecipe(1));
		dns.addRecipe(GenerateTestUtil.getTestRecipe(1));
		assertTrue(dns.getRecipes().size() == 1);
		
		xpl.addRecipe(GenerateTestUtil.getTestRecipe(1));
		xpl.addRecipe(GenerateTestUtil.getTestRecipe(1));
		assertTrue(xpl.getRecipes().size() == 1);
	}
	
	@Test
	public void testAddRestaurant() {
		fav.addRestaurant(GenerateTestUtil.getTestRestaurant(1));
		fav.addRestaurant(GenerateTestUtil.getTestRestaurant(1));
		assertTrue(fav.getRestaurant(0).getID() == 1);
		assertTrue(fav.getRestaurants().size() == 1);
		
		dns.addRestaurant(GenerateTestUtil.getTestRestaurant(1));
		dns.addRestaurant(GenerateTestUtil.getTestRestaurant(1));
		assertTrue(dns.getRestaurants().size() == 1);
		
		xpl.addRestaurant(GenerateTestUtil.getTestRestaurant(1));
		xpl.addRestaurant(GenerateTestUtil.getTestRestaurant(1));
		assertTrue(xpl.getRestaurants().size() == 1);
	}
}