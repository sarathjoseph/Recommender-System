package kpt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class MovieVsm implements Serializable{


	private static final long serialVersionUID = -6034082021169038415L;
	private ArrayList<Movie> movies;
	private ArrayList<String> taglist;
	private ArrayList<ArrayList<Movie>> movielists;
	private ArrayList<String> stoptags;
	private ArrayList<String> keytags;
	private HashMap<Integer, Double> len_norm;
	private int N;
	MovieMaker moviemaker;

	public ArrayList<ArrayList<Movie>>getMovielists(){
		
		return movielists;
		
		
	}
	
	public ArrayList<String> getTagList(){
		
		
		return taglist;
	}
	public MovieVsm(MovieMaker maker, String stoptaglistfile,
			String keytaglistfile) {

		moviemaker = maker;
		movies = maker.getMovies();
		taglist = new ArrayList<String>();
		movielists = new ArrayList<ArrayList<Movie>>();
		stoptags = new ArrayList<String>();
		keytags = new ArrayList<String>();

		try {
			String stoptag = "";
			String keytag = "";
			BufferedReader br = new BufferedReader(new FileReader(
					stoptaglistfile));

			while ((stoptag = br.readLine()) != null)
				stoptags.add(stoptag.toLowerCase());

			Collections.sort(stoptags);

			BufferedReader b = new BufferedReader(
					new FileReader(keytaglistfile));

			while ((keytag = b.readLine()) != null)
				keytags.add(stem(keytag));

			br.close();
			b.close();

		} catch (FileNotFoundException e) {

			System.out.println(e.getLocalizedMessage());
		}

		catch (IOException e) {

			e.printStackTrace();
		}

		for (int i = 0; i < movies.size(); i++) {
			int id = movies.get(i).getId();
			ArrayList<String> tags = movies.get(i).getTags();
			String tag;

			for (int j = 0; j < tags.size(); j++) {

				tag = tags.get(j).toLowerCase();
				if (searchTag(tag, "stoptags") != -1)
					continue;

				String[] tagline = tag.split(" ");

				if (tagline.length > 1) {

					for (String t : tagline) {
						t = stem(t);
						if (searchTag(t, "keytags") != -1)
							processTag(t, id);

					}

				}
				tag = stem(tag);

				processTag(tag, id);

			}

		}

		ArrayList<Movie> movielist;
		len_norm = new HashMap<Integer, Double>();

		N = movies.size();
		for (int i = 0; i < taglist.size(); i++) {

			movielist = movielists.get(i);
			int df = movielist.size();
			Movie movie;

			for (int j = 0; j < movielist.size(); j++) {
				movie = movielist.get(j);
				double tfidf = (1 + Math.log(movie.mw))
						* Math.log(N / (df * 1.0));

				movie.mw = tfidf;
				movielist.set(j, movie);

				if (!len_norm.containsKey(movie.getId())) {

					len_norm.put(movie.getId(), tfidf * tfidf);
				} else {
					len_norm.put(movie.getId(), len_norm.get(movie.getId())
							+ tfidf * tfidf);
				}
			}

		}

		// compute values to use for normalization

		for (@SuppressWarnings("rawtypes")
		Map.Entry entry : len_norm.entrySet()) {

			int movid = (Integer) entry.getKey();
			double score = (Double) (entry.getValue());

			len_norm.put(movid, Math.sqrt(score));

		}
		
		for (int i = 0; i < taglist.size(); i++) {

			movielist = movielists.get(i);
			Movie movie;

			for (int j = 0; j < movielist.size(); j++) {
				movie = movielist.get(j);
				movie.mw=movie.mw/len_norm.get(movie.getId());
				
			}
			
		}

	}

	public void processTag(String tag, int id) {

		ArrayList<Movie> movielist;
		if (!taglist.contains(tag)) {

			taglist.add(tag);
			movielist = new ArrayList<Movie>();
			Movie movie = new Movie(id, 1);
			movielist.add(movie);
			movielists.add(movielist);

		} else {
			int index = taglist.indexOf(tag);
			movielist = movielists.get(index);

			boolean match = false;

			for (Movie movie : movielist) {
				if (movie.getId() == id) {
					movie.mw++;
					match = true;
					break;
				}

			}

			if (!match) {
				Movie movie = new Movie(id, 1);
				movielist.add(movie);
			}
		}

	}

	public String stem(String term) {

		Stemmer st = new Stemmer();
		st.add(term.toLowerCase().toCharArray(), term.length());
		st.stem();

		return st.toString();
	}

	public int searchTag(String key, String type) {
		ArrayList<String> tlist;
		if (type.equals("stoptags"))
			tlist = stoptags;
		else
			tlist = keytags;
		int lo = 0;
		int hi = tlist.size() - 1;
		while (lo <= hi) {

			int mid = lo + (hi - lo) / 2;
			int result = key.compareTo(tlist.get(mid));
			if (result < 0)
				hi = mid - 1;
			else if (result > 0)
				lo = mid + 1;
			else
				return mid;
		}
		return -1;
	}

	public String toString() {

		StringBuilder matrixString = new StringBuilder();
		ArrayList<Movie> movielist;
		for (int i = 0; i < taglist.size(); i++) {

			matrixString.append(String.format("%-15s", taglist.get(i)));
			movielist = movielists.get(i);
			for (int j = 0; j < movielist.size(); j++) {
				matrixString.append(" " + movielist.get(j) + "\t");
			}
			matrixString.append("\n");
		}
		return matrixString.toString();
	}

	public ArrayList<Movie> rankedSearchById(Movie orig, int size) {

		String id = orig.getImdb_id();

		ArrayList<Movie> results = rankedSearch(orig.getTags(), size);
		int index = 0;
		for (Movie m : results) {

			if (m.getImdb_id().equals(id)) {
				index = results.indexOf(m);
				break;
			}

		}
		results.remove(index);

		return results;

	}

	public ArrayList<Movie> rankedSearchByTags(String[] tags, int size) {
		
		// The loop is present for the sake of testing and possible extensions
		
		ArrayList<String> searchtag = new ArrayList<String>();
		for (String tag : tags){
			searchtag.add(tag);
		}
	
		return rankedSearch(searchtag, size);

	}

	public ArrayList<Movie> rankedSearch(ArrayList<String> tags, int size) {

		ArrayList<String> query = new ArrayList<String>();

		for (String tag : tags) {
			
			tag=tag.toLowerCase();
			if ((searchTag(tag, "stoptags") != -1))
				continue;
			String temp[] = tag.split(" ");
			if (temp.length > 1) {
				for (String t : temp) {
					t = stem(t);
					if (searchTag(t, "keytags") != -1)
						query.add(t);
				}
			}
			query.add(stem(tag));
		}

		// Use cosine similarity for calculating similar movies
		HashMap<Integer, Double> docs = new HashMap<Integer, Double>();
		HashMap<String, Double> qscores = new HashMap<String, Double>();
		HashMap<String, Integer> qcount = new HashMap<String, Integer>();
		ArrayList<Movie> movielist;

		int c = 0;
		ArrayList<String> mov_query = new ArrayList<String>();
		for (String q : query) {

			if (!qcount.containsKey(q)) {
				qcount.put(q, 1);
				mov_query.add(q);

			} else {
				c = qcount.get(q) + 1;
				qcount.put(q, c);
			}

		}

		// Computation of the query term scores
		double qlength = 0.0;
		for (String q : mov_query) {

			int index = taglist.indexOf(q);
			if (index < 0)
				continue;
			movielist = movielists.get(index);
			double w_t = Math.log(N * 1.0 / movielist.size())
					* (1 + Math.log(qcount.get(q)));
			qscores.put(q, w_t);
			qlength += w_t * w_t;

		}

		qlength = Math.sqrt(qlength);

		for (String q : mov_query) {

			int index = taglist.indexOf(q);
			if (index < 0)
				continue;
			movielist = movielists.get(index);
			// Normalize query weight
			double w_t = qscores.get(q) / qlength;
			Movie movie;
			for (int j = 0; j < movielist.size(); j++) {
				movie = movielist.get(j);

				// divide by normalized length
				//double score = w_t * movie.mw / len_norm.get(movie.getId());
				double score = w_t * movie.mw;
				if (!docs.containsKey(movie.getId())) {
					docs.put(movie.getId(), score);
				} else {
					score += docs.get(movie.getId());
					docs.put(movie.getId(), score);
				}
			}
		}

		docs = (HashMap<Integer, Double>) Helper.sortMapByValues(docs);
		//System.out.println(docs);
		int mapsize = docs.size();
		if (mapsize < 2) // No similar movies
			return null;
		Iterator<Integer> it = docs.keySet().iterator();
		ArrayList<Movie> recommendation = new ArrayList<Movie>();

		int i = 0;
		int j = 0; // for cases where required No of recommendations is lesser
					// than map size  

		while (i <= size && j < mapsize) {

			Movie movie = moviemaker.getMovieById(it.next());
			recommendation.add(movie);
			i++;
			j++;
		}
		
		return recommendation;

	}

}
