package threads;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public abstract class APIcall {
	public JsonObject readAndParseJSON(HttpURLConnection con) {
		con.setConnectTimeout(8000);
		con.setReadTimeout(8000);
		JsonObject body = null;
		try {
			//Use Gson to parse and return the JSON response
			Gson gson = new Gson();
			InputStream is = con.getInputStream();
			body = gson.fromJson(new JsonReader(new InputStreamReader(is, "UTF-8")), JsonObject.class);
		} catch (IOException e) {
			System.out.println("IOE: " + e.getMessage());
		}
		return body;
	}
}
