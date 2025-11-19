/**
 * Authors:
 * Darius Lakas dtlakas@mtu.edu
*/
import java.util.ArrayList;
import java.util.HashMap;

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
     * @param plays  The number of times the user plaid the track
     */
    public void addTrack(HashMap<String, ArrayList<TrackInfo>> map, String user,
			 int rank, String title, String artist, String album,
			 String genre, int plays) {
		ArrayList<TrackInfo> elements = map.get(user);
		TrackInfo element = new TrackInfo(user, rank, title, artist, album, genre, plays);
    }

    /**
     * Given a filename read in all tracks from the file into the ~userTrackMap~
     * HashMap. Use the ~addTrack~ method above.
     *
     * @param filename The file name to read data from.
     */
    public void readInput(String filename) {

    }

    /**
     * Generate a list of all artists associated with a given genre in ~userTrackMap~
     *
     * @param genre The genre to generate a list for
     */
    public ArrayList<String> listGenreArtists(String genre) {
	return null;
    }

    public static void main(String[] args) {
	ReadData rd = new ReadData();
	//Do your testing here!
    }
}
