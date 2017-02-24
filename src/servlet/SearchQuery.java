package servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
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
import classes.Trieuser;

/**
 * Servlet implementation class SearchQuery
 */
@WebServlet("/SearchQuery")
public class SearchQuery extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static SecretKeySpec secretKey;
    private static byte[] key;
    public static HashMap<String,Integer> wd=new HashMap<String,Integer>();
    public static Trieuser trieUser;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchQuery() {
        super();
        // TODO Auto-generated constructor stub
    }
    public static boolean check_for_word() {
        // System.out.println(word);
        try {
            BufferedReader in = new BufferedReader(new FileReader("D:\\major\\abc\\words.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                wd.put(str,1);
            }
            in.close();
        } catch (IOException e) {
        }

        return false;
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
		
		System.out.print("finished reading serialaized bloomfilter");
		FileInputStream inFile = new FileInputStream("SerializedBloomFilter.txt");
		BufferedInputStream bin = new BufferedInputStream(inFile);
        int character;
        String temptext="";
        while((character=bin.read())!=-1) {
            temptext = temptext + (char)character;
        }
        bin.close();
        inFile.close();
        Log.printLine(temptext);
        System.out.print("finished reading serialaized bloomfilter");
        
        
        
	        
	        
	        
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
	    public static void bringText(String ciphertext,String key)
	    {
			String temp = "";
			for(int i=0;i<ciphertext.length();i++)
			{
				if(ciphertext.charAt(i)!='$')
				{
					temp = temp + ciphertext.charAt(i);
				}
				else
				{
					temp = decrypt(temp,key);
					 Log.printLine(temp);
					temp = "";
				}
			}
	    }
	 
	   
	    
	    
	    
}
