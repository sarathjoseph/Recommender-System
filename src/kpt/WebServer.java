package kpt;
import org.restlet.data.Protocol;

import org.restlet.resource.ServerResource;
import org.restlet.Component;

 	
public class WebServer extends ServerResource{
 
	
	public static void main(String[] args) throws Exception {
		Component component = new Component();  
		component.getServers().add(Protocol.HTTP,4444); 
		component.getDefaultHost().attach(new RouterApplication());  
		component.start();  
	
	}
	
 
	
 
}