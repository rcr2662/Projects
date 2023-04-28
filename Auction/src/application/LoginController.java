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
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController extends Observable{
	@FXML
	private Label lblStatus;
	
	@FXML
	private TextField txtUsername;
	
	private static String host = "127.0.0.1";
	private BufferedReader fromServer;
	private PrintWriter toServer;
	private Scanner consoleInput = new Scanner(System.in);
	  
	
	public static ArrayList<String> itemNames = new ArrayList<String>();
	public static ArrayList<String> itemDescriptions = new ArrayList<String>();
	public static ArrayList<Double> minimumPrice = new ArrayList<Double>();
	public static ArrayList<Double> currentBid = new ArrayList<Double>();
	public static ArrayList<Integer> sold = new ArrayList<Integer>();
	public static ArrayList<Double> highestBid = new ArrayList<Double>();
	public static ArrayList<String> images= new ArrayList<String>();
	
	
	public void login(ActionEvent event) throws Exception {
		if(!txtUsername.getText().equals("")) {
			lblStatus.setText("Success");
			Stage primaryStage = new Stage();																
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Client.fxml"));
			Parent root = loader.load();
			ClientController controller = (ClientController) loader.getController();
			controller.welcome.setText("Welcome " + txtUsername.getText() + "!");
			
			setUpNetworking(controller);
			
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle(txtUsername.getText() + "'s Auction Client");
			primaryStage.show();
						
			controller.setComboBoxData();
			
			toServer.println("sendClientUpdatedData,null,null,null");	
			toServer.flush();
		}
		else
			lblStatus.setText("Failed");
			lblStatus.setTextFill(Color.color(1, 0, 0));
	}
	
	private void setUpNetworking(ClientController controller) throws Exception {
		@SuppressWarnings("resource")
	    Socket socket = new Socket(host, 4242);
	    System.out.println("Connecting to... " + socket);
	    fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    toServer = new PrintWriter(socket.getOutputStream());
	    
	    controller.writer = toServer;
	    controller.reader = fromServer;

	    Thread readerThread = new Thread(new Runnable() {
	      @Override
	      public void run() {
	        String input;
	        try {
	          while ((input = fromServer.readLine()) != null) {
	            System.out.println("From server: " + input);
	            
	            //receive item data 
	            String[] temp = input.split(",");
	            if(temp[0].equals("updateBid")) {
	            	int itemIndex= Integer.parseInt(temp[1]); 
	                Double updatedBid =Double.parseDouble(temp[2]);
	            	currentBid.set(itemIndex, updatedBid);
	            	if(itemIndex == ClientController.currentItemIndex && ClientController.isInitialized == 1)
	            		controller.currentBid.setText("CURRENT BID -> " + updatedBid);		
	            }
	            if(temp[0].equals("removeItem")) {
	            	int itemIndex= Integer.parseInt(temp[1]);
	                Double updatedBid =Double.parseDouble(temp[2]);
	            	sold.set(itemIndex, 1);
	            	currentBid.set(itemIndex, updatedBid);
	            	System.out.println(sold.toString());
	            	if(itemIndex == ClientController.currentItemIndex && ClientController.isInitialized == 1) {
	            		controller.currentBid.setText("FINAL BID -> " + updatedBid);		
	            	}
	            }
	            
	            if(temp[0].equals("updatedCurrentBidForLaterClients")) {
	            	int itemIndex = Integer.parseInt(temp[1]);
	            	Double updatedCurrBid = Double.parseDouble(temp[2]);
	            	currentBid.set(itemIndex, updatedCurrBid);
	            }
	            if(temp[0].equals("updatedItemStatusForLaterClients")) {
	            	int itemIndex = Integer.parseInt(temp[1]);
	            	int updatedItemStatus = Integer.parseInt(temp[2]);
	            	sold.set(itemIndex, updatedItemStatus);
	            }
	            if(temp[0].equals("log")) {
	            	fromServer.close();
	            	toServer.close();
	            	System.exit(0);
	            }
	            
	            else
	            	processRequest(input); //receive items from server
	          }
	        } catch (Exception e) {
	          e.printStackTrace();
	        }
	      }
	    });

	    Thread writerThread = new Thread(new Runnable() {
	      @Override
	      public void run() {
	        while (true) {
	          String input = consoleInput.nextLine();
	          sendToServer(input);
	        }
	      }
	    });

	    readerThread.start();
	    writerThread.start();
	  }

	  protected void processRequest(String input) {
		  String[] arr = input.split(",");
		  if(arr[0].equals("itemName")) {
			  itemNames.add(arr[1]);
		  }
		  if(arr[0].equals("itemDesc")) {
			  itemDescriptions.add(arr[1]);
		  }
		  if(arr[0].equals("minPrice")) {
			  Double n = Double.parseDouble(arr[1]);
			  minimumPrice.add(n);
		  }
		  if(arr[0].equals("currBid")) {
			  Double n = Double.parseDouble(arr[1]);
			  currentBid.add(n);
		  }
		  if(arr[0].equals("buyPrice")) {
			  Double n = Double.parseDouble(arr[1]);
			  highestBid.add(n);
		  }
		  if(arr[0].equals("sold")) {
			  Integer n = Integer.parseInt(arr[1]);
			  sold.add(n);
		  }
		  if(arr[0].equals("image")) {
			  images.add(arr[1]);
		  }
	  }

	  protected void sendToServer(String string) {
	    System.out.println("Sending to server: " + string);
	    toServer.println(string);
	    toServer.flush();
	  }

}
