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

import java.util.Map;
import java.util.Set;

import assignment6.Flight.SeatClass;
import assignment6.Flight.Ticket;
import assignment6.Flight.Seat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.io.IOException;
import java.lang.Thread;

public class BookingClient {
    Map<String, SeatClass[]> offices;
    Flight flight;
	/**
     * @param offices maps ticket office id to seat class preferences of customers in line
     * @param flight the flight for which tickets are sold for
     */
    public BookingClient(Map<String, SeatClass[]> offices, Flight flight) {
        // TODO: Implement this constructor
        this.offices = offices;
        this.flight = flight;
    }

    /**
     * Starts the ticket office simulation by creating (and starting) threads
     * for each ticket office to sell tickets for the given flight
     *
     * @return list of threads used in the simulation,
     * should have as many threads as there are ticket offices
     */
    public List<Thread> simulate() {
        List<Thread> threads = new ArrayList<Thread>();
        Office office;
        for(String id : offices.keySet()){
            office = new Office(id, offices.get(id));
            threads.add(office);
        }
        for(Thread thread : threads){
            thread.run();
        }
        return threads;
    }

    public static void main(String[] args) {
        // TODO: Initialize test data to description
        Map<String, SeatClass[]> offices;
        offices.put("TO1", ["FIRST, BUSINESS, ECONOMY, BUSINESS, ECONOMY, FIRST"]);
        Flight flight = new Flight("TR123", 2, 7, 12);
        BookingClient client = new BookingClient(offices, flight);
        client.simulate();
    }

    public class Office extends Thread{
        String id;
        SeatClass[] classes;
    
        public Office(String id, SeatClass[] classes){
            this.id = id;
            this.classes = classes;
        }
        public void run(){
            for(int i = 0; i < classes.length; i++){
                Seat seat = flight.getNextAvailableSeat(classes[i]);
                Ticket ticket = flight.printTicket(id, seat, i+1);
                if(ticket != null )System.out.println(ticket.toString());
                else{
                    try {
                        full();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        //e.printStackTrace();
                    }
                } 
            }
        }
    }
    public synchronized void full() throws IOException{
        throw new IOException("Sorry, we are sold out!");
    }
}
