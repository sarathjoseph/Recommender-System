package kpt;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Server {

	final static int N = 10;

	public static void main(String[] args) {

		new Server();
	}

	public Server() {
		ServerSocket ss = null;

		try {

			ss = new ServerSocket(8005);
			Socket cs = null;

			Recommender recommender = new Recommender(N);
			while (true) {
				cs = ss.accept();
				ThreadServer ths = new ThreadServer(cs, recommender);
				ths.start();
			}
		} catch (BindException be) {
			System.out
					.println("Server already running on this computer, stopping.");
		} catch (IOException ioe) {
			System.out.println("IO Error");
			ioe.printStackTrace();
		}

	}
}

class ThreadServer extends Thread {
	Socket cs;
	Recommender recommender;

	public ThreadServer(Socket cs, Recommender recommender) {
		this.cs = cs;
		this.recommender = recommender;
	}

	public boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void searchById(BufferedReader br, PrintWriter opw, String input_id) {
		try {

			if (!isInteger(input_id)) {
				opw.write("{\"response\":\"false\"}");
				opw.flush();
				br.close();
				opw.close();
				cs.close();
				System.out.println("\nconnection closed");
				return;
			}
			int id = Integer.parseInt(input_id);

			ArrayList<Movie> recommendation = recommender
					.getRecommendationById(id);

			StringBuffer sb = new StringBuffer();
			sb.append("{\"recommendation\":[");
			if (recommendation != null) {
				System.out.println("TOP " + Server.N + " recommendations for "
						+ recommender.moviemaker.getMovieById(id).getTitle()
						+ "\n");

				for (Movie m : recommendation) {
					System.out.println(m.getTitle() + "	" + m.getYear());
					sb.append(m).append(",");
				}
				String movie_json = sb.substring(0, sb.length() - 1).concat(
						"],\"response\":\"true\",\"movie\":"
								+ recommender.moviemaker.getMovieById(id)
										.toString() + "}");
				opw.write(movie_json);
				opw.flush();
			}

			else {
				System.out.println("No listing");
				opw.write("{\"response\":\"false\"}");
				opw.flush();
			}

			cs.close();
			opw.close();
			br.close();
			System.out.println("\nconnection closed");
		} catch (IOException e) {
			System.out.println("Something went wrong.");
			e.printStackTrace();

		}

	}

	public void run() {
		BufferedReader br;
		PrintWriter opw;
		try {

			System.out.println("New connection\n");
			br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
			opw = new PrintWriter(new OutputStreamWriter(cs.getOutputStream()));
			String input = br.readLine();
			Object o = JSONValue.parse(input);
			
			JSONObject jo = (JSONObject) o;
			String action = (String) jo.get("action");

			if (action.equals("searchById")) {

				String id = (String) jo.get("id");
				searchById(br, opw, id);

			}

			if (action.equals("searchByTags")) {

				String tags = (String) jo.get("tags");
				searchByTags(br, opw, tags);

			}

		}

		catch (IOException e) {
			System.out.println("Something went wrong.");
			e.printStackTrace();

		}
	}

	public void searchByTags(BufferedReader br, PrintWriter opw, String tags) {
		
		ArrayList<Movie> recommendation = recommender
				.getRecommendationByTags(tags);
		try{
		StringBuffer sb = new StringBuffer();
		sb.append("{\"recommendation\":[");
		if (recommendation != null) {
			System.out.println("TOP " + Server.N + " recommendations for search : "
					+ tags
					+ "\n");

			for (Movie m : recommendation) {
				System.out.println(m.getTitle() + "	" + m.getYear());
				sb.append(m).append(",");
			}
			String movie_json = sb.substring(0, sb.length() - 1).concat(
					"],\"response\":\"true\"}");
			opw.write(movie_json);
			opw.flush();
		}

		else {
			System.out.println("No listing");
			opw.write("{\"response\":\"false\"}");
			opw.flush();
		}

		cs.close();
		opw.close();
		br.close();
		System.out.println("\nconnection closed");
	} catch (IOException e) {
		System.out.println("Something went wrong.");
		e.printStackTrace();

	}

	}

}
