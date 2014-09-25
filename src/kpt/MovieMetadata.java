package kpt;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class MovieMetadata {

	public static void updateMeta(Movie movie, String[]attributes) {

		String Title = attributes[0].replace("\"", "");
		String Year = "\""+attributes[1].replace("\"", "")+"\"";
		String Release_Date ="\""+ attributes[3].replace("\"", "")+"\"";
		String Runtime = "\""+attributes[4].replace("\"", "")+"\"";
		String Genre = "\""+attributes[5].replace("\"", "")+"\"";
		String Director ="\""+ attributes[6].replace("\"", "")+"\"";
		String Actors ="\""+ attributes[8].replace("\"", "")+"\"";
		String Plot ="\""+ attributes[9].replace("\"", "")+"\"";
		String Language = "\""+attributes[10].replace("\"", "")+"\"";
		String Country ="\""+ attributes[11].replace("\"", "")+"\"";
		String Poster = "\""+attributes[13].replace("\"", "")+"\"";
		String imdb_rating = "\""+attributes[15].replace("\"", "")+"\"";
		String Imdb_Id = "\""+attributes[17].replace("\"", "")+"\"";

		movie.setReleasedate(Release_Date);
		movie.setPlot(Plot);
		movie.setTitle(Title);
		movie.setYear(Year);
		movie.setGenre(Genre);
		movie.setActors(Actors);
		movie.setCountry(Country);
		movie.setDirector(Director);
		movie.setLanguage(Language);
		movie.setCountry(Country);
		movie.setImage(Poster);
		movie.setRating(imdb_rating);
		movie.setImdb_id(Imdb_Id);
		movie.setRuntime(Runtime);

		//saveImage(Poster.replace("\"", ""),Imdb_Id.replace("\"", ""));
		
		String[] genres = Genre.split(",");
		String[] actors = Actors.split(",");
		String[]languages=Language.split(",");
		
		
		
		movie.addTag(Director.replace("\"", ""));
		movie.addTag(Country.replace("\"", ""));
		movie.addTag(Title.replace("\"", ""));
		
		for(String lang:languages){
			
			movie.addTag(lang.replace("\"", ""));
		}

		for (String genre : genres) {
			movie.addTag(genre.replace("\"", ""));

		}

		for (String actor : actors) {

			
			movie.addTag(actor.replace("\"", ""));
		}
		
		/**/
		
	}
	
	public static void saveImage(String Url,String id){
		
		URL url;
		try {
			url = new URL(Url);
		
		InputStream in = new BufferedInputStream(url.openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1!=(n=in.read(buf)))
		{
		   out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();
		FileOutputStream fos = new FileOutputStream("/var/www/612/img/movies/"+id+".jpg");
		fos.write(response);
		fos.close();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
