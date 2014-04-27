package ExtractionApi;

public class GalleryAlbum {
	public String id;
	public String title;
	public String description;
	public int datetime;
	public String cover;
	public int cover_width;
	public int cover_height;
	public String account_url;
	public String privacy;
	public String layout;
	public int views;
	public String link;
	public int ups;
	public int downs;
	public int score;
	public boolean is_album;
	public String vote;
	public int images_count;
	public Image[] images;
	
	public GalleryAlbum()
	{
		
	}
}


class Image
{
	public String id;
	public String title;
	public String description;
	public int datetime;
	public String type;
	public boolean animated;
	public int width;
	public int height;
	public int size;
	public int views;
	public int bandwidth;
	public String deletehash;
	public String section;
	public String link;
	
	public Image()
	{
		
	}
	
}