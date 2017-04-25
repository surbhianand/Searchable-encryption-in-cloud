package servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/PostUserServlet")
public class PostUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost/major";

	   //  Database credentials
	   static final String USER = "root";
	   static final String PASS = "";

    /**
     * Default constructor. 
     */
    public PostUserServlet() {
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		response.setContentType("text/html");  
         PrintWriter pw = response.getWriter();  
         String connectionURL = "jdbc:mysql://localhost:3306/major";// userdb is the database  
         Connection connection;  
         try{  
           String FirstName = request.getParameter("first-name");  
           String LastName= request.getParameter("last-name");  
           String Email = request.getParameter("email");  
           String Password= request.getParameter("password");  
           String ConfirmPassword = request.getParameter("confirm-password");  
           String Category="U";
           
           if(!Password.equals(ConfirmPassword))
           {
        	   System.out.println("Passwords don't match"); 
           }
           else{
           Class.forName("com.mysql.jdbc.Driver");  
           connection = DriverManager.getConnection(connectionURL, "root", "");  
           PreparedStatement pst = connection.prepareStatement("insert into `user_details`(`Firstname`,`Lastname`,`Email`,`Password`,`Category`) values(?,?,?,?,?)");
           pst.setString(1,FirstName);  
           pst.setString(2,LastName); 
           pst.setString(3,Email); 
           pst.setString(4,Password); 
           pst.setString(5,Category); 
           
           int i = pst.executeUpdate();  
           if(i!=0){  
             pw.println("<br>Record has been inserted"); 
             String site = new String("http://www.photofuntoos.com");

             response.setStatus(response.SC_MOVED_TEMPORARILY);
             response.setHeader("Location", site);    
             
           }  
           else{  
             pw.println("failed to insert the data");  
            }  
         }  
         }
         catch (Exception e){  
           pw.println(e);  
         }
		
		
         

       }  
		
	
      
	}

