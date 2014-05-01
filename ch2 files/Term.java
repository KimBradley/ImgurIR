import java.text.DecimalFormat;
/**
 * A term in a document vector, consisting of three fields:
 * <ul>
 * <li> word: A string representing the actual term.</li>
 * <li> freq: Number of times the term appears in the document.</li>
 * <li> normalizedFreq: Ratio freq/(largest frequency of any term in the document).</li>
 * </ul>
 * */
public class Term
{
    String word;
    int freq;
    double normalizedFreq;
    public Term(String initWord)
    {
        word = initWord;
        freq = 1;
        normalizedFreq = 0;
    }
    public String toString()
    {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        df.setDecimalSeparatorAlwaysShown(true);
                                         
        return word + " " + Integer.toString(freq) + " " +
            df.format(normalizedFreq);
    }
    public void setFreq(int f){ freq = f;}
    public void setNormalizedFreq(double n){ normalizedFreq = n;}
    public void setWord(String w){ word = w;}
    public int getFreq(){return freq;}
    public double getNormalizedFreq(){return normalizedFreq;}
    public String getWord(){return word;}
}
