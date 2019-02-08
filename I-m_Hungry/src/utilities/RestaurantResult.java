package utilities;

public class RestaurantResult {
	private long ID;
	private String name;
	private int rating;
	private String address;
	private String priceRange = "";
	
	public RestaurantResult(long res_id, String name, float rating, String address, short price_range) {
		this.ID = res_id;
		this.name = name;
		this.rating = (int)((rating / 5) * 100);				//Get rating as percentage
		this.address = address;
		this.priceRange = ("$").repeat(price_range);	//Display dollar signs
	}
	
	public long getID() {
		return ID;
	}
	public String getName() {
		return name;
	}
	public int getRating() {
		return rating;
	}
	public String getAddress() {
		return address;
	}
	public String getPriceRange() {
		return priceRange;
	}
}
