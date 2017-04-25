package servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

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

import com.google.gson.Gson;

import classes.BloomObject;
//import classes.BloomierFilter;
import classes.BloomierObject;
import classes.Pair;
import classes.Stemmer;
import classes.Stopwords;
import classes.User;
import classes.spellcheck;
import classes.wordnet;

/**
 * Servlet implementation class SearchQuery
 */
@WebServlet("/Search5Query")
public class Search5Query extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static SecretKeySpec secretKey;
    private static byte[] key;
    public static HashMap<String,Integer> wd=new HashMap<String,Integer>();
    public static HashMap<Integer,List< List<Pair<String,Integer>>>> finalResult;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search5Query() {
        super();
        // TODO Auto-generated constructor stub
    }
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String key=request.getParameter("keyword");
		String d=request.getParameter("precision");
		spellcheck checker = new spellcheck();
		wordnet similarity = new wordnet();
		 HashMap<Integer,List<List<Pair<String,Integer>>>> finalResult=new HashMap<Integer, List< List<Pair<String, Integer>>> >();
		
		 List<String> dataToBeDisplayed=new ArrayList<String>();
		key = checker.getSpell(key);
		Set<String> set = similarity.getSimilarWords(key);
		Iterator iter = set.iterator();
		while (iter.hasNext()) {
		    String temp2=(String) iter.next();
		 
   		 if (BloomObject.hm.get(temp2)!=null) { 
				
   			 	List<Pair<String,Integer>> myList=new ArrayList<Pair<String, Integer>>();
					
					myList=BloomObject.hm.get(temp2);
					if(!finalResult.containsKey(1))
					{
						List< List<Pair<String,Integer>>> inter = new ArrayList<List<Pair<String, Integer>>>();
						inter.add(myList);
						finalResult.put(1,inter);
					}
					else
					{
						List< List<Pair<String,Integer>>> inter = finalResult.get(1);
						finalResult.remove(1);
						inter.add(myList);
						finalResult.put(1,inter);
					}
					
					}					
		}
		
		
		
		HashMap<String,Integer> filevisited = new HashMap<String, Integer>();
	    ArrayList<String> uniquefiles = new ArrayList<String>();
	    HashMap<Float,String> ranked = new HashMap<Float,String>();
       
        		List< List<Pair<String, Integer>>> wordlist = finalResult.get(1);
        		
        		for(int j=0;j<wordlist.size();j++)
        		{
        			List<Pair<String,Integer>> indword = wordlist.get(j);
        			for(int k=0;k<indword.size();k++)
        			{
        				Pair<String,Integer> p = indword.get(k);
        				ranked.put(score(p.getR(),indword.size()), p.getL());
        			}
        		}
       
        
        ArrayList<String> filetodisplay = new ArrayList<String>();
		Iterator it = ranked.entrySet().iterator();
	    while (it.hasNext()) {
	        HashMap.Entry pair = (HashMap.Entry)it.next();
	        filetodisplay.add((String) pair.getValue());
	        it.remove(); 
	    }
	    Collections.reverse(filetodisplay);
	 
	    for(int j=0;j<filetodisplay.size();j++)
	    {
	    	if(!filevisited.containsKey(filetodisplay.get(j)))
	    	{
	   
	    		filevisited.put(filetodisplay.get(j), 1);
	    		uniquefiles.add(filetodisplay.get(j));
	    	}
	   }
	   
	   
        for(int j=0;j<uniquefiles.size();j++)
	    {
	    	FileInputStream inFile = new FileInputStream(uniquefiles.get(j));
	    	System.out.println(uniquefiles.get(j));
			BufferedInputStream bin = new BufferedInputStream(inFile);
	        int ch;
	        String temptext="";
	        while((ch=bin.read())!=-1) {
	            temptext = temptext + (char)ch;
	        }
	        
	        bin.close();
	        inFile.close();
	        dataToBeDisplayed.add(temptext);
	    	
	    	
	    }
        Gson gson=new Gson();
        String json = new Gson().toJson(dataToBeDisplayed);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);		
	}
	
	Float score(Integer a,Integer b)
	{
		return (float) (a+b);
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


