
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Vector;

import ExtractionApi.Comments;
import ExtractionApi.DatabaseHandler;
import ExtractionApi.Gallery;
import ExtractionApi.ImageItem;
import Processing.Stemmer;
import Processing.TokenizedDoc;

import com.google.gson.Gson;




public class ExtractComments
{
	static ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
	private static Vector<String> stopWords;
	public static void main(String [] args) throws Exception
	{
		
    	stopWords = new Vector<String>();
    	File file = new File("stopwords.txt");
    	Scanner stopwordScanner = new Scanner(file);
    	while(stopwordScanner.hasNext())
    	{
    		stopWords.add(stopwordScanner.nextLine());
    	}
    	
    	stopwordScanner.close();
	
		printRemainingCredits();

		
		try
		{
			printRemainingCredits();
		

		
	
		//testComments();
		//printRemainingCredits();
		
		for(int page=1;page<100;page++)
		{
			//System.out.println("Page: "+page);
			System.out.println("\nPage: "+page+"\n ");
			getGallery("https://api.imgur.com/3/gallery/hot/viral/"+page+".json");
			
			
		}
		//System.out.println("Items: "+imageItems.size());
		
		//printRemainingCredits();
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
		System.out.println("Updating db...");
		
		//addToDB();
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
			
			Stemmer stemmer = new Stemmer();
			try{
				for(int j=0;j<comments.data.length;j++)
				{
				
					TokenizedDoc td= new TokenizedDoc(comments.data[j].comment,"!?,;.?","stopwords.txt");
		    		for(String word: (Vector<String>)td.getTokens())
					{
		    			if(!stopWords.contains(word))
		    			{
		    				stemmer.add(word.toLowerCase().toCharArray(),word.length());
		    				stemmer.stem();
		    				stemmer.toString();
		    				wordList.add(stemmer.toString());
		    			}
					}
		    	
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
			DatabaseHandler.addToDB(item);
			//imageItems.add(item);
			
		}
		System.out.println(imageItems.size());
	}
	


}
