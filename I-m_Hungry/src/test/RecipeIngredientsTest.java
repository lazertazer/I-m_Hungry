package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import main.utilities.Ingredient;
import main.utilities.RecipeIngredients;

public class RecipeIngredientsTest {
	private RecipeIngredients ingr;

	@Before
	public void setUp() {
		ArrayList<Ingredient> ingr_list = new ArrayList<Ingredient>();
		ingr_list.add(new Ingredient((long)1, "onion", "0.5", "ounce", "0.5 ounce onion"));
		ingr = new RecipeIngredients(1, ingr_list);
		new RecipeIngredients(0);
	}
	
	@Test
	public void testGetNumIngredients() {
		assertEquals(1, ingr.getNumIngredients());
	}

	@Test
	public void testGetIngredients() {
		assertEquals(1, ingr.getIngredients().size());
	}

	@Test
	public void testGetIngredient() {
		assertTrue(ingr.getIngredient(0).getName().equals("onion"));
	}

	@Test
	public void testAddIngredient() {
		ingr.addIngredient(new Ingredient((long)2, "tst", "0.75", "litres", "0.75 litres tst"));
		assertEquals(2, ingr.getIngredients().size());
	}
}