package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.utilities.Recipe;
import main.utilities.RecipeIngredients;
import main.utilities.RecipeInstructions;

public class RecipeTest {
	private Recipe r;
	
	@Before
	public void setUp() {
		r = GenerateTestUtil.getTestRecipe(3);
	}
	
	@Test
	public void testGetName() {
		assertTrue(r.getName().equals("testRecipe"));
	}

	@Test
	public void testGetSourceURL() {
		assertTrue(r.getSourceURL().equals("testURL"));
	}

	@Test
	public void testUseTotalMinutes() {
		assertFalse(r.useTotalMinutes());
		Recipe r = GenerateTestUtil.getTestRecipePrepCook(3, 0, 20);
		assertTrue(r.useTotalMinutes());
	}

	@Test
	public void testGetTotalMinutes() {
		assertEquals(10, r.getTotalMinutes());
	}

	@Test
	public void testGetPrepMinutes() {
		assertEquals(5, r.getPrepMinutes());
	}

	@Test
	public void testGetCookMinutes() {
		assertEquals(5, r.getCookMinutes());
	}

	@Test
	public void testGetServings() {
		assertEquals(4, r.getServings());
	}

	@Test
	public void testGetImageURL() {
		assertTrue(r.getImageURL().equals("testURL"));
	}

	@Test
	public void testGetScore() {
		assertEquals(5, r.getScore());
	}

	@Test
	public void testGetInstructions() {
		RecipeInstructions inst = r.getInstructions();
		assertEquals(3, inst.getRecipeID());
	}

	@Test
	public void testGetIngredientInfo() {
		RecipeIngredients ingr = r.getIngredientInfo();
		assertEquals(1, ingr.getNumIngredients());
	}
}