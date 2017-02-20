package org.cloudbus.cloudsim.examples;

import java.util.HashMap;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.ParameterException;

public class CloudFile extends org.cloudbus.cloudsim.File {
private String cloudCipherText;
private String cloudCipherKey;
private HashMap<String,Integer> uniqueWords;
	public CloudFile(File file) throws ParameterException 
	{
		super(file);
	}
	public CloudFile(String fileName, int fileSize) throws ParameterException 
	{
	  super(fileName,fileSize);	
		
	}
	public void addCipherData(String cText,String cKey,HashMap<String,Integer> words)
	{
		cloudCipherText=cText;
		cloudCipherKey=cKey;
		uniqueWords = words;
	}
	public HashMap<String,Integer> getUniqueWords()
	{
		return uniqueWords;
	}
	public String getCipherData(String cType)
	{
		String result="";
		if (cType=="cText")
		{
		  result=cloudCipherText;
		}
		if (cType=="cKey")
		{
		  result=cloudCipherKey;
		}
		

		return result;
	}	
}
