/* MULTITHREADING <BookingClient.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * Roberto Reyes
 * rcr2662
 * 17360
 * Slip days used: <0>
 * Fall 2021
 */
package assignment6;
import assignment6.Flight.SeatClass;
import assignment6.Flight;

public class Office extends Thread{
    String id;
    SeatClass[] classes;

    public Office(String id, SeatClass[] classes){
        this.id = id;
        this.classes = classes;
    }
    public void run(){
    }
}
