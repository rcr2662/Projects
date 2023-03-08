/* WORD LADDER Group.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * Roberto Reyes
 * rcr2662
 * 17360
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Spring 2022
 */

package assignment3;
import java.util.*;

class WordComparator implements Comparator<String> {
 
    // Function to compare
    public int compare(String w1, String w2)
    {
        if (Main.Difference(w1, Main.END, const_len).length()) == Main.Difference(w2, Main.END, const_len).length()))
            return 0;
        else if (Main.Difference(w1, end, const_len).length()) > Main.Difference(w2, end, const_len).length()))
            return 1;
        else
            return -1;
    }
}