package utilities;

public class Location {
	private String address;
	private String locality;
	private String city;
	private double latitude;
	private double longitude;
	private String zipcode;
	public Location(String address, String locality, String city,
					double latitude, double longitude, String zipcode) {
		this.address = address;
		this.locality = locality;
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
		this.zipcode = zipcode;
	}
	public String getAddress() {
		return address;
	}
	public String getLocality() {
		return locality;
	}
	public String getCity() {
		return city;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public String getZipcode() {
		return zipcode;
	}
}