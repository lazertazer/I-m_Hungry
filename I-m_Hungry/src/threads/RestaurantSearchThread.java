package threads;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import utilities.ListContainer;
import utilities.Location;
import utilities.Restaurant;

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
		String Zomato_url = "https://developers.zomato.com/api/v2.1/search?";	//Zomato restaurant API
		Zomato_url += "lat=34.020560";											//Coordinates of Tommy Trojan
		Zomato_url += "&lon=-118.285427";
		Zomato_url += "&sort=real_distance";									//Sort by distance from TT
		Zomato_url += "&count=" + numResults;									//Set max results
		Zomato_url += "&q=" + parameters;										//Set query
		
		try {
			//Set up connection
			HttpURLConnection con = (HttpURLConnection) (new URL(Zomato_url)).openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("user-key", "796b065d0378b247df1c455220bf2e58");	//API key
			
			//Parse response using superclass method
			JsonObject body = readAndParseJSON(con);
			
			//Get the the list of restaurant IDs from the user lists that affect search results
			ArrayList<Long> favoriteIDs = userLists.getFavorites().getRestaurantIDs();
			ArrayList<Long> doNotShowIDs = userLists.getNoShow().getRestaurantIDs();
			
			//Populate array of Restaurant helper class
			JsonArray items = body.getAsJsonArray("restaurants");
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
				//TODO phone numbers are not included with the free API key :(
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
				
				Restaurant r = new Restaurant(ID, name, websiteURL, imgURL, phoneNumber, location, rating, priceRange);
				
				//If restaurant is in favorites, add to the beginning
				//else, add to the end
				if (favoriteIDs.contains(ID)) {
					restaurants.add(0, r);
				}
				else {
					restaurants.add(r);	
				}
			}
			//Include restaurants in request for display on results.jsp
			request.setAttribute("restaurantResults", restaurants);
		} catch (IOException ioe) {}
	}
}
