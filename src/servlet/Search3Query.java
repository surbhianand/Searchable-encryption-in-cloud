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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

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
import classes.LRUCache;

/**
 * Servlet implementation class SearchQuery
 */
@WebServlet("/Search3Query")
public class Search3Query extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static SecretKeySpec secretKey;
    private static byte[] key;
    public static HashMap<String,Integer> wd=new HashMap<String,Integer>();
    public static LRUCache<String, HashMap<Integer,List<List<Pair<String,Integer>>>>> cache = LRUCache.newInstance(20);  
    /**q
     * @see HttpServlet#HttpServlet()
     */
    public Search3Query() {
        super();
        // TODO Auto-generated constructor stub
    }
    public static boolean check_for_word() {
        // System.out.println(word);
        try {
            BufferedReader in = new BufferedReader(new FileReader(User.location_uniquestemwords));
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
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String key=request.getParameter("keyword");
		String d=request.getParameter("precision");
		Integer di=Integer.parseInt(d);
		Stemmer s1 = new Stemmer();
		key=s1.stem(key);
        List<String> dataToBeDisplayed=new ArrayList<String>();
       	HashMap<Integer,List<List<Pair<String,Integer>>>> fr=new HashMap<Integer, List<List<Pair<String, Integer>>>>();
       	if(cache.containsKey(key))
        {
        	fr = cache.get(key);
        	cache.put(key,fr);
        }
        else
        {
        	fr=search(key,di);
        	cache.put(key,fr);
        }	
        HashMap<Float,String> ranked = new HashMap<Float,String>();
		ArrayList<String> uniquefiles = new ArrayList<String>();
		for(int i=0;i<=di;i++)
		{
			if(fr.containsKey(i))
			{
			   	List< List<Pair<String, Integer>>> wordlist = fr.get(i);
			    for(int j=0;j<wordlist.size();j++)
			    {
			       	List<Pair<String,Integer>> indword = wordlist.get(j);
			       	for(int k=0;k<indword.size();k++)
			        {
			        	Pair<String,Integer> p = indword.get(k);
			        	ranked.put(score(p.getR(),indword.size()), p.getL());
			        }
			    }
			        		
			} 
			        	    
		}
		ArrayList<String> filetodisplay = new ArrayList<String>();
	    Iterator it = ranked.entrySet().iterator();
	    while (it.hasNext()) {
	    	HashMap.Entry pair = (HashMap.Entry)it.next();
	       	filetodisplay.add((String) pair.getValue());
	       	it.remove(); // avoids a ConcurrentModificationException
	       	}
	       	Collections.reverse(filetodisplay);
	       	HashMap<String,Integer> filevisited = new HashMap<String, Integer>();
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
	        dataToBeDisplayed.add(uniquefiles.get(j)+" : "+temptext);
	       	}
			Gson gson=new Gson();
			String json = new Gson().toJson(dataToBeDisplayed);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}
	
	public HashMap<Integer,List<List<Pair<String,Integer>>>> search(String key,Integer di)
	{
		HashMap<Integer,List<List<Pair<String,Integer>>>> finalResult= new HashMap<Integer, List<List<Pair<String, Integer>>>>();
		 HashMap<String,Integer> hm=new HashMap<String,Integer>();
		 String input = key;
		 
		 input.toLowerCase();
		 check_for_word();
		 Queue q = new LinkedList();
		 q.add(input);
		 q.add("$");
		 int count=0;
		 int num=0;
		 while(!q.isEmpty())
		 {
			 Object obj = new Object();
			 obj  = q.remove();
			 String temp = obj.toString();
			 if(num>100000)
			 {
				 break;
			 }
			 if(temp.equals("$"))
			 {
				 if(q.size()!=0)
				 {
					 count=count+1;
					 q.add("$");
				 }
				 if(count>di)
					 break;
			 }
			 else
			 {
				 if(wd.containsKey(temp))
				 {
					 System.out.print("Looking for "+temp+" "+num);
					 if (BloomObject.bloomFilter.contains(temp)) { 
						 	System.out.println("checked in bloom filter");
							List<Pair<String,Integer>> myList=new ArrayList<Pair<String, Integer>>();
							
							myList=BloomObject.hm.get(temp);
							System.out.println("list retreived from bloom filter");
							
							if(!finalResult.containsKey(count))
							{
								System.out.println("hash map already contains");
								List< List<Pair<String,Integer>>> inter = new ArrayList<List<Pair<String, Integer>>>();
								inter.add(myList);
								finalResult.put(count,inter);
							}
							else
							{
								System.out.println("hash map did'nt contain newly created");
								List< List<Pair<String,Integer>>> inter = finalResult.get(count);
								finalResult.remove(count);
								inter.add(myList);
								finalResult.put(count,inter);
							}
							
							
							for(int i=0;i<myList.size();i++)
							{
					
							 System.out.println(myList.get(i).getL()+" "+myList.get(i).getR()+"\n");
							}
							}
							else
							{
								System.out.println("Not present");	
							}
					 
					       
				 }
				 
				 
				 
				 
				 //delete
				 for(int i=0;i<temp.length();i++)
				 {
					 String temp2 = "";
					 for(int j=0;j<temp.length();j++)
					 {
						 if(i!=j)
						 {
							 temp2 = temp2 + temp.charAt(j);
						 }
					 }
					 if(!hm.containsKey(temp2) && temp2.length() > 0 )
					 {
						 hm.put(temp2, count);
						 q.add(temp2);
						 num+=1;
						 if(num>10000)
							 break;
					 }
				 }
				 //replace
				 for(int i=0;i<temp.length();i++)
				 {
					 String left = "";
					 String right = "";
					 for(int j=0;j<i;j++)
						 left=left+temp.charAt(j);
					 for(int j=i+1;j<temp.length();j++)
						 right=right+temp.charAt(j);
					 for(char j='a';j<='z';j++)
					 {
						 String temp2 = left + j + right;
						 if(!hm.containsKey(temp2)&& temp2.length()>0)
						 {
							 hm.put(temp2, count);
							 q.add(temp2);
							 num+=1;
							 if(num>10000)
								 break;
						 }
					 }
				 }
				 //insert
				 String lefti="";
				 for(int i=0;i<temp.length();i++)
				 {
					 String right = temp.substring(i);
					 for(char j='a';j<='z';j++)
					 {
						 String temp2 = lefti + j + right;
						 if(!hm.containsKey(temp2)&&temp2.length()>0)
						 {
							 hm.put(temp2, count);
							 q.add(temp2);
							 num+=1;
							 if(num>10000)
								 break;
						 }
					 }
					 lefti = lefti + temp.charAt(i);
				 }
				 for(char j='a';j<='z';j++)
				 {
					 String temp2 = lefti + j ;
					 if(!hm.containsKey(temp2)&&temp2.length()>0)
					 {
						 hm.put(temp2, count);
						 q.add(temp2);
						 num+=1;
						 if(num>10000)
							 break;	 
					 }
				 }
			 }
			 
			 System.out.println("out of else after checking for "+temp);
		 }
		
	return finalResult;	
		
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


