package classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class wordnet 
 {
    static String a[]=new String[2];
    public wordnet()
    {
    	System.setProperty("wordnet.database.dir", User.location_dict);
    }
    public Set<String> getSimilarWords(String word)
    {
        NounSynset nounSynset;
        WordNetDatabase database = WordNetDatabase.getFileInstance();
        Synset[] synsets = database.getSynsets(word, SynsetType.NOUN);
        Set<String> set = new HashSet<String>();
        set.add(word);
        for (int i = 0; i < synsets.length; i++)
            {
        nounSynset = (NounSynset)(synsets[i]);
       set.add(nounSynset.getWordForms()[0]);
        //System.err.println(nounSynset.getWordForms()[0]);
       
            }
        return set;
    }
}