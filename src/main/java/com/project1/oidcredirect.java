package com.project1;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import com.nimbusds.jwt.JWT;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.ParseException;
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
import com.nimbusds.oauth2.sdk.util.JSONObjectUtils;
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
 * Servlet implementation class oidcredirect
 */

public class oidcredirect extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	String returnURL;
	URI tokenEndpointURL = null;
	TokenRequest accessTokenRequest = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public oidcredirect() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	       
                
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
					
						
				     System.out.println(code);
				     
					State state = authzSuccess.getState();
					
		ClientID clientID =	new ClientID("155986685728-sovl8obg8tkrur8frlrufkod3vmpjnr1.apps.googleusercontent.com");
			Secret clientSecret = new Secret("ekr-p1dJbRrm-wMX22f5vTRY");
		ClientAuthentication clientAuth = new ClientSecretPost(clientID, clientSecret);
	
		//tokenEndpointURL = new URI("https://www.googleapis.com/oauth2/v4/token");
		tokenEndpointURL = new URI("https://accounts.google.com/o/oauth2/token");
				
		accessTokenRequest = new TokenRequest(tokenEndpointURL, clientAuth,
										//error was found around here(look for returnURL )
										new AuthorizationCodeGrant(code, new URI("https://gandra.fim.uni-passau.de/redir/192.168.12.190:8080/project1/oidcredirect")));
		
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
							
							System.out.println(userInfoResponse);
							
							if (userInfoResponse instanceof 
							UserInfoErrorResponse) {
							return; // TODO: error handling...
							
							}
							
							UserInfo userInfo =    
									((UserInfoSuccessResponse)userInfoResponse).getUserInfo();
							
									String info = userInfo.toJSONObject().toString();
					System.out.println(info);
					
					response.sendRedirect("dashboard.jsp");
					
					
					
				
			}
											
											
			
										
					}			
            }					
					
		catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		/**
		// TODO Auto-generated method stub
    	if (request.getParameter("NEW") != null) {
    		response.sendRedirect("oidcloginsev");
    		return;
    	}else if (request.getParameter("UPDATE")!= null){
    		request.getRequestDispatcher("oidcredirect").forward(request, response);
    		
    	}else if (request.getParameter("SAVE")!= null){
    		request.getRequestDispatcher("login.jsp").forward(request, response);
    	}
    		
    		
    		*/
    	}
    	
    	 
	}
	


