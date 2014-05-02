import java.io.*;
import java.util.*;
/**
 * A document represented using a vector of stems of words,
 * associated frequency, and normalized frequency.
 * */
public class DocVector
{
    String ID;
    Vector vec;
    /**
     * @return A vector of Term: a triplet consisting of
     * stemmed words, frequency, and normalized frequency.
     * @see Term
     * */
    public Vector getVector(){return vec;}
    /**
     * @return A string identifying the document
     * */
    public String getID(){return ID;}
    /**
     * @param initVec A vector of Term: a triplet consisting of
     * stemmed words, frequency, and normalized frequency.
     * @see Term
     * */
    public void setVector(Vector initVector){vec = initVector;}
    /**
     * @param initID A string identifying the document
     * */
    public void setID(String initID){ID = initID;}
    /**
     * Calculates frequency and normalized frequency
     * of words contained in the wordVector.
     * @param wordVector A Vector of words
     * */
    void frequentize(Vector wordVector)
    {
        Collections.sort(wordVector);
        vec = new Vector();
        Term  item = new Term (wordVector.get(0).toString());
        double max = 0;
        for(int i = 1; i < wordVector.size(); i++)
        {
            String word = wordVector.get(i).toString();
            if(item.word.equals(word))
               item.freq++;
            else
            {
                if(max < item.freq) max = item.freq;
                vec.add(item);
                item = new Term (word);
            }
        }
        vec.add(item);
        for(int i = 0; i < vec.size(); i++)
        {
            item = (Term)vec.get(i);
            item.normalizedFreq = item.freq / max;
            vec.setElementAt(item,i);
        }
    }
    /**
     * Stems words contained in the wordVector.
     * @param wordVector A Vector of words
     * */

    void stem(Vector wordVector)
    {
        for(int i = 0; i < wordVector.size(); i++)
        {
            Stemmer s = new Stemmer();
            String doc = wordVector.get(i).toString();
            for(int j = 0; j < doc.length(); j++)
            {
                char ch = doc.charAt(j);
                s.add(ch);
            }
            s.stem();
            wordVector.setElementAt(s.toString(),i);
        }
    }

    /**
     * Default Constructor
     * */
    public DocVector()
    {
    }

    /**
     * Creates a vector of Term using the words/tokens from the wordVector.
     * @param wordVector A Vector of words
     * @param initID A string used to identify the document
     * @see Term
     * */
    public DocVector(Vector wordVector, String initID)
    {
        ID = initID;
        stem(wordVector);
        frequentize(wordVector);
    }
    /**
     * @param args An array of Strings, e.g. d1.txt "\!\[\](),;.?" stopwords.txt
     * */

    public static void main(String [] args)
    {
        if(args.length < 3)
        {
            System.err.println("Usage: java DocVector fileName separator stopWordFile");
            return;
        }
        String test = "";
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));
            String s = in.readLine();
            while(s != null)
            {
                test += s + "\n";
                s = in.readLine();
            }
        }catch(IOException e){}
        Vector wordVector = new TokenizedDoc(test, args[1], args[2]).getTokens();
        DocVector documentVector = new DocVector(wordVector, args[0]);
        Vector cdv = documentVector.getVector();
        System.out.println(".I "+documentVector.getID());
        for(int i = 0; i < cdv.size(); i++)
            System.out.println(cdv.get(i));
    }
}
