/*EE422C Project 2 submission by
 * Roberto Reyes
 *rcr2662
 * 17360
 * Spring 2022
 */
package assignment2;

import java.util.ArrayList;

public class History {

    private static final int NUM_LETTERS = 26;

    private ArrayList<String> guesses;
    private ArrayList<String> feedbackGiven;
    private ArrayList<Character> greenLetters;
    private ArrayList<Character> yellowLetters;
    private ArrayList<Character> absentLetters;
    private boolean[] unusedLetters;

    public History() {
        guesses = new ArrayList<>();
        feedbackGiven = new ArrayList<>();
        greenLetters = new ArrayList<>();
        yellowLetters = new ArrayList<>();
        absentLetters = new ArrayList<>();
        unusedLetters = new boolean[NUM_LETTERS];
        // originally all letters are unused
        for (int index = 0; index < NUM_LETTERS; index++) {
            unusedLetters[index] = true;
        }
    }

    // Print the history of guesses and letters
    public void printHistory() {
        for (int index = 0; index < guesses.size(); index++) {
            System.out.println(guesses.get(index) + "->" + feedbackGiven.get(index));
        }
        System.out.println("--------");
        printList(greenLetters, "Green");
        printList(yellowLetters, "Yellow");
        printList(absentLetters, "Absent");
        printUnusedArray(unusedLetters);
    }

    // Print a list of letters
    private void printList(ArrayList<Character> lettersList, String header) {
        System.out.print(header + ":");
        if (lettersList.size() > 0) {
            System.out.print(" " + lettersList.get(0));
        }
        for (int index = 1; index < lettersList.size(); index++) {
            System.out.print(", " + lettersList.get(index));
        }
        System.out.println();
    }

    // Print the unused array of letters
    private void printUnusedArray(boolean[] lettersArray) {
        System.out.print("Unused:");
        int startIndex = 0;
        while (!lettersArray[startIndex]) {
            startIndex++;
        }
        System.out.print(" " + ((char) ('a' + startIndex)));
        for (int index = startIndex + 1; index < NUM_LETTERS; index++) {
            if (lettersArray[index]) {
                System.out.print(", " + ((char) ('a' + index)));
            }
        }
        System.out.println();
    }

    // Add a new guess to the history
    public void registerGuess(String guess, String feedback) {
        guesses.add(guess);
        feedbackGiven.add(feedback);
        ArrayList<Character> greenCopy = new ArrayList<>(greenLetters);
        // Check each letter in the feedback
        for (int index = 0; index < feedback.length(); index++) {
            char result = feedback.charAt(index);
            char letter = guess.charAt(index);
            // Add to list of green letters if this letter has previously 
            // been yellow and not green
            if (result == 'G') {
                if (yellowLetters.contains(letter) && !greenLetters.contains(letter)) {
                    yellowLetters.remove(yellowLetters.indexOf(letter));
                    greenLetters.add(letter);
                } else if (!greenCopy.contains(letter)) {
                    // Do not add duplicate letters to the green category
                    greenLetters.add(letter);
                }
            } else if (result == 'Y') {
                // Add to yellow if yellow or green feedback has not been given before
                if (!yellowLetters.contains(letter) && !greenCopy.contains(letter)) {
                    yellowLetters.add(letter);
                }
            } else if (result == '_') {
                // If this letter has another green feedback, add to green feedback instead
                if (findGreen(guess, feedback, letter, index + 1) && !greenCopy.contains(letter)) {
                    greenLetters.add(letter);
                    greenCopy.add(letter);
                } else if (!greenLetters.contains(letter) && !yellowLetters.contains(letter)
                    && !absentLetters.contains(letter)) {
                    // Add to absent letters if letter is also not in any other category
                    absentLetters.add(letter);
                }
            }
            // Mark letter as unused
            int letterIndex = letter - 'a';
            unusedLetters[letterIndex] = false;
        }
    }

    // Find a green letter in the feedback string
    private boolean findGreen(String guess, String feedback, char target, int startIndex) {
        for (int index = startIndex; index < guess.length(); index++) {
            if (guess.charAt(index) == target && feedback.charAt(index) == 'G') {
                return true;
            }
        }
        return false;
    }

    // Clear the history of guesses for a new game
    public void clearHistory() {
        guesses.clear();
        feedbackGiven.clear();
        greenLetters.clear();
        yellowLetters.clear();
        absentLetters.clear();
        for (int index = 0; index < NUM_LETTERS; index++) {
            unusedLetters[index] = true;
        }
    }
}
