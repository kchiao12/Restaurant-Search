package restaurants;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
//import com.google.maps.*;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Comparator;
import java.lang.StringBuilder;

/**
 * Derived from USC ITP 470 - Independent Study Project,
 * This project utilizes the Google Maps API and restaurant data gathered from
 * the County of Los Angeles to suggest restaurants to LA-based users 
 * based on restaurant quality. 
 * 
 * @author Karen Chiao
 * @since 2015-08-20
 */

public class LArests {
	
	private String API_KEY;
	private ArrayList<PlaceInformation> al;
	private boolean sorted;
	private int indexStart;
	private int indexStop;
	private String lastType;
	

	public LArests() {
		API_KEY = "";
		al = new ArrayList<PlaceInformation>();
		sorted = false;
		indexStart = 0;
		indexStop = 10;
		lastType = "";
	}
	
	/**
	 * @return boolean true if the compiled data has been sorted.
	 */
	public boolean isSorted() {
		return sorted;
	}
	
	/**
	 * This method reads in a csv file containing Restaurant data from LA County 
	 * and stores the pieces of data as PlaceInformation objects in an ArrayList.
	 */
	public void read() {
		final String data = "C:\\Users\\Karen\\workspace\\Project1\\src\\restaurants\\Point_Map_Of_Restaurants_by_Name_with_Grading_and_Score (1).csv";
		BufferedReader br = null;
		String line = "";
		
		try {			
			br = new BufferedReader(new FileReader(data));
			br.readLine();
			while ((line = br.readLine()) != null) {	// Name, street address
				String[] arr = line.split(",");
				String name = arr[0];
				String add = arr[1] + ", ";
				add += br.readLine();
				
				arr = br.readLine().split(",");
				double lat = Double.parseDouble(arr[0].substring(1));
				double lng = Double.parseDouble(arr[1].substring(1, arr[1].length() - 2));
				String grade = arr[arr.length-1];
				int i = arr.length - 2;
				int rating = Integer.parseInt(arr[i]);
				
				String des = "";
				for (int j = 2; j < i; j++) {
					des += arr[j] + ",";
				}
				des = des.substring(0,des.length()-1);
				
				PlaceInformation obj = new PlaceInformation(name, add, lat, lng, des, rating, grade);
				al.add(obj);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File was not found.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException.");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Failed to close BufferedReader.");
				}
			}
		}	
	}
	
	public void setAPIkey(String key) {
		API_KEY = key;
	}
	
	/**
	 * This method takes a String address in the format of postal codes and 
	 * sends a request to the Google Maps Geocoding API to receive geographical
	 * coordinates of the given address. The coordinates are then stored and 
	 * returned in a GeoLocation object.
	 * 
	 * @param address given by the user
	 * @return GeoLocation object containing the coordinates of the given address
	 * @throws Exception when the request to Google fails
	 */
	public GeoLocation requestGoogle(String address) throws Exception {
		GeoApiContext context = new GeoApiContext().setApiKey(API_KEY);
		GeocodingResult[] results =  GeocodingApi.geocode(context,
		    address).await();
		System.out.println(results[0].formattedAddress);
//		System.out.println(results[0].geometry.location);
		
		String result = results[0].geometry.location.toString();
		double lat = Double.parseDouble(result.substring(0, result.indexOf(",")));
		double lng = Double.parseDouble(result.substring(result.indexOf(",")+1));
		GeoLocation gl = new GeoLocation(lat, lng);
//		GeocodingApiRequest req = GeocodingApi.newRequest(context).address("Sydney");
//		try {
//		    req.await();
//		    // Handle successful request.
//		} catch (Exception e) {
//		    // Handle error
//		}
//		req.awaitIgnoreError(); // No checked exception.
		
		return gl;
	}
	
	/**
	 * This method takes the given address and sorts the data based on distance,
	 * with the closest restaurants at the top.
	 * 
	 * @param gl Coordinates of the given location.
	 */
	public void compareDist(GeoLocation gl) {
		lastType = "Distance";
		TreeMap<Double, PlaceInformation> tm = 
				new TreeMap<Double, PlaceInformation>(new Comparator<Double>() {
					public int compare(Double d1, Double d2) {
						return (int)(d1 - d2);
					}
				});
		for (PlaceInformation i : al) {
			Double d = gl.distanceFrom(i.getLoc());
			i.setDist(d);
			tm.put(d, i);
		}
		al = new ArrayList<PlaceInformation>(tm.values());	
		sorted = true;
	}
	
	/**
	 * This method organizes the presorted data by restaurant ratings.
	 */
	public void compareRating() {
		lastType = "Rating";
		TreeMap<Integer, PlaceInformation> tm = new TreeMap<Integer, PlaceInformation>();
//		for (PlaceInformation i : al) {
//			tm.put(i.getRating(), i);
//		}
		Collections.sort(al.subList(indexStart, indexStop), new Comparator<PlaceInformation> () {
			public int compare(PlaceInformation p1, PlaceInformation p2) {
				return p2.getRating() - p1.getRating();
			}
		});
		
		al = new ArrayList<PlaceInformation>(tm.values());	
	}
	
	/**
	 * This method organizes the presorted data by restaurant grades.
	 */
	public void compareGrade() {
		lastType = "Grade";
		TreeMap<String, PlaceInformation> tm = new TreeMap<String, PlaceInformation>();
//		for (PlaceInformation i : al) {
//			tm.put(i.getGrade(), i);
//		}
		Collections.sort(al.subList(indexStart, indexStop), new Comparator<PlaceInformation> () {
			public int compare(PlaceInformation p1, PlaceInformation p2) {
				return (p1.getGrade()).compareTo(p2.getGrade());
			}
		});
		
		al = new ArrayList<PlaceInformation>(tm.values());	
	}
	
	/**
	 * This method organizes the presorted data by name alphabetically.
	 */
	public void sortABC() {
		lastType = "Name";
		TreeMap<String, PlaceInformation> tm = new TreeMap<String, PlaceInformation>();
		Collections.sort(al.subList(indexStart, indexStop), new Comparator<PlaceInformation> () {
			public int compare(PlaceInformation p1, PlaceInformation p2) {
				return (p1.getName()).compareTo(p2.getName());
			}
		});
		
		al = new ArrayList<PlaceInformation>(tm.values());	
	}
	
	/**
	 * This method formats and prints results from the sorted data in 10's. 
	 */
	public void printFormat() {
		String header = "Results sorted by " + lastType + ":\n";
		String divider = "++++++++++++++++++++++++++++++++++++++++++++++++++\n";
		String body = "";
		
		for (PlaceInformation i : al.subList(indexStart, indexStop)) {
				body += i.toString() + "\n";
		}	
		System.out.println(header + divider + body + divider);
	}

	
	/**
	 * This method allows for pagination. Depending on the user's selection,
	 * pages can move forward by 1, 5, or to the end.
	 * 
	 * @param numPages 
	 */
	public void pageNext(int numPages) {
		if (numPages < 0) {
			indexStart = al.size() - 11;
			indexStop = al.size();
		} else {
			if (indexStop == al.size()) {
				printFormat();
				System.out.println("There are no more results.");
				return;
			}
			indexStart += 10 * numPages;
			indexStop += 10 * numPages;
			if (indexStop >= al.size()) {
				indexStop = al.size();
			}
		}
		printFormat();
	}
	
	/**
	 * This method allows for pagination. Depending on the user's selection,
	 * pages can move backwards by 1, 5, or to the beginning.
	 * 
	 * @param numPages 
	 */
	public void pageBack(int numPages) {
		if (numPages < 0) {
			indexStart = 0;
			indexStop = 10;
		} else {
			if (indexStart == 0) {
				printFormat();
				System.out.println("You have reached the beginning of the results.");
				return;
			}
			indexStart -= 10 * numPages;
			indexStop -= 10 * numPages;
			if (indexStart < 0) {
				indexStart = 0;
			}
			if (indexStop < 10) {
				indexStop = 10;
			}
		}
		printFormat();
	}

	/**
	 * This method converts the String array input to a single single String.
	 * 
	 * @param args from user input
	 * @return String address
	 */
	public String getAddress(String[] args) {
//		Removed the first element from args where necessary
		StringBuilder builder = new StringBuilder();
		for(String s : args) {
		    builder.append(s);
		}
		return builder.toString();
	}
	
	/**
	 * The main method with run the program and ask the user for an address. 
	 * Various commands may also be used to customize the results printed.
	 * 
	 * @param args user given input with commands and addresses
	 */
	public static void main(String[] args) {
		LArests larests = new LArests();
		System.out.println("This program requires a Google Maps Geocoding API Key.\n");
		System.out.println("To create a key, go to the following website. Follow the directions to GET A KEY.");
		System.out.println("Website:  https://developers.google.com/maps/documentation/geocoding/get-api-key\n");
		System.out.println("Please enter your API Key:");
		Scanner sc = new Scanner(System.in);
		larests.setAPIkey(sc.toString()); 
		larests.read();
		sc.close();
		
		System.out.println("For help and info, use the 'help' command. Otherwise,");
		System.out.println("Please enter street address:");

		switch(args[0]) {
		case "help":
			System.out.println("Available commands:");
			System.out.println("===========================================");
			System.out.println("'...' represents address field");
			System.out.println("===========================================");
			System.out.println("Sort by distance: 		-d ...");
			System.out.println("Sort by health rating:  -r ...");
			System.out.println("Sort by health grade:   -g ...");
			System.out.println("Sort by name (ABC):     -a ...");
			System.out.println("[DEFAULT] sort by dist:    ...");
			System.out.println("===========================================");
			System.out.println("Pagination options after a completed sort:");
			System.out.println("===========================================");
			System.out.println("Next page of results:    >  ");
			System.out.println("Skip forward 5 pages:    >> ");
			System.out.println("Skip to last page:       >>>");
			System.out.println("Last page of results:    <  ");
			System.out.println("Return back 5 pages:     << ");
			System.out.println("Return to first page:    <<<");
			System.out.println("[DEFAULT] returns next page of results");
			System.out.println("===========================================");		
			break;
		case "quit":
			return;
		case "-d":			// Sort by distance
			try {
				String address = larests.getAddress(Arrays.copyOfRange(args, 1, args.length));
				larests.compareDist(larests.requestGoogle(address));
				larests.printFormat();
			} catch (Exception e) {
				System.out.println("Failed to get address coordinates.");
				System.out.println("Please reenter postal code address:");
				e.printStackTrace();
			}
			break;
		case "-r":			// Sort by number rating
			try {
				String address = larests.getAddress(Arrays.copyOfRange(args, 1, args.length));
				larests.compareDist(larests.requestGoogle(address));
				larests.compareRating();
				larests.printFormat();
			} catch (Exception e) {
				System.out.println("Failed to get address coordinates.");
				System.out.println("Please reenter postal code address:");
				e.printStackTrace();
			}
			break;
		case "-g":			// Sort by letter grade
			try {
				String address = larests.getAddress(Arrays.copyOfRange(args, 1, args.length));
				larests.compareDist(larests.requestGoogle(address));
				larests.compareGrade();
				larests.printFormat();
			} catch (Exception e) {
				System.out.println("Failed to get address coordinates.");
				System.out.println("Please reenter postal code address:");
				e.printStackTrace();
			}
			break;
		case "-a":			// Sort alphabetically
			try {
				String address = larests.getAddress(Arrays.copyOfRange(args, 1, args.length));
				larests.compareDist(larests.requestGoogle(address));
				larests.sortABC();
				larests.printFormat();
			} catch (Exception e) {
				System.out.println("Failed to get address coordinates.");
				System.out.println("Please reenter postal code address:");
				e.printStackTrace();
			}
			break;
		case "<":			// Move back to last page of results
			if (larests.isSorted()) {
				larests.pageBack(1);
			} else {
				System.out.println("Please enter a postal code address:");
			}
			break;
		case "<<":			// Jump back 5 pages of results
			if (larests.isSorted()) {
				larests.pageBack(5);
			} else {
				System.out.println("Please enter a postal code address:");
			}
			break;
		case "<<<":			// Return to first page of results
			if (larests.isSorted()) {
				larests.pageBack(-1);
			} else {
				System.out.println("Please enter a postal code address:");
			}
			break;
		case ">":			// Move to next page of results
			if (larests.isSorted()) {
				larests.pageNext(1);
			} else {
				System.out.println("Please enter a postal code address:");
			}
			break;
		case ">>":			// Skip forward 5 pages of results
			if (larests.isSorted()) {
				larests.pageNext(5);
			} else {
				System.out.println("Please enter a postal code address:");
			}
			break;
		case ">>>":			// Jump to final page of results
			if (larests.isSorted()) {
				larests.pageNext(-1);
			} else {
				System.out.println("Please enter a postal code address:");
			}
			break;
		default:
			if (larests.isSorted()) {	// Pagination
				larests.pageNext(1);
			} else if (args.length == 0) {			
				System.out.println("Please enter postal code address;");
			} else {					// Sort by distance
				try {
					String address = larests.getAddress(args);
					larests.compareDist(larests.requestGoogle(address));
					larests.printFormat();
				} catch (Exception e) {
					System.out.println("Failed to get address coordinates.");
					System.out.println("Please reenter postal code address:");
					e.printStackTrace();
				}
			}
			break;
		}
		
	}

}
