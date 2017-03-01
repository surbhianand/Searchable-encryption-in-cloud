package main.java.edu.utexas.ece.mpc.bloomier;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import classes.MutableBloomierFilter;

import classes.Pair;

public class Test {
	private static MutableBloomierFilter<String, List<Pair<String, Integer>>> uut;
    private final static Map<String, List<Pair<String,Integer>>> originalMap = new HashMap<String, List<Pair<String,Integer>>>();
   
    public static void main(String args[]){

        
        	List<Pair<String,Integer>> l = new ArrayList<Pair<String,Integer>>();
        	Pair<String,Integer> p=new Pair<String,Integer>("D:\\major\\majordataset\\Lung Cancer.txt",2);
        	Pair<String,Integer> p2=new Pair<String,Integer>("hey2",2);
        	l.add(p);
        	l.add(p2);
            originalMap.put("textarea", l);
        	List<Pair<String,Integer>> l2 = new ArrayList<Pair<String,Integer>>();
        	Pair<String,Integer> p3=new Pair<String,Integer>("hey3",3);
        	Pair<String,Integer> p4=new Pair<String,Integer>("hey4",4);
        	l2.add(p3);
        	l2.add(p4);
            originalMap.put("key2", l2);

        try {
			uut = new MutableBloomierFilter<String, List<Pair<String,Integer>>>(originalMap, originalMap.keySet().size() * 10, 10, 32,
			                                                  10000);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        List<Pair<String,Integer>> x=uut.get("key1");
        if(x != null)
        for(int i=0;i<x.size();i++){
        	System.out.println(x.get(i).getL()+" "+x.get(i).getR());
        }
        List<Pair<String,Integer>> y=uut.get("key2");
        if(y != null)
        for(int i=0;i<y.size();i++){
        	System.out.println(y.get(i).getL()+" "+y.get(i).getR());
        }
        System.out.println(uut.get("key3"));
        
        
    }
}
