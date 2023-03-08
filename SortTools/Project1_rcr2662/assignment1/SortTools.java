// SortTools.java
/*
 * EE422C Project 1 submission by
 * Replace <...> with your actual data.
 * Roberto Reyes
 * rcr2662
 * 17360
 * Spring 2022
 * Slip days used:
 */

package assignment1;
import java.util.Arrays;

public class SortTools {
    /**
      * Return whether the first n elements of x are sorted in non-decreasing
      * order.
      * @param x is the array
      * @param n is the size of the input to be checked
      * @return true if array is sorted
      */
    public static boolean isSorted(int[] x, int n) {
        if (n == 0 || x.length == 0){
            return false;
        }

        for (int i = 0; i < n-1; i++){
            if (x[i] > x[i+1]){
                return false;
            }
        }
        return true;
    }

    /**
     * Return an index of value v within the first n elements of x.
     * @param x is the array
     * @param n is the size of the input to be checked
     * @param v is the value to be searched for
     * @return any index k such that k < n and x[k] == v, or -1 if no such k exists
     */
    public static int find(int[] x, int n, int v) {
        //left and right indices
        int left = 0, right = n - 1;
        
        //Binary Search
        while (left <= right) {

            int mid = left + (right - left) / 2;
 
            if (v == x[mid])
                return mid;

             if (v > x[mid])
                left = mid + 1;
 
            else
                right = mid - 1;
        }
        return -1;
    }

    /**
     * Return a sorted, newly created array containing the first n elements of x
     * and ensuring that v is in the new array.
     * @param x is the array
     * @param n is the number of elements to be copied from x
     * @param v is the value to be added to the new array if necessary
     * @return a new array containing the first n elements of x as well as v
     */
    public static int[] copyAndInsert(int[] x, int n, int v) {
        int[] y = Arrays.copyOfRange(x, 0, n - 1);
        int pos = 0;

        //v is in x
        if (find(y, n, v) != -1){
            return y;
        }

        //Find what index v should be
        while(v > x[pos] && pos < n){
            pos++;
        }
        
        //Insert v
        for (int i = 0; i < n + 1; i++) {
            if (i < pos)
                y[i] = x[i];
            else if (i == pos)
                y[i] = v;
            else
                y[i] = x[i - 1];
        }
        return y;
    }

    /**
     * Insert the value v in the first n elements of x if it is not already
     * there, ensuring those elements are still sorted.
     * @param x is the array
     * @param n is the number of elements in the array
     * @param v is the value to be added
     * @return n if v is already in x, otherwise returns n+1
     */
    public static int insertInPlace(int[] x, int n, int v) {
        if (find(x, n, v) != -1){
            return n;
        }
        int pos = 0;
        while(v > x[pos] && pos < n){
            pos++;
        }
        int[] copy = Arrays.copyOfRange(x, 0, n);
        for (int i = 0; i <= n; i++) {
            if (i < pos)
                x[i] = copy[i];
            else if (i == pos)
                x[i] = v;
            else
                x[i] = copy[i - 1];
        }
        return n + 1;
    }

    /**
     * Sort the first n elements of x in-place in non-decreasing order using
     * insertion sort.
     * @param x is the array to be sorted
     * @param n is the number of elements of the array to be sorted
     */
    public static void insertSort(int[] x, int n) {
        for (int i=1; i < n; ++i)
        {
            int curr = x[i];
            int prev = i-1;

            while (prev >= 0 && x[prev] > curr)
            {
                x[prev + 1] = x[prev];
                prev = prev - 1;
            }
            x[prev + 1] = curr;
        }
    }
}
