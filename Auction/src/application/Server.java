/*
* EE422C Final Project submission by
* Replace <...> with your actual data.
* Roberto Reyes
* rcr2662
* 17360
* Spring 2022
*/

package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


public class Server extends Observable{
	
	public static ArrayList<String> itemNames = new ArrayList<String>();
	public static ArrayList<String> itemDescriptions = new ArrayList<String>();
	public static ArrayList<Double> minimumPrice = new ArrayList<Double>();
	public static ArrayList<Double> currentBid = new ArrayList<Double>();
	public static ArrayList<Integer> sold = new ArrayList<Integer>();
	public static ArrayList<Double> highestBid = new ArrayList<Double>();
	public static ArrayList<String> images = new ArrayList<String>();
	
	ArrayList<String> currentClients;		//for multiple clients
		
	public static void mitemsn(String[] args) throws IOException{
		JsonReader reader = new JsonReader(new FileReader("C:/Users/reyes/eclipse-workspace/Final_Project/src/application/ItemJsonFile.json"));
		Item[] items = new Gson().fromJson(reader, Item[].class);
		try {
			Server a = new Server();
			a.setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(4242);
		while (true) {
			Socket clientSocket = serverSock.accept();
			
			ClientHandler handler = new ClientHandler(this, clientSocket);
		    this.addObserver(handler);

		    Thread t = new Thread(handler);
		    t.start();
		}
	}
	
	protected void processRequest(String input) {
		String[] parse = input.split(",");
		if(parse[3].equals("0")) {
            int itemIndex= Integer.parseInt(parse[1]); 
            double updatedBid = Double.parseDouble(parse[2]);
            currentBid.set(itemIndex, updatedBid);
            System.out.println("inside process request");
            this.setChanged();
            this.notifyObservers("updateBid,"+ itemIndex + "," + updatedBid);
		}
		if(parse[3].equals("1")) {
			int itemIndex= Integer.parseInt(parse[1]); 
            double updatedBid = Double.parseDouble(parse[2]); 
			sold.set(itemIndex, 1);
            currentBid.set(itemIndex, updatedBid);
			this.setChanged();
			this.notifyObservers("removeItem,"+ itemIndex + "," + updatedBid);	
		}
		
		if(parse[0].equals("sendClientUpdatedData")) {
			//send updated current bids and item status
			System.out.println(currentBid.toString());
			for(int i = 0; i < 5; i++) {
				this.setChanged();
				this.notifyObservers("updatedCurrentBidForLaterClients," + i + "," + currentBid.get(i));
			}
			for(int i = 0; i < 5; i++) {
				this.setChanged();
				this.notifyObservers("updatedItemStatusForLaterClients," + i + "," + sold.get(i));
			}
		}
	 }
	
	class ClientHandler implements Runnable, Observer {

		  private Server server;
		  private Socket clientSocket;
		  private BufferedReader fromClient;
		  private PrintWriter toClient;

		  protected ClientHandler(Server server, Socket clientSocket) {
		    this.server = server;
		    this.clientSocket = clientSocket;
		    try {
		      fromClient = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
		      toClient = new PrintWriter(this.clientSocket.getOutputStream());
		      
		      //send json object as string when sendItemsFlag is true
		      JsonReader reader = new JsonReader(new FileReader("C:/Users/reyes/eclipse-workspace/Final_Project/src/application/ItemJsonFile.json"));
			Item[] items = new Gson().fromJson(reader, Item[].class);
		      for(int i = 0; i < items.length; i++) {
					itemNames.add(items[i].getItemName());
					itemDescriptions.add(items[i].getItemDes());
					minimumPrice.add(items[i].getMinPrice());
					currentBid.add(items[i].getCurrBid());
					highestBid.add(items[i].getBuyPrice());
					sold.add(items[i].getSold());	
					images.add(items[i].getImageString());
				}
		      
		      for(int i = 0; i < items.length; i++) {
					this.sendToClient("itemName," + items[i].getItemName());
					this.sendToClient("itemDesc," + items[i].getItemDes());
					this.sendToClient("minPrice," + items[i].getMinPrice());
					this.sendToClient("currBid," + items[i].getCurrBid());
			    	this.sendToClient("buyPrice," + items[i].getBuyPrice());
			    	this.sendToClient("sold," + items[i].getSold());
			    	this.sendToClient("image," + items[i].getImageString());
				}
		      
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		  }

		  protected void sendToClient(String string) {
		    System.out.println("Sending to client: " + string);
		    toClient.println(string);
		    toClient.flush();
		  }

		  @Override
		  public void run() {
		    String input;
		    try {
		      while ((input = fromClient.readLine()) != null) {
		        System.out.println("From client: " + input);
		        if(input.equals("logout")) {
		        	toClient.println("log,");
		        	toClient.flush();
		        	toClient.close();
		        	fromClient.close();
		        	clientSocket.close();
		        	return;
		        }
		        else
		        	server.processRequest(input);
		      }
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		  }
		  
		  
		  @Override
		  public void update(Observable o, Object arg) {
		    this.sendToClient((String) arg);
		  }
	}
}