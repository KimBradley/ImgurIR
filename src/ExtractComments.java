
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import ExtractionApi.Comments;
import ExtractionApi.Gallery;
import ExtractionApi.ImageItem;
import ExtractionApi.TimeStamp;
import Processing.Term;

import com.google.gson.Gson;




public class ExtractComments
{
	static ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
	
	public static void main(String [] args) throws Exception
	{
		
	
		printRemainingCredits();

		
		try
		{
			printRemainingCredits();
		

		
	
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
		
			if(Integer.parseInt(connection.getHeaderField("X-RateLimit-UserRemaining"))<10)
			{
				long epoch = Long.parseLong(connection.getHeaderField("X-RateLimit-UserReset"));
				Date resetDate = new Date(epoch*1000);
				Date currentDate = new Date();
				System.out.println(resetDate.getTime()-currentDate.getTime());
				long timeToWait =resetDate.getTime()-currentDate.getTime()+300000;
				System.out.println("UserRemaining: "+connection.getHeaderField("X-RateLimit-UserRemaining"));
				System.out.println("UserReset: "+connection.getHeaderField("X-RateLimit-UserReset"));
				System.out.println("ClientRemaining: "+connection.getHeaderField("X-RateLimit-ClientRemaining"));
				
				System.out.println("Waiting for "+ timeToWait/1000+" seconds");
				Thread.sleep(timeToWait);
			}
		
			BufferedReader input =new BufferedReader(new InputStreamReader( connection.getInputStream()));
			String resp = input.readLine();
			
			Gson gson = new Gson();
			Gallery s=  gson.fromJson(resp, Gallery.class);
		
			extractImage(s);	
			connection.disconnect();
	}
	public static void extractImage(Gallery gal) throws Exception
	{
		
		
		for(int i=0;i<gal.data.length;i++)
		{
			String imageUrl = gal.data[i].link;
			String galleryUrl = "imgur.com/gallery/"+gal.data[i].id;
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
			
			
			ImageItem item = new ImageItem(imageUrl,galleryUrl, wordList);
			
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
	    	st=conn.prepareStatement("INSERT INTO wordsearch (word,link,frequency,normalizedFrequency, gallerylink) VALUES (?,?,?,?,?)"
	    			+ " ON DUPLICATE KEY UPDATE frequency=?, normalizedFrequency=?");
	    	
	    	for(ImageItem item: imageItems)
	    	{
	    		st.setString(2, item.imageUrl);
	    		st.setString(5,item.imgurLink);
	    		for(Term t: item.words)
	    		{
	    			st.setString(1, t.getWord());
	    			st.setInt(3, t.getFreq());
	    			st.setDouble(4, t.getNormalizedFreq());
	    			
	    			st.setInt(6,t.getFreq());
	    			st.setDouble(7,t.getNormalizedFreq());
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
