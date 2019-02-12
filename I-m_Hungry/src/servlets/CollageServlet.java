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

/**
 * INPUT: request attribute named 'query'
 * FUNCTION:
 *     Calls Google Custom Search API with the user query and extracts the URLs of the images using Gson
 *     Includes the URLs as a list attribute in the request, and forwards to results.jsp for display
 */
@WebServlet("/CollageServlet")
public class CollageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CollageServlet() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response)
							throws ServletException, IOException {
		//Split query into an array of words
		String[] queryArray = ((String)request.getAttribute("query")).split("[ \t&?+_\\/-]");
		
		//Concatenate search query terms with '+'
		String parameters = queryArray[0];
		for (int i = 1; i < queryArray.length; i++) {
			parameters += "+" + queryArray[i];
		}
		
		String CSE_url = "https://www.googleapis.com/customsearch/v1?";	//Google Custom Search Engine API (50 calls per day)
		CSE_url += "key=AIzaSyAVrKq--dMNOkfH4p6kjrKIiqGhZ4alq5k";		//API key
		CSE_url += "&cx=016870486013668844652:p_im0w326so";				//CSE identifier
		CSE_url += "&searchType=image&num=10";							//Set to search 10 images
		CSE_url += "&filter=1";											//Enable duplicate content filter
		CSE_url += "&q=" + parameters;									//Set query
		
		//Set up connection
		HttpURLConnection con = (HttpURLConnection) (new URL(CSE_url)).openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		
		//Read response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer CSE_response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
		    CSE_response.append(inputLine);
		}
		in.close();
		con.disconnect();
		
		//Use Gson to parse the JSON response
		Gson gson = new Gson();
		JsonObject body = gson.fromJson(CSE_response.toString(), JsonObject.class);
		JsonArray items = body.getAsJsonArray("items");
		
		//Add each image URL to a list
		ArrayList<String> images = new ArrayList<String>();
		for (JsonElement e : items) {
			images.add(e.getAsJsonObject().get("link").getAsString());
		}
		
		//Forward to results page
		request.setAttribute("images", images);
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/results.jsp");
		dispatch.forward(request,  response);
	}
}
