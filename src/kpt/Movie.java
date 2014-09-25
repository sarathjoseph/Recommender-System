package kpt;



import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable{

	private static final long serialVersionUID = -9081738197367817635L;
	private int id;
	private ArrayList<String> tags;
	double mw;
	private String title;
	private String imdb_id;
	private String image;
	private String year;
	private String rating;
	private String genre;
	private String runtime;
	private String director;
	private String actors;
	private String plot;
	private String release_date;
	private String language;
	private String country;
	
	@Override
	public boolean equals(Object obj) {
		Movie m = null;
		if(obj instanceof Movie)
		 m=(Movie)obj;
		
		if(this.id==m.id)
			return true;
		else
			return false;
	}
	

	public int hashCode() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public void setMw(double mw) {
		this.mw = mw;
	}


	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public void setPlot(String plot) {
		this.plot = plot;
	}

	public void setReleasedate(String releasedate) {
		this.release_date = releasedate;
	}


	public void setLanguage(String language) {
		this.language = language;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImdb_id() {
		return imdb_id;
	}

	public void setImdb_id(String imdb_id) {
		this.imdb_id = imdb_id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	
	public Movie(int i, String tag) {
		tags = new ArrayList<String>();
		id = i;
		tags.add(tag);
	}

	public Movie(int m_id, double weight) {
		id = m_id;
		mw = weight;
	}

	public void addTag(String tag) {

		tags.add(tag);
	}

	public ArrayList<String> getTags() {

		return tags;
	}

	public String toString() {

		String movieString = "{\"Title\":\"" + title + "\",\"Image\":" + image
				+ ",\"Actors\":" + actors
				+ ",\"Plot\":" + plot
				+ ",\"Director\":" + director
				+ ",\"Runtime\":" + runtime
				+ ",\"Genre\":" + genre
				+ ",\"ReleaseDate\":" + release_date
				+ ",\"Language\":" + language
				+ ",\"Country\":" + country
				+ ",\"Year\":" + year + ",\"Imdb_Id\":" + imdb_id + ",\"Id\":"
				+ id + ",\"Rating\":" + rating + "}";
		return movieString;
	}

	public int getId() {

		return id;
	}

}