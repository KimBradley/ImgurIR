package Extraction;

import java.util.List;
import java.util.Vector;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import ExtractionApi.DatabaseHandler;
import ExtractionApi.ImageItem;
import Processing.TokenizedDoc;

/**
 * Created by michael on 4/9/2014.
 * This class takes an imgur web page and extracts teh comments from it into an array of strings
 * I am a little worried about how long it takes to work. Each page takes a little bit to process
 * I am thinking about trying to either
 *  -tweak the selenium settings to make it run faster
 *            or
 *  -try using the imgur api to make a better extractor
 *
 *  this will work for now though at least as  a backup or main thing if needed
 */
public class PageParser {
	
	//private static WebDriverWait wait;
	private static WebDriver driver;
    public static void main(String [] args) throws InterruptedException 
    {
    	
    	DesiredCapabilities dCaps;
    	dCaps = new DesiredCapabilities();
    	dCaps.setJavascriptEnabled(true);
    	dCaps.setCapability("takesScreenshot",false);
    	
   
        driver = new PhantomJSDriver(dCaps);
    	//driver = new FirefoxDriver();

        
        System.out.println("Starting program");
        String url = "http://imgur.com/gallery/6TvIuik";
        driver.get(url);  
       
    	ImageItem img = ExtractImage();
    	DatabaseHandler.addToDB(img);
    	
    	//for(int i=0;i<10;i++)
    	while(true)
    	{
    		
    		
    		List<WebElement> navNext = driver.findElements(By.cssSelector(".navNext"));
    		navNext.get(navNext.size()-1).click();
    		driver.get(driver.getCurrentUrl());
    		img= ExtractImage();
    		DatabaseHandler.addToDB(img);
    	}	
    	// driver.quit();


    }
    
    
    public static ImageItem ExtractImage()
    {
    	try
    	{
    	System.out.println(driver.getCurrentUrl());
    	ImageItem imageItem=null;
    	WebElement picUrl = driver.findElement(By.cssSelector("div#image img"));
    	Vector<String> words = new Vector<String>();
    	List<WebElement> commentElements = driver.findElements(By.cssSelector(".comment .caption div.usertext.textbox.first1"));

    	for(WebElement el: commentElements)
    	{
    		//problems using []() in tokenizeddoc
    		//System.out.println(el.getText());
    		
    		TokenizedDoc td= new TokenizedDoc(el.getText(),"!?,;.?","stopwords.txt");
    		for(String word: (Vector<String>)td.getTokens())
    		{
    			words.add(word.toLowerCase());
    		}
    	}
    	if(words.size()==0)
    	{
    		System.out.println("Words=0");
 
    	}
    	else
    	{
    		imageItem = new ImageItem(picUrl.getAttribute("src"),driver.getCurrentUrl(), words);
    		//System.out.println(imageItem);
    	}
    	
    	
    	
    	
    	return imageItem;
    	} 
    	catch(Exception e)
    	{
    		System.out.println("Skipping because: "+e.getMessage());
    		return null;
    	}
    	
    }
    
    

}



//wait = new WebDriverWait(driver,10);
/*
Queue<String> urlQueue = new LinkedList<String>();

driver.get("http://imgur.com/gallery");


JavascriptExecutor jse = (JavascriptExecutor)driver;

//at 10 was 537
//at 50 was 912
//hard to get past aorund 907 need to try clicking link at this point
for(int i=0;i<60;i++)
{
	jse.executeScript("window.scrollTo(0, document.body.scrollHeight);", "");
	Thread.sleep(100);
}
List<WebElement> elements = driver.findElements(By.cssSelector(".posts img"));



for(int i=0;i<elements.size();i++)
{
	System.out.println(i+") "+elements.get(i).getAttribute("src"));
}




*/


