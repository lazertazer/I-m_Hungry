package utilities;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Restaurant extends Item {
	private long ID;
	private String name;
	private String siteURL;
	private String imageURL;
	private String phoneNumber;
	private Location location;
	private short rating;
	private String priceRange = "";
	private int minutesFromTT;
	private String directionsURL;
	
	public Restaurant(long ID, String name, String websiteURL, String imgURL,
					String phoneNumber, Location location, float ratingOf5, short priceRange) {
		this.ID = ID;
		this.name = name;
		this.siteURL = websiteURL;
		this.imageURL = imgURL;
		//Generate random phone when API doesn't provide one
		this.phoneNumber = phoneNumber.trim().equals("") ? randomPhone() : phoneNumber;
		//Helper class
		this.location = location;
		//Get rating as percentage
		this.rating = (short)((ratingOf5 / 5) * 100);
		//1-4 dollar signs to represent price
		this.priceRange = new String(new char[priceRange]).replace('\0', '$');
		//28.6 mph average driving speed in LA --> 718.8403 meters per minute
		double distanceFromTT = distance(34.020560, location.getLatitude(), -118.285427, location.getLongitude());
		int minutes = (int)((distanceFromTT/718.8403) * 1.5);
		this.minutesFromTT = minutes > 0 ? minutes : 1;
		//generate URL that links to Google Maps directions
		createDirectionsURL();
		setType("restaurant");
	}
	/**
	 * Calculate distance between two points in latitude and longitude.
	 * Uses Haversine method
	 * Returns distance in meters
	 */
	private static double distance(double lat1, double lat2, double lon1, double lon2) {
	    final int R = 6371; // Radius of the earth
	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters
	    return distance;
	}
	private void createDirectionsURL() {
		String[] addressTerms = getLocation().getAddress().split(" ,");
		String params = addressTerms[0];
		for (int i = 1; i < addressTerms.length; i++) {
			params += "+" + addressTerms[i];
		}
		this.directionsURL = "https://www.google.com/maps/search/?api=1&query=" + params;
	}
	private String randomPhone() {
		ArrayList<String> areaCodes = new ArrayList<String>() {{
			add("(213)");
			add("(424)");
			add("(323)");
			add("(310)");
		}};
		int areaCodeRand = ThreadLocalRandom.current().nextInt(0, areaCodes.size());
		int threeDigitRand = ThreadLocalRandom.current().nextInt(100, 1000);
		int fourDigitRand = ThreadLocalRandom.current().nextInt(1000, 10000);
		return areaCodes.get(areaCodeRand) + " " + threeDigitRand + "-" + fourDigitRand;
	}
	public long getID() {
		return ID;
	}
	public String getName() {
		return name;
	}
	public String getURL() {
		return siteURL;
	}
	public String getImageURL() {
		return imageURL;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public Location getLocation() {
		return location;
	}
	public int getMinutesFromTT() {
		return minutesFromTT;
	}
	public short getRating() {
		return rating;
	}
	public String getPriceRange() {
		return priceRange;
	}
	public String getDirectionsURL() {
		return directionsURL;
	}
}
