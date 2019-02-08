package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import utilities.RestaurantResult;

/**
 * INPUT: request attributes 'query' and 'numResults'
 * FUNCTION:
 *     Call Zomato restaurant API with user query
 *     Extract relevant information from json
 *     Put into array of helper class RestaurantResult and forward to collage servlet
 */
@WebServlet("/RestaurantSearchServlet")
public class RestaurantSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public RestaurantSearchServlet() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//For testing purposes TODO remove
		request.setAttribute("query", "pie/pizza hypercar_burger");
		request.setAttribute("numResults", 10);
		
		//Get user input and split query into an array of words
		String[] queryArray = ((String)request.getAttribute("query")).split("[ \t&?+_\\/-]");
		int numResults = (int)request.getAttribute("numResults");
		
		//Concatenate search query terms with '+'
		String parameters = queryArray[0];
		for (int i = 1; i < queryArray.length; i++) {
			parameters += "+" + queryArray[i];
		}
		
		String Zomato_url = "https://developers.zomato.com/api/v2.1/search?";	//Zomato restaurant API
		Zomato_url += "lat=34.020560";											//Coordinates of Tommy Trojan
		Zomato_url += "&lon=-118.285427";
		Zomato_url += "&sort=real_distance";									//Sort by distance from TT
		Zomato_url += "&count=" + numResults;									//Set max results
		Zomato_url += "&q=" + parameters;										//Set query
		
		//Set up connection
		HttpURLConnection con = (HttpURLConnection) (new URL(Zomato_url)).openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("user-key", "796b065d0378b247df1c455220bf2e58");	//API key
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		
		//Read response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer Zomato_response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
		    Zomato_response.append(inputLine);
		}
		in.close();
		con.disconnect();
		
		//Use Gson to parse the JSON response
		Gson gson = new Gson();
		JsonObject body = gson.fromJson(Zomato_response.toString(), JsonObject.class);
		JsonArray items = body.get("restaurants").getAsJsonArray();
		
		//Populate array of helper class results
		ArrayList<RestaurantResult> restaurants = new ArrayList<RestaurantResult>();
		for (JsonElement e : items) {
			JsonObject obj = e.getAsJsonObject().get("restaurant").getAsJsonObject();
			
			long ID = obj.get("id").getAsLong();
			String name = obj.get("name").getAsString();
			float rating = obj.get("user_rating").getAsJsonObject().get("aggregate_rating").getAsFloat();
			String address = obj.get("location").getAsJsonObject().get("address").getAsString();
			short priceRange = obj.get("price_range").getAsShort();
			
			restaurants.add(new RestaurantResult(ID, name, rating, address, priceRange));
		}
		
		//Forward to collage servlet
		request.setAttribute("restaurantResults", restaurants);
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/CollageServlet");
		dispatch.forward(request,  response);
	}
}
