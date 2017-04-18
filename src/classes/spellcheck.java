package classes;

import java.io.*;
import java.util.*;

public class spellcheck {
    
    Hashtable<String,String> dictionary;   // To store all the words of the dictionary
    boolean suggestWord ;           // To indicate whether the word is spelled correctly or not.
    
    public String getSpell(String word) 
    {
        dictionary = new Hashtable<String,String>();
        
        try 
        {
            
            //Read and store the words of the dictionary 
            BufferedReader dictReader = new BufferedReader(new FileReader("D:\\major\\abc\\unique_stem_words.txt"));
            
            while (dictReader.ready()) 
            {
                String dictInput = dictReader.readLine() ;
                String [] dict = dictInput.split("\\s");
                
                for(int i = 0; i < dict.length;i++) 
                {
                    // key and value are identical
                    dictionary.put(dict[i], dict[i]);
                }
            }
            dictReader.close();
            
            // Initialising a spelling suggest object
            spellingsuggest suggest = new spellingsuggest("D:\\major\\abc\\wordprobabilityDatabase.txt");
         
            // Reads input lines one by one
                    suggestWord = true;
                    String outputWord = checkWord(word);
                    
                    if(suggestWord)
                    {
                    	return suggest.correct(outputWord);
                        //System.out.println("Suggestions for "+word+" are:  "+suggest.correct(outputWord)+"\n");
                    }
                    else
                    	return word;
        }
        catch (IOException e) 
        {
            System.out.println("IOException Occured! ");
            e.printStackTrace();
      //      System.exit(-1);
        }
		return word;
    }
    
    public String checkWord(String wordToCheck) 
    {
        String wordCheck, unpunctWord;
        String word = wordToCheck.toLowerCase();
        
        // if word is found in dictionary then it is spelt correctly, so return as it is.
        //note: inflections like "es","ing" provided in the dictionary itself.
        if ((wordCheck = (String)dictionary.get(word)) != null)
        {
            suggestWord = false;            // no need to ask for suggestion for a correct word.
            return wordCheck;
        }
        
        // Removing punctuations at end of word and giving it a shot ("." or "." or "?!")
        int length = word.length();
        
 
         //Checking for the beginning of quotes(example: "she )
        if (length > 1 && word.substring(0,1).equals("\"")) 
        {
            unpunctWord = word.substring(1, length);
            
            if ((wordCheck = (String)dictionary.get(unpunctWord)) != null)
            {
                suggestWord = false;            // no need to ask for suggestion for a correct word.
                return wordCheck ;
            }
            else // not found
                return unpunctWord;                  // removing the punctuations and returning
        }
 
        // Checking if "." or ",",etc.. at the end is the problem(example: book. when book is present in the dictionary).
        if( word.substring(length - 1).equals(".")  || word.substring(length - 1).equals(",") ||  word.substring(length - 1).equals("!")
        ||  word.substring(length - 1).equals(";") || word.substring(length - 1).equals(":"))
        {
            unpunctWord = word.substring(0, length-1);
            
            if ((wordCheck = (String)dictionary.get(unpunctWord)) != null)
            {
                suggestWord = false;            // no need to ask for suggestion for a correct word.
                return wordCheck ;
            }
            else
            {
                return unpunctWord;                  // removing the punctuations and returning
            }
        }

        // Checking for "!\"",etc ...  in the problem (example: watch!" when watch is present in the dictionary)
        if (length > 2 && word.substring(length-2).equals(",\"")  || word.substring(length-2).equals(".\"") 
            || word.substring(length-2).equals("?\"") || word.substring(length-2).equals("!\"") )
        {
            unpunctWord = word.substring(0, length-2);
            
            if ((wordCheck = (String)dictionary.get(unpunctWord)) != null)
            {
                suggestWord = false;            // no need to ask for suggestion for a correct word.
                return wordCheck ;
            }
            else // not found
                return unpunctWord;                  // removing the inflections and returning
        }
        
        
        
        // After all these checks too, word could not be corrected, hence it must be misspelt word. return and ask for suggestions
        return word;
    }
    
}

