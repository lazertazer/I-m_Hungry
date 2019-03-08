package test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;

import main.threads.RestaurantSearchThread;
import main.utilities.ListContainer;
import main.utilities.Restaurant;

public class RestaurantSearchThreadTest {
	private RestaurantSearchThread rst;

	@Before
	public void setUp() {
		rst = new RestaurantSearchThread(null, "french+pastry", 3, new ListContainer());
	}

	@Test
	public void testParseResponseRestaurants() {
		try {
			System.out.println(rst == null);

			JsonObject json = rst.connectReadParse(
					rst.buildZomatoURL("french+pastry", 3));
			ArrayList<Restaurant> restaurants = rst.parseResponseRestaurants(
													json, new ArrayList<Long>());
			assertTrue(restaurants.size() <= 3);
			assertTrue(restaurants.size() > 0);
			assertTrue(restaurants.get(0).getID() > 0);
		} catch (Exception e) {}
	}

	@Test
	public void testConnectReadParse() {
		try {
			JsonObject json = rst.connectReadParse(rst.buildZomatoURL("french+pastry", 3));
			assertTrue(json.size() > 1);
		} catch (Exception e) {}
	}

	@Test
	public void testBuildZomatoURL() {
		String ZomatoURL = rst.buildZomatoURL("french+pastry", 3);
		
		assertTrue(ZomatoURL
				.equals("https://developers.zomato.com/api/v2.1/search?lat=34.020560&lon=-118.285427&sort=real_distance&count=3&q=french+pastry"));
		
	}
}