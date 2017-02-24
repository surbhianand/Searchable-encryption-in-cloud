package servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ParameterException;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.CloudFile;
import org.cloudbus.cloudsim.examples.CloudHarddriveStorage;

/**
 * Servlet implementation class FuzzySearchQuery
 */
@WebServlet("/FuzzySearchQuery")
public class FuzzySearchQuery extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static SecretKeySpec secretKey;
    private static byte[] key;
    private final static HashMap<String, Integer> nWords = new HashMap<String, Integer>();

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FuzzySearchQuery() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String keyword = request.getParameter("keyword");  
		int num_user = 1;
		Calendar calendar = Calendar.getInstance();
		boolean trace_flag = false;
		CloudSim.init(num_user, calendar, trace_flag);
		//******************************************************
		//Creating Storages
		CloudHarddriveStorage hdst = null;
		try {
			hdst = new CloudHarddriveStorage(50);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double hdstCap = hdst.getCapacity();
		double hdstAvailSpace = hdst.getAvailableSpace();
		
		
		 Log.printLine("reading out the list of files while searching");
		    
		    FileInputStream inFile = new FileInputStream("ListOfFiles.txt");
			BufferedInputStream bin = new BufferedInputStream(inFile);
	        int character;
	        String temptext="";
	        List<String> li=new ArrayList<String>();
	        
	        while((character=bin.read())!=-1) {
	        	char temp=(char)character;
	        	
	        	if(temp!='\n')
	            temptext = temptext + (char)character;
	        	else
	        	{
	        		li.add(temptext);
	        		temptext="";
	        	}
	        		
	        }
	        bin.close();
	        inFile.close();
	        
	        Log.printLine("List of files"+li);
	        
	      int i;
	      String cipherKey = "pro"; // 128 bit key
	       
	        for(i=0;i<li.size();i++)
	        {
	        	String path=li.get(i);
	        	 Log.printLine("storing file in cloud for searching"+path);
	        	 FileInputStream in = new FileInputStream(path);
	 			BufferedInputStream bi = new BufferedInputStream(in);
	 	        int charac = 0;
	 	        String data="";
	 	        while((charac=bi.read())!=-1) {
	 	            data = data + (char)charac;
	 	        }
	 	       Log.printLine("file read as "+data);
	 	        bin.close();
	 	        inFile.close();
	 	        
		          String ciphertext= createCipherText(path,cipherKey);
		          
		          HashMap<String,Integer> hm;
		          hm=buildMap(path,cipherKey);
		          Log.printLine(""+hm);
		          CloudFile file2 = null;
					try {
						file2 = new CloudFile(path,2);
					} catch (ParameterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						file2.addCipherData(ciphertext, cipherKey,hm);
						double timetaken = hdst.addCloudFile(file2);
						
	        }
	        CloudSim.startSimulation();
	        Log.printLine("search startd for keyword "+keyword);
	        BufferedReader in = new BufferedReader(new FileReader("big.txt"));
			Pattern p = Pattern.compile("\\w+");
			for(String temp = ""; temp != null; temp = in.readLine()){
				Matcher m = p.matcher(temp.toLowerCase());
				while(m.find()) nWords.put((temp = m.group()), nWords.containsKey(temp) ? nWords.get(temp) + 1 : 1);
			}
			in.close();
			correct(keyword);
			Log.printLine("Set of similar words are "+nWords);
	        
	        searchWord(hdst,keyword);
	        
	        
	        
	        
	}
	
	   public static void setKey(String myKey) 
	    {
	        MessageDigest sha = null;
	        try {
	            key = myKey.getBytes("UTF-8");
	            sha = MessageDigest.getInstance("SHA-1");
	            key = sha.digest(key);
	            key = Arrays.copyOf(key, 16); 
	            secretKey = new SecretKeySpec(key, "AES");
	        } 
	        catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        } 
	        catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	    }
	 
	    public static String encrypt(String strToEncrypt, String secret) 
	    {
	        try
	        {
	            setKey(secret);
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	        } 
	        catch (Exception e) 
	        {
	            System.out.println("Error while encrypting: " + e.toString());
	        }
	        return null;
	    }
	 
	    public static String decrypt(String strToDecrypt, String secret) 
	    {
	        try
	        {
	            setKey(secret);
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	            cipher.init(Cipher.DECRYPT_MODE, secretKey);
	            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	        } 
	        catch (Exception e) 
	        {
	            System.out.println("Error while decrypting: " + e.toString());
	        }
	        return null;
	    }
	 
	    public static String createCipherText(String fileName,String key) throws FileNotFoundException
	    {
	    	FileInputStream inFile = new FileInputStream(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inFile));
			String line;
			String ciphertext="";
			try {
				while ((line = reader.readLine()) != null) {
					 // Do something with line
						String temp = encrypt(line,key);
						ciphertext = ciphertext + temp;
						ciphertext = ciphertext + '$';
					}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ciphertext;
	    }
	 
	    public static HashMap<String,Integer> buildMap(String fileName,String key) throws IOException{
	    	HashMap<String,Integer> hm=new HashMap<String,Integer>();
	    	FileInputStream inFile = new FileInputStream(fileName);
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(inFile));
	        String line = null;
	        while( (line = reader.readLine())!= null ){
	            // \\s+ means any number of whitespaces between tokens
	            String [] tokens = line.split("\\s+");
	            for(int i=0;i<tokens.length;i++)
	            {
	            	String temp = encrypt(tokens[i],key); 
	            	hm.put(temp, 1);
	            }
	        }	
	    	return hm;
	    }
	    public static void bringText(String ciphertext,String key)
	    {
			String temp = "";
			for(int i=0;i<ciphertext.length();i++)
			{
				if(ciphertext.charAt(i)!='$')
				{
					temp = temp + ciphertext.charAt(i);
				}
				else
				{
					temp = decrypt(temp,key);
					 Log.printLine(temp);
					temp = "";
				}
			}
	    }
	    private final ArrayList<String> edits(String word) {
			ArrayList<String> result = new ArrayList<String>();
			for(int i=0; i < word.length(); ++i) result.add(word.substring(0, i) + word.substring(i+1));
			for(int i=0; i < word.length()-1; ++i) result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
			for(int i=0; i < word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
			for(int i=0; i <= word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
			return result;
		}

		public final String correct(String word) {
			if(nWords.containsKey(word)) return word;
			ArrayList<String> list = edits(word);
			HashMap<Integer, String> candidates = new HashMap<Integer, String>();
			for(String s : list) if(nWords.containsKey(s)) candidates.put(nWords.get(s),s);
			if(candidates.size() > 0) return candidates.get(Collections.max(candidates.keySet()));
			for(String s : list) for(String w : edits(s)) if(nWords.containsKey(w)) candidates.put(nWords.get(w),w);
			return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : word;
		}
		
	    public static void searchWord(CloudHarddriveStorage hdst,String text)
	    {
	    	
	    	List<CloudFile> fileList = hdst.getFileList();
			for(int i=0;i<fileList.size();i++)
			{
				HashMap<String,Integer> hm = fileList.get(i).getUniqueWords();
				String key = fileList.get(i).getCipherData("cKey");
				String temp = encrypt(text,key);
				if(hm.containsKey(temp))
				{
					 Log.printLine("keyword found in file "+fileList.get(i));
					String ciphertext=fileList.get(i).getCipherData("cText");
					bringText(ciphertext,key);
				}
			}
	    
	    }
	    
	    
}
