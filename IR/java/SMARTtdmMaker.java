import java.io.*;
import java.util.*;

/** A class used to create term document matrix from a
 * SMART collection of documents.
 * */

public class SMARTtdmMaker
{
    /**
     * @param args An array of Strings, e.g. cacm.all 3204  "\!\[\](),;.?" stopwords.txt
     * */
    public static void main(String [] args)
    {
        if(args.length < 4)
        {
            System.err.println("Usage: java SMARTtdmMaker inputFileName numberOfDocuments separatorString stopwordsFile");
            return;
        }
        SMARTdoc [] docs = new SMARTparser(args[0], new Integer(args[1]).intValue()).getDocArray();
        for(int i = 0; i < docs.length; i++)
        {
            Vector wordVector = new TokenizedDoc(docs[i].toString(), args[2], args[3]).getTokens();
            DocVector documentVector = new DocVector(wordVector, docs[i].getID());
            Vector cdv = documentVector.getVector();
            System.out.println(".I "+documentVector.getID());
            for(int j = 0; j < cdv.size(); j++)
                System.out.println(cdv.get(j));
 
        }
    }
}
