package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.utilities.Ingredient;

public class IngredientTest {
	private Ingredient ing;
	
	@Before
	public void setUp() {
		ing = new Ingredient((long)1, "onion", "0.5", "cup", "0.5 cup onion");
	}

	@Test
	public void testGetIngredientID() {
		assertTrue(ing.getIngredientID() == 1);
	}

	@Test
	public void testGetName() {
		assertTrue(ing.getName().equals("onion"));
	}

	@Test
	public void testGetAmount() {
		assertTrue(ing.getAmount().equals("1/2"));
		Ingredient ing2 = new Ingredient((long)2, "tst", "1.333333333",
										"ounce", "1.333333333 ounce tst");
		assertTrue(ing2.getAmount().equals("1 + 1/3"));
		Ingredient ing3 = new Ingredient((long)2, "tst", "4",
										"ounce", "4 ounce tst");
		assertTrue(ing3.getAmount().equals("4"));
		Ingredient ing4 = new Ingredient((long)2, "tst", "0.75",
										"ounce", "0.75 ounce tst");
		assertTrue(ing4.getAmount().equals("3/4"));
		Ingredient ing5 = new Ingredient((long)2, "tst", "0.25",
										"ounce", "0.25 ounce tst");
		assertTrue(ing5.getAmount().equals("1/4"));
		Ingredient ing6 = new Ingredient((long)2, "tst", "0.666666",
										"ounce", "0.666666 ounce tst");
		assertTrue(ing6.getAmount().equals("2/3"));
		Ingredient ing7 = new Ingredient((long)2, "tst", "0.8",
										"ounce", "0.8 ounce tst");
		assertTrue(ing7.getAmount().equals("0.8"));
	}

	@Test
	public void testGetUnit() {
		assertTrue(ing.getUnit().equals("cup"));
	}

	@Test
	public void testGetOriginalString() {
		assertTrue(ing.getOriginalString().equals("0.5 cup onion"));
	}
}