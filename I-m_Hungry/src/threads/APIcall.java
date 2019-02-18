package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public abstract class APIcall {
	public JsonObject readAndParseJSON(HttpURLConnection con) {
		con.setConnectTimeout(10000);
		con.setReadTimeout(10000);
		JsonObject body = null;
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
			body = gson.fromJson(CSE_response.toString(), JsonObject.class);
		} catch (IOException e) {
			System.out.println("IOE: " + e.getMessage() + e.getStackTrace());
		}
		return body;
	}
}
