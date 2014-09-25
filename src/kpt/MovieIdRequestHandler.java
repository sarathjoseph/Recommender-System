package kpt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class MovieIdRequestHandler extends ServerResource {
	

	
	public String getServerResponse(){
		StringBuffer json= new StringBuffer();
		try {
			
			Socket socket = new Socket("localhost", 8005);
			InputStream in = socket.getInputStream();
			BufferedReader bin = new BufferedReader(new InputStreamReader(in));
		
			// open output to server
			OutputStream out = socket.getOutputStream();
			PrintWriter pout = new PrintWriter(new OutputStreamWriter(out));
			String movieid = (String) getRequestAttributes().get("movid");
			
			
			pout.write("{\"action\":\"searchById\",\"id\":"+"\""+movieid+"\""+"}"+"\n");
			pout.flush();
			String b;
			while((b=bin.readLine())!=null){
				System.out.print(b);
				json.append(b);
			}
			bin.close();
			pout.close();
			socket.close();
		
		}

		catch (IOException ioe) {
			System.out.println("IO error");
			ioe.printStackTrace();
		}
		return json.toString();
	}
    
	@Get
	public String toString() {
		
		return getServerResponse();
	}
}