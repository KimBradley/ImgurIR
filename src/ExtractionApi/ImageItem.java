package ExtractionApi;
import java.util.ArrayList;


public class ImageItem {
	public String imageUrl;
	public ArrayList<String> words;
	
	public ImageItem(String imageUrl, ArrayList<String>words)
	{
		this.imageUrl = imageUrl;
		this.words = words;
	}
}
