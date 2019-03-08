package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.utilities.Location;
import main.utilities.Restaurant;

public class RestaurantTest {
	private Restaurant r;
	
	@Before
	public void setUp() {
		r = GenerateTestUtil.getTestRestaurant(5);
	}

	@Test
	public void testGetName() {
		assertTrue(r.getName().equals("testRestaurant"));
	}

	@Test
	public void testGetURL() {
		assertTrue(r.getURL().equals("testURL"));
	}

	@Test
	public void testGetImageURL() {
		assertTrue(r.getImageURL().equals("testURL"));
	}

	@Test
	public void testGetPhoneNumber() {
		assertTrue(r.getPhoneNumber().equals("(425) 895-8542"));
		Restaurant q = new Restaurant((long)1, "", "", "", "",
										new Location("", "", "", 10, 20, ""),
										(float) 3.2, (short)1);
		assertTrue(q.getPhoneNumber().contains("("));
		assertTrue(q.getPhoneNumber().contains(")"));
		assertTrue(q.getPhoneNumber().contains("-"));
	}

	@Test
	public void testGetLocation() {
		assertTrue(r.getLocation().getAddress().equals("test address"));
	}

	@Test
	public void testGetMinutesFromTT() {
		assertTrue(r.getMinutesFromTT() > 1000);
	}

	@Test
	public void testGetRating() {
		assertEquals(50, r.getRating());
	}

	@Test
	public void testGetPriceRange() {
		assertTrue(r.getPriceRange().equals("$$"));
	}

	@Test
	public void testGetDirectionsURL() {
		assertTrue(r.getDirectionsURL().contains("https://www.google.com/maps/dir/"));
	}
}