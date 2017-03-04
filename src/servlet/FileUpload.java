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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.esotericsoftware.kryo.io.Input;


import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ParameterException;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.CloudFile;
import org.cloudbus.cloudsim.examples.CloudHarddriveStorage;

import classes.BloomierObject;
import classes.Pair;
import classes.Stopwords;
import classes.MutableBloomierFilter;

/**
 * Servlet implementation class FileUpload
 */

@WebServlet("/FileUpload")
@MultipartConfig()
public class FileUpload extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static SecretKeySpec secretKey;
    private static byte[] key;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUpload() {
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
	private String getFileName(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				return cd.substring(cd.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return null;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
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
		Log.printLine("Reading files in folder");
		File folder = new File("D:\\major\\majordataset");
		File[] listOfFiles = folder.listFiles();
		
		List<CloudFile> listOfCloudFiles =new ArrayList();
		 
		    for (int i = 0; i < listOfFiles.length; i++) {
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
				          //System.out.print("encrypted text is "+ciphertext);
				          HashMap<String,Integer> hm;
				          hm=buildMap(path,cipherKey);
				         
				          File log = new File("ListOfFilesNew1.txt");
				        
				        		    try{
				        		    if(log.exists()==false){
				        		            System.out.println("We had to make a new file at "+log.getAbsolutePath());
				        		            log.createNewFile();
				        		    }
				        		    System.out.println("exists at "+log.getAbsolutePath());
				        		    PrintWriter out = new PrintWriter(new FileWriter(log, true));
				        		    out.append(path + "\n");
				        		    out.close();
				        		    }catch(IOException e){
				        		        System.out.println("COULD NOT LOG!!");
				        		    }
				          
				        		    Log.printLine("reading out the list of files");
				        		    
				        		    inFile = new FileInputStream("ListOfFilesNew1.txt");
				        			bin = new BufferedInputStream(inFile);
				        	        int ch;
				        	        String temptext="";
				        	        while((ch=bin.read())!=-1) {
				        	            temptext = temptext + (char)ch;
				        	        }
				        	        bin.close();
				        	        inFile.close();
				        	        Log.printLine(temptext);
				        	        Log.printLine("finished");
				        	        Stopwords s=new Stopwords();
				        	      
				        	      
				        	        try {
										s.RemoveStopWords(path);
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
				        		    
				        //  CloudFile file2 = null;
						try {
							CloudFile cf= new CloudFile(path,5);
							listOfCloudFiles.add(cf);
						
						} catch (ParameterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						listOfCloudFiles.get(i).addCipherData(ciphertext, cipherKey,hm);
							//Log.printLine(file2.getName());
							
				
		       // System.out.println("File " + listOfFiles[i].getName());
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }
			CloudSim.startSimulation();
			 for (int i = 0; i < listOfCloudFiles.size(); i++) {
			double timetaken = hdst.addCloudFile(listOfCloudFiles.get(i));
			hdstCap =hdst.getCapacity();
			hdstAvailSpace =hdst.getAvailableSpace();
			Log.printLine("Capacity="+hdstCap+" Available="+hdstAvailSpace);
			/*CloudFile fd1 =hdst.getCloudFile(path);
			ciphertext=fd1.getCipherData("cText");
			cipherKey=fd1.getCipherData("cKey");
			String dectext=decrypt(ciphertext,cipherKey);
					
			Log.printLine("decrypted text "+ dectext);*/
			String site = "Search.jsp" ;
        	 response.setStatus(response.SC_MOVED_TEMPORARILY);
        	 response.setHeader("Location", site); 
			 }
		    
		    
		/*String text = "";
		String path=null;
		for(Part part : request.getParts()){
	         path=getFileName(part);
	         Log.printLine("file path "+ path);
	        FileInputStream inFile = new FileInputStream(path);
			BufferedInputStream bin = new BufferedInputStream(inFile);
	        int character;
	        
	        while((character=bin.read())!=-1) {
	            text = text + (char)character;
	        }
	        bin.close();
	        inFile.close();
	    }
		
		Log.printLine("actual text "+ text);
		
		 String cipherKey = "pro"; // 128 bit key
	        
	      
	          String ciphertext= createCipherText(path,cipherKey);
	          //System.out.print("encrypted text is "+ciphertext);
	          HashMap<String,Integer> hm;
	          hm=buildMap(path,cipherKey);
	          Log.printLine("enctext "+ciphertext);
	          Log.printLine("file path "+ path);
	          File log = new File("ListOfFilesNew1.txt");
	        
	        		    try{
	        		    if(log.exists()==false){
	        		            System.out.println("We had to make a new file at "+log.getAbsolutePath());
	        		            log.createNewFile();
	        		    }
	        		    System.out.println("exists at "+log.getAbsolutePath());
	        		    PrintWriter out = new PrintWriter(new FileWriter(log, true));
	        		    out.append(path + "\n");
	        		    out.close();
	        		    }catch(IOException e){
	        		        System.out.println("COULD NOT LOG!!");
	        		    }
	          
	        		    Log.printLine("reading out the list of files");
	        		    
	        		    FileInputStream inFile = new FileInputStream("ListOfFilesNew1.txt");
	        			BufferedInputStream bin = new BufferedInputStream(inFile);
	        	        int character;
	        	        String temptext="";
	        	        while((character=bin.read())!=-1) {
	        	            temptext = temptext + (char)character;
	        	        }
	        	        bin.close();
	        	        inFile.close();
	        	        Log.printLine(temptext);
	        	        Log.printLine("finished");
	        	        Stopwords s=new Stopwords();
	        	        
	        	      
	        	        try {
							s.RemoveStopWords(path);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	        		    
	          CloudFile file2 = null;
			try {
				file2 = new CloudFile(path,2);
			} catch (ParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				file2.addCipherData(ciphertext, cipherKey,hm);
				//Log.printLine(file2.getName());
				CloudSim.startSimulation();
				
				double timetaken = hdst.addCloudFile(file2);
				hdstCap =hdst.getCapacity();
				hdstAvailSpace =hdst.getAvailableSpace();
				Log.printLine("Capacity="+hdstCap+" Available="+hdstAvailSpace);
				CloudFile fd1 =hdst.getCloudFile(path);
				ciphertext=fd1.getCipherData("cText");
				cipherKey=fd1.getCipherData("cKey");
				String dectext=decrypt(ciphertext,cipherKey);
						
				Log.printLine("decrypted text "+ dectext);
				String site = "UploadFile.jsp" ;
	        	 response.setStatus(response.SC_MOVED_TEMPORARILY);
	        	 response.setHeader("Location", site); 
						*/
						

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
