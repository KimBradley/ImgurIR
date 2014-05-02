import java.io.*;
/**
 * A class that stores a parsed document collection
 * supplied by the SMART project.
 * */

public class SMARTparser
{
    SMARTdoc [] docArray;
    /**
     * @return An array of SMARTdoc
     * @see SMARTdoc
     * */
 
    public SMARTdoc [] getDocArray(){ return docArray; }

    /** 
     * @param fileName  Name of the file containing a SMART document collection.
     * @param sizeDocs Number of documents in the collection.
     * */
    
    public SMARTparser(String fileName, int sizeDocs)
    {
        docArray = new SMARTdoc[sizeDocs];
       
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            int i = -1;
            String line = in.readLine();
            while(line != null)
            {
                if(line.startsWith(".I"))
                {
                    i++;
                    if(i >= docArray.length) return;
                    docArray[i] = new SMARTdoc();
                    docArray[i].ID = line.substring(3);
                    line = in.readLine();
                }
                else if(line.startsWith(".A"))
                {
                    if(!docArray[i].authors.equals(""))
                    {
                        docArray[i].authors += "\n";
                    }
                    line = in.readLine();
                    while(line != null && !line.startsWith("."))
                    {
                        docArray[i].authors += line;
                        docArray[i].authors += "\n";
                        line = in.readLine();
                    }
                }
                else if(line.startsWith(".W"))
                {
                    if(!docArray[i].contents.equals(""))
                    {
                        docArray[i].contents += "\n";
                    }
                    line = in.readLine();
                    while(line != null && !line.startsWith("."))
                    {
                        docArray[i].contents += line;
                        docArray[i].contents += "\n";
                        line = in.readLine();
                    }
                }
                else if(line.startsWith(".T"))
                {
                    if(!docArray[i].title.equals(""))
                    {
                        docArray[i].title += "\n";
                    }
                    line = in.readLine();
                    while(line != null && !line.startsWith("."))
                    {
                        docArray[i].title += line;
                        docArray[i].title += "\n";
                        line = in.readLine();
                    }
                }
                  
                else if(line.startsWith(".B"))
                {
                    if(!docArray[i].date.equals(""))
                    {
                        docArray[i].date += "\n";
                    }
                    line = in.readLine();
                    while(line != null && !line.startsWith("."))
                    {
                        docArray[i].date += line;
                        docArray[i].date += "\n";
                        line = in.readLine();
                    }
                  
                }
                else
                {
                    line = in.readLine();
                    while(line != null && !line.startsWith("."))
                    {
                        line = in.readLine();
                    }
                }
            }
        }catch(Exception e)
        {
            //End of file
        }
    }
    /**
     * @param args An array of Strings, e.g. cacm.all 3204
     * */
    public static void main(String [] args)
    {
        if(args.length < 2)
        {
            System.err.println("Usage: java SMARTparser inputFileName numberOfDocuments");
            return;
        }
        SMARTdoc [] docs = new SMARTparser(args[0], new Integer(args[1]).intValue()).getDocArray();
        for(int i = 0; i < docs.length; i++)
        {
            System.out.println("Collection = " + args[0] + " Document ID = " + docs[i].getID());
            System.out.println(docs[i]);
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }
    }
}
