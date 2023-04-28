/*
* EE422C Final Project submission by
* Replace <...> with your actual data.
* Roberto Reyes
* rcr2662
* 17360
* Spring 2022
*/

package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage){
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
	        Parent root = fxmlLoader.load(getClass().getResource("Login.fxml").openStream());
			Scene scene = new Scene(root,400, 300);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Login Menu");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
