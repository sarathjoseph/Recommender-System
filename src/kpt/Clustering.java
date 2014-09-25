package kpt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;





public class Clustering {

	MovieVsm mvsm;
	private ArrayList<ArrayList<Movie>> movielists;
	private ArrayList<String> taglist;
	MovieMaker moviemaker;
	private ArrayList<Movie> movies;
	int N;
	HashMap<StringBuilder, ArrayList<Movie>> cache;
	
	public String toString() {

		StringBuilder matrixString = new StringBuilder();
		ArrayList<Movie> movielist;
		for (int i = 0; i < taglist.size(); i++) {

			matrixString.append(String.format("%-15s", taglist.get(i)));
			movielist = movielists.get(i);
			for (int j = 0; j < movielist.size(); j++) {
				matrixString.append(" " + movielist.get(j).getId() + "	"+ movielist.get(j).mw +"\t");
			}
			matrixString.append("\n");
		}
		return matrixString.toString();
	}
	
	public Clustering(int n){
		
		try{
			N=n;
			 cache=new HashMap<StringBuilder, ArrayList<Movie>>();
	        
		FileInputStream fileIn =new FileInputStream("movievsm.obj");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        this.mvsm = (MovieVsm) in.readObject();
         moviemaker=mvsm.moviemaker;
         movies=moviemaker.getMovies();
         movielists=mvsm.getMovielists();
         taglist=mvsm.getTagList();
         in.close();
         fileIn.close();
         /*
         FileInputStream fis = new FileInputStream("moviecache2.obj");
         ObjectInputStream ois = new ObjectInputStream(fis);
         cache = (HashMap) ois.readObject();
         ois.close();
         fis.close();
         */
         
        
        ArrayList<Movie>movielist;
        for(Movie movie:movies){
        	
        for(String s:movie.getTags()){
        int index = taglist.indexOf(s);

		if (index < 0)
			continue;
		movielist = movielists.get(index);
		cache.put(new StringBuilder(s), movielist);
        	}
      
    	
        }
        
        FileOutputStream fos =
                new FileOutputStream("moviecache2.obj");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
             oos.writeObject(cache);
             oos.close();
             fos.close();
             
		}
        catch(IOException i)
        {
           i.printStackTrace();
           return;
        }catch(ClassNotFoundException c)
        {
           System.out.println("Movie obj not found");
           c.printStackTrace();
           return;
        }
		
		
	}
	
	public double computeCosine(int movId,HashSet<Movie> cluster) {

		double cosine_sim = 0.0;
		Movie input = new Movie(movId, 1.0);
		for (Movie movie : cluster) {

			if (movie == null)
				continue;
			for (String s : movie.getTags()) {

				ArrayList<Movie> movielist;

				if (cache.containsKey(s))
					movielist = cache.get(s);
				
				else {
					int index = taglist.indexOf(s);

					if (index < 0)
						continue;
					movielist = movielists.get(index);
				//	cache.put(s, movielist);
				}

				double mov_score = 0.0;

				int index1 = movielist.indexOf(input);
				if (index1 < 0)
					continue;
				mov_score = movielist.get(index1).mw;

				int index2 = movielist.indexOf(movie);
				if (index2 < 0)
					continue;
				cosine_sim += movielist.get(index2).mw * mov_score;

			}

		}

		return cosine_sim / cluster.size();
	}

	public void cluster() {

		HashSet<Movie> c1 = new HashSet<Movie>();
		HashSet<Movie> c2 = new HashSet<Movie>();
		HashSet<Movie> c3 = new HashSet<Movie>();
		HashSet<Movie> c4 = new HashSet<Movie>();
		HashSet<Movie> c5 = new HashSet<Movie>();
		HashSet<Movie> c6 = new HashSet<Movie>();
		HashSet<Movie> c7 = new HashSet<Movie>();
		HashSet<Movie> c8 = new HashSet<Movie>();
		HashSet<Movie> c9 = new HashSet<Movie>();
		HashSet<Movie> c10 = new HashSet<Movie>();
	
		//clusters = new ArrayList<ArrayList<Movie>>();
		ArrayList<HashSet<Movie>>clusters= new ArrayList<HashSet<Movie>>();
		
		
		
		//Intialize two clusters
		
		Movie m9=moviemaker.getMovieById(4220); //The longest Yard(Sport)
		Movie m2=moviemaker.getMovieById(7317); //Euro Trip (Comedy)
		Movie m3=moviemaker.getMovieById(1307); //When Harry Met Sally(Romantic)
		Movie m4=moviemaker.getMovieById(4896); // Harry Potter(Fantasy)
		Movie m5=moviemaker.getMovieById(3623); //Mission Impossible(Action)
		Movie m6=moviemaker.getMovieById(5679); //The Ring(Horror)
		Movie m7=moviemaker.getMovieById(5066); // Walk to Remember (Drama)
		Movie m8=moviemaker.getMovieById(45175); //Kinky Boots(Foreign)
		Movie m1=moviemaker.getMovieById(8961); //The Incredibles(Animation)
		Movie m10=moviemaker.getMovieById(2028); //Saving Private Ryan (War)
	
		//System.out.println(m1.getTags());
		c1.add(m1);
		c2.add(m2);
		c3.add(m3);
		c4.add(m4);
		c5.add(m5);
		c6.add(m6);
		c7.add(m7);
		c8.add(m8);
		c9.add(m9);
		c10.add(m10);
		
		
		
		clusters.add(c1);
		clusters.add(c2);
		clusters.add(c3);
		clusters.add(c4);
		clusters.add(c5);
		clusters.add(c6);
		clusters.add(c7);
		clusters.add(c8);
		clusters.add(c9);
		clusters.add(c10);
		
		

		Boolean change=true;
		int iter=0;
		
		while(change && iter<10){

			iter++;

			change = false;

			for (Movie movie : movies) {

				int s = movie.getId();
				Double higher = 0.0;
				Double cosine = 0.0;
				int cluster_num = 0;

				for (int r = 0; r < N; r++) {

					cosine = computeCosine(s, clusters.get(r));
					if (cosine > higher) {
						higher = cosine;
						cluster_num = r;
					}

				}
				HashSet<Movie> c = clusters.get(cluster_num);

				Movie m = new Movie(s, 1.0);

				if (!c.contains(m)) {
					for (int r = 0; r < N; r++) {
						HashSet<Movie> cluster = clusters.get(r);
				

						if (cluster.contains(m)) {
							
							cluster.remove(m);
							break;
						}

					}
					
					c.add(movie);
					System.out.println(clusters.get(0).size()+" "+clusters.get(1).size()+" "+clusters.get(2).size()+" "+clusters.get(3).size()+" "+clusters.get(4).size()+" "+clusters.get(5).size()+" "+clusters.get(6).size()+" "+clusters.get(7).size()+" "+clusters.get(8).size()+" "+clusters.get(9).size());
					change = true;

				}

			}
			
		}	
		
		for(int r=0;r<N;r++){
			System.out.println("CLUSTER "+(r+1)+"\n");
		for(Movie m:clusters.get(r)){
			
			System.out.println(m.getTitle());
		}
		System.out.println("--------------------------------\n");
		}
		
		//System.out.println(clusters.get(0));
		System.out.println("\nNumber of iterations:"+iter);
		

	}
	
	public static void main(String[] args) {
		 
		Clustering c=new Clustering(10);
		c.cluster();
	
	}
}
