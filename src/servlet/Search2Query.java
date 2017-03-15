package servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
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

/**
 * Servlet implementation class Search2Query
 */
@WebServlet("/Search2Query")
public class Search2Query extends HttpServlet {
	private static final long serialVersionUID = 1L;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search2Query() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				String key=request.getParameter("keyword");
				String d=request.getParameter("precision");
				Integer di=Integer.parseInt(d);
			    List<String> dataToBeDisplayed=new ArrayList<String>();
			    TrieUser2.finalResult=new HashMap<Integer, List< List<Pair<String, Integer>>> >();
				if(!User.isTrieDictCreated)
				{	

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
			       // System.out.println(dataToBeDisplayed);
			       System.out.println("convertinnnnnnnnnnnng");
			        //String json=(JSONArray)JSONSerializer.toJSON(objList)
			        String json = new Gson().toJson(dataToBeDisplayed);
			        System.out.println("printed iiiiiiiiiiiiiiiiii"+json);

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


class TrieNode2 
{
    char content; 
    boolean isEnd; 
    int count;  
    LinkedList<TrieNode2> childList; 
 
    /* Constructor */
    public TrieNode2(char c)
    {
        childList = new LinkedList<TrieNode2>();
        isEnd = false;
        content = c;
        count = 0;
    }  
    public TrieNode2 subNode(char c)
    {
        if (childList != null)
            for (TrieNode2 eachChild : childList)
                if (eachChild.content == c)
                    return eachChild;
        return null;
    }
}
 
class Trie2
{
    public static TrieNode2 root;
    
     /* Constructor */
    public Trie2()
    {
        root = new TrieNode2(' '); 
    }
     /* Function to insert word */
    public void insert(String word)
    {    
    	//System.out.println("inserting");
        TrieNode2 current = root; 
        for (char ch : word.toCharArray() )
        {
            TrieNode2 child = current.subNode(ch);
            if (child != null)
                current = child;
            else 
            {
                 current.childList.add(new TrieNode2(ch));
                 current = current.subNode(ch);
            }
        }
        current.count++;
        current.isEnd = true;
    }
    public static void traverse(TrieNode2 root,String temp,String file)
	{
	    int len = root.childList.size();
	    if(len==0)
	    	return ;
	    for(int i=0;i<len;i++)
	    {
	    	TrieNode2 child = root.childList.get(i);
	    	String temp2 = temp + child.content;
	    	if(child.isEnd)
	    	{
	    		
	    		System.out.println(temp2+" "+file+" "+child.count);
	    		
	    	}
	    	traverse(child,temp2,file);
	    }
	}
    
    
    
    /* Function to search for word */
    public boolean search(String word)
    {
        TrieNode2 current = root;  
        for (char ch : word.toCharArray() )
        {
            if (current.subNode(ch) == null)
                return false;
            else
                current = current.subNode(ch);
        }      
        if (current.isEnd == true) 
            return true;
        return false;
    }
    /* Function to remove a word */
    public void remove(String word)
    {
        if (search(word) == false)
        {
            System.out.println(word +" does not exist in trie\n");
            return;
        }             
        TrieNode2 current = root;
        for (char ch : word.toCharArray()) 
        { 
            TrieNode2 child = current.subNode(ch);
            if (child.count == 1) 
            {
                current.childList.remove(child);
                return;
            } 
            else 
            {
                child.count--;
                current = child;
            }
        }
        current.isEnd = false;
    }
}
    
//Class Trie Test 
class TrieUser2
{
	public static Trie2 t;
	public static HashMap<Integer,List< List<Pair<String,Integer>>>> finalResult;
	
	public static Integer[] calculate(Integer prev[],String word,String temp2)
	{
		Integer nwrow[]=new Integer[word.length()+1];
		Integer j = temp2.length();
		for(int i=0;i<=word.length();i++)
		{
			if(i==0)
				nwrow[i]=j;
			else if(word.charAt(i-1)==temp2.charAt(j-1))
			{
				nwrow[i]=prev[i-1];
			}
			else
			{
				nwrow[i]=1+min(prev[i-1],min(nwrow[i-1],prev[i]));
			}
		}
		return nwrow;
	}
	private static Integer min(Integer x, Integer y) {
		// TODO Auto-generated method stub
		if(x<y)
			return x;
		return y;
	}
	public static void traversefile(TrieNode2 root,String temp,Integer d,Integer prevrow[],String word)
	{
	    int len = root.childList.size();
	    if(len==0)
	    	return ;
	    
	    //System.out.println("here");
	    for(int i=0;i<len;i++)
	    {
	    	TrieNode2 child = root.childList.get(i);
	    	String temp2 = temp + child.content;
	    	Integer nwrow[] = calculate(prevrow,word,temp2);
	    	if(child.isEnd && nwrow[word.length()]<=d)
	    	{
	    		//System.out.println("distance "+nwrow[word.length()]+" "+temp2);
	    		
	    		 if (BloomObject.hm.get(temp2)!=null) { 
					
	    			 	List<Pair<String,Integer>> myList=new ArrayList<Pair<String, Integer>>();
						
						myList=BloomObject.hm.get(temp2);
						if(!finalResult.containsKey(nwrow[word.length()]))
						{
							List< List<Pair<String,Integer>>> inter = new ArrayList<List<Pair<String, Integer>>>();
							inter.add(myList);
							finalResult.put(nwrow[word.length()],inter);
						}
						else
						{
							List< List<Pair<String,Integer>>> inter = finalResult.get(nwrow[word.length()]);
							finalResult.remove(nwrow[word.length()]);
							inter.add(myList);
							finalResult.put(nwrow[word.length()],inter);
						}
						
						}
						
				       
	    		
	    	}
	    	traversefile(child,temp2,d,nwrow,word);
	    }
	}
	
	
}
	

