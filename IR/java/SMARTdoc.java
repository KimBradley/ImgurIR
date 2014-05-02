/**
 * A document from a SMART collection, consisting of four fields:
 * <ul>
 * <li> ID: A string used to identify the document.</li>
 * <li> authors: A string used to store author names.</li>
 * <li> title: A string used to store title.</li>
 * <li> contents: A string used to store contents.</li>
 * <li> date: A string used to store date of publication.</li>
 * </ul>
 * */
public class SMARTdoc
{
    String ID="";
    String authors="";
    String title="";
    String contents="";
    String date="";
    public void setID(String initID){ID = initID;}
    public void setAuthors(String initAuthors){authors = initAuthors;}
    public void setTitle(String initTitle){title = initTitle;}
    public void setContents(String initContents){contents = initContents;}
    public void setDate(String initDate){date = initDate;}
    public String getID(){return ID;}
    public String getAuthors(){return authors;}
    public String getTitle(){return title;}
    public String getContents(){return contents;}
    public String getDate(){return date;}
    public String toString()
    {
        String temp = "";
        if(!authors.equals(""))
                temp += authors+ "\n";
        if(!title.equals(""))
                temp += title+ "\n";
        if(!contents.equals(""))
                temp += contents +"\n";
        if(!date.equals(""))
                temp += date +"\n";
        return temp;
    }
}
