package com.project1;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.db.Chat;
import com.db.Connector;

/**
 * Servlet implementation class Logout
 */
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	
    public Logout() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
        
		/**
		 * 
		 * 
		 * DO YOUR DEAUTHORIZATION MAGIC HERE
		 * 
		 * 
		 */
		
		
		
		
		try
		{      
			
			HttpSession session  = request.getSession(false);
			
		    session.removeAttribute("userid"); 
		    session.removeAttribute("userName");
		    
		    String pageToForward = request.getContextPath();
		    
		    response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
		    
		    
		    Object access = session.getAttribute("access");
		    
				

			if (session.getAttribute("realm") == "GOOGLE") {
				session.invalidate(); 
				
				//System.out.println("google");
				response.sendRedirect("https://www.google.com/accounts/Logout?continue=https://appengine.google.com/_ah/logout?continue=https://gandra.fim.uni-passau.de/redir/192.168.12.22:8080/project1/");
				
				//response.sendRedirect("https://www.google.com/accounts/Logout?continue=https://appengine.google.com/_ah/logout?continue=https://gandra.fim.uni-passau.de/redir/192.168.12.190:8080/project1/");

				
			} else {
				//System.out.println("facebook");
				
				 session.removeAttribute("userid"); 
				 session.removeAttribute("userName");
				 session.invalidate(); 
				//response.sendRedirect("https://www.facebook.com/logout.php?client_id=291742937887420&access_token=session.getAttribute("+access+")&next=https://gandra.fim.uni-passau.de/redir/192.168.12.190:8080/project1/");
				//response.sendRedirect("https://facebook.com/logout.php?appid=291742937887420&access_token="+access.toString()+"&next=https://gandra.fim.uni-passau.de/redir/192.168.12.190:8080/project1/index.jsp");
				//response.sendRedirect("https://facebook.com/logout.php?appid=291742937887420&access_token="+access.toString()+"&next=https://gandra.fim.uni-passau.de/redir/192.168.12.190:8080/project1/");
				
				 
				response.sendRedirect("https://www.facebook.com/logout.php?next=https://gandra.fim.uni-passau.de/redir/192.168.12.22:8080/project1/&access_token="+access.toString());
				//response.sendRedirect("http://m.facebook.com/logout.php?confirm=1&next=https://gandra.fim.uni-passau.de/redir/192.168.12.22:8080/project1");
				 
				//response.sendRedirect("https://facebook.com/logout.php?appid=291742937887420&access_token="+access.toString()+"&next=https://gandra.fim.uni-passau.de/redir/192.168.12.190:8080/project1/");
				
				//response.sendRedirect("https://facebook.com/logout.php?appid=291742937887420&access_token="+access.toString()+"&next=https://gandra.fim.uni-passau.de/redir/192.168.12.190:8080/project1/");
				//session.removeAttribute("access");
				//response.sendRedirect("https://gandra.fim.uni-passau.de/redir/192.168.12.190:8080/project1/index.jsp");
				
				//https://gandra.fim.uni-passau.de/git/csec_ws2016/group_4_project_1.git
				
				//response.sendRedirect("https://gandra.fim.uni-passau.de/redir/192.168.12.22:8080/project1/");
				
			}

		      
		    
		    
		}
		catch (Exception sqle)
		{
		    System.out.println("error UserValidateServlet message : " + sqle.getMessage());
		    System.out.println("error UserValidateServlet exception : " + sqle);
		    
		}
		
		//FOR DELETE ALL DATA
		
		/**Session session = null;
        Transaction tx = null;
        
        try 
        {
        	Connector conn = new Connector();
            session = conn.configureSessionFactory().openSession();
            tx = session.beginTransaction();
             
            session.createQuery("delete from User").executeUpdate();
            session.createQuery("delete from Chat").executeUpdate();
        } 
        catch (Exception ex) 
        {
        	tx.rollback();
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
        }*/
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
		
	}

}
