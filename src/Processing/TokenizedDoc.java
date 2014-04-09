package Processing;
import java.io.*;
import java.util.*;
/**
 * A document represented using a vector of words/tokens
 * appearing in the original document.
 * The vector is called tokens.
 * */
public class TokenizedDoc
{
    Vector tokens;
    /**
     * @return A vector of words/tokens.
     * */
    public Vector getTokens(){return tokens;}
    /**
     * @param doc represented as a single string
     * @param separators String of characters used as separators, e.g. "!?[](),;.?"
     * @param stopWordFile name of the file containing stopwords, e.g. "stopwords.txt"
     */
    public TokenizedDoc(String doc, String separators, String stopWordFile)
    {
        tokens = new Vector();
        Vector stopWords = new Vector();
        doc = doc.toLowerCase();
        separators = "\\[" + separators + "\\]";
        doc = doc.replaceAll(separators, " ");
        // Read all the words
        try
        {
            StreamTokenizer tok = new StreamTokenizer(new StringReader(doc));
            while(tok.nextToken() != tok.TT_EOF)
            {
                if(tok.ttype == tok.TT_WORD)
                    tokens.add(tok.sval);
            }
        }catch(IOException e){}
        // Read all the stopwords
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(stopWordFile)));
            String s = in.readLine();
            while(s != null)
            {
                stopWords.add(s);
                s = in.readLine();
            }
        }catch(IOException e){}

        tokens.removeAll(stopWords);
        Collections.sort(tokens);
    }
}
