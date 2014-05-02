import java.io.*;
import java.util.*;
/**
 * A class that provides interactive retrieval
 * from a SMART collection based on Vector Space Model
 * */

public class VSMinteract
{
    /**
     * @param line A string containing keywords of a query
     * @return query represented as a document vector
     * @see DocVector
     * */
    public static DocVector createQueryVector(String line)
    {
        DocVector qVec = new DocVector();
        qVec.ID = "";
        qVec.vec = new Vector();
        double normalizedWeight = 1.0;
        int weight = 10;
        try
        {
            StreamTokenizer tok = new StreamTokenizer(new StringReader(line));
            while(tok.nextToken() != tok.TT_EOF)
            {
                if(tok.ttype == tok.TT_WORD)
                {
                    Stemmer s = new Stemmer();
                    String word = tok.sval;
                    word = word.toLowerCase();
                    for(int j = 0; j < word.length(); j++)
                    {
                        char ch = word.charAt(j);
                        s.add(ch);
                    }
                    s.stem();
                    Term t = new Term(s.toString());
                    t.setFreq(weight);
                    t.setNormalizedFreq(normalizedWeight);
                    if(weight > 1)
                    {
                        weight--;
                        normalizedWeight -= 0.1;
                    }
                    qVec.vec.add(t);
                }
            }
        }catch(IOException e){}
        return qVec;
    }
    static String prompt1(BufferedReader in) throws IOException
    {
        System.out.println("*************************************************");
        System.out.println("* Please input your query:\t\t\t*");
        System.out.println("* '.q' followed by <ENTER> to quit.\t\t*");
        System.out.println("*************************************************");
        String line = in.readLine();
        return line;
    }
    static String prompt2(BufferedReader in) throws IOException
    {
        System.out.println("*************************************************");
        System.out.println("* Press <ENTER> to see the next document\t*");
        System.out.println("* '.n' followed by <ENTER> to start a new query\t*");
        System.out.println("* '.q' followed by <ENTER> to quit.\t\t*");
        System.out.println("*************************************************");
        String line = in.readLine();
        return line;
    }
    static void display1(int totalRetrieved)
    {
        System.out.println("*************************************************");
        System.out.println("***  Total number of documents retrieved: " + totalRetrieved + "  ***");
    }
    static void display2(String i, int j, int totalRetrieved)
    {
        System.out.println("*************************************************");
        System.out.print("***  Document ID: " + i );
        System.out.println(" Ranked " + (j+1) + " out of " + totalRetrieved + "     ***");
        System.out.println("*************************************************");
    }
    /**
     * @param args An array of Strings, e.g. cacm.all 3204 cacm.tdm
     * */
    public static void main(String [] args)
    {
        if(args.length < 3)
        {
            System.err.println("Usage: java VSMinteract docCollectionFile numberOfDoc docTDMfile");
            return;
        }
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            SMARTdoc [] docs = new SMARTparser(args[0], new Integer(args[1]).intValue()).getDocArray();
            Vector docTDM = VSMranker.readTDM(args[2]);
            String line = prompt1(in);
            while(!line.startsWith(".q"))
            {
                DocVector q = createQueryVector(line);
                Vector rankedDoc = VSMranker.rank(docTDM,q);
                int j = 0;
                int totalRetrieved = rankedDoc.size();
                display1(totalRetrieved);
                while(j < totalRetrieved && !line.startsWith(".n") && !line.startsWith(".q"))
                {
                    IDscore dv = (IDscore)rankedDoc.get(j);
                    display2(dv.ID,j,totalRetrieved);
                    for(int i = 0; i < docs.length; i++)
                    {
                       if(dv.ID.equals(docs[i].getID()))
                       {
                          System.out.println(docs[i]);
                          break;
                       }
                    }
                    line = prompt2(in);
                    j++;
                }
                if(!line.startsWith(".q"))
                    line = prompt1(in);
            }
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }
}
