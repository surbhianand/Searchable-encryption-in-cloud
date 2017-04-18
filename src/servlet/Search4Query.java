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
		
		 
		/* try {
	           // BufferedReader in = new BufferedReader(new FileReader(
	             //       "C:\\Users\\Shubham\\Desktop\\major1\\repeat.txt"));
	            String str;
	            long startTime = System.nanoTime();
	            int count=0;
	            FileWriter ofstream = new FileWriter("D:\\major\\abc\\repeatcachet.txt");  // after run, you can see the output file in the specified location
	            BufferedWriter iout = new BufferedWriter(ofstream);
	            while ((str = in.readLine()) != null) {
	            		String key=str;
	    				String d=2;
	    				System.out.println("d "+d);
	    				Integer di=2;
	    			    System.out.println("cAme here hurrrryyyyyyyyy"+ str);
	    			    if(cache.containsKey(key))
	    			    {
	    			    	HashMap<Integer,List< List<Pair<String,Integer>>>> fr = cache.get(key);
	    			    	cache.put(key, fr);
	    			    }
	    			    else
	    			    {
	    			    	List<String> dataToBeDisplayed=new ArrayList<String>();
		    			    TrieUser2.finalResult=new HashMap<Integer, List< List<Pair<String, Integer>>> >();
		    				if(!User.isTrieDictCreated)
		    				{	
		    					 System.out.println("cAme here");
		    					TrieUser2.t=new Trie2();
		    			     
		    			        	  FileReader file_to_read2=new FileReader("D:\\major\\abc\\unique_stem_words.txt"); // you can change file path.
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
	    			count=count+1;
	    			if(count%50==0)
	    			{
	    				long stopTime = System.nanoTime();
	    				float tt = (stopTime - startTime)/1000000000;
	    				iout.write("Time taken "+tt);
			     		 iout.write("\n");
	    			}
	            }
	            in.close();
	            iout.close();
	            ofstream.close();
	            
	        } catch (IOException e) {
	        }*/
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	Float score(Integer a,Integer b)
	{
		return (float) (a+b);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
	   	 
	   	 
		
	}
	
	
	
}

	

