package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import test.GenerateTestUtil;
import main.utilities.Item;

public class ItemTest {
	private Item item1;
	private Item item2;
	
	@Before
	public void setUp() {
		item1 = GenerateTestUtil.getTestRecipe(3);
		item2 = GenerateTestUtil.getTestRestaurant(4);
	}

	@Test
	public void testGetID() {
		assertEquals(3, item1.getID());
		assertEquals(4, item2.getID());
	}

	@Test
	public void testGetType() {
		assertTrue(item1.getType().equals("recipe"));
		assertTrue(item2.getType().equals("restaurant"));
	}
}