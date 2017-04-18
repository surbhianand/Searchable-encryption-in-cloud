package servlet;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import classes.BloomObject;
import classes.Pair;
import classes.Stemmer;
import classes.User;


@WebServlet("/Search6Query")
public class Search6Query extends HttpServlet {
	public static HashMap<Integer, List<String>> indexes;
	private static final long serialVersionUID = 1L;
	public static HashMap<Integer,List< List<Pair<String,Integer>>>> finalResult;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search6Query() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    Float score(Integer a,Integer b)
	{
		return (float) (1+Math.log(a)*Math.log(1+1/b));
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String key=request.getParameter("keyword");
		Stemmer s1 = new Stemmer();
		key=s1.stem(key);
		finalResult=new HashMap<Integer, List< List<Pair<String, Integer>>> >();
		
		String d=request.getParameter("precision");
		Integer di=Integer.parseInt(d);
		
		if(!User.isRefStringTrieCreated)
		{
		BufferedWriter out = null;
		  String line,token,line_no1,line_no2;
		  
		 FileReader file_to_read2 = null;
		try {
			file_to_read2 = new FileReader("D:\\major\\abc\\refstrwithaddress2.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	        Scanner filesc2=new Scanner(file_to_read2);//scanner for file
	        TrieUser3.t = new Trie3(); 
	        while(filesc2.hasNextLine())
	        {
	        line=filesc2.nextLine();
	        Scanner linesc=new Scanner(line);//scanner for line
	            while(linesc.hasNext())
	            { 	
	            token=linesc.next();	
	            line_no1=linesc.next();
	            line_no2=linesc.next();
	            Integer l1=Integer.parseInt(line_no1);
	            Integer l2=Integer.parseInt(line_no2);
	            
	            TrieUser3.t.insert(token,l1,l2);
	            System.out.println("inserted "+token);
	            }
	           
	            linesc.close();
	        }
	        System.out.println("Trie done");
	        User.isRefStringTrieCreated=true;
	}
		indexes = new HashMap<Integer,List<String>>();
		 String temp = "";
		 Integer prevrow[]=new Integer[key.length()+1];
	        for(int i=0;i<=key.length();i++)
	        	prevrow[i]=i;
	        System.out.println("loooooooooooooooooooking "+key);
	        di+=3;
	        TrieUser3.traversefile(TrieUser3.t.root,temp,di,prevrow,key);	
		
	        
	        int cou=0;
    		Iterator entries = Search6Query.indexes.entrySet().iterator();
    		while (entries.hasNext()) {
    		  Entry thisEntry = (Entry) entries.next();
    		  Integer key1 = (Integer) thisEntry.getKey();
    		  List<String> value = (List<String>) thisEntry.getValue();
    		  
    		  for(int i1=0;i1<value.size();i1++)
    		  {
    			  cou++;
    			  if(cou>10)
    				  break;
    			  List<Pair<String,Integer>> myList=new ArrayList<Pair<String, Integer>>();
					
					myList=BloomObject.hm.get(value.get(i1));
					
					if(!finalResult.containsKey(key1))
					{
						List< List<Pair<String,Integer>>> inter = new ArrayList<List<Pair<String, Integer>>>();
						inter.add(myList);
						finalResult.put(key1,inter);
					}
					else
					{
						List< List<Pair<String,Integer>>> inter = finalResult.get(key1);
						finalResult.remove(key1);
						inter.add(myList);
						finalResult.put(key1,inter);
					}	  
    		  }
    		  
    		}
    		 List<String> dataToBeDisplayed=new ArrayList<String>();
    		 HashMap<String,Integer> filevisited = new HashMap<String, Integer>();
     	    ArrayList<String> uniquefiles = new ArrayList<String>();
     	    HashMap<Float,String> ranked = new HashMap<Float,String>();
		        for(int i=0;i<=di;i++)
		        {
		        	if(finalResult.containsKey(i))
		        	{
		        		List< List<Pair<String, Integer>>> wordlist = finalResult.get(i);
		        		
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
	
}


class TrieNode3
{
    char content; 
    boolean isEnd; 
    int count;  
    int starting;
    int ending;
    LinkedList<TrieNode3> childList; 
 
    /* Constructor */
    public TrieNode3(char c)
    {
        childList = new LinkedList<TrieNode3>();
        isEnd = false;
        content = c;
        count = 0;
    }  
    public TrieNode3 subNode(char c)
    {
        if (childList != null)
            for (TrieNode3 eachChild : childList)
                if (eachChild.content == c)
                    return eachChild;
        return null;
    }
}
 
class Trie3
{
    public static TrieNode3 root;
    
     /* Constructor */
    public Trie3()
    {
        root = new TrieNode3(' '); 
    }
     /* Function to insert word */
    public void insert(String word,int start,int end)
    {    
    	//System.out.println("inserting");
        TrieNode3 current = root; 
        for (char ch : word.toCharArray() )
        {
            TrieNode3 child = current.subNode(ch);
            if (child != null)
                current = child;
            else 
            {
                 current.childList.add(new TrieNode3(ch));
                 current = current.subNode(ch);
            }
        }
        current.count++;
        current.isEnd = true;
        current.starting=start;
        current.ending=end;
    }
    public static void traverse(TrieNode3 root,String temp,String file)
	{
	    int len = root.childList.size();
	    if(len==0)
	    	return ;
	    for(int i=0;i<len;i++)
	    {
	    	TrieNode3 child = root.childList.get(i);
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
        TrieNode3 current = root;  
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
        TrieNode3 current = root;
        for (char ch : word.toCharArray()) 
        { 
            TrieNode3 child = current.subNode(ch);
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
class TrieUser3
{
	public static Trie3 t;
	public static HashMap<Integer,List< List<Pair<String,Integer>>>> finalResult;
	
	 static int min(int x,int y,int z)
	 {
	     if (x < y && x <z) return x;
	     if (y < x && y < z) return y;
	     else return z;
	 }

	 static int editDistDP(String str1, String str2, int m, int n)
	 {
	     // Create a table to store results of subproblems
	     int dp[][] = new int[m+1][n+1];
	   
	     // Fill d[][] in bottom up manner
	     for (int i=0; i<=m; i++)
	     {
	         for (int j=0; j<=n; j++)
	         {
	             // If first string is empty, only option is to
	             // isnert all characters of second string
	             if (i==0)
	                 dp[i][j] = j;  // Min. operations = j
	   
	             // If second string is empty, only option is to
	             // remove all characters of second string
	             else if (j==0)
	                 dp[i][j] = i; // Min. operations = i
	   
	             // If last characters are same, ignore last char
	             // and recur for remaining string
	             else if (str1.charAt(i-1) == str2.charAt(j-1))
	                 dp[i][j] = dp[i-1][j-1];
	   
	             // If last character are different, consider all
	             // possibilities and find minimum
	             else
	                 dp[i][j] = 1 + min(dp[i][j-1],  // Insert
	                                    dp[i-1][j],  // Remove
	                                    dp[i-1][j-1]); // Replace
	         }
	     }

	     return dp[m][n];
	 }


	
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
	public static void traversefile(TrieNode3 root,String temp,Integer d,Integer prevrow[],String word)
	{
	    int len = root.childList.size();
	    if(len==0)
	    	return ;
	    
	    System.out.println("here");
	    for(int i=0;i<len;i++)
	    {
	    	System.out.println("Inside for");
	    	TrieNode3 child = root.childList.get(i);
	    	String temp2 = temp + child.content;
	    	Integer nwrow[] = calculate(prevrow,word,temp2);
	    	if(child.isEnd && nwrow[word.length()]<=d)
	    	{	
	    		System.out.println("edit distance of "+nwrow[word.length()]+ "found for word "+temp2);
		        Integer lineno1_int=child.starting;
		        Integer lineno2_int=child.ending;
		        
	    		try (Stream<String> lines = Files.lines(Paths.get("D:\\major\\abc\\partition_strings.txt"))) {
	    			List<String> getPart = lines.skip(lineno1_int-1).limit(lineno2_int-lineno1_int).collect(Collectors.toList());;
	    			for(int in=0;in<getPart.size();in++){
	    				if(!getPart.get(in).equals("")){
	    					 String[] partString = getPart.get(in).split("\\s+");
	    					 int edd=editDistDP(partString[0] ,word, partString[0].length(), word.length());
	    		    		 if(edd<=d-3){
	    		    			 if (BloomObject.hm.get(partString[0])!=null) { 
	    		    				 if(Search6Query.indexes.get(edd) != null)
	    		    				 {
	    		    					List<String> li= Search6Query.indexes.get(edd);
	    		    					li.add(partString[0]);
	    		    					Search6Query.indexes.remove(edd);
	    		    					Search6Query.indexes.put(edd, li);
	    		    				 }
	    		    				 else
	    		    				 {
	    		    					List<String> l=new ArrayList<String>();
	    		    					l.add(partString[0]);
	    		    					Search6Query.indexes.put(edd, l);
	    		    				 }
	    		    			 }
	    		    		 }
	    				}
	    			}
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    				   
	    				   
	    			 	/*List<Pair<String,Integer>> myList=new ArrayList<Pair<String, Integer>>();
						
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
						}*/
						
					
	    	}
	    	traversefile(child,temp2,d,nwrow,word);
	    }
	}
	
	
}
	
