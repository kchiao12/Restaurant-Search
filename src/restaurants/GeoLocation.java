package restaurants;

/**
 * These objects stores the latitude and longitude coordinates of 
 * a given location on Earth. 
 * 
 * @author Karen Chiao
 * @since 2015-08-20
 */
public class GeoLocation {

	private double latitude;
	private double longitude;
	
	/**
	 * @param lat Latitude.
	 * @param lon Longitude.
	 */
	public GeoLocation(double lat, double lon) {
		latitude = lat;
		longitude = lon;
	}
	
	/**
	 * Call the private distance method to calculate the metric distance 
	 * between two GeoLocation objects.
	 * 
	 * @param loc Object from data list.
	 * @return double Distance in meters.
	 */
	public double distanceFrom(GeoLocation loc) {
		double distance = distance(latitude, loc.latitude, longitude, loc.longitude);
		return distance;
	}
	
	/*
	 * Adapted from StackOverflow, modified for own purposes:
	 * http://stackoverflow.com/questions/3694380/calculating-distance-between-two-
	 * 		points-using-latitude-longitude-what-am-i-doi
	 * 
	 * Calculate distance between two points in latitude and longitude taking
	 * into account height difference. If you are not interested in height
	 * difference pass 0.0. Uses Haversine method as its base.
	 * 
	 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
	 * el2 End altitude in meters
	 * @returns Distance in Meters
	 */
	private double distance(double lat1, double lat2, double lon1, double lon2) {

	    final int R = 6371; // Radius of the earth

	    Double latDistance = Math.toRadians(lat2 - lat1);
	    Double lonDistance = Math.toRadians(lon2 - lon1);
	    Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters
	    distance = Math.pow(distance, 2);

	    return Math.sqrt(distance);
	}
	
	/* 
	 * The returned String will be formated like the following: "latitude, longitude".
	 */
	public String toString() {
		String s = String.valueOf(latitude) + ", " + String.valueOf(longitude);
		return s;
	}
}
