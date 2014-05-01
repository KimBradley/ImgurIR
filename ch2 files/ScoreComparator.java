import java.util.*;
/**
 * A class that allows for comparison between two
 * elements of the type IDscore based on scores
 * @see IDscore
 * */

public class ScoreComparator implements Comparator
{
    public int compare(Object l, Object r)
    {

        IDscore left = (IDscore) l;
        IDscore right = (IDscore) r;
        if(left.score < right.score) return 1;
        if(left.score > right.score) return -1;
        return 0;
    }
}

