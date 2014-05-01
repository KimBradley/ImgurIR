import java.io.*;
import java.util.*;
/** 
 * A class used to retieve a document from given SMART collection of documents.
 * */

public class SMARTfetcher
{
    /**
     * @param args An array of Strings, e.g. cacm.all 3204 1071
     * */
    public static void main(String [] args)
    {
        if(args.length < 3)
        {
            System.err.println("Usage: java SMARTtdmMaker inputFileName numberOfDocuments documentToBeFetched");
            return;
        }
        SMARTdoc [] docs = new SMARTparser(args[0], new Integer(args[1]).intValue()).getDocArray();
        for(int i = 0; i < docs.length; i++)
            if(docs[i].getID().equals(args[2]))
            {
                System.out.println(docs[i]);
                return;
            }
    }
}
