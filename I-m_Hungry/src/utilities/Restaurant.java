package utilities;

public class Restaurant {
	private long ID;
	private String name;
	private String url;
	private String featuredImageUrl;
	private String phoneNumber;
	private Location location;
	private short rating;
	private String priceRange = "";
	
	public Restaurant(long ID, String name, String websiteURL, String imgURL,
					String phoneNumber, Location location, float ratingOf5, short priceRange) {
		this.ID = ID;
		this.name = name;
		this.url = websiteURL;
		this.featuredImageUrl = imgURL;
		this.phoneNumber = phoneNumber;
		this.location = location;						//Helper class
		this.rating = (short)((rating / 5) * 100);		//Get rating as percentage
		this.priceRange = ("$").repeat(priceRange);	//1-3 dollar signs to represent price
	}
	public long getID() {
		return ID;
	}
	public String getName() {
		return name;
	}
	public String getURL() {
		return url;
	}
	public String getImageURL() {
		return featuredImageUrl;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public Location getLocation() {
		return location;
	}
	public short getRating() {
		return rating;
	}
	public String getPriceRange() {
		return priceRange;
	}
}
