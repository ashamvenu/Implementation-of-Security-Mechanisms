package com.project1;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.minidev.json.JSONObject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.db.Connector;
import com.db.User;
import com.nimbusds.jwt.JWT;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretPost;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.nimbusds.openid.connect.sdk.AuthenticationErrorResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponse;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponseParser;
import com.nimbusds.openid.connect.sdk.UserInfoErrorResponse;
import com.nimbusds.openid.connect.sdk.UserInfoRequest;
import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import com.nimbusds.openid.connect.sdk.UserInfoSuccessResponse;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;


/**
 * Servlet implementation class ReturnGoogle
 */


public class ReturnGoogle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	URI redirectURI = null;
	URI authURI = null;
	URI parameterizedRedirectURI = null;
	String gname = null;
	String gemail = null;
	
	
    public ReturnGoogle() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	
		try {
       	 
        	TokenResponse tokenResponse = null;
        	URI s = null;
			String query = request.getRequestURL().append("?").append(request.getQueryString()).toString();
			URI tokenEndpointURL = null;
			TokenRequest accessTokenRequest = null;
			AuthenticationResponse authResponse = null;
			JSONObject jsonObject = null;
			s = new URI(query);
			
			
			authResponse = AuthenticationResponseParser.parse(s);
				
				if(authResponse instanceof AuthenticationErrorResponse) {
				// TODO: error handling: authentication error
					AuthenticationErrorResponse error = (AuthenticationErrorResponse)authResponse;
	                System.out.println(((AuthenticationErrorResponse) authResponse).getErrorObject());
	                response.sendRedirect("error.jsp");
					
				}else{
				
				
				AuthenticationSuccessResponse authzSuccess = (AuthenticationSuccessResponse)authResponse;
				AuthorizationCode code = authzSuccess.getAuthorizationCode();
				
					
			
			     
				State state = authzSuccess.getState();
				
	ClientID clientID =	new ClientID("155986685728-puplmesk6evrvatig8euv4jskmoht4ut.apps.googleusercontent.com");
		Secret clientSecret = new Secret("hHPQ2sBTqjhdYsoVVBWZUR5S");
	ClientAuthentication clientAuth = new ClientSecretPost(clientID, clientSecret);

	//tokenEndpointURL = new URI("https://www.googleapis.com/oauth2/v4/token");
	tokenEndpointURL = new URI("https://accounts.google.com/o/oauth2/token");
			
	
	
	 
	
	accessTokenRequest = new TokenRequest(tokenEndpointURL, clientAuth,
									//error was found around here(look for returnURL )
									new AuthorizationCodeGrant(code, new URI("https://gandra.fim.uni-passau.de/redir/192.168.12.22:8080/project1/ReturnGoogle")));
	
	
	//accessTokenRequest = new TokenRequest(tokenEndpointURL, clientAuth,
			//error was found around here(look for returnURL )
			//new AuthorizationCodeGrant(code, new URI("https://gandra.fim.uni-passau.de/redir/192.168.12.190:8080/project1/ReturnGoogle")));

	
		HTTPRequest httpRequest;
		httpRequest = accessTokenRequest.toHTTPRequest();
		HTTPResponse httpResponse = httpRequest.send();
								
								
	
								
	tokenResponse = OIDCTokenResponseParser.parse(httpResponse);
							
	 
	
    if (tokenResponse instanceof TokenErrorResponse) {
			// do error handling
	TokenErrorResponse tokenError = (TokenErrorResponse)tokenResponse;
								
	return;
		}else{
			
			
			  OIDCTokenResponse tokenSuccess =(OIDCTokenResponse)tokenResponse;
				
				BearerAccessToken accessToken = (BearerAccessToken) tokenSuccess.getOIDCTokens().getBearerAccessToken();
																					
				RefreshToken refreshToken = tokenSuccess.getOIDCTokens().getRefreshToken();
				JWT idToken = tokenSuccess.getOIDCTokens().getIDToken();
				jsonObject = idToken.getJWTClaimsSet().toJSONObject();
				response.getWriter().print(jsonObject.toString());
				
				URI userinfoEndpointURL = new    
						URI("https://www.googleapis.com/oauth2/v3/userinfo");
						UserInfoRequest userInfoRequest = new
						UserInfoRequest(userinfoEndpointURL, accessToken);
						HTTPRequest urequest = userInfoRequest.toHTTPRequest();
						httpResponse = userInfoRequest.toHTTPRequest().send();
						UserInfoResponse userInfoResponse = 
						UserInfoResponse.parse(httpResponse);
						
						
						if (userInfoResponse instanceof 
						UserInfoErrorResponse) {
						return; // TODO: error handling...
						
						}
						
						UserInfo userInfo =    
								((UserInfoSuccessResponse)userInfoResponse).getUserInfo();
						
								String info = userInfo.toJSONObject().toString();
								
								Object received = userInfo.toJSONObject();
								
								
				
				JSONObject jsonObject1 = (JSONObject) received;
				gname = (String) jsonObject1.get("name");
				gemail = (String) jsonObject1.get("email");
			
				
				/* ---- CHECK TO OUR DB BEGIN ----*/
				//String name = request.getParameter("user");
				
				//System.out.println(name);
						
				//String email = name;
				
						
				String realm = "GOOGLE"; //Change this parameter based what you need
				
				int uid=0;
											
				Session session = null;
		        Transaction tx = null;
		        
		        //System.out.println("am here");
		        
				try 
				{
					
					
										 
					Connector conn = new Connector();
		            session = conn.configureSessionFactory().openSession();
		            tx = session.beginTransaction();
		            
		            String hsql = "from User where email = :user_email and realm = :login_realm";
		            Query query1 = session.createQuery(hsql);
		            query1.setParameter("user_email", gemail);
		            query1.setParameter("login_realm", realm);
		            List<User> result = query1.list();
		            
		            if(result.size() != 0)
		            {
		            	//USER IS EXIST, DO THE AUTHORIZATION METHOD
		            	for(User u : result)
		            	{
		            		// System.out.println("Id: " + u.getId() + " | Name:"  + u.getName() + " | Email:" + u.getEmail() + " | Realm:" + u.getRealm());
		            		 
		            		 uid=u.getId();
		            		 
		            		 
		            		
		            		 HttpSession sessionreal = request.getSession(false);
		             		sessionreal.invalidate();
		             		sessionreal = request.getSession(true);
		             		sessionreal.setMaxInactiveInterval(20*60); 
		            		sessionreal.setAttribute("userName", gname);
		 	                sessionreal.setAttribute("userid", uid);
		 	               sessionreal.setAttribute("realm", realm);
		            		/**
		        			 * 
		        			 * 
		        			 * DO YOUR AUTHORIZATION MAGIC HERE
		        			 * 
		        			 * 
		        			 */
		            		 
		            		 
		            		
		            	}
		            }
		            else
		            {
		            	//USER IS NOT EXIST, STORE IN DB
		            	try
		            	{
		            		User newUser = new User(0, gname, gemail,realm);
		            		session.save(newUser);
		            		uid=newUser.getId();  // does this work?
		            		System.out.println(uid);
		            		
		         	            		
		            		
		            	}
		            	catch(Exception ex)
		            	{
		            		ex.printStackTrace();
		            		tx.rollback();
		            	}
		            	
		            	
		            	HttpSession sessionreal = request.getSession(false);
		        		sessionreal.invalidate();
		        		sessionreal = request.getSession(true);
		        		sessionreal.setMaxInactiveInterval(20*60); 
		            	sessionreal.setAttribute("userName", gname);
		                sessionreal.setAttribute("userid", uid);
		                sessionreal.setAttribute("realm", realm);
		            	 
		            	/**
		    			 * 
		    			 * 
		    			 * DO YOUR AUTHORIZATION MAGIC HERE
		    			 * 
		    			 * 
		    			 */
		            }
		        } 
				catch (Exception ex) 
				{
					
		            ex.printStackTrace();
		            System.out.println(ex.toString());
		        } 
				finally
				{
					if(session != null && session.isOpen())
					{
						tx.commit();
						session.flush();
						session.close();
					}
				response.sendRedirect("messageBoard.jsp?userid="+uid);
		        }
				/* ---- CHECK TO OUR DB END ----*/
				
				
						
		
		}
										
								
									
				}			
        }					
				
	catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				 //System.out.println(e.toString());
			}

		
		} 
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}
