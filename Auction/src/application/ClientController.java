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
import java.io.PrintWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

public class ClientController{

	@FXML
	public Label welcome;
	@FXML
	public TextField txtSend;
	@FXML
	private ComboBox<String> dropDown;
	@FXML
	private Label chooseItem;
	@FXML
	private Label itemName;
	@FXML
	private Label itemDesc;
	@FXML
	private Label minPrice;
	@FXML
	private Label buyPrice;
	@FXML
	public Label currentBid;
	@FXML
	private TextField bidAmount;
	@FXML
	private Label yourCurrentBid;
	@FXML
	private TextArea bidHistory;
	@FXML
	private TextField Feedback;
	@FXML
	public Label itemStatus;
	@FXML
	private ImageView image;
	
	public BufferedReader reader;
	public PrintWriter writer;
	
	public static int currentItemIndex = 0;
	public static int isInitialized = 0;
	
	
	public void logout(ActionEvent event) {
		writer.println("logout");
		writer.flush();
	}
	
	public void sendOver(ActionEvent event) {
		writer.println(txtSend.getText());	//from client to server
		writer.flush();
	}	
	//add items to drop down
	public void setComboBoxData() {
		dropDown.getItems().clear();
		for(int i = 0; i < LoginController.itemNames.size(); i++) {
			dropDown.getItems().addAll(LoginController.itemNames.get(i));
		}
			
	}
	
	//play sound and update item availability and bid
	public void buyNow(ActionEvent event) {
		if(LoginController.sold.get(currentItemIndex)==0) {
			String path = ClientController.class.getResource("/Kaching.mp3").toString();
	        Media media = new Media(path);
	        MediaPlayer effect = new MediaPlayer(media);
	        effect.play();
			Double temp = LoginController.highestBid.get(currentItemIndex);
			LoginController.currentBid.set(currentItemIndex,temp);
		
			bidHistory.setText(bidHistory.getText() + "For $" + temp +", you bought a " + LoginController.itemNames.get(currentItemIndex) + "!\n");
			yourCurrentBid.setText("$" + temp);
		
			sendCurrentBidBackToServer(currentItemIndex, temp, 1);
		}
		else
			Feedback.setText("This item is not available.");
	}
	//send bid to server if valid and update current bid
	public void sendBid(ActionEvent event) {
		int doNotDisplayFlag = 0;
		  try 
	        { 
	            Double temp = Double.parseDouble(bidAmount.getText()); 
	            if(LoginController.sold.get(currentItemIndex) == 1) {
	            	Feedback.setText("This item is not available.");
	            }
	            else if(temp >= LoginController.minimumPrice.get(currentItemIndex) && 
	            		temp > LoginController.currentBid.get(currentItemIndex) && 
	            		temp < LoginController.highestBid.get(currentItemIndex)) {
	            	Feedback.setText("Valid bid!");
	            	LoginController.currentBid.set(currentItemIndex, temp);
	            	currentBid.setText("CURRENT BID -> " +bidAmount.getText());
	    			yourCurrentBid.setText(bidAmount.getText());
	    			bidHistory.setText(bidHistory.getText() + "You bid $" + temp + " dollars on a "+LoginController.itemNames.get(currentItemIndex)+"!\n");
	    			
	    			//send bid to server
	    			sendCurrentBidBackToServer(currentItemIndex,temp, 0);
	            }
	            else if(temp >= LoginController.highestBid.get(currentItemIndex)) {
	            	Feedback.setText("This " + LoginController.itemNames.get(currentItemIndex) +" is yours now! Congrats!");
	            	yourCurrentBid.setText(bidAmount.getText());
	    			bidHistory.setText(bidHistory.getText() + "For $" + temp +", you won a " + LoginController.itemNames.get(currentItemIndex) + "!\n");

	    			yourCurrentBid.setText("$" + temp);
	    			
	    			//update item status
	    			sendCurrentBidBackToServer(currentItemIndex, temp, 1);
	            }
	            else
	            	doNotDisplayFlag = 1;
	        }  
	        catch (NumberFormatException e)  
	        { 
            	Feedback.setText("Please submit a valid bid!");
	        } 
		  
		  if(doNotDisplayFlag == 1) {
          	Feedback.setText("Your bid is too low.");
		  }
	}
	
	public void sendCurrentBidBackToServer(int itemIndex, double updatedBid, int itemStatus) {
		writer.println("updateBid,"+ itemIndex + "," + updatedBid + ","+ itemStatus);
		writer.flush();
	}
	

	@FXML
	public void initialize() {
		dropDown.setOnAction(e ->{
			String selectedItem = (String) dropDown.getValue();
			chooseItem.setText(" BID! BID! BID! BID! BID! BID! BID! BID! BID!");
			yourCurrentBid.setText("");
			isInitialized = 1;
			
			if(selectedItem != "" || selectedItem != null) {
				currentItemIndex = LoginController.itemNames.indexOf(selectedItem);
			}
			
			if(currentItemIndex >= 0) {
				Image i = new Image(LoginController.images.get(currentItemIndex));
				image.setImage(i);
				itemName.setText("Item Name: " +LoginController.itemNames.get(currentItemIndex));
				itemDesc.setText("Item Description: \n" + LoginController.itemDescriptions.get(currentItemIndex));
				itemDesc.setTextFill(Color.web("#0076a3"));
				minPrice.setText("Starting Price: " + (LoginController.minimumPrice.get(currentItemIndex)).toString());
				buyPrice.setText("Buy Price: " + (LoginController.highestBid.get(currentItemIndex)).toString());
				if(LoginController.sold.get(currentItemIndex) == 1) {
					itemStatus.setText("ITEM STATUS -> SOLD");
					currentBid.setText("FINAL BID -> "+(LoginController.currentBid.get(currentItemIndex)).toString());
				}
				else {
					currentBid.setText("CURRENT BID -> "+(LoginController.currentBid.get(currentItemIndex)).toString());
					itemStatus.setText("ITEM STATUS -> AVAILABLE");
				}
			}
		});
	}
}
