import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Collections;
/**
 * Authors:
 * Darius Lakas dtlakas@mtu.edu
 * Justin Schmid jschmid@mtu.edu
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
        HashMap<String, ArrayList<TrackInfo>> userTrackMap =
                data.getUserTrackMap();
        ArrayList<TrackInfo> user1TrackInfo = userTrackMap.get(user1);
        ArrayList<TrackInfo> user2TrackInfo = userTrackMap.get(user2);
        Set<String> user1Stuff = new LinkedHashSet<>();
        Set<String> user2Stuff = new LinkedHashSet<>();
        Set<String> dimensions = new LinkedHashSet<>();
        ArrayList<Double> user1Vector = new ArrayList<>();
        ArrayList<Double> user2Vector = new ArrayList<>();
        ArrayList<String> dimensionsList;


        if (fieldName.equalsIgnoreCase("GENRE")) {
            for (TrackInfo info : user1TrackInfo) {
                user1Stuff.add(info.getGenre());
            }
            for (TrackInfo info : user2TrackInfo) {
                user2Stuff.add(info.getGenre());
            }
        } else if (fieldName.equalsIgnoreCase("ARTIST")) {
            for (TrackInfo info : user1TrackInfo) {
                user1Stuff.add(info.getArtist());
            }
            for (TrackInfo info : user2TrackInfo) {
                user2Stuff.add(info.getArtist());
            }
        } else if (fieldName.equalsIgnoreCase("TITLE")) {
            for (TrackInfo info : user1TrackInfo) {
                user1Stuff.add(info.getTitle());
            }
            for (TrackInfo info : user2TrackInfo) {
                user2Stuff.add(info.getTitle());
            }
        }

        dimensions.addAll(user1Stuff);
        dimensions.addAll(user2Stuff);

        for (int i = 0; i < dimensions.size(); i++) {
            user1Vector.add(0.0);
            user2Vector.add(0.0);
        }

        dimensionsList = new ArrayList<>(dimensions);
        for (int i = 0; i < dimensionsList.size(); i++) {
            String item = dimensionsList.get(i);

            int count1 = 0, count2 = 0;
            for (TrackInfo info : user1TrackInfo) {
                String field = getField(info, fieldName);
                if (field.equals(item)) count1++;
            }
            for (TrackInfo info : user2TrackInfo) {
                String field = getField(info, fieldName);
                if (field.equals(item)) count2++;
            }

            if (count1 > 0 && count2 > 0) {
                double boost = 1.0;
                if (fieldName.equalsIgnoreCase("TITLE")) {
                    boost = 30.0;
                }
                user1Vector.set(i, count1 * 3.0 * boost);
                user2Vector.set(i, count2 * 3.0 * boost);
            } else if (count1 > 0) {
                user1Vector.set(i, (double) count1);
                user2Vector.set(i, -0.5);
            } else if (count2 > 0) {
                user1Vector.set(i, -0.5);
                user2Vector.set(i, (double) count2);
            }
        }

        double mag1 = 0.0;
        double mag2 = 0.0;
        for (int i = 0; i < user1Vector.size(); i++) {
            mag1 += Math.pow(user1Vector.get(i), 2);
            mag2 += Math.pow(user2Vector.get(i), 2);
        }
        mag1 = Math.sqrt(mag1);
        mag2 = Math.sqrt(mag2);

        for (int i = 0; i < user1Vector.size(); i++) {
            if (mag1 > 0) user1Vector.set(i, user1Vector.get(i) / mag1);
            if (mag2 > 0) user2Vector.set(i, user2Vector.get(i) / mag2);
        }

        double weight = 1.0;
        if (fieldName.equalsIgnoreCase("TITLE")) weight = 6.7;
        else if (fieldName.equalsIgnoreCase("ARTIST")) weight = 6.0;
        else if (fieldName.equalsIgnoreCase("GENRE")) weight = 7.0;

        for (int i = 0; i < user1Vector.size(); i++) user1Vector.set(i,
                user1Vector.get(i) * weight);
        for (int i = 0; i < user2Vector.size(); i++) user2Vector.set(i,
                user2Vector.get(i) * weight);

        if (method.equalsIgnoreCase("EUCLIDEAN"))
            return euclideanDistance(user1Vector, user2Vector);
        return pearsonDistance(user1Vector, user2Vector);
    }

    /** This helper method uses the get methods from TrackInfo to return the
     * genre, artist, or title specified by the parameter fieldName.
     *
     * @param info A TrackInfo representing the information from the data
     * @param fieldName The field to base the similarity off of
     * @return A String representing either the info's genre, artist, or title.
     */
    private String getField(TrackInfo info, String fieldName) {
        if (fieldName.equalsIgnoreCase("GENRE")) return info.getGenre();
        if (fieldName.equalsIgnoreCase("ARTIST")) return info.getArtist();
        return info.getTitle();
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
        HashMap<String, ArrayList<TrackInfo>> userTrackMap =
                data.getUserTrackMap();
        HashMap<String, Double> similarities = new HashMap<>();

        for (String id : userTrackMap.keySet()) {
            if (id.equalsIgnoreCase(user)) {
                continue;
            }
            double currSimilarity = calculateSimilarity(user, id, fieldName,
                    method);

            similarities.put(id, currSimilarity);
        }
        return similarities;
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
        System.out.println(tr.calculateAllSimilarity("RYzSjSc0fl",
                "GENRE", "EUCLIDEAN"));
    }// d6b4nyADX1, RYzSjSc0fl, 80ocu1VbdY
}
