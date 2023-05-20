package com.project1;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.Nonce;


/**
 * Servlet implementation class LoginRedirection
 */
public class LoginRedirection extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	URI redirectURI = null;
	URI authURI = null;
	URI parameterizedRedirectURI = null;
	String redirectString = null;
	
    public LoginRedirection() 
    {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String realm = request.getParameter("direction");
		if(realm.equalsIgnoreCase("Google"))
		{
			URI redirectURI = null;
			URI authURI = null;
			URI parameterizedRedirectURI = null;
			String redirectString = null;
			
			try {
				
				redirectURI = new URI ("https://gandra.fim.uni-passau.de/redir/192.168.12.22:8080/project1/ReturnGoogle");
				

				//redirectURI = new URI ("https://gandra.fim.uni-passau.de/redir/192.168.12.190:8080/project1/ReturnGoogle");
				
				
				
				authURI = new URI("https://accounts.google.com/o/oauth2/v2/auth");
				
				ResponseType rt = new ResponseType("code");
				
				//what you want access to
				Scope scope = new Scope("openid", "email", "profile");
				// insert your real clientID instead of the “***”
				ClientID clientID = new ClientID("155986685728-puplmesk6evrvatig8euv4jskmoht4ut.apps.googleusercontent.com");
				
				State state = new State();
				
				Nonce nonce = null;  // new Nonce(); --nonce not supported by Google
				
				AuthenticationRequest authRequest = new AuthenticationRequest(redirectURI, rt, scope, clientID, redirectURI, state, nonce); 
				
				
				parameterizedRedirectURI = new URI(authURI.toString() + "?" + authRequest.toQueryString());
				
				 redirectString = parameterizedRedirectURI.toString();
				
				 response.sendRedirect(redirectString);
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		}
		else if(realm.equalsIgnoreCase("Paypal"))
		{
			//REDIRECT THEM TO Paypal
		}
		else if(realm.equalsIgnoreCase("Facebook"))
		{
		
			
			try {
				
				//url to get stuff
				//scope: to get profile info give profile as scope, to get email address give email as scope
				URI authURI = new URI("https://www.facebook.com/dialog/oauth?client_id=291742937887420&redirect_uri=https://gandra.fim.uni-passau.de/redir/192.168.12.22:8080/project1/ReturnFacebook&scope=email&scope=profile&scope=user_friends");
			
				//URI authURI = new URI("https://www.facebook.com/dialog/oauth?client_id=291742937887420&redirect_uri=https://gandra.fim.uni-passau.de/redir/192.168.12.190:8080/project1/ReturnFacebook&scope=email&scope=profile&scope=user_friends");
				
				 response.sendRedirect(authURI.toString());
				
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
				
			//REDIRECT THEM TO Facebook
				
		}else{
			
			// Fake code that accepts the name of a user that is automatically authenticated
			response.getWriter().write("<html><body>"
					+ "<form method=\"get\" action=\"ReturnGoogle\">"
					+ "Username: <input type=text name=\"user\"/>"
					+ "<button id=login type=submit>Login</button>"+
					"</form></body></html>");
			response.flushBuffer();
			
			
		}
		
		
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	/**	
	public static String googlelog() {
		  
		URI redirectURI = null;
		URI authURI = null;
		URI parameterizedRedirectURI = null;
		String redirectString = null;
		
		try {
			
			redirectURI = new URI ("https://gandra.fim.uni-passau.de/redir/192.168.12.22:8080/project1/ReturnGoogle");
				
			authURI = new URI("https://accounts.google.com/o/oauth2/v2/auth");
			
			ResponseType rt = new ResponseType("code");
			
			//what you want access to
			Scope scope = new Scope("openid", "email", "profile");
			// insert your real clientID instead of the “***”
			ClientID clientID = new ClientID("155986685728-sovl8obg8tkrur8frlrufkod3vmpjnr1.apps.googleusercontent.com");
			
			State state = new State();
			
			Nonce nonce = null;  // new Nonce(); --nonce not supported by Google
			
			AuthenticationRequest authRequest = new AuthenticationRequest(redirectURI, rt, scope, clientID, redirectURI, state, nonce); 
			
			
			parameterizedRedirectURI = new URI(authURI.toString() + "?" + authRequest.toQueryString());
			
			 redirectString = parameterizedRedirectURI.toString();
			
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		   return redirectString; 
		}
	
*/

}
