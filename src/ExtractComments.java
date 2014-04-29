
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Vector;

import ExtractionApi.Comments;
import ExtractionApi.Gallery;
import ExtractionApi.ImageItem;
import Processing.DocVector;
import Processing.Term;

import com.google.gson.Gson;




public class ExtractComments
{
	static ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
	
	public static void main(String [] args)
	{
		
		try
		{
			printRemainingCredits();
		
		Vector<String> v = new Vector<String>();
		v.add("test");
		v.add("something");
		v.add("test");
		v.add("this");
		v.add("words");
		v.add("add");
		v.add("this");
		
		
		
		ImageItem i = new ImageItem("url.com",v);
		ImageItem j = new ImageItem("other.com",v);
		imageItems.add(i);
		imageItems.add(j);
		
		
		
	
		//testComments();
		//printRemainingCredits();
		
		for(int page=0;page<1;page++)
		{
			//System.out.println("Page: "+page);
			getGallery("https://api.imgur.com/3/gallery/hot/viral/"+0+".json");
			
		}
		//System.out.println("Items: "+imageItems.size());
		
		//printRemainingCredits();
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
		System.out.println("Updating db...");
		
		addToDB();
			System.out.println(imageItems);
		
    }
	
	public static void testComments() throws Exception
	{
		Gson gson = new Gson();
		
		URL url = new URL("https://api.imgur.com/3/gallery/");
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Authorization","Client-ID b85db05245a02ce");
		connection.connect();
		BufferedReader input =new BufferedReader(new InputStreamReader( connection.getInputStream()));
		String resp = input.readLine();;
		//Comments comments = gson.fromJson(resp,Comments.class);
		System.out.println(resp);
		connection.disconnect();
	}
	public static void printRemainingCredits() throws Exception
	{
		//https://api.imgur.com/3/credits
				URL url = new URL("https://api.imgur.com/3/credits");
				HttpURLConnection connection =
				    (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Authorization","Client-ID b85db05245a02ce");
				connection.connect();
			
				
			
				BufferedReader input =new BufferedReader(new InputStreamReader( connection.getInputStream()));
				String resp = input.readLine();
					System.out.println(resp);
				//getGallery("https://api.imgur.com/3/gallery.json");
			//"data":{"UserLimit":500,"UserRemaining":403,"UserReset":1398539587,"ClientLimit":12500,"ClientRemaining":12293},"success":true,"status":200}
			
	}
	public static void getGallery(String uri) throws Exception
	{
		
			
			URL url = new URL(uri);
			HttpURLConnection connection =
			    (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization","Client-ID b85db05245a02ce");
			connection.connect();
		
			
		
			BufferedReader input =new BufferedReader(new InputStreamReader( connection.getInputStream()));
			String resp = input.readLine();
			
			//System.out.println(resp);
			Gson gson = new Gson();
			Gallery s=  gson.fromJson(resp, Gallery.class);
		
			extractImage(s);	
				//System.out.println(s);
			connection.disconnect();
	}
	public static void extractImage(Gallery gal) throws Exception
	{
		
		
		for(int i=0;i<gal.data.length;i++)
		{
			String imageUrl = gal.data[i].link;
			Vector<String> wordList = new Vector<String>();

			Gson gson = new Gson();
			
			URL url = new URL(" https://api.imgur.com/3/gallery/image/"+gal.data[i].id+"/comments");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization","Client-ID b85db05245a02ce");
			connection.connect();
			BufferedReader input =new BufferedReader(new InputStreamReader( connection.getInputStream()));
			String resp = input.readLine();;
			Comments comments = gson.fromJson(resp,Comments.class);
			
			String[] tokens;
			try{
				for(int j=0;j<comments.data.length;j++)
				{
					tokens = comments.data[j].comment.split("\\W+");
				
					for(String word:tokens)
						wordList.add(word);
				}
			}
			catch(NullPointerException e)
			{
				System.err.println(e.getMessage());
			}
			
			connection.disconnect();
			
			
			ImageItem item = new ImageItem(imageUrl, wordList);
			
			System.out.println(item.imageUrl);
			System.out.println(item.words);
			
			imageItems.add(item);
			
		}
		System.out.println(imageItems.size());
	}
	

	public static void addToDB()
	{
		Connection conn = null;
	    PreparedStatement st = null;
	    
	    try
	    {
	    	conn = DriverManager.getConnection("jdbc:mysql://localhost/imgurir?user=imgurir&password=imgurir");
	    	st=conn.prepareStatement("INSERT INTO wordsearch (word,link,frequency,normalizedFrequency) VALUES (?,?,?,?)"
	    			+ " ON DUPLICATE KEY UPDATE frequency=?, normalizedFrequency=?");
	    	
	    	for(ImageItem item: imageItems)
	    	{
	    		st.setString(2, item.imageUrl);
	    		for(Term t: item.words)
	    		{
	    			st.setString(1, t.getWord());
	    			st.setInt(3, t.getFreq());
	    			st.setDouble(4, t.getNormalizedFreq());
	    			st.setInt(5,t.getFreq());
	    			st.setDouble(6,t.getNormalizedFreq());
	    			st.executeUpdate();
	    		}
	    		
	    	}
	    	conn.close();
	    }
	    catch(Exception e)
	    {
	    	System.err.println(e.getMessage());
	    }
	    
	}
}
