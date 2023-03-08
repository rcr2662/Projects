/* MULTITHREADING <Flight.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * Roberto Reyes
 * rcr2662
 * 17360
 * Slip days used: <0>
 * Fall 2021
 */
package assignment6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class Flight {
    /**
     * the delay time you will use when print tickets
     */
    private int printDelay; // 50 ms. Use it to fix the delay time between prints.
    private SalesLogs log;
	private String flightNo;
	private ArrayList<Seat> first;
	private ArrayList<Seat> business;
	private ArrayList<Seat> economy;
	private ArrayBlockingQueue<Ticket> queue = new ArrayBlockingQueue<Ticket>(1, true);

    public Flight(String flightNo, int firstNumRows, int businessNumRows, int economyNumRows) {
		this.flightNo = flightNo;
    	this.printDelay = 50;// 50 ms. Use it to fix the delay time between
    	this.log = new SalesLogs();
		this.first = new ArrayList<Seat>();
		this.business = new ArrayList<Seat>();
		this.economy = new ArrayList<Seat>();
		Seat seat;
		SeatLetter[] first_seats = {SeatLetter.A, SeatLetter.B, SeatLetter.E, SeatLetter.F};
		SeatClass sc = SeatClass.FIRST;

		//Adds first class seats
		for (int i = 1; i <= firstNumRows; i++){
			for(SeatLetter letter: first_seats){
				seat = new Seat(sc, i, letter);
				first.add(seat);
			}
		}
		//Adds business class seats
		for (int i = firstNumRows + 1; i <= businessNumRows; i++){
			for(SeatLetter letter: SeatLetter.values()){
				seat = new Seat(sc, i, letter);
				business.add(seat);
			}
		}

		//Adds economy class seats
		for (int i = firstNumRows + businessNumRows + 1; i <= businessNumRows; i++){
			for(SeatLetter letter: SeatLetter.values()){
				seat = new Seat(sc, i, letter);
				economy.add(seat);
			}
		}
		


        // TODO: Implement the rest of this constructor
    }
    
    public void setPrintDelay(int printDelay) {
        this.printDelay = printDelay;
    }

    public int getPrintDelay() {
        return printDelay;
    }

    /**
     * Returns the next available seat not yet reserved for a given class
     *
     * @param seatClass a seat class(FIRST, BUSINESS, ECONOMY)
     * @return the next available seat or null if flight is full
     */
	public Seat getNextAvailableSeat(SeatClass seatClass) {
		// TODO: Implement this method
		Seat available;
		if(economy.size() != 0){
			switch(seatClass){
				case FIRST:
					if(first.size() == 0){
						if(business.size() == 0){
							available = economy.get(0);
							economy.remove(0);
						}
						else{
							available = business.get(0);
							business.remove(0);
						} 
					}
					else{
						available = first.get(0);
						first.remove(0);
					}
					break;
				case BUSINESS:
					if(business.size() == 0){
						available = economy.get(0);
						economy.remove(0);
					}
					else{
						available = business.get(0);
						business.remove(0);
					} 	
					break;
				default:
					available = economy.get(0);
					economy.remove(0);
			}
			log.addSeat(available);
			return available;
		}
		return null;
        
	}

	/**
     * Prints a ticket to the console for the customer after they reserve a seat.
     *
     * @param seat a particular seat in the airplane
     * @return a flight ticket or null if a ticket office failed to reserve the seat
     */
	public synchronized Ticket printTicket(String officeId, Seat seat, int customer) {
        // TODO: Implement this method
		if (seat != null){
			Ticket ticket = new Ticket(flightNo, officeId, seat, customer);
			log.addTicket(ticket);
			try {
				queue.put(ticket);
				wait(printDelay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			return ticket;
		}
        return null;
    }

	/**
     * Lists all seats sold for this flight in order of purchase.
     *
     * @return list of seats sold
     */
    public List<Seat> getSeatLog() {
        // TODO: Implement this method
        return log.getSeatLog();
    }

    /**
     * Lists all tickets sold for this flight in order of printing.
     *
     * @return list of tickets sold
     */
    public List<Ticket> getTransactionLog() {
        // TODO: Implement this method
        return log.getTicketLog();
    }
    
    static enum SeatClass {
		FIRST(0), BUSINESS(1), ECONOMY(2);

		private Integer intValue;

		private SeatClass(final Integer intValue) {
			this.intValue = intValue;
		}

		public Integer getIntValue() {
			return intValue;
		}
	}

	static enum SeatLetter {
		A(0), B(1), C(2), D(3), E(4), F(5);

		private Integer intValue;

		private SeatLetter(final Integer intValue) {
			this.intValue = intValue;
		}

		public Integer getIntValue() {
			return intValue;
		}
	}

	/**
     * Represents a seat in the airplane
     * FIRST Class: 1A, 1B, 1E, 1F ... 
     * BUSINESS Class: 2A, 2B, 2C, 2D, 2E, 2F  ...
     * ECONOMY Class: 3A, 3B, 3C, 3D, 3E, 3F  ...
     * (Row numbers for each class are subject to change)
     */
	static class Seat {
		private SeatClass seatClass;
		private int row;
		private SeatLetter letter;

		public Seat(SeatClass seatClass, int row, SeatLetter letter) {
			this.seatClass = seatClass;
			this.row = row;
			this.letter = letter;
		}

		public SeatClass getSeatClass() {
			return seatClass;
		}

		public void setSeatClass(SeatClass seatClass) {
			this.seatClass = seatClass;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public SeatLetter getLetter() {
			return letter;
		}

		public void setLetter(SeatLetter letter) {
			this.letter = letter;
		}

		@Override
		public String toString() {
			return Integer.toString(row) + letter + " (" + seatClass.toString() + ")";
		}
	}

	/**
 	 * Represents a flight ticket purchased by a customer
 	 */
	static class Ticket {
		private String flightNo;
		private String officeId;
		private Seat seat;
		private int customer;
		public static final int TICKET_STRING_ROW_LENGTH = 31;

		public Ticket(String flightNo, String officeId, Seat seat, int customer) {
			this.flightNo = flightNo;
			this.officeId = officeId;
			this.seat = seat;
			this.customer = customer;
		}

		public int getCustomer() {
			return customer;
		}

		public void setCustomer(int customer) {
			this.customer = customer;
		}

		public String getOfficeId() {
			return officeId;
		}

		public void setOfficeId(String officeId) {
			this.officeId = officeId;
		}
		
		@Override
		public String toString() {
			String result, dashLine, flightLine, officeLine, seatLine, customerLine, eol;

			eol = System.getProperty("line.separator");

			dashLine = new String(new char[TICKET_STRING_ROW_LENGTH]).replace('\0', '-');

			flightLine = "| Flight Number: " + flightNo;
			for (int i = flightLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				flightLine += " ";
			}
			flightLine += "|";

			officeLine = "| Ticket Office ID: " + officeId;
			for (int i = officeLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				officeLine += " ";
			}
			officeLine += "|";

			seatLine = "| Seat: " + seat.toString();
			for (int i = seatLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				seatLine += " ";
			}
			seatLine += "|";

			customerLine = "| Client: " + customer;
			for (int i = customerLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				customerLine += " ";
			}
			customerLine += "|";

			result = dashLine + eol + flightLine + eol + officeLine + eol + seatLine + eol + customerLine + eol
					+ dashLine;

			return result;
		}
	}

	/**
	 * SalesLogs are security wrappers around an ArrayList of Seats and one of Tickets
	 * that cannot be altered, except for adding to them.
	 * getSeatLog returns a copy of the internal ArrayList of Seats.
	 * getTicketLog returns a copy of the internal ArrayList of Tickets.
	 */
	static class SalesLogs {
		private ArrayList<Seat> seatLog;
		private ArrayList<Ticket> ticketLog;

		private SalesLogs() {
			seatLog = new ArrayList<Seat>();
			ticketLog = new ArrayList<Ticket>();
		}

		@SuppressWarnings("unchecked")
		public ArrayList<Seat> getSeatLog() {
			return (ArrayList<Seat>) seatLog.clone();
		}

		@SuppressWarnings("unchecked")
		public ArrayList<Ticket> getTicketLog() {
			return (ArrayList<Ticket>) ticketLog.clone();
		}

		public void addSeat(Seat s) {
			seatLog.add(s);
		}

		public void addTicket(Ticket t) {
			ticketLog.add(t);
		}
	}
}
