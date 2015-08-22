package restaurants;

/**
 * This class creates an restaurant object containing information about the place.
 * 
 * @author Karen Chiao
 * @since 2015-08-20
 */

public class PlaceInformation {
	
	private String name;
	private String address;
	private double dist;
	private GeoLocation loc;
	private String descrip;
	private int rating;
	private String grade;
	
	/**
	 * @param n Name of restaurant.
	 * @param a Address of restaurant.
	 * @param lat Latitude.
	 * @param lng Longitude.
	 * @param d Description of restaurant.
	 * @param r Restaurant rating.
	 * @param g Restaurant grade.
	 */
	public PlaceInformation(String n, String a, double lat, double lng, String d, int r, String g) {
		name = n;
		address = a;
		dist = 0.0;
		loc = new GeoLocation(lat, lng);
		descrip = d;
		rating = r;
		grade = g;
	}
	
	/**
	 * @return false if the object is empty
	 */
	public boolean isEmpty() {
		if (name == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * @return String name of restaurant.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return String address of restaurant.
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * @return double Distance from given address in miles
	 */
	public double getDist() {
		return dist;
	}
	
	/**
	 * Set object distance from given address in miles.
	 * 
	 * @param d Distance in meters.
	 */
	public void setDist(double d) {
		dist = d * 0.000621371192;
	}
	
	/**
	 * @return GeoLocation object containing geographical coordinates.
	 */
	public GeoLocation getLoc() {
		return loc;
	}
	
	/**
	 * @return String representation of geographical coordinates.
	 */
	public String getLocStr() {
		return loc.toString();
	}
	
	/**
	 * @return String Restaurant health description.
	 */
	public String getDescrip() {
		return descrip;
	}
	
	/**
	 * @return int Restaurant number rating.
	 */
	public int getRating() {
		return rating;
	}
	
	/**
	 * @return String Restaurant letter grade.
	 */
	public String getGrade() {
		return grade;
	}
	
	/**
	 * Returns the distance between two restaurants.
	 * 
	 * @param pi Another location
	 * @return double Distance in meters between two objects.
	 */
	public double distanceFrom(PlaceInformation pi) {
		return loc.distanceFrom(pi.loc);
	}
	
	/* 
	 * Formatted as:
	 * "Name, Address, Distance, Description, Rating, Grade
	 */
	public String toString() {
		String c = ", ";
		String s = name + c + address + c + dist + c + descrip + c + String.valueOf(rating) + c + grade;
		return s;
	}
}
