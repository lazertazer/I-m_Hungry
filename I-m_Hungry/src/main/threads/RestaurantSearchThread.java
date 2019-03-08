package main.threads;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import main.utilities.ListContainer;
import main.utilities.Location;
import main.utilities.Restaurant;

public class RestaurantSearchThread extends APIcall implements Runnable {
	private HttpServletRequest request;
	private String parameters;
	private int numResults;
	private ListContainer userLists;
	
	public RestaurantSearchThread(HttpServletRequest request, String parameters, int numResults, ListContainer userLists) {
		this.request = request;
		this.parameters = parameters;
		this.numResults = numResults;
		this.userLists = userLists;
	}
	public void run() {
		String ZomatoURL = buildZomatoURL(parameters, numResults);
		try {
			JsonObject body = connectReadParse(ZomatoURL);
			ArrayList<Long> doNotShowIDs = userLists.getNoShow().getRestaurantIDs();
			ArrayList<Restaurant> restaurants = parseResponseRestaurants(body, doNotShowIDs);
			//Include restaurants in request for display on results.jsp, and save to session
			if(request != null) {
				request.setAttribute("restaurantResults", restaurants);
				request.getSession().setAttribute("restaurantResults", restaurants);
			}
		} catch (IOException ioe) {System.out.println(ioe.getLocalizedMessage());}
	}
	public ArrayList<Restaurant> parseResponseRestaurants(JsonObject json, ArrayList<Long> doNotShowIDs) {
		//Populate array of Restaurant helper class
		JsonArray items = json.getAsJsonArray("restaurants");
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		for (JsonElement e : items) {
			JsonObject obj = e.getAsJsonObject().get("restaurant").getAsJsonObject();
			
			long ID = obj.get("id").getAsLong();
			if (doNotShowIDs.contains(ID)) {
				//Skip adding this restaurant because it's on the 'do not show' list
				continue;
			}
			String name = obj.get("name").getAsString();
			String websiteURL = obj.get("url").getAsString();
			String imgURL = obj.get("featured_image").getAsString();
			if (imgURL.equals("")) {
				imgURL = obj.get("thumb").getAsString();
			}
			String phoneNumber = "";
			if (obj.has("phone_numbers")) {
				phoneNumber = obj.get("phone_numbers").getAsString();
			}
			
			JsonObject JSONlocation = obj.getAsJsonObject("location");
			String address = JSONlocation.get("address").getAsString();
			String locality = JSONlocation.get("locality").getAsString();
			String city = JSONlocation.get("city").getAsString();
			double latitude = JSONlocation.get("latitude").getAsDouble();
			double longitude = JSONlocation.get("longitude").getAsDouble();
			String zipcode = JSONlocation.get("zipcode").getAsString();
			Location location = new Location(address, locality, city, latitude, longitude, zipcode);
			
			float rating = obj.get("user_rating").getAsJsonObject().get("aggregate_rating").getAsFloat();
			short priceRange = obj.get("price_range").getAsShort();
			
			//create restaurant and add to results
			Restaurant r = new Restaurant(ID, name, websiteURL, imgURL, phoneNumber, location, rating, priceRange);
			restaurants.add(r);
		}
		return restaurants;
	}
	public JsonObject connectReadParse(String endpoint) throws IOException {
		//Set up connection
		HttpURLConnection con = (HttpURLConnection) (new URL(endpoint)).openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Accept", "application/json");
		//Zomato API key here
		con.setRequestProperty("user-key", "796b065d0378b247df1c455220bf2e58");
		//Parse response using superclass method
		return readAndParseJSON(con);
	}
	public String buildZomatoURL(String params, int num) {
		String ZomatoURL = "https://developers.zomato.com/api/v2.1/search?";	//Zomato restaurant API
		ZomatoURL += "lat=34.020560";											//Coordinates of Tommy Trojan
		ZomatoURL += "&lon=-118.285427";
		ZomatoURL += "&sort=real_distance";										//Sort by distance from TT
		ZomatoURL += "&count=" + num;											//Set max results
		ZomatoURL += "&q=" + params;											//Set query
		return ZomatoURL;
	}
}