package classes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import classes.MutableBloomierFilter;
//import class.Pair;

public class BloomierObject {
	public static double falsePositiveProbability = 0.1;
	public static int expectedSize = 500000;
	public static MutableBloomierFilter<String, List<Pair<String, Integer>>> bloomierFilter;
    public final static Map<String, List<Pair<String,Integer>>> originalMap= new HashMap<String, List<Pair<String,Integer>>>();;
	
   
}
