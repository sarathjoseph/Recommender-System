package kpt;

import java.io.*;
import java.util.ArrayList;

public class Recommender {
	MovieMaker moviemaker;
	MovieVsm movievsm;
	int Number;

	public Recommender(int n) {

		try {
			this.Number = n;
			String data = null;
			ArrayList<String> tagdata = new ArrayList<String>();
			ArrayList<String> moviedata = new ArrayList<String>();

			String file1 = "tags.dat";
			String file2 = "stoptags";
			String file3 = "keytags";
			String file4 = "movies_omdb.dat";

			BufferedReader br = new BufferedReader(new FileReader(file1));
			BufferedReader b = new BufferedReader(new FileReader(file4));

			while ((data = br.readLine()) != null)
				tagdata.add(data);

			while ((data = b.readLine()) != null)
				moviedata.add(data);

			br.close();
			b.close();

			moviemaker = new MovieMaker(tagdata);

			for (String movierow : moviedata.subList(1, moviedata.size())) {

				String attributes[] = movierow.split("::");
				int id = Integer.parseInt(attributes[20]);
				Movie movie = moviemaker.getMovieById(id);
				if (movie != null) {

					MovieMetadata.updateMeta(movie, attributes);
				}
			}

			// Remove these movies because they don't have Metadata
			moviemaker.removeMovie(4973);
			moviemaker.removeMovie(25940);

			File file = new File("/var/www/612/movies.js");

			if (!file.exists()) {
				file.createNewFile();
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(
					file.getAbsoluteFile()));
			writer.write("movies=" + moviemaker.getMovies().toString());
			writer.flush();
			writer.close();

			movievsm = new MovieVsm(moviemaker, file2, file3);
		

			FileOutputStream fileOut = new FileOutputStream("movievsm.obj");
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(movievsm);
			outStream.close();
			fileOut.close();

		} catch (FileNotFoundException e) {

			System.out.println(e.getLocalizedMessage());

		}

		catch (IOException e) {

			e.printStackTrace();
		}

	}

	public ArrayList<Movie> getRecommendationById(int id) {

		ArrayList<Movie> recommendation;
		Movie movie = null;
		if (moviemaker != null)
			movie = moviemaker.getMovieById(id);
		if (movie != null) {
			recommendation = movievsm.rankedSearchById(movie, Number);
			return recommendation;
		} else
			return null;
	}

	public ArrayList<Movie> getRecommendationByTags(String tags) {

		ArrayList<Movie> recommendation;

		String search[] = { tags };
		recommendation = movievsm.rankedSearchByTags(search, Number);
		return recommendation;

	}

}