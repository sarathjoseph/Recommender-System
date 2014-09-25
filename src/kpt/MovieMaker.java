package kpt;

import java.io.Serializable;
import java.util.ArrayList;

class MovieMaker implements Serializable{


	private static final long serialVersionUID = 524419885591113784L;
	private ArrayList<Movie> movies;
	private ArrayList<String> reviews;
	private ArrayList<Integer> movielist;
	private ArrayList<String> imdb_id_list;

	public ArrayList<Movie> getMovies() {

		return movies;
	}

	public Movie getMovieById(int id) {

		int index = movielist.indexOf(id);

		if (index > -1)
			return movies.get(index);
		else
			return null;
	}

	public String sanitizeImdbId(String id) {

		String imdb_id = "";

		// To handle wrong Imdb Ids in tags.dat

		if (id.length() == 7) {
			imdb_id = "tt" + id;

		} else if (id.length() == 6) {
			imdb_id = "tt0" + id;

		} else if (id.length() == 5) {
			imdb_id = "tt00" + id;

		} else if (id.length() == 4) {
			imdb_id = "tt000" + id;

		} else if (id.length() == 3) {
			imdb_id = "tt0000" + id;

		} else if (id.length() == 2) {
			imdb_id = "tt00000" + id;

		} else if (id.length() == 1) {
			imdb_id = "tt000000" + id;

		}
		return "\""+imdb_id+"\"";
	}

	public MovieMaker(ArrayList<String> s) {
		movies = new ArrayList<Movie>();
		movielist = new ArrayList<Integer>();
		imdb_id_list= new ArrayList<String>();
		Movie movie;
		reviews = s;

		for (String review : reviews) {

			String[] component = review.split("::");
			
			if(component.length<4)
			continue;
			
			int movie_id = Integer.parseInt(component[0]);
			String tag = component[1];
		
			String imdb_id = sanitizeImdbId(component[3]);

			if (!movielist.contains(movie_id)) {

				if (!imdb_id_list.contains(imdb_id)) {

					movie = new Movie(movie_id, tag);
					movie.setImdb_id(imdb_id);
					movies.add(movie);
					movielist.add(movie_id);
					imdb_id_list.add(imdb_id);
				}

				else {

					int index = imdb_id_list.indexOf(imdb_id);
					movie = movies.get(index);
					movie.addTag(tag);

				}

			} else {

				int index = movielist.indexOf(movie_id);
				movie = movies.get(index);
				movie.addTag(tag);
				// movies.set(index, movie);
			}

		}

	}

	public void removeMovie(int id){
		
		int index=movielist.indexOf(id);
		movies.remove(index);
		movielist.remove(index);
		imdb_id_list.remove(index);
		
		
	}
	public String toString() {

		StringBuilder moviestring = new StringBuilder();

		for (Movie movie : movies)
			moviestring.append(movie.getId() + " " + movie.getTags() + "\n");

		return moviestring.toString();
	}

}
