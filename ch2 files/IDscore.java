import java.text.DecimalFormat;
/**
 * A class that stores document ID and corresponding relevance score.
 * The class contains two fields:
 * <ul>
 * <li> ID of type String </li>
 * <li> score of type double </li>
 * </ul>
 * */

public class IDscore
{
    String ID;
    double score;
    public String toString()
    {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(50);
        df.setMinimumFractionDigits(10);
        df.setDecimalSeparatorAlwaysShown(true);
        return ID + " " + df.format(score);
    }
}
