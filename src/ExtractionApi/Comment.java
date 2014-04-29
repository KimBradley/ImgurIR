package ExtractionApi;

public class Comment {
	public String id;
	public String image_id;
	public String comment;
	public String author;
	public int author_id;
	public boolean on_album;
	public String album_cover;
	public int ups;
	public int downs;
	public float points;
	public int datetime;
	public int parent_id;
	public boolean deleted;
	public Comment[] children;
	
	public Comment()
	{
		
	}
	
}

