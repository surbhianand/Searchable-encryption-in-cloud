package servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ParameterException;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.CloudFile;
import org.cloudbus.cloudsim.examples.CloudHarddriveStorage;

import classes.Stopwords;
import classes.User;

/**
 * Servlet implementation class LoginUser
 */
@WebServlet("/LoginUser")
public class LoginUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost/major";
	   private static SecretKeySpec secretKey;
	    private static byte[] key;

	   //  Database credentials
	   static final String USER = "root";
	   static final String PASS = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		
		int num_user = 1;
		Calendar calendar = Calendar.getInstance();
		boolean trace_flag = false;
		CloudSim.init(num_user, calendar, trace_flag);
		//******************************************************
		//Creating Storages
		CloudHarddriveStorage hdst = null;
		try {
			hdst = new CloudHarddriveStorage(50);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double hdstCap = hdst.getCapacity();
		double hdstAvailSpace = hdst.getAvailableSpace();
		Log.printLine("Capacity="+hdstCap+" Available="+hdstAvailSpace);
		
		Connection conn = null;
		   Statement stmt = null;
		   try{
		      Class.forName("com.mysql.jdbc.Driver");
		      System.out.println("Connecting to a selected database...");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      System.out.println("Connected database successfully...");
		      String EnteredEmail = request.getParameter("email");  
	           String EnteredPassword= request.getParameter("password"); 
		      System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql = "SELECT * FROM `user_details`";
		      ResultSet rs = stmt.executeQuery(sql);
		      //STEP 5: Extract data from result set
		      while(rs.next()){
		         String email = rs.getString("Email");
		         String psw = rs.getString("Password");
		         if(email.equals(EnteredEmail)&&psw.equals(EnteredPassword))
		         {
		        	 /*
		     		BloomierObject.originalMap= new HashMap<String, List<Pair<String,Integer>>>();
		     		List<Pair<String,Integer>> l2 = new ArrayList<Pair<String,Integer>>();
		         	Pair<String,Integer> p3=new Pair<String,Integer>("hey3",3);
		         	Pair<String,Integer> p4=new Pair<String,Integer>("hey4",4);
		         	l2.add(p3);
		         	l2.add(p4);
		             BloomierObject.originalMap.put("key2", l2);

		         try {
		         	BloomierObject.bloomierFilter = new MutableBloomierFilter<String, List<Pair<String,Integer>>>(BloomierObject.originalMap, BloomierObject.originalMap.keySet().size() * 3000000, 100, 320,10000);
		     	} catch (TimeoutException e) {
		     		// TODO Auto-generated catch block
		     		e.printStackTrace();
		     	}
		         
		         */
		        	 
		        	 Log.printLine("Reading files in folder initially");
		     		File folder = new File("D:\\major\\majordataset");
		     		File[] listOfFiles = folder.listFiles();
		     		float maxtime = 0;
		     		List<CloudFile> listOfCloudFiles =new ArrayList();
		     		 
		     		    for (int i = 0; i < 50 && i< listOfFiles.length ; i++) {
		     		    	long startTime = System.nanoTime();
		     		    	if (listOfFiles[i].isFile()) {
		     		    	  String text = "";
		     		  		  String path=null;
		     		  		
		     		    	  Log.printLine("File " + listOfFiles[i].getPath());
		     		    	  path=listOfFiles[i].getPath();
		     		    	  FileInputStream inFile = new FileInputStream(path);
		     				  BufferedInputStream bin = new BufferedInputStream(inFile);
		     			      int character;
		     			        
		     			        while((character=bin.read())!=-1) {
		     			            text = text + (char)character;
		     			        }
		     			        bin.close();
		     			        inFile.close();
		     						
		     					 String cipherKey = "pro"; // 128 bit key
		     				          String ciphertext= createCipherText(path,cipherKey);
		     				          HashMap<String,Integer> hm;
		     				          hm=buildMap(path,cipherKey);
		     				        	        Stopwords s=new Stopwords();
		     				        	        try {
		     										s.RemoveStopWords(path);
		     									} catch (Exception e1) {
		     										// TODO Auto-generated catch block
		     										e1.printStackTrace();
		     									}
		     				        
		     						try {
		     							CloudFile cf= new CloudFile(path,5);
		     							listOfCloudFiles.add(cf);
		     						
		     						} catch (ParameterException e) {
		     							// TODO Auto-generated catch block
		     							e.printStackTrace();
		     						}
		     						listOfCloudFiles.get(i).addCipherData(ciphertext, cipherKey,hm);
		     							
		     		      } else if (listOfFiles[i].isDirectory()) {
		     		        System.out.println("Directory " + listOfFiles[i].getName());
		     		      }
		     		     long stopTime = System.nanoTime();
			     		 float tt = (stopTime - startTime)/1000000000;
		     		    }
		    		   System.out.println("time taken "+ tt);
		     		    
		     		   CloudSim.startSimulation();
		   			 for (int i = 0; i < listOfCloudFiles.size(); i++) {
		   			double timetaken = hdst.addCloudFile(listOfCloudFiles.get(i));
		   			hdstCap =hdst.getCapacity();
		   			hdstAvailSpace =hdst.getAvailableSpace();
		   			Log.printLine("Capacity="+hdstCap+" Available="+hdstAvailSpace);
		   			
		   			
		   			 }
		        	 User.isLoggedIn=true;
		        	 String site = "Search.jsp" ;
		        	 response.setStatus(response.SC_MOVED_TEMPORARILY);
		        	 response.setHeader("Location", site); 
		        	 break;
		         }
		       
		         System.out.print(", First: " +email );
		         System.out.println(", Last: " + psw);
		      }
		      rs.close();
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		 		   System.out.println("Goodbye!");
		   
		   
	}
	
	public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
	
	
	public static String createCipherText(String fileName,String key) throws FileNotFoundException
    {
    	FileInputStream inFile = new FileInputStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inFile));
		String line;
		String ciphertext="";
		try {
			while ((line = reader.readLine()) != null) {
				 // Do something with line
					String temp = encrypt(line,key);
					ciphertext = ciphertext + temp;
					ciphertext = ciphertext + '$';
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ciphertext;
    }
	
	
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
 
    public static String decrypt(String strToDecrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    
    public static HashMap<String,Integer> buildMap(String fileName,String key) throws IOException{
    	HashMap<String,Integer> hm=new HashMap<String,Integer>();
    	FileInputStream inFile = new FileInputStream(fileName);
    	BufferedReader reader = new BufferedReader(new InputStreamReader(inFile));
        String line = null;
        while( (line = reader.readLine())!= null ){
            // \\s+ means any number of whitespaces between tokens
            String [] tokens = line.split("\\s+");
            for(int i=0;i<tokens.length;i++)
            {
            	String temp = encrypt(tokens[i],key); 
            	hm.put(temp, 1);
            }
        }
    	return hm;
    }



}
