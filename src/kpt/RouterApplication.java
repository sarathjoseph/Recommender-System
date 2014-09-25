package kpt;



import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
 
public class RouterApplication extends Application{
	
	@Override
	public synchronized Restlet createInboundRoot() {
		
		Router router = new Router(getContext());
	
		router.attach("/search/movie/{movid}", MovieIdRequestHandler.class);
		router.attach("/search/tags/{tags}",TagRequestHandler.class);
		 OriginFilter originFilter = new OriginFilter(getContext()); 
	      originFilter.setNext(router); 
	      return originFilter; 
		//return router;
	}
}