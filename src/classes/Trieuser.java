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
    }
	