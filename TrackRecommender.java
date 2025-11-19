public class TrackRecommender {

    /**
     * Utility that reads the data for the track recommender.
     */
    private ReadData data = null;

    public TrackRecommender(String filename) {
	//Initialize ReadData
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
    public int calculateSimilarity(String user1, String, user2,
				   String fieldName, String method) {
	return 0;
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
     * @param numberoftracks The total number of tracks
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
	TrackRecommender rec = new TrackRecommender(args[0]);
	//TEST YOUR CODE HERE
    }
}
