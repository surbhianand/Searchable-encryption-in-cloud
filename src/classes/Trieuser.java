package classes;
/*
 *  Java Program to Implement Trie
 */
 
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.TimeoutException;

import org.cloudbus.cloudsim.Log;

import classes.MutableBloomierFilter;
//import main.java.edu.utexas.ece.mpc.bloomier.Pair;
 
class TrieNode 
{
    char content; 
    boolean isEnd; 
    int count;  
    LinkedList<TrieNode> childList; 
 
    /* Constructor */
    public TrieNode(char c)
    {
        childList = new LinkedList<TrieNode>();
        isEnd = false;
        content = c;
        count = 0;
    }  
    public TrieNode subNode(char c)
    {
        if (childList != null)
            for (TrieNode eachChild : childList)
                if (eachChild.content == c)
                    return eachChild;
        return null;
    }
}
 
class Trie
{
    public static TrieNode root;
    
     /* Constructor */
    public Trie()
    {
        root = new TrieNode(' '); 
    }
   /* public static void bloom(String file){
		String temp="";
		System.out.println("Traversing global trie");
		traverse(root,temp,file);
		//System.out.println("contain" +bloomFilter.contains("csc"));
		System.out.println("Serialaing");
		//serialize();
	}*/
     /* Function to insert word */
    public void insert(String word)
    {    
        TrieNode current = root; 
        for (char ch : word.toCharArray() )
        {
            TrieNode child = current.subNode(ch);
            if (child != null)
                current = child;
            else 
            {
                 current.childList.add(new TrieNode(ch));
                 current = current.subNode(ch);
            }
        }
        current.count++;
        current.isEnd = true;
    }
    public static void traverse(TrieNode root,String temp,String file)
	{
	    int len = root.childList.size();
	    if(len==0)
	    	return ;
	    for(int i=0;i<len;i++)
	    {
	    	TrieNode child = root.childList.get(i);
	    	String temp2 = temp + child.content;
	    	if(child.isEnd)
	    	{
	    		
	    		System.out.println("Before adding "+temp2+" "+file+" "+child.count);
	    		
	    		Pair<String,Integer> p= new Pair<String, Integer>(file,child.count);
	    		
	    		 
	    		/*if(BloomierObject.bloomierFilter.get(temp2) != null){
	    			
	    			List<Pair<String,Integer>> myListHere=new ArrayList<Pair<String, Integer>>();
	    			myListHere=BloomierObject.bloomierFilter.get(temp2);
	    			
	    			for(int j=0;j<myListHere.size();j++)
	    			{
	    				System.out.println("until now "+myListHere.get(j).getL()+" "+myListHere.get(j).getR());
	    			}
	    		myListHere.add(p);
	    		
	    		BloomierObject.bloomierFilter.set(temp2, myListHere);
	    			
	    		}
	    		else{
	    			
	    		List<Pair<String,Integer>> myList=new ArrayList<Pair<String,Integer>>();
	    		myList.add(p);
	    		BloomierObject.originalMap.put(temp2,myList);
	    		//BloomObject.hm.put(temp2,myList);
	    		try {
					BloomierObject.bloomierFilter = new MutableBloomierFilter<String, List<Pair<String,Integer>>>(BloomierObject.originalMap, BloomierObject.originalMap.keySet().size() * 10, 10, 32,
					        10000);
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		}*/
	    		
	    		
	    		
                 if(BloomObject.bloomFilter.contains(temp2))
                    {
	    			
	    			List<Pair<String,Integer>> myListHere=new ArrayList<Pair<String, Integer>>();
	    			myListHere=BloomObject.hm.get(temp2);
	    			
	    			for(int j=0;j<myListHere.size();j++)
	    			{
	    				System.out.println("until now "+myListHere.get(j).getL()+" "+myListHere.get(j).getR());
	    			}
	    		myListHere.add(p);
	    		
	    		BloomObject.hm.put(temp2, myListHere);
	    			
	    		}
	    		else{
	    			
	    		List<Pair<String,Integer>> myList=new ArrayList<Pair<String,Integer>>();
	    		myList.add(p);	    		
	    		BloomObject.hm.put(temp2,myList);
	    		
	    		}
	    		
	    	}
	    	traverse(child,temp2,file);
	    }
	}
    
    
    
    /* Function to search for word */
    public boolean search(String word)
    {
        TrieNode current = root;  
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
        TrieNode current = root;
        for (char ch : word.toCharArray()) 
        { 
            TrieNode child = current.subNode(ch);
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
	/*public static void serialize(){
		
		
		File log = new File("SerializedBloomFilterNew1.txt");
		 
		 if(log.exists()==false){
	            System.out.println("new created at"+log.getAbsolutePath());
	           
	    }
		 if(log.exists()==true){
			 log.delete();
	            System.out.println("file alreafy existed"+log.getAbsolutePath());
	           
	    }
		 try {
			log.createNewFile();
			PrintWriter out = new PrintWriter(new FileWriter(log, true));
		
		 String temp="";
		 bloomTraverse(root,temp);
		 out.close();
		 FileInputStream inFile;
		 System.out.println("Finally serializable");
		
			inFile = new FileInputStream("SerializedBloomFilterNew1.txt");
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			

		
	}*/
}    


/*class GlobalTrieNode 
{
    char content; 
    boolean isEnd; 
    int count;  
    LinkedList<GlobalTrieNode> childList;
    List<Pair<String,Integer>> pairList;
    
     Constructor 
    public GlobalTrieNode(char c)
    {
        childList = new LinkedList<GlobalTrieNode>();
        isEnd = false;
        content = c;
        count = 0;
        pairList = new ArrayList<Pair<String,Integer>>();
    }  
    public GlobalTrieNode subNode(char c)
    {
        if (childList != null)
            for (GlobalTrieNode eachChild : childList)
                if (eachChild.content == c)
                    return eachChild;
        return null;
    }
}
 
class GlobalTrie
{
    public GlobalTrieNode root;
 
      Constructor 
    public GlobalTrie()
    {
        root = new GlobalTrieNode(' '); 
    }
      Function to insert word 
    public void insert(String word,Pair<String,Integer> pp)
    {   
        GlobalTrieNode current = root; 
        for (char ch : word.toCharArray() )
        {
            GlobalTrieNode child = current.subNode(ch);
            if (child != null)
                current = child;
            else 
            {
                 current.childList.add(new GlobalTrieNode(ch));
                 current = current.subNode(ch);
            }
        }
        current.count++;
        current.isEnd = true;
        current.pairList.add(pp);
    }
    
    
     Function to search for word 
    public boolean search(String word)
    {
        GlobalTrieNode current = root;  
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
}    
 Class Trie Test 
public class Trieuser
{
	static double falsePositiveProbability = 0.1;
    static int expectedSize = 100;
	public static GlobalTrie tg = new GlobalTrie();
	public static File log = null;
	public static PrintWriter out;
	public static void traversefile(TrieNode root,String temp,String name)
	{
	    int len = root.childList.size();
	    if(len==0)
	    	return ;
	    for(int i=0;i<len;i++)
	    {
	    	TrieNode child = root.childList.get(i);
	    	String temp2 = temp + child.content;
	    	if(child.isEnd)
	    	{
	    		Pair<String,Integer> pp = new Pair<String,Integer>(name,child.count);
	    		bloomFilter.add(,);
	    		//tg.insert(temp2, pp);
	    	}
	    	traversefile(child,temp2,name);
	    }
	}
	public static void bloom(){
		String temp="";
		System.out.println("Traversing global trie");
		globaltraverse(tg.root,temp);
		System.out.println("contain" +bloomFilter.contains("csc"));
		System.out.println("Serialaing");
		serialize();
	}

	
	public static void bloomTraverse(GlobalTrieNode root,String temp)
	{
	    int len = root.childList.size();
	    if(len==0)
	    	return ;
	    for(int i=0;i<len;i++)
	    {
	    	GlobalTrieNode child = root.childList.get(i);
	    	String temp2 = temp + child.content;
	    	if(child.isEnd)
	    	{
	    		
	    		System.out.println(","+temp2+">");
	    		out.append(","+temp2+">");
	    		List<Pair<String,Integer>> plist = child.pairList;
	    		bloomFilter.add(temp2,child.pairList);
	    		int plen = plist.size();
	    		System.out.println("(");
	    		out.append("(");
	    		for(i=0;i<plen;i++)
	    		{
	    			System.out.println("{"+plist.get(i).getL()+"," +plist.get(i).getR() +"}");
	    			out.append("{"+plist.get(i).getL()+"," +plist.get(i).getR() +"}");
	    			System.out.println(plist.get(i).getL()+" "+plist.get(i).getR());
	    		}
	    		System.out.println(")");
	    		out.append(")");
	    	}
	    	bloomTraverse(child,temp2);
	    }
	}
	
	public static void globaltraverse(GlobalTrieNode root,String temp)
	{
	    int len = root.childList.size();
	    if(len==0)
	    	return ;
	    for(int i=0;i<len;i++)
	    {
	    	GlobalTrieNode child = root.childList.get(i);
	    	String temp2 = temp + child.content;
	    	if(child.isEnd)
	    	{
	    		
	    		System.out.println(temp2+" "+child.count);
	    		List<Pair<String,Integer>> plist = child.pairList;
	    		bloomFilter.add(temp2,child.pairList);
	    		int plen = plist.size();
	    		for(i=0;i<plen;i++)
	    		{
	    			System.out.println(plist.get(i).getL()+" "+plist.get(i).getR());
	    		}
	    	}
	    	globaltraverse(child,temp2);
	    }
	}


	
	
    public static void main(String[] args)
    {
        Trie t = new Trie(); 
        t.insert("abcd");
        t.insert("abcdef");
        t.insert("xbcd");
        Trie t1 = new Trie(); 
        t1.insert("abcd");
        t1.insert("abcdef");
        t1.insert("xbcd");
        String temp = "";
        traversefile(t.root,temp,"file1");
        temp = "";
        traversefile(t1.root,temp,"file2");
        temp="";
        globaltraverse(tg.root,temp);
    }


}


*/