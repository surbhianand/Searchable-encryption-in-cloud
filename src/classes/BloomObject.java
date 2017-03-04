package classes;

import java.util.HashMap;
import java.util.List;

public class BloomObject {
	
	public static double falsePositiveProbability = 0.1;
	public static int expectedNumberOfElements = 500000;
	
public	static BloomFilter<String> bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedNumberOfElements);
public static HashMap<String,List<Pair<String,Integer>>> hm=new HashMap<String,List<Pair<String,Integer>>>();
}
