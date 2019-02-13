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
	
	public Restaurant(long ID, String name, String websiteURL, String imgURL,
					String phoneNumber, Location location, float ratingOf5, short priceRange) {
		this.ID = ID;
		this.name = name;
		this.siteURL = websiteURL;
		this.imageURL = imgURL;
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
	public short getRating() {
		return rating;
	}
	public String getPriceRange() {
		return priceRange;
	}
}
