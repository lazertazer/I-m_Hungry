package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public abstract class APIcall {
	public JsonObject readAndParseJSON(HttpURLConnection con) {
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		try {
			//Read response
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer CSE_response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
			    CSE_response.append(inputLine);
			}
			in.close();
			con.disconnect();
			
			//Use Gson to parse and return the JSON response
			Gson gson = new Gson();
			JsonObject body = gson.fromJson(CSE_response.toString(), JsonObject.class);
			return body;	
		} catch (IOException e) {
			return null;
		}
	}
}
