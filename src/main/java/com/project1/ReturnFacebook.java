package com.project1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.db.Connector;
import com.db.User;
import com.google.gson.Gson;
import com.nimbusds.oauth2.sdk.id.ClientID;


/**
 * Servlet implementation class ReturnFacebook
 */
public class ReturnFacebook extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	URI redirectURI = null;
	URI authURI = null;
	URI parameterizedRedirectURI = null;
	String accessToken = null;
	
	String name = null; //Change this parameter based what you need
	String email = null;//Change this parameter based what you need
	
	
    public ReturnFacebook() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 
		 * 
		 * DO YOUR OPENID MAGIC HERE
		 * 
		 * 
		 */
		
		try {
			
				
		ClientID clientID = new ClientID("291742937887420");
		
		//Secret clientSecret = new Secret("437f47f2c07c312c76b88070c8e2518e");
		
		redirectURI = new URI ("https://gandra.fim.uni-passau.de/redir/192.168.12.22:8080/project1/ReturnFacebook");
		//redirectURI = new URI ("https://gandra.fim.uni-passau.de/redir/192.168.12.190:8080/project1/ReturnFacebook");
		
		
		String code = request.getParameter("code");
		
		
		/**
		URL url = new URL("https://graph.facebook.com/oauth/access_token?client_id="
		        + clientID + "&redirect_uri=" + redirectURI
		        + "&client_secret=" + "437f47f2c07c312c76b88070c8e2518e"
		        + "&code=" + code);
		
		 */

		URL url = new URL("https://graph.facebook.com/oauth/access_token?client_id="
		        + clientID + "&redirect_uri=" + redirectURI
		        + "&client_secret=" + "4058caa710dfb3d316532ca83c4911be"
		        + "&code=" + code);
		
		
		//request for acess code 
		
	
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		String line, outputString = "";
		
		BufferedReader reader = new BufferedReader(
		        new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
		
		
		
		
		while ((line = reader.readLine()) != null) {
		    outputString += line;
		    
		}
		
				
		
		
		String accessToken = null;
		
	
		if(outputString.indexOf("access_token")!=-1) {
			
		    int k=outputString.length();
		    accessToken = outputString.substring(13,outputString.indexOf("&"));
		    
		}
		
		

			System.out.println(accessToken);
		url = new URL("https://graph.facebook.com/v2.8/me?fields=id,name,email&access_token="+ accessToken);
		
		//"https://graph.facebook.com/v2.8/me?fields=id,name,email,first_name,last_name
		
		
		URLConnection conn1 = url.openConnection();
		outputString = "";
		reader = new BufferedReader(new InputStreamReader(conn1.getInputStream()));
		while ((line = reader.readLine()) != null) {
		    outputString += line;
		}
		reader.close();
		

			
		FaceBookPojo fbp = new Gson().fromJson(outputString, FaceBookPojo.class);
	
	//	System.out.println(fbp.email);
	//	System.out.println(fbp.getId());
	//	System.out.println(fbp.name);
		
		
		
		
		
		/* ---- CHECK TO OUR DB BEGIN ----*/
		 name = fbp.name; //Change this parameter based what you need
		 email = fbp.email;//Change this parameter based what you need
		 	 
		

		String realm = "FACEBOOK"; //Change this parameter based what you need
		int uid=0;
		
		Session session = null;
        Transaction tx = null;
        
        
		try 
		{
			Connector conn11 = new Connector();
            session = conn11.configureSessionFactory().openSession();
            tx = session.beginTransaction();
            
            String hsql = "from User where email = :user_email and realm = :login_realm";
            Query query = session.createQuery(hsql);
            query.setParameter("user_email", email);
            query.setParameter("login_realm", realm);
            List<User> result = query.list();
            
            if(result.size() != 0)
            {
            	
            	System.out.println("Exist");
            	
            	//USER IS EXIST, DO THE AUTHORIZATION METHOD
            	
            	for(User u : result)
            	{
            	
            	//	System.out.println("Id: " + u.getId() + " | Name:"  + u.getName() + " | Email:" + u.getEmail() + " | Realm:" + u.getRealm());
            		 
            		uid=u.getId();
            		
            		HttpSession sessionreal = request.getSession(false);
            		sessionreal.invalidate();
            		sessionreal = request.getSession(true);
            		sessionreal.setMaxInactiveInterval(20*60); 
            		sessionreal.setAttribute("userName", name);
	                sessionreal.setAttribute("userid", uid);
	                sessionreal.setAttribute("access", accessToken);
            		
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
            	
            	System.out.println("Not Exist");
            	//USER IS NOT EXIST, STORE IN DB
            	try
            	{
            		User newUser = new User(0, name, email,realm);
            		
            		session.save(newUser);
            		
            		uid=newUser.getId();
            		
            		System.out.println(uid);
            		
            	}
            	catch(Exception ex)
            	{
            		ex.printStackTrace();
            		tx.rollback();
            	}
            	
            	//System.out.println(uid);
            	
            	HttpSession sessionreal = request.getSession(false);
        		sessionreal.invalidate();
        		sessionreal = request.getSession(true);
        		sessionreal.setMaxInactiveInterval(20*60); 
        		sessionreal.setAttribute("userName", name);
                sessionreal.setAttribute("userid", uid);
                sessionreal.setAttribute("access", accessToken);
            	 	
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
		
		
		
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}
	


}
