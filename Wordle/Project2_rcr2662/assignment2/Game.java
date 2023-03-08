/*EE422C Project 2 submission by
 * Roberto Reyes
 *rcr2662
 * 17360
 * Spring 2022
 */
package assignment2;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Game {
	private GameConfiguration config;
	private Scanner sc;
    private History history;

	public Game(GameConfiguration configuration, Scanner scanner) {
        config = configuration;
		sc = scanner;
	}

    // play the game
	public void runGame() {
		boolean play = true;
		String fileName = config.wordLength + "_letter_words.txt";
		Dictionary dictionary = new Dictionary(fileName);
        history = new History();
        // while the user still wants to play, play a round
		while (play) {
			System.out.println("Do you want to play a new game? (y/n)");
			String input = sc.nextLine();
			if (input.equals("y")) {
				playRound(dictionary);
			} else if (input.equals("n")) {
				play = false;
			}
            // clear history after each game
            history.clearHistory();
		}
	}

    // play one game
	private void playRound(Dictionary dictionary) {
        // get a random word
		String word = dictionary.getRandomWord();
		if (config.testMode) {
			System.out.println(word);
		}
        int guessesLeft = config.numGuesses;
        String guess = "";
        // while the user has not yet guessed the correct word or they still
        // have guesses left, process their guess
        do {
            guess = getGuess(dictionary);
            // special history case
            if (guess.equals("[history]")) {
                history.printHistory();
            } else {
                // get feedback and add this guess to the history
                String feedback = processGuess(word, guess);
                history.registerGuess(guess, feedback);
                System.out.println(feedback);
                guessesLeft--;
                if (guessesLeft > 0 && !guess.equals(word)) {
                    System.out.println("You have " + guessesLeft + " guess(es) remaining.");
                }
            }
        } while (guessesLeft > 0 && !guess.equals(word));
        if (guess.equals(word)) {
            System.out.println("Congratulations! You have guessed the word correctly.");
        } else if (guessesLeft == 0) {
            System.out.println("You have run out of guesses.");
            System.out.println("The correct word was \"" + word + "\".");
        }
	}

    // Get a guess from the user, if they enter an invalid guess, keep prompting
    // for a new guess
    private String getGuess(Dictionary dictionary) {
        String guess = "";
        boolean guessValid = false;
        while (!guessValid) {
            System.out.println("Enter your guess: ");
            guess = sc.nextLine();
            if (guess.equals("[history]")) {
                return guess;
            } else if (guess.length() != config.wordLength) {
			    System.out.println("This word has an incorrect length. Please try again.");
		    } else if (!dictionary.containsWord(guess)) {
			    System.out.println("This word is not in the dictionary. Please try again.");
		    } else {
                guessValid = true;
            }
        }
        return guess;
    }

    // Process a users guess and return the feedback
    private String processGuess(String word, String guess) {
        // organize a map of letters with the indexes of them in the word as the values
        HashMap<Character, ArrayList<Integer>> correctLetters = getLetters(word);
        HashMap<Character, ArrayList<Integer>> guessLetters = getLetters(guess);
        char[] feedback = new char[config.wordLength];
        // loop through each letter in the guess and report the feedback for that letter
        for (Map.Entry<Character, ArrayList<Integer>> entry : guessLetters.entrySet()) {
            char letter = entry.getKey();
            ArrayList<Integer> indexes = entry.getValue();
            if (correctLetters.containsKey(letter)) {
                ArrayList<Integer> correctIndexes = correctLetters.get(letter);
                if (indexes.size() == 1) {
                    // letter appears once in the word
                    handleSingleInstances(indexes, correctIndexes, feedback);
                } else {
                    // letter appears multiple times in the word
                    handleMultipleInstances(indexes, correctIndexes, feedback);
                }
            } else {
                // Letter is not in the correct word, put an underscore at its index
                for (int index = 0; index < indexes.size(); index++) {
                    int position = indexes.get(index);
                    feedback[position] = '_';
                }
            }
        }
        // return a string of the feedback char array
        return String.valueOf(feedback);
    }

    // Handle feedback for a letter that only appears once in the correct word 
    private void handleSingleInstances(ArrayList<Integer> indexes,
                ArrayList<Integer> correctIndexes, char[] feedback) {
        int position = indexes.get(0);
        // correctIndexes are the indexes of letters in the correct word
        // The correctIndexes will contain the index in the guess if they are
        // in the same position
        int correctPosition = correctIndexes.indexOf(position);
        if (correctPosition != -1) {
            feedback[position] = 'G';
        } else {
            feedback[position] = 'Y';
        }
    }

    // Provide feedback for letters that appear multiple times
    private void handleMultipleInstances(ArrayList<Integer> indexes,
                ArrayList<Integer> correctIndexes, char[] feedback) {
        // First provide green feedback for each letter that is in the correct
        // position
        for (int index = 0; index < indexes.size(); index++) {
            int position = indexes.get(index);
            int correctPosition = correctIndexes.indexOf(position);
            if (correctPosition != -1) {
                feedback[position] = 'G';
                indexes.remove(index);
                correctIndexes.remove(correctPosition);
                index--;
            }
        }
        // Then provide yellow feedback for the remaining instances that the correct
        // word can handle
        while (correctIndexes.size() > 0 && indexes.size() > 0) {
            int position = indexes.get(0);
            feedback[position] = 'Y';
            indexes.remove(0);
            correctIndexes.remove(0);
        }
        // Provide absent feedback for every other position
        for (int index = 0; index < indexes.size(); index++) {
            int position = indexes.get(index);
            feedback[position] = '_';
        }
    }

    // Organize a map of a word, with the letters as keys and their indexes
    // as the values
    private HashMap<Character, ArrayList<Integer>> getLetters(String word) {
        HashMap<Character, ArrayList<Integer>> letters = new HashMap<>();
        for (int index = 0; index < word.length(); index++) {
            char letter = word.charAt(index);
            // add a new index to the list of indexes
            if (letters.containsKey(letter)) {
                ArrayList<Integer> indexes = letters.get(letter);
                indexes.add(index);
                letters.replace(letter, indexes);
            } else {
                // make a new entry for this new letter
                ArrayList<Integer> indexes = new ArrayList<>();
                indexes.add(index);
                letters.put(letter, indexes);
            }
        }
        return letters;
    }
}
