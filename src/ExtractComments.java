package ExtractionApi;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;




public class ExtractComments
{
	static ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
	
	public static void main(String [] args) throws Exception
	{
		testComments();
		printRemainingCredits();
		/*
		for(int page=0;page<1;page++)
		{
			System.out.println("Page: "+page);
			getGallery("https://api.imgur.com/3/gallery/hot/viral/"+page+".json");
			
		}
		System.out.println("Items: "+imageItems.size());
		
		printRemainingCredits();
		*/
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
			ArrayList<String> wordList = new ArrayList<String>();

	
		
			
		

			
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
}
