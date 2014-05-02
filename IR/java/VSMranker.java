import java.io.*;
import java.util.*;

/**
 * A class that provides utilities to rank documents using
 * Vector Space Model
 * */
public class VSMranker
{
    /**
     * @param d of the type DocVector
     * @param q of the type DocVector
     * @return similarity score between d and q as a double
     * @see DocVector
     * */
    public static double sim(DocVector d, DocVector q)
    {
	    double num = 0, den1 = 0, den2 = 0;

        int i = 0, j = 0;
        while(i < d.vec.size() && j < q.vec.size())
        {
            Term dTerm = (Term) d.vec.get(i);
            Term qTerm = (Term) q.vec.get(j);
            if(dTerm.word.equals(qTerm.word))
            {
                num += dTerm.normalizedFreq*qTerm.normalizedFreq;
                i++;
                j++;
            }
            else if(dTerm.word.compareTo(qTerm.word) < 0)
                i++;
            else
                j++;
        }

        for(int k = 0; k < d.vec.size(); k++)
        {
            Term dTerm = (Term) d.vec.get(k);
            den1 += dTerm.normalizedFreq*dTerm.normalizedFreq;
        }
        for(int k = 0; k < q.vec.size(); k++)
        {
            Term qTerm = (Term) q.vec.get(k);
            den2 += qTerm.normalizedFreq*qTerm.normalizedFreq;
        }
        double denominator = Math.sqrt(den1)*Math.sqrt(den2);
        if(denominator != 0)
            return num/denominator;
        else
            return 0;
    }

    static Term createTerm(String line)
    {
        Term t = new Term("");
        try
        {
           StreamTokenizer tok = new StreamTokenizer(new StringReader(line));
           tok.nextToken();
           t.setWord(tok.sval);
           tok.nextToken();
           t.setFreq(new Double(tok.nval).intValue());
           tok.nextToken();
           t.setNormalizedFreq(tok.nval);
        }catch(IOException e)
        {
            System.err.println(e);
        }
        return t;
    }

    /**
     * @param fileName A String repesenting a file containing the TDM
     * @return A term document matrix (TDM) represented as a Vector of DocVector
     * @see DocVector
     * */
    public static Vector readTDM(String fileName)
    {
        Vector tdm = new Vector();
        try
        {
              BufferedReader in = new BufferedReader
                  (new InputStreamReader(new FileInputStream(fileName)));
              String line = in.readLine();
              while(line != null)
              {
                  if(line.startsWith(".I"))
                  {
                      DocVector doc = new DocVector();
                      doc.ID = line.substring(3);
                      doc.vec = new Vector();
                      line = in.readLine();
                      while(line != null && !line.startsWith(".I"))
                      {
                          Term t = createTerm(line);
                          doc.vec.add(t);
                          line = in.readLine();
                      }
                      tdm.add(doc);
                  }
              }
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
        return tdm;
    }
    /**
     * @param docTDM A term document matrix (TDM) represented as a Vector of DocVector
     * @param q of the type DocVector
     * @return A Vector of IDscore sorted according to scores
     * @see IDscore
     * @see DocVector
     * */
    public static Vector rank(Vector docTDM, DocVector q)
    {
        Vector vecIDscore = new Vector();
        for(int i = 0; i < docTDM.size(); i++)
        {
            DocVector doc = (DocVector)docTDM.get(i);
            IDscore pair = new IDscore();
            pair.ID = doc.ID;
            pair.score = sim(doc,q);
            if(pair.score != 0)
              vecIDscore.add(pair);
        }
        ScoreComparator c = new ScoreComparator();
        Collections.sort(vecIDscore,c);
        return vecIDscore;
    }

    /**
    ** @param args An array of Strings, e.g. cacm.tdm query.tdm 7
    ** */
     
    public static void main(String [] args)
    {
        if(args.length < 3)
        {
            System.err.println("Usage: java VSMranker docTDMfile queryTDMfile numberOfDocToList");
            return;
        }
        Vector vecIDscore = new Vector();
        Vector docTDM = readTDM(args[0]);
        Vector qTDM = readTDM(args[1]);
        int toPrint = Integer.parseInt(args[2]);
        for(int i = 0; i < qTDM.size(); i++)
        {
            DocVector q = (DocVector)qTDM.get(i);
            Vector rankedDoc = rank(docTDM,q);
            for(int j = 0; j < rankedDoc.size() && j < toPrint; j++)
                System.out.println(q.ID + " " + rankedDoc.get(j));
        }
    }
}
