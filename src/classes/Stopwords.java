package classes;

import java.io.*;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

 
public class Stopwords {
	
	public static String removePunctuationWord(String original) {
		String result = "";
		char[] chars = original.toCharArray();
		for(char character : chars) {
			if((character >= 'a' && character <= 'z') || character == ' ' || (character >= 'A' && character <= 'Z') || character =='\n') {
				result += character;
			}
		}
		return result;
	}
	
	
public void RemoveStopWords(String file) throws Exception
{
        String a,s,line,token;
        //read the input file
        FileReader file_to_read=new FileReader(file); // you can change file path.
        FileWriter ofstream = new FileWriter("intermidiate.txt");  // after run, you can see the output file in the specified location
        BufferedWriter iout = new BufferedWriter(ofstream);
           Scanner filesc=new Scanner(file_to_read);//scanner for file
           int k=0;
           ArrayList<String> wordsList = new ArrayList<String>();
           String sCurrentLine;
           //System.out.println("hey there");
           String[] stopwords = new String[2000];
        try {
        	 FileReader fr=new FileReader(User.location_stopwords);//stopwordsloc here
             BufferedReader br= new BufferedReader(fr);
             while ((sCurrentLine = br.readLine()) != null){

                 //System.out.println(sCurrentLine);
                 stopwords[k]=sCurrentLine;
                 k++;
             }
             String text = filesc.useDelimiter("\\A").next();
             text=removePunctuationWord(text);
             StringBuilder builder = new StringBuilder(text);
             String[] words = builder.toString().split("\\s");
             for (String word : words){
                 wordsList.add(word);
             }
             for(int ii = 0; ii < wordsList.size(); ){

                 //System.out.println(wordsList.get(ii));
             	int x=0;
                 for(int jj = 0; jj < k; jj++){
                     if(stopwords[jj].contains(wordsList.get(ii).toLowerCase())){
                         wordsList.remove(ii);
                         x=1;
                         break;
                     }
                  }
                 if(x==0){
                 	ii++;
                 }
             }
             for (String str : wordsList){
             	//System.out.println(str);
                 iout.write(str);
                 iout.write(" ");
             }   
        } catch (IOException ioe) {
     	   ioe.printStackTrace();
     	}
     	finally
     	{ 
     	   try{
     	      if(iout!=null)
     		 iout.close();
     	   }catch(Exception ex){
     	       System.out.println("Error in closing the BufferedWriter"+ex);
     	    }
     	}
        BufferedWriter out = null;
        Stemmer s1 = new Stemmer();
        Trie trie = new Trie(); 
        try{
        FileReader file_to_read2=new FileReader("intermidiate.txt"); // you can change file path.
        Scanner filesc2=new Scanner(file_to_read2);//scanner for file
        FileWriter ofstream2 = new FileWriter("output.txt");  // after run, you can see the output file in the specified location
        out = new BufferedWriter(ofstream2);
        System.out.println("Stemmed words");
        while(filesc2.hasNextLine())
        {
        line=filesc2.nextLine();
        Scanner linesc=new Scanner(line);//scanner for line
     
            while(linesc.hasNext())
            {
             token=linesc.next();
             a=s1.stem(token);//method to access the porter stemmer for english
             a.toLowerCase();
             trie.insert(a);
             out.write(a);
            // System.out.println(a);
             out.write(" ");
            }
            out.newLine();
            linesc.close();
        }
        String temp = "";
       // trieUser.traversefile(trie.root,temp,file);
       
        
        trie.traverse(trie.root,temp,file);
        
       

        } catch (IOException ioe) {
	   ioe.printStackTrace();
	}
	finally
	{ 
	   try{
	      if(out!=null)
		 out.close();
	   }catch(Exception ex){
	       System.out.println("Error in closing the BufferedWriter"+ex);
	    }
	}
    }
}