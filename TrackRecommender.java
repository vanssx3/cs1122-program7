import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Set;
import java.util.Collections;
/**
 * Authors:
 * Darius Lakas dtlakas@mtu.edu
 */
public class TrackRecommender {

    /**
     * Utility that reads the data for the track recommender.
     */
    private ReadData data = null;

    public TrackRecommender(String filename) {
        data = new ReadData();
        data.readInput(filename);
    }

    /**
     * Calculates the similarity score between two users based on a field
     * and method.
     *
     * @param user1     The first user
     * @param user2     The second user
     * @param fieldName The field to base the similarity off of
     * @param method    The method to calculate similarity
     */
    public double calculateSimilarity(String user1, String user2,
                                      String fieldName, String method) {
        ArrayList<Double> user1Taste = new ArrayList<>();
        ArrayList<Double> user2Taste = new ArrayList<>();
        ArrayList<String> user1Field;
        ArrayList<String> user2Field;
        Set<String> user1UniqueStuff = new TreeSet<>();
        Set<String> user2UniqueStuff = new TreeSet<>();

        HashMap<String, ArrayList<TrackInfo>> userTrackMap =
                data.getUserTrackMap();

        if (fieldName.equalsIgnoreCase("GENRE")) {
            ArrayList<TrackInfo> user1TrackInfo = userTrackMap.get(user1);
            ArrayList<TrackInfo> user2TrackInfo = userTrackMap.get(user2);

            for (int i = 0; i < user1TrackInfo.size(); i++) {
                user1UniqueStuff.add(user1TrackInfo.get(i).getGenre());
            }

            for (int i = 0; i < user2TrackInfo.size(); i++) {
                user2UniqueStuff.add(user2TrackInfo.get(i).getGenre());
            }




            user1Field = new ArrayList<>(user1UniqueStuff);
            user2Field = new ArrayList<>(user2UniqueStuff);
            for(int i = 0; i < user1Field.size(); i++) {
                user1Taste.add(i, 0.0);
                for(int j = 0; j < user1TrackInfo.size(); j++) {
                    if (user1Field.get(i).equalsIgnoreCase(user1TrackInfo.get(j)
                            .getGenre())) {
                        user1Taste.set(i, user1Taste.get(i) + 1);
                    }
                }
            }


            for(int i = 0; i < user2Field.size(); i++) {
                user2Taste.add(i, 0.0);
                for(int j = 0; j < user2TrackInfo.size(); j++) {
                    if (user2Field.get(i).equalsIgnoreCase(user2TrackInfo.get(j)
                            .getGenre())) {
                        user2Taste.set(i, user2Taste.get(i) + 1);
                    }
                }
            }

            while (user1Taste.size() < user2Taste.size()) {
                user1Taste.add(0.0);
            }
            while (user1Taste.size() > user2Taste.size()) {
                user2Taste.add(0.0);
            }
            System.out.println(user1Taste.toString());
            System.out.println(user2Taste.toString());

            if (method == "EUCLIDEAN") {
                return euclideanDistance(user1Taste, user2Taste);
            }
            return pearsonDistance(user1Taste, user2Taste);

        }
        return -1;
    }

    /**
     * Calculates all similarity scores between one user and all other users
     * based on a field and method.
     *
     * @param user      The user
     * @param fieldName The field to base the similarity off of
     * @param method    The method to calculate similarity
     */
    public HashMap<String, Double> calculateAllSimilarity(String user,
                                                          String fieldName,
                                                          String method) {
        return null;
    }

    /**
     * Generates a playlist based on a user of a specified number of tracks.
     *
     * @param user           The user
     * @param fieldName      The field to base the similarity off of
     * @param method         The method to calculate similarity
     * @param numberOfTracks The total number of tracks
     */
    public ArrayList<TrackInfo> makePlaylist(String user, String fieldName,
                                             String method,
                                             int numberOfTracks) {
        return null;
    }

    /**
     * Calculates the likelihood that two users share the same music tastes
     * using the Euclidean Distance algorithm.
     * @param array1
     * @param array2
     * @return a similarity score
     */
    public double euclideanDistance( ArrayList< Double > array1,
                                     ArrayList< Double > array2 ) {
        double sum = 0.0;
        for( int i = 0; i < array1.size(); i++ ) {
            sum += Math.pow((array1.get(i) - array2.get(i)), 2.0);
        }
        return 1.0 / (1.0 + Math.sqrt(sum));
    }

    /**
     * Calculates the likelihood that two users share the same music tastes
     * using the Pearson Distance algorithm.
     * @param array1
     * @param array2
     * @return a similarity score
     */
    public double pearsonDistance( ArrayList< Double > array1,
                                   ArrayList< Double > array2 ) {
        double mean1 = 0.0, mean2 = 0.0;
        for( int i = 0; i < array1.size(); i++ ) {
            mean1 += array1.get(i);
            mean2 += array2.get(i);
        }
        mean1 /= array1.size();
        mean2 /= array2.size();
        double sumXY = 0.0, sumX2 = 0.0, sumY2 = 0.0;
        for( int i = 0; i < array1.size(); i++ ) {
            sumXY += ((array1.get(i) - mean1) * (array2.get(i) - mean2));
            sumX2 += Math.pow(array1.get(i) - mean1, 2.0);
            sumY2 += Math.pow(array2.get(i) - mean2, 2.0);
        }
        return (1.0 + (sumXY / (Math.sqrt(sumX2) * Math.sqrt(sumY2)))) / 2.0;
    }

    public static void main(String[] args) {
        //TrackRecommender rec = new TrackRecommender(args[0]);
        //TEST YOUR CODE HERE
        TrackRecommender tr = new TrackRecommender("cs1122-2025.csv");
        System.out.println(tr.calculateSimilarity("RYzSjSc0fl",
                "dBv2mfmjnH", "GENRE", "PEARSON" ));
    }
}
