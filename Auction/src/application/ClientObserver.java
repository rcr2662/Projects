/*
* EE422C Final Project submission by
* Replace <...> with your actual data.
* Roberto Reyes
* rcr2662
* 17360
* Spring 2022
*/

package application;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

public class ClientObserver extends PrintWriter implements Observer {
	public ClientObserver(OutputStream out) {
		super(out);
	}
	@Override
	public void update(Observable o, Object arg) {
		this.println(arg); 
		this.flush(); 
	}

}