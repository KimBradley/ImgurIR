package ExtractionApi;
import java.util.Vector;

import Processing.DocVector;
import Processing.Term;


public class ImageItem {
	public String imageUrl;
	public Vector<Term> words;

	
	public ImageItem(String imageUrl, Vector<String>words)
	{
		this.imageUrl = imageUrl;
		
		DocVector dv = new DocVector(words,"id");
		this.words = 		dv.getVector();
	}
	
	public String toString()
	{
		return "imageUrl: "+imageUrl+"\n"+words+"\n";
	}
	
	
	
}
