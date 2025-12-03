/**
 * Authors:
 * Darius Lakas dtlakas@mtu.edu
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Iterator;
import java.io.FileNotFoundException;

public class ReadData {

    private HashMap<String, ArrayList<TrackInfo>> userTrackMap;

    public HashMap<String, ArrayList<TrackInfo>> getUserTrackMap() {
	return userTrackMap;
    }

    public void setUserTrackMap(HashMap<String, ArrayList<TrackInfo>> value) {
	this.userTrackMap = value;
    }

    /**
     * This method adds a track to a Java Collections HashMap that associates
     * users to a list of tracks.
     *
     * @param map    A HashMap to insert a new track into
     * @param user   A string user name
     * @param rank   The rank of the track for this user
     * @param title  The title of the track
     * @param artist The artist's name who made the track
     * @param album  The album number for the track
     * @param genre  The genre of the track
     * @param plays  The number of times the user played the track
     *
     * This one is rather simple, it just adds the track to the ArrayList
     * corresponding to the user's tracks.
     * If the track is already present, the function will do nothing.
     */
    public void addTrack(HashMap<String, ArrayList<TrackInfo>> map, String user,
			 int rank, String title, String artist, String album,
			 String genre, int plays) {
	ArrayList<TrackInfo> elements = map.get(user);
	TrackInfo element = new TrackInfo(user, title, artist, album, genre, rank, plays);
	System.out.println("Adding track...");
	if (elements == null) {
		elements = new ArrayList<TrackInfo>();
		System.out.println("Created new TrackInfo ArrayList");
		map.put(user, elements);
	}
	if (!elements.contains(element)) {
		elements.add(element);
		System.out.println("Successfully added track.");
	}
	else {
		System.out.println("Track already found.");
	}
    }

    /**
     * Given a filename read in all tracks from the file into the ~userTrackMap~
     * HashMap. Use the ~addTrack~ method above.
     *
     * @param filename The file name to read data from.
     * Note:
     * CSV stands for comma-separated values. This means everything
     * is delimited by commas. Order is as follows:
     * 1: Username, 2: Rank, 3: Title, 4: Artist, 5: Genre, 6: Album, 7: # Plays
     * This means each track has 7 corresponding entries before the next.
     */
    public void readInput(String filename) {
	    //I suppose there is a way to use streams but we just learned that
	    File inputfile = new File(filename);
	    try(Scanner scan = new Scanner(inputfile)) {
		    scan.useDelimiter(",|\\R"); //use a comma or a line end as delimiter
			    String user, title, artist, album, genre;
		    int rank, plays;
		    //if the data is properly formatted, this shouldn't error out.
		    for (int i = 0; i < 7 && scan.hasNext(); i++) { //skipping the first 7 items (the headers)
			    System.out.println("Discarding: " + scan.next());
		    }
		    while (scan.hasNext()) {
			    user = scan.next(); System.out.println("User: " + user);
			    rank = Integer.parseInt(scan.next()); //this is important because
								  //we don't want to skip ahead
			    System.out.println("Rank: " + rank);
			    title = scan.next(); System.out.println("Title: " + title);
			    artist = scan.next(); System.out.println("Artist: " + artist);
			    genre = scan.next(); System.out.println("Genre: " + genre);
			    album = scan.next(); System.out.println("Album: " + album);
			    plays = Integer.parseInt(scan.next()); System.out.println("Plays: " + plays);
			    addTrack(userTrackMap, user, rank, title, artist, album, genre, plays);
		    }
		    scan.close();
	    }
	    catch (FileNotFoundException e) {System.out.println("Error attempting to read file");}
    }

    /**
     * Generate a list of all artists associated with a given genre in ~userTrackMap~
     *
     * @param genre The genre to generate a list for
     * Using sets for structural deduplication
     */
    public ArrayList<String> listGenreArtists(String genre) {
	//disregarding users, retrieving all ArrayLists
	//note that the HashSet is backed by the HashMap so don't modify that
	//this is a non-destructive method
	HashSet<ArrayList<TrackInfo>> elements = new HashSet<ArrayList<TrackInfo>>();
	System.out.println("Values: " + userTrackMap.values());
	elements.addAll(userTrackMap.values());
	//the data is now iterable
	Iterator<ArrayList<TrackInfo>> elemIter = elements.iterator();
	HashSet<String> genreArtists = new HashSet<String>();
	while (elemIter.hasNext()) { //iterating through all ArrayList<TrackInfo>
		ArrayList<TrackInfo> arr = elemIter.next();
		Iterator<TrackInfo> arrIter = arr.iterator();
		while (arrIter.hasNext()) { //iterating through all TrackInfo
			TrackInfo arrItem = arrIter.next();
			System.out.println("Iterating through TrackInfo" + arrItem);
			if (arrItem.getGenre().equalsIgnoreCase(genre)) {
				genreArtists.add(arrItem.getArtist());
				System.out.println("Found Artist" + arrItem.getArtist());
			}
			else {
				System.out.println("Genre " + arrItem.getGenre() + " does not match " + genre);
			}
		}
	}
	//this should work because HashSet is a collection
	return (new ArrayList<String>(genreArtists));
    }

    public ReadData() {
	userTrackMap = new HashMap<String, ArrayList<TrackInfo>>();
    }

    public String toString() {
	System.out.println(userTrackMap);
	return null;
    }


    public static void main(String[] args) {
	ReadData rd = new ReadData();
	rd.readInput("cs1122-2025.csv");
	rd.toString();
	System.out.println("Metal Artists: " + rd.listGenreArtists("Metal"));
	System.out.println("Death Metal Artists: " + rd.listGenreArtists("Death Metal"));
	System.out.println("Rock Artists: " + rd.listGenreArtists("Rock"));
    }
}
