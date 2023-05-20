package com.project1;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.db.Chat;
import com.db.Connector;
/**
 * Servlet implementation class SendChat
 */
public class SendChat extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public SendChat() {
        super();

        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sessionreal = request.getSession(false);
		
		String uid = request.getParameter("userid");
		
		String chat = request.getParameter("chat");
		
		Object uids = sessionreal.getAttribute("userid");
		
		
		
		int user_id = uid!=null?Integer.parseInt(uids.toString()):1; //need to be implemented with something else
		
		//int user_id = uid!=null?Integer.parseInt(request.getSession().getId()):1; //need to be implemented with something else
		
		//int user_id = request.getSession().getId().toString().trim();
		//String user_id = request.getSession().getId();
		
		//System.out.println(request.getSession().getId().toString().trim());
	
		Session session = null;
        Transaction tx = null;
        SessionFactory sf = null;
        
        try 
        {
        	Connector conn = new Connector();
        	sf = conn.configureSessionFactory();
            session = sf.openSession();
            tx = session.beginTransaction();
             
            
            Chat chat1 = new Chat(0,user_id,chat);
            session.save(chat1);
            
            List<Chat> chats = session.createQuery("from Chat").list();
            for(Chat c : chats)
            {
            	System.out.println(c.getUser_id()+ " : "+c.getChat());
            }
             
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
            tx.rollback();
        } 
        finally
        {
        	if(session != null && session.isOpen())
			{
        		tx.commit();
				session.flush();
				session.close();
				sf.close();
			}
        	
        	response.sendRedirect("messageBoard.jsp?userid="+uid);
        }
	}

}
