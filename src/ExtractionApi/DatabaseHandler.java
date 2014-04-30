package ExtractionApi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import Processing.Term;

public class DatabaseHandler {
	public static void addToDB(ImageItem item)
	{
		Connection conn = null;
	    PreparedStatement st = null;
	    
	    try
	    {
	    	conn = DriverManager.getConnection("jdbc:mysql://localhost/imgurir?user=imgurir&password=imgurir");
	    	st=conn.prepareStatement("INSERT INTO wordsearch (word,link,frequency,normalizedFrequency, gallerylink) VALUES (?,?,?,?,?)"
	    			+ " ON DUPLICATE KEY UPDATE frequency=?, normalizedFrequency=?");
	    	
	
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
	    		
	    	
	    	conn.close();
	    }
	    catch(Exception e)
	    {
	    	System.err.println(e.getMessage());
	    }
	    
	}
}
