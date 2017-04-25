package servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cloudbus.cloudsim.Log;

import com.google.gson.Gson;

import classes.BloomObject;
import classes.Pair;
import classes.Stemmer;
import classes.User;
import classes.LRUCache;
/**
 * Servlet implementation class Search2Query
 */
@WebServlet("/Search4Query")
public class Search4Query extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static LRUCache<String, HashMap<Integer,List< List<Pair<String,Integer>>>>> cache = LRUCache.newInstance(20);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search4Query() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String key=request.getParameter("keyword");
		String d=request.getParameter("precision");
		Stemmer s1 = new Stemmer();
		key=s1.stem(key);
		List<String> dataToBeDisplayed=new ArrayList<String>();
		Integer di=Integer.parseInt(d);	
	   	if(cache.containsKey(key))
	    {
	    	HashMap<Integer,List< List<Pair<String,Integer>>>> fr = cache.get(key);
	    	cache.put(key, fr);
	    }
	    else
	    {
	    	
		    TrieUser2.finalResult=new HashMap<Integer, List< List<Pair<String, Integer>>> >();
		    if(!User.isTrieDictCreated)
		    {	
		  		System.out.println("cAme here");
		    	TrieUser2.t=new Trie2();
		    	FileReader file_to_read2=new FileReader(User.location_uniquestemwords); // you can change file path.
		    	Scanner filesc2=new Scanner(file_to_read2);//scanner for file
		    	String line="",token="";
		    	while(filesc2.hasNextLine())
		    	{
		    		line=filesc2.nextLine();
		    		Scanner linesc=new Scanner(line);//scanner for line
		    		while(linesc.hasNext())
		    		{
		    			token=linesc.next();
		    			TrieUser2.t.insert(token);
		    		}
		    		linesc.close();
		    			              
		    	}			
		  		User.isTrieDictCreated=true;
		    }				
			String temp = "";
		 	Integer prevrow[]=new Integer[key.length()+1];
		    for(int i=0;i<=key.length();i++)
		    	prevrow[i]=i;
		    TrieUser2.traversefile(TrieUser2.t.root,temp,di,prevrow,key);
		    cache.put(key, TrieUser2.finalResult);
	    }    
	    HashMap<String,Integer> filevisited = new HashMap<String, Integer>();
	        	ArrayList<String> uniquefiles = new ArrayList<String>();
	        	HashMap<Float,String> ranked = new HashMap<Float,String>();
			    for(int i=0;i<=di;i++)
			    {
			       	if(TrieUser2.finalResult.containsKey(i))
			        {
			        	List< List<Pair<String, Integer>>> wordlist = TrieUser2.finalResult.get(i);	
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
	        	 
	        	   // HashMap<String,Integer> filevisited = new HashMap<String, Integer>();
	        	for(int j=0;j<filetodisplay.size();j++){
	        	   	if(!filevisited.containsKey(filetodisplay.get(j)))
	        	    {
	        	   	    filevisited.put(filetodisplay.get(j), 1);
	        	    	uniquefiles.add(filetodisplay.get(j));
	        	    }
	        	}   
			    for(int j=0;j<uniquefiles.size();j++){
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
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	Float score(Integer a,Integer b)
	{
		return (float) (1+Math.log(a)*Math.log(1+1/b));
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
	   	 
	   	 
		
	}
	
	
	
}

	

