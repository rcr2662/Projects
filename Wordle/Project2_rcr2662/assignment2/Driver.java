/*EE422C Project 2 submission by
 * Roberto Reyes
 *rcr2662
 * 17360
 * Spring 2022
 */
package assignment2;

import java.util.Scanner;

public class Driver {

	public void start(GameConfiguration config) {
		System.out.println("Hello! Welcome to Wordle.");
		Scanner sc = new Scanner(System.in);
		Game wordle = new Game(config, sc);
		wordle.runGame();
		sc.close();
    }

	public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration(5, 6, false);
		Driver driver = new Driver();
		driver.start(config);
	}
}
