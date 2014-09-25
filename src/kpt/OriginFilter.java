package kpt;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.routing.Filter;

public class OriginFilter extends Filter { 

   public OriginFilter(Context context) { 
      super(context); 
   } 

   @Override 
   protected int beforeHandle(Request request, Response response) { 
      if(Method.OPTIONS.equals(request.getMethod())) { 
         Form requestHeaders = (Form) request.getAttributes().get("org.restlet.http.headers"); 
         String origin = requestHeaders.getFirstValue("Origin", true); 

     
            Form responseHeaders = (Form) response.getAttributes().get("org.restlet.http.headers"); 
            if (responseHeaders == null) { 
                responseHeaders = new Form(); 
                response.getAttributes().put("org.restlet.http.headers", responseHeaders); 
            } 
            responseHeaders.add("Access-Control-Allow-Origin", origin); 
            responseHeaders.add("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS"); 
            responseHeaders.add("Access-Control-Allow-Headers", "Content-Type"); 
            responseHeaders.add("Access-Control-Allow-Credentials", "true"); 
            responseHeaders.add("Access-Control-Max-Age", "60"); 
            response.setEntity(new EmptyRepresentation()); 
            return SKIP; 
         
      } 

      return super.beforeHandle(request, response); 
   } 

   @Override 
   protected void afterHandle(Request request, Response response) { 
      if(!Method.OPTIONS.equals(request.getMethod())) { 
         Form requestHeaders = (Form) request.getAttributes().get("org.restlet.http.headers"); 
         String origin = requestHeaders.getFirstValue("Origin", true); 

   
            Form responseHeaders = (Form) response.getAttributes().get("org.restlet.http.headers"); 
            if (responseHeaders == null) { 
                responseHeaders = new Form(); 
                response.getAttributes().put("org.restlet.http.headers", responseHeaders); 
            } 
            responseHeaders.add("Access-Control-Allow-Origin", origin); 
            responseHeaders.add("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS"); 
            responseHeaders.add("Access-Control-Allow-Headers", "Content-Type"); 
            responseHeaders.add("Access-Control-Allow-Credentials", "true"); 
            responseHeaders.add("Access-Control-Max-Age", "60"); 
        
      } 
      super.afterHandle(request, response); 
   } 
} 