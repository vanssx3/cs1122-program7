# In your groups, discuss the data gather by the survey to accomplish the following:

Agree upon the validity of the data for use at generating recommendations and make changes to assure validity. This might include the following.
Discussing the removal or amendment of particular entries for misspellings or nonsensical inputs.
Organizing, understanding, and documenting the data. This might include creating graphs or data visualizations.
Highlighting possible short-comings or flaws in data collection or data presentation that will limit recommendation quality.
You will need to agree on a minimal set of genres.
Count the number of entries for each genre.
Check to see if you agree that each reported song is in that genre.
Check if any genres can be merged; e.g., should "comedy" and "pardoy" be merged.
It could also help to standardize to all uppercase or all lowercase strings for these genres.
As part of your reflection document for this program, include the following. Please respond to each with a paragraph response.

If you removed or altered any entries in the dataset, please give a detailed description as to why, include the data that was removed. Discuss what implications might exist from removing or changing it and how you dealt with those implications in your project.
Detail your chosen list of genres. Discuss how this list was agreed upon and what implications it might have on your recommendation system. What genres might be missing from your list? Why are they missing?
Include at least two tables or graphs that examine a particular aspect of the dataset. For example, a bar graph of included genres, table of popular artists, or list of the most played songs. You should explain your choice of graph.
What short-comings or flaws do you see in the way in which we collected data. If you had to collect data like this again, what would you change, and why.
Part 3: Data Representation and Reading
You are required to create a utility that reads the data from the dataset source file into a collection of java classes called TrackInfo. The data is stored in a file using the CSV file format. This file format stands for comma separate values, in that each line of the file represents a single entry in the dataset and each value in the entry is separated by a comma character. The data file might look something like the following.

``USER,       RANK, TITLE, ARTIST,       ALBUM, GENRE, PLAYS ``

``X630960520, 1,    Clean, Taylor Swift, 1989,  POP,   34 ``

``X630960520, 2,    Style, Taylor Swift, 1989,  POP,   40 ``

The first line is merely for human readability. It indicates the order of the fields for the rest of the file. In this part you will be writing a handful of method inside the ReadData class.

# Problem 1: addTrack
Create a method that adds track information to a Java Collections HashMapLinks to an external site. that associates each user with a list of music tracks.

We want to store the track information in a HashMap. A HashMap is a data structure that associates keys and values. In this case, the key is the user argument (a String) passed to the addTrack method. The value is an ArrayList of that user's favorite music tracks. That's important. We are given all the information for a single track, but the value in the HashMap is an ArrayList. The task here is to retrieve the ArrayList from the HashMap and add to it a new instance of the TrackInfo class (created using the arguments supplied to the addTrack method).

The following method signature should be used:

``void addTrack(HashMap<String, ArrayList<TrackInfo>> map, String user, int rank, String title, String artist, String album, String genre, int plays)``
          
# Problem 2: readInput
Create a method that reads track information from a supplied file.

The filename will be supplied to the readInput method. You must open the file and read its contents storing them in the instance level HashMap, userTrackMap, using the above addTrack method. You must read all the data in the file.

The following method signature should be used:

``void readInput(String filename)``

# Problem 3: listGenreArtists
Write a method that returns a list of all artists associated with a given genre.

The list returned should contain no duplicates. To do this, you will need to look at all the data for all users in the instance level HashMap.

``ArrayList<String> listGenreArtists(String genre)``

Testing
It would be a good idea to write a few tests in the main method of ReadData to make sure you are getting results that make sense on this part before moving on. The next part will rely on the correctness of this part.

Work out a handful of answers to each problem by hand. Test against these known values.
Try to think about possible edge cases that could break your code. Test these inputs as well.

# Part 4: Recommendation System
This part will consist of you creating a music recommendation system that uses similarities between users to generate playlists and music recommendations. This will be implemented in a Java class called TrackRecommender.

Finding Similar Users
As part of the provided code we have given two ways of determining similar users. The rest of this program will rely heavily on similarity scores. A similarity score is a double value in a range of [0, 1] and represents the likelihood that two people share the same tastes. We have proved two algorithms for calculating similarity scores: Euclidean Distance and Pearson Correlation.

Euclidean Distance

You have probably solved for Euclidean Distance between two points using the Pythagorean Theorem. Here, we generalize to n dimensions. This means that we are using the same idea as Euclidean Distance in 2 or 3 dimensions and generalizing to any number of dimensions.

To demonstrate this formula, we will show a simple one dimension example. The euclidean distance between two points, p and q can be represented as the following equation. p and q are points on the same line for the purposes of this one dimensional example.

 

We can then generalize this formula to two dimensions by breaking the component parts of the points (i.e., they have an x and y component now). The distance between two 2 dimensional points p and q can be represented as follows.

 

Lastly, to illustrate how the pattern continues to higher numbers, consider a three dimensional example. The distance between two 3 dimensional points p and q can be represented as follows.

 

If we consider the aspects of a user's track list as an array list of double values (more on this later) we can create a method to calculate the distance between two users, by taking the inverse of 1 + the distance.

``public double euclideanDistance(ArrayList<Double> array1, ArrayList<Double> array2) { 
          double sum = 0.0; 
          for (int i = 0; i < array1.size(); i++) { 
          sum += Math.pow((array1.get(i) - array2.get(i)), 2.0);
    } 
    return 1.0 / (1.0 + Math.sqrt(sum));
}``

# Pearson Correlation

The Pearson Correlation Coefficient provides a measure of how well two sets of data fit on one line. Pearson's is a bit harder to understanding without a strong background in statistics, but can be thought of as how close a dataset of points are to being on the same line (i.e., how correlated two sets of data are). As with the Euclidean distance, we need to manipulate the resulting value to get it into the range [0, 1].

``public double pearsonDistance(ArrayList<Double> array1, ArrayList<Double> array2) {
          double mean1 = 0.0, mean2 = 0.0;
          for (int i = 0; i < array1.size(); i++) {
          mean1 += array1.get(i);
          mean2 += array2.get(i);
} 
          mean1 /= array1.size();
          mean2 /= array2.size();
          double sumXY = 0.0, sumX2 = 0.0, sumY2 = 0.0;
          for (int i = 0; i < array1.size(); i++) {
          sumXY += (array1.get(i) - mean1) * (array2.get(i) - mean2);
          sumX2 += Math.pow(array1.get(i) - mean1, 2.0);
          sumY2 += Math.pow(array2.get(i) - mean2, 2.0);
    }
    return (1.0 + (sumXY / (Math.sqrt(sumX2) * Math.sqrt(sumY2)))) / 2.0;
}``

# Problem 0: Initialize Data
Make a constructor to initialize the data members using your answer to Part 3.

# Problem 1: calculateSimilarity
We want to calculate the similarity between two users with respect to one of the data fields, e.g. TITLE, ARTIST, or GENRE. To use the above distance methods, we need to construct an ArrayList representing a vector of the user's tastes with respect to the chosen field. One way to do this is to count how many times each possible field value appears in the user's track list.

Your task is to calculate the similarity between two users with respect to a data field and a specified distance method. You should use the following method signature.

``double calcualteSimilarity(String user1, String user2, String fieldName, String method)``
Here fieldName is one of "TITLE", "ARTIST", or "GENERE" and method is one of "EUCLIDEAN" or "PEARSON".

# Problem 2: calculateAllSimilarity
Write a method that calculates the similarity between one user (source user) and all other users (target users) and return the data scores in a HashMap that associates the target user with the resulting similarity score. Use the following method header.

``HashMap<String, Double> calcualteAllSimilarity(String user, String fieldName, String method)``
fieldName and method should be the exact same as calculateSimilarity.

# Problem 3: makePlaylist
Having calculated the similarity with all other users, make a playlist of tracks from the most similar users. Start with the user with the highest similarity score, adding their tracks, and continuing to the user with the next highest similarity score, and so on. Stop when either the specified number of tracks has been added or there are no more users available. Use the following method header.
