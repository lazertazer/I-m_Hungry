package utilities;

public class Restaurant {
	private long ID;
	private String name;
	private String siteURL;
	private String imageURL;
	private String phoneNumber;
	private Location location;
	private short rating;
	private String priceRange = "";
	private int minutesFromTT;
	
	public Restaurant(long ID, String name, String websiteURL, String imgURL,
					String phoneNumber, Location location, float ratingOf5, short priceRange) {
		this.ID = ID;
		this.name = name;
		this.siteURL = websiteURL;
		this.imageURL = imgURL;
		this.phoneNumber = phoneNumber;
		this.location = location;						//Helper class
		this.rating = (short)((ratingOf5 / 5) * 100);		//Get rating as percentage
		this.priceRange = ("$").repeat(priceRange);	//1-3 dollar signs to represent price
		
		double distanceFromTT = distance(34.020560, location.getLatitude(), -118.285427, location.getLongitude());
		//28.6 mph average driving speed in LA --> 718.8403 meters per minute
		int minutes = (int)(distanceFromTT/718.8403);
		this.minutesFromTT = minutes > 0 ? minutes : 1;
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
}
