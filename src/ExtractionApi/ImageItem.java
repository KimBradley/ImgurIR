package ExtractionApi;
import java.util.Vector;

import Processing.DocVector;
import Processing.Term;


public class ImageItem {
	public String imageUrl;
	public String imgurLink;
	public Vector<Term> words;

	
	public ImageItem(String imageUrl, String imgurLink,Vector<String>words)
	{
		this.imageUrl = imageUrl;
		this.imgurLink = imgurLink;
		DocVector dv = new DocVector(words,"id");
		this.words = 		dv.getVector();
	}
	
	public String toString()
	{
		return "imageUrl: "+imageUrl+"\nImgurLink: "+imgurLink+"\n"+words+"\n";
	}
	
	
	
}
