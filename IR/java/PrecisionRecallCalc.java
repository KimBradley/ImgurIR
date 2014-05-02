import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
public class PrecisionRecallCalc
{
    TreeSet [] relevant;
    TreeSet [] retrieved;
    int numQueries;

    public PrecisionRecallCalc(String relevantFile, String retrievedFile, int initNumQueries)
    {
        numQueries = initNumQueries;
        relevant = readIDs(relevantFile);
        retrieved = readIDs(retrievedFile);
    }

    public TreeSet [] readIDs(String fileName)
    {
        TreeSet [] dIDarray = new TreeSet[numQueries];
        for(int i = 0; i < numQueries; i++)
            dIDarray[i] = new TreeSet();
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String s = in.readLine();
            while(s != null)
            {
                int qID, dID;
                StreamTokenizer tok = new StreamTokenizer(new StringReader(s));
                tok.nextToken();
                qID = new Double(tok.nval).intValue();
                tok.nextToken();
                dID = new Double(tok.nval).intValue();
                dIDarray[qID-1].add(new Integer(dID));
                s = in.readLine();
            }
        }catch(IOException e)
	    {
	    	System.err.println(e);
	    }
        return dIDarray;
    }
    public void precisionRecall()
    {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        df.setDecimalSeparatorAlwaysShown(true);
        double averagePrecision = 0, averageRecall = 0;
        for(int i = 0; i < numQueries; i++)
        {
            TreeSet intersection = new TreeSet(relevant[i]);
            intersection.retainAll(retrieved[i]);
            double precision = 0, recall = 0;
            if(retrieved[i].size() > 0)
                precision = 1.0*intersection.size()/retrieved[i].size();
            if(relevant[i].size() > 0)
                recall = 1.0*intersection.size()/relevant[i].size();
            averagePrecision += precision;
            averageRecall += recall;
            System.out.println("Query: " + (i+1) + " Precision: "
                    + df.format(precision) + "\tRecall: " + df.format(recall));
        }
        System.out.println("Average " + " Precision: " +
                df.format(averagePrecision/numQueries)
                + "\tRecall: " + df.format(averageRecall/numQueries));
    }
    public static void main(String [] args)
    {
        if(args.length < 3)
        {
            System.err.println("Usage: java PrecisionRecallCalc relevantFile retrievedFile numberOfQueries");
            return;
        }

        PrecisionRecallCalc pr = new PrecisionRecallCalc(args[0],args[1],new Integer(args[2]).intValue());
        pr.precisionRecall();
    }
}
