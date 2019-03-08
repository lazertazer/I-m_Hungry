package threads;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CollageThread extends APIcall implements Runnable {
	private HttpServletRequest request;
	private String parameters;
	
	public CollageThread(HttpServletRequest request, String parameters) {
		this.request = request;
		this.parameters = parameters;
	}
	public void run() {
		String CSE_url = buildCSE_URL(parameters);
		try {
			JsonObject body = connectReadParse(CSE_url);
			ArrayList<String> images = getCSEimages(body);
			//Include images in request for display on results.jsp, and save to session
			if(request != null) {
				request.setAttribute("images", images);
				request.getSession().setAttribute("images", images);
			}
		} catch (IOException ioe) {System.out.println(ioe);}
	}
	public ArrayList<String> getCSEimages(JsonObject json) {
		//Add each image URL to a list
		JsonArray items = json.getAsJsonArray("items");
		ArrayList<String> imgs = new ArrayList<String>();
		for (JsonElement e : items) {
			imgs.add(e.getAsJsonObject().get("link").getAsString());
		}
		return imgs;
	}
	public JsonObject connectReadParse(String endpoint) throws IOException {
		//Set up connection
		HttpURLConnection con = (HttpURLConnection) (new URL(endpoint)).openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		
		//Parse response using abstract superclass method
		return readAndParseJSON(con);
	}
	public String buildCSE_URL(String param) {
		String CSE_url = "https://www.googleapis.com/customsearch/v1?";	//Google Custom Search Engine API (50 calls per day)
		CSE_url += "key=AIzaSyAVrKq--dMNOkfH4p6kjrKIiqGhZ4alq5k";		//API key
		CSE_url += "&cx=016870486013668844652:p_im0w326so";				//CSE identifier
		CSE_url += "&searchType=image&num=10";							//Set to search 10 images
		CSE_url += "&filter=1";											//Enable duplicate content filter
		CSE_url += "&q=food+" + param;									//Set query
		return CSE_url;
	}
}