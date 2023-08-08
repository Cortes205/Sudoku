package main.components;
import main.*;
import javax.swing.*;
import checkers.*;
import java.awt.*;
import java.util.*;

public interface PuzzleMaker extends PossibilityRemover {
	
	Random roll = new Random();
	String[] board = new String[81];
	ArrayList<Integer> takenBoxes = new ArrayList<Integer>();

  	// Organizes the board randomly
	default void setPuzzle(ArrayList<Box> boxes, JFrame frame, int difficulty, Application app) {
		try {
			for (int gridNumber = 0; gridNumber < 9; gridNumber++) {
				drawGrid(boxes, gridNumber, frame, difficulty, app);
			}
			savePuzzle(boxes);
			clear(boxes);
			printPuzzle(boxes, difficulty);
		} catch (Exception e) {
			reset(boxes, frame, difficulty, app);
		}
	}
	
	// Stores the solution into an array
	default void savePuzzle(ArrayList<Box> boxes) {
		for (int boxNumber = 0; boxNumber < board.length; boxNumber++) {
			board[boxNumber] = boxes.get(boxNumber).getText();
		}
	}
	
	// Makes the board blank
	default void clear(ArrayList<Box> boxes) {
		for (int boxNumber = 0; boxNumber < boxes.size(); boxNumber++) {
			boxes.get(boxNumber).setText("");
			boxes.get(boxNumber).decolour();
			boxes.get(boxNumber).setEditable(true);
		}
	}
	
	// Print out random boxes (amount based on difficulty)
	default void printPuzzle(ArrayList<Box> boxes, int difficulty) {
		int totalBoxAmount = 0;
		switch (difficulty) {
			case 0: totalBoxAmount = 36; break;
			case 1: totalBoxAmount = 30; break;
			case 2: totalBoxAmount = 25; break;
			case 3: totalBoxAmount = 15; break;
		}
		
		for (int amountOfBoxes = 0; amountOfBoxes < totalBoxAmount; amountOfBoxes++) {
			boolean taken = true;
			int box = 0;
			while (taken) {
				box = roll.nextInt(81);
				if (takenBoxes.size() != 0) {
					if (!takenBoxes.contains(box)) taken = false;
				} else {taken = false;}
			}
			takenBoxes.add(box);
			boxes.get(box).setText(board[box]);
			boxes.get(box).setEditable(false);
			boxes.get(box).setTextColor(new Color(0, 50, 200));
		}
	}
	
	// Reveals a random box (limit of hints based on difficulty)
	default int giveHint(ArrayList<Box> boxes, JFrame frame, int hints, int difficulty) {
		if (hasGivenUp(boxes)) return hints;
		if (difficulty == 3) {
			JOptionPane.showMessageDialog(frame, "There are no hints in this gamemode", "Hint Manager" , 1);
			return hints;
		}
		
		if (hints != 0) {
			boolean taken = true;
			int box = 0;
			while (taken) {
				box = roll.nextInt(81);
				if (takenBoxes.size() != 0) {
					if (!takenBoxes.contains(box)) taken = false;
				} else {taken = false;}
			}
			takenBoxes.add(box);
			if (boxes.get(box).isEditable()) {
				boxes.get(box).setText(board[box]);
				boxes.get(box).setEditable(false);
				boxes.get(box).setTextColor(new Color(0, 50, 200));
				boxes.get(box).setBackground(Color.YELLOW);	
			}
			hints--;
			JOptionPane.showMessageDialog(frame, "You have " + hints + " hints left", "Hint Manager" , 1);
		} else {
			JOptionPane.showMessageDialog(frame, "You have zero hints left", "Hint Manager" , 1);
		}
		return hints;
	}
	
	// Reveals the entire solution if a player has given up
	default void printSolution(ArrayList<Box> boxes, JFrame frame) {
		boolean givenUp = hasGivenUp(boxes);
		int choice = -1;
		if (!givenUp) choice = JOptionPane.showConfirmDialog(frame, "Are you ready to see the solution?", "Game Message", 0);
		if (choice == JOptionPane.YES_OPTION) {
			for (int boxNumber = 0; boxNumber < board.length; boxNumber++) {
				if (boxes.get(boxNumber).isEditable()) {
					boxes.get(boxNumber).decolour();
					boxes.get(boxNumber).setText(board[boxNumber]);
					boxes.get(boxNumber).setEditable(false);
				}
				frame.repaint();
				frame.revalidate();
			}
		}
	}
	
	// Highlights the boxes the player has put in (red = wrong, green = correct)
	default void validate(ArrayList<Box> boxes) {
		boolean complete = false;
		for (int boxNumber = 0; boxNumber < board.length; boxNumber++) {
				if (boxes.get(boxNumber).isEditable() && !boxes.get(boxNumber).getText().equals("")) {
					complete = true;
				}
		}
		if (complete) new SolutionChecker(boxes, board);
	}
	
	// Asks the player if they want to restart, this will clear the board then create a new one.
	default boolean reset(ArrayList<Box> boxes, JFrame frame, int difficulty, Application app) {
		int choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to restart? All progress will be lost", "Game Message", 0);
		if (choice == JOptionPane.YES_OPTION) {
			takenBoxes.removeAll(takenBoxes);
			int hints = 15;
			switch(difficulty) {
				case 1: hints = 10; break;
				case 2: hints = 5; break;
				case 3: hints = 0; break;
			}
			app.setHints(hints);
			new LoadingScreen(true, boxes, frame, difficulty, app);
			return true;
		}
		return false;
	}
	
	// Show/hide menu bar
	default void hide(JMenuBar menuBar) {
		if (menuBar.isVisible()) menuBar.setVisible(false);
		else menuBar.setVisible(true);
	}
	
	// Exit application
	default void exit(JFrame frame) {
		int choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Game Message", 0);
		if (choice == JOptionPane.YES_OPTION) System.exit(0);
	}
	
	/* Checks whether or not the player has given up or not so it does not react when you ask for a hint
	 * or try to check the solution while you've already given up */
	default boolean hasGivenUp(ArrayList<Box> boxes) {
		boolean givenUp = true;
		for (int boxNumber = 0; boxNumber < board.length; boxNumber++) {
				if (boxes.get(boxNumber).isEditable() && boxes.get(boxNumber).getText().equals("")) {
					givenUp = false;
				}
		}
		return givenUp;
	}
	
	// Checks if it is possible to input a number into a certain box, and does so if true
	default boolean input(ArrayList<Box> boxes, String addition, int boxNumber) {
		if (checkArea(boxes, addition, boxNumber)) {
			boxes.get(boxNumber).setText(addition);
			removePossibilities(boxes, addition, boxNumber);
			return true;
		}
		return false;
    }

	// Switch two boxes with each other if it is safe to do so without breaking the solution
	default boolean switchBoxes(ArrayList<Box> boxes, int boxOne, int boxTwo) {
		String boxOneText = boxes.get(boxOne).getText();
	    String boxTwoText = boxes.get(boxTwo).getText();
	    if (boxOneText.equals(boxTwoText)) return false;
	    boxes.get(boxOne).setText(""); // Sets the two boxes as blank so when checking it does not check itself
	    boxes.get(boxTwo).setText("");
	    if (checkArea(boxes, boxOneText, boxTwo) && checkArea(boxes, boxTwoText, boxOne)) {
	    	input(boxes, boxTwoText, boxOne); // Switches boxes if true (okay to switch)
		    input(boxes, boxOneText, boxTwo);
		    return true;
		}
	    boxes.get(boxOne).setText(boxOneText); // Keep the boxes as the same if false
	    boxes.get(boxTwo).setText(boxTwoText);
	    return false;
	}
		
	// Force two boxes to switch with each other regardless whether or not the solution is broken
	default boolean forceSwitch(ArrayList<Box> boxes, int boxOne, int boxTwo) {
		 String boxOneText = boxes.get(boxOne).getText();
		 String boxTwoText = boxes.get(boxTwo).getText();
		 if (boxOneText.equals(boxTwoText)) return false;
		 boxes.get(boxOne).setText(String.valueOf(boxTwoText));
		 boxes.get(boxTwo).setText(String.valueOf(boxOneText));
		 return true;
	 }
  
	// Checks to see whether or not the board is complete (this does not check validity, only if the board is filled or not)
	default boolean complete(ArrayList<Box> boxes, int gridNumber) {
		int[] box = collectGrid(gridNumber, 0);
		for (int index = 0; index < box.length; index++) {
			if (boxes.get(box[index]).getText().equals("")) return false;
		}
		return true;
	}
  
	// Follows steps and uses strategies in order to fill any individual grid on the board without creating an invalid solution
	default void drawGrid(ArrayList<Box> boxes, int gridNumber, JFrame frame, int difficulty, Application app) {		
		
		// Get all the box numbers within a grid
		int[] boxNumbersWithinGrid = collectGrid(gridNumber, 0);
		resetAllPossibilities(boxes, gridNumber);
		
		// Randomly fill the grid with its possibilities, keeping track of what box and what number has been used
		ArrayList<String> takenNum = new ArrayList<String>();
		ArrayList<Integer> takenBox = new ArrayList<Integer>();
		String addition = "";
		int boxToFill = 0;
		int retry = 0;
		for (int boxNumber = 0; boxNumber < 9; boxNumber++) {
		      boolean taken = true;
		      while (taken) {
		        if (retry == 18) break; // breaks an infinite loop after 18 tries
		    	boxToFill = boxNumbersWithinGrid[roll.nextInt(9)];
		        if (boxes.get(boxToFill).getPossibilitySize() == 0) {
		        	retry++;
		        	continue;
		        }
		        addition = boxes.get(boxToFill).getPossibilities().get(roll.nextInt(boxes.get(boxToFill).getPossibilitySize()));
		        if (takenNum.size() != 0) {
		          if (!takenNum.contains(addition) && !takenBox.contains(boxToFill)) taken = false;
		        } else {taken = false;}
		      }
		      takenNum.add(addition);
		      takenBox.add(boxToFill);
		      input(boxes, addition, boxToFill);
		}
		
		/* If the board is still not complete, this section goes through each 
		 * individual empty box and checks if it can input any remaining number 
		 * that is missing */
		if (!complete(boxes, gridNumber)) {
			ArrayList<Integer> emptyBoxes = findEmptyBoxes(boxes, gridNumber);
			ArrayList<String> missing = findMissingNumbers(boxes, boxNumbersWithinGrid);
			for (int emptyBoxNum = 0; emptyBoxNum < emptyBoxes.size(); emptyBoxNum++) {
				for (int missingNumberNum = 0; missingNumberNum < missing.size(); missingNumberNum++) {
					if (input(boxes, missing.get(missingNumberNum), emptyBoxes.get(emptyBoxNum))) {
						emptyBoxes.remove(emptyBoxNum);
						emptyBoxNum--;
						missing.remove(missingNumberNum);
						break;
					}
				}
			}
			
			/* If the board is still not complete, this section goes through each 
			 * remaining empty box and checks what box is impeding it from placing
			 * a certain number inside */
			if (!complete(boxes, gridNumber)) {
				for (int loopThrough = 0; loopThrough < 10; loopThrough++) { // loops through 10 times in order to ensure max results
					for (int missingNumberIndex = 0; missingNumberIndex < missing.size(); missingNumberIndex++) {
						for (int emptyBoxIndex = 0; emptyBoxIndex < emptyBoxes.size(); emptyBoxIndex++) {
							boolean removeMissingNumbers = false;
							ArrayList<Integer> impedingBoxes = impeding(boxes, missing.get(missingNumberIndex), emptyBoxes.get(emptyBoxIndex));
							
							for (int impedingBoxNumber = 0; impedingBoxNumber < impedingBoxes.size(); impedingBoxNumber++) {						
								int gridMovement = 1; // initially sets the movement to 1 (switching numbers within a row)
								int initialGrid = findGrid(emptyBoxes.get(emptyBoxIndex));
								int impedingGrid = findGrid(impedingBoxes.get(impedingBoxNumber));
								// if the impeding grid is to the left of the initial grid, you must switch numbers within a column (+/- 9)
								if (initialGrid - impedingGrid == 1) gridMovement = 9;
								int start = 0;
								
								// If your switching within a row, start with the first number of that row
								if (gridMovement == 1) start = minimumOfGridRow(boxes, impedingBoxes.get(impedingBoxNumber)); 
								// If your switching within a column, start with the first number of that column
								else start = minimumOfGridCol(boxes, impedingBoxes.get(impedingBoxNumber));
								
								for (int boxToSwitch = 0; boxToSwitch < 3; boxToSwitch++) { // only three possible boxes to switch within a grid
									// Check to see if you can switch the impeding box with another box in its row/col
									if (switchBoxes(boxes, impedingBoxes.get(impedingBoxNumber), start + (boxToSwitch*gridMovement))) {
										/* If you switched the boxes safely, check to see if you can input the missing number into the empty 
										 * box from the initial grid */
										if (input(boxes, missing.get(missingNumberIndex), emptyBoxes.get(emptyBoxIndex))) {
											// If you can input the number then you will remove the missing number and empty box from the lists
											removeMissingNumbers = true;
											break;
										}
									}
								}
								
								/* When you reach the end of the impeding boxes list & you were able to input the missing numbers, 
								 * remove both the empty box and missing number from the list */
								if (impedingBoxNumber == impedingBoxes.size()-1 && removeMissingNumbers) {
									emptyBoxes.remove(emptyBoxIndex);
									missing.remove(missingNumberIndex);
									
									// These two are used to avoid out of bound errors
									missingNumberIndex--;
									if (missingNumberIndex == -1) missingNumberIndex = 0;
								}
							}
						}
					}
				}
			}
			
			/* If the board is still not complete, this section fills in an empty box regardless of
			 * validity, then checks to see if it can switch with any other box in the grid in
			 * order to create a valid spot */
			if (!complete(boxes, gridNumber)) {
				for (int emptyBoxNumber = 0; emptyBoxNumber < emptyBoxes.size(); emptyBoxNumber++) {
					for (int boxNumberWithinGrid = 0; boxNumberWithinGrid < boxNumbersWithinGrid.length; boxNumberWithinGrid++) {
						boxes.get(emptyBoxes.get(emptyBoxNumber)).setText(missing.get(emptyBoxNumber)); // Forces number in
						// Checks to see if the two boxes can be successfully switched
						if (switchBoxes(boxes, boxNumbersWithinGrid[boxNumberWithinGrid], emptyBoxes.get(emptyBoxNumber))) {
							emptyBoxes.remove(emptyBoxNumber);
							missing.remove(emptyBoxNumber);
							emptyBoxNumber--;
							break;
						}
						boxes.get(emptyBoxes.get(emptyBoxNumber)).setText(""); // Takes number out if it could not be switched with anything
					}
				}
			}
			
			/* If the board is still not complete, this section goes through each 
			 * individual empty box and checks if it can input any remaining number 
			 * that is missing (this was already done before but now done again just in case */
			if (!complete(boxes, gridNumber)) {
				for (int emptyBoxNumber = 0; emptyBoxNumber < emptyBoxes.size(); emptyBoxNumber++) {
					for (int missingNumberNum = 0; missingNumberNum < missing.size() ; missingNumberNum++) {
						if (input(boxes, missing.get(missingNumberNum), emptyBoxes.get(emptyBoxNumber))) {
							emptyBoxes.remove(emptyBoxNumber);
							emptyBoxNumber--;
							missing.remove(missingNumberNum);
							break;
						}
					}
				}
			}
			
			/* If the board is still not complete, this section goes through each 
			 * remaining empty box and checks what box is impeding it from placing
			 * a certain number inside, it then forces the number inside, then switches
			 * the number within the impeding box, but if it can't do that it tries with
			 * the other number. This case is decently rare because a specific situation
			 * has to occur in order for it to run. It is also very confusing. */
			if (!complete(boxes, gridNumber) && gridNumber != 8) {
				int undoOne = 0;
				int undoTwo = 0;
				int undoThree = 0;
				int undoFour = 0;
				try { // Attempts to do this but if an error occurs for any reason it gets sent down to the catch statement
					for (int missingNumberNum = 0; missingNumberNum < missing.size(); missingNumberNum++) {
						for (int emptyBoxNumber = 0; emptyBoxNumber < emptyBoxes.size(); emptyBoxNumber++) {
							boolean undo = true;
							ArrayList<Integer> impedingBoxes = impeding(boxes, missing.get(missingNumberNum), emptyBoxes.get(emptyBoxNumber));
							for (int impedingBoxNumber = 0; impedingBoxNumber < impedingBoxes.size(); impedingBoxNumber++) { 
								int gridMovement = 1; // initially sets the movement to 1 (switching numbers within a row)
								int initialGrid = findGrid(emptyBoxes.get(emptyBoxNumber));
								int impedingGrid = findGrid(impedingBoxes.get(impedingBoxNumber));
								// if the impeding grid is to the left of the initial grid, you must switch numbers within a column (+/- 9)
								if (initialGrid - impedingGrid == 1) gridMovement = 9;
								int start = 0;
								
								// If your switching within a row, start with the first number of that row
								if (gridMovement == 1) start = minimumOfGridRow(boxes, impedingBoxes.get(impedingBoxNumber));
								// If your switching within a column, start with the first number of that column
								else start = minimumOfGridCol(boxes, impedingBoxes.get(impedingBoxNumber));
								
								for (int boxesToSwitch = 0; boxesToSwitch < 3; boxesToSwitch++) {
									/* Returns true when it forces to switch a box with itself. It only returns false if it attempts to 
									 * switch itself with itself */
									if (forceSwitch(boxes, impedingBoxes.get(impedingBoxNumber), start + (boxesToSwitch*gridMovement))) {
										// Stores the moves into variables so they can be undone if needed
										undoOne = impedingBoxes.get(impedingBoxNumber);
										undoTwo = start + (boxesToSwitch*gridMovement);
										break;
									}
								}
								
								// Stores new number of the impeding box into a variable so it can be searched for
								String nextImpediment = boxes.get(impedingBoxes.get(impedingBoxNumber)).getText();
								boxes.get(impedingBoxes.get(impedingBoxNumber)).setText(""); // removed so it does not search itself
								
								// Finds the impeding box that is now impeding the previous impeding box
								ArrayList<Integer> doubleImpeding = impeding(boxes, nextImpediment, impedingBoxes.get(impedingBoxNumber));
								boxes.get(impedingBoxes.get(impedingBoxNumber)).setText(nextImpediment); // put back into place
								
								gridMovement = 1; // initially sets the movement to 1 (switching numbers within a row)
								initialGrid = findGrid(doubleImpeding.get(impedingBoxNumber));
								impedingGrid = findGrid(impedingBoxes.get(impedingBoxNumber));
								// if the impeding grid is to the left of the initial grid, you must switch numbers within a column (+/- 9)
								if (initialGrid - impedingGrid == 1) gridMovement = 9;
								start = 0;
								
								// If your switching within a row, start with the first number of that row
								if (gridMovement == 1) start = minimumOfGridRow(boxes, doubleImpeding.get(impedingBoxNumber));
								// If your switching within a column, start with the first number of that column
								else start = minimumOfGridCol(boxes, doubleImpeding.get(impedingBoxNumber));
								
								for (int boxesToSwitch = 0; boxesToSwitch < 3; boxesToSwitch++) {
									// Check to see if you can switch the impeding box with another box in its row/col
									if (switchBoxes(boxes, doubleImpeding.get(impedingBoxNumber), start + (boxesToSwitch*gridMovement))) {
										/* If you switched the boxes safely, check to see if you can input the missing number into the empty 
										 * box from the initial grid */
										if (input(boxes, missing.get(missingNumberNum), emptyBoxes.get(emptyBoxNumber))) {
											// If it's a success, you've fixed the grid and don't need to undo anything
											undo = false;
											break;
										}
									}
								}
								
								// If you still need to undo things, try one more thing before you do
								if (undo) {
									gridMovement = 1; // initially sets the movement to 1 (switching numbers within a row)
									initialGrid = findGrid(doubleImpeding.get(impedingBoxNumber));
									impedingGrid = findGrid(impedingBoxes.get(impedingBoxNumber));
									// if the impeding grid is to the left of the initial grid, you must switch numbers within a column (+/- 9)
									if (initialGrid - impedingGrid == 1) gridMovement = 9;
									start = 0;
									
									// If your switching within a row, start with the first number of that row
									if (gridMovement == 1) start = minimumOfGridRow(boxes, doubleImpeding.get(impedingBoxNumber));
									// If your switching within a column, start with the first number of that column
									else start = minimumOfGridCol(boxes, doubleImpeding.get(impedingBoxNumber));
									
									for (int boxesToSwitch = 0; boxesToSwitch < 3; boxesToSwitch++) {
										// Force the switch of two boxes within row/col
										if (forceSwitch(boxes, doubleImpeding.get(impedingBoxNumber), start + (boxesToSwitch*gridMovement))) {
											// Store move into a variable in case it needs to be undone
											undoThree = impedingBoxes.get(impedingBoxNumber);
											undoFour = start + (boxesToSwitch*gridMovement);
											break;
										}
									}
									
									// Stores new number of the impeding box into a variable so it can be searched for
									nextImpediment = boxes.get(doubleImpeding.get(impedingBoxNumber)).getText();
									boxes.get(doubleImpeding.get(impedingBoxNumber)).setText(""); // removed so it does not search itself
									
									// Finds the impeding box that is now impeding the previous impeding box
									ArrayList<Integer> tripleImpeding = impeding(boxes, nextImpediment, doubleImpeding.get(impedingBoxNumber));
									boxes.get(doubleImpeding.get(impedingBoxNumber)).setText(nextImpediment); // put back into place
									
									gridMovement = 1; // initially sets the movement to 1 (switching numbers within a row)
									initialGrid = findGrid(doubleImpeding.get(impedingBoxNumber));
									impedingGrid = findGrid(tripleImpeding.get(impedingBoxNumber));
									// if the impeding grid is to the left of the initial grid, you must switch numbers within a column (+/- 9)
									if (initialGrid - impedingGrid == 1) gridMovement = 9;
									start = 0;
									
									// If your switching within a row, start with the first number of that row
									if (gridMovement == 1) start = minimumOfGridRow(boxes, tripleImpeding.get(impedingBoxNumber));
									// If your switching within a column, start with the first number of that column
									else start = minimumOfGridCol(boxes, tripleImpeding.get(impedingBoxNumber));
									
									for (int x = 0; x < 3; x++) {
										// Check to see if you can switch the impeding box with another box in its row/col
										if (switchBoxes(boxes, tripleImpeding.get(impedingBoxNumber), start + (x*gridMovement))) {
											/* If you switched the boxes safely, check to see if you can input the missing number into the empty 
											 * box from the initial grid */
											if (input(boxes, missing.get(missingNumberNum), emptyBoxes.get(emptyBoxNumber))) {
												// If it's a success, you've fixed the grid and don't need to undo anything
												undo = false;
												break;
											}
										}
									}
								}
								
								// If you still need to undo stuff, undo it all
								if (undo) {
									forceSwitch(boxes, undoTwo, undoOne);
									forceSwitch(boxes, undoFour, undoThree);
								}
							}
						}
					}
				} catch (Exception e) {
					// Undoes any forced switches if error occurs
					forceSwitch(boxes, undoTwo, undoOne);
					forceSwitch(boxes, undoFour, undoThree);
				}
			}
			
			/* If the grid is still not complete and you're filling out the final grid, 
			 * this section will constantly switch impeding boxes within a row in order
			 * to find the perfect solution */
			if (!complete(boxes, gridNumber) && gridNumber == 8) {
				ArrayList<Integer> impeding = new ArrayList<Integer>();
				
				// Goes through each empty box and checks to see what is impeding each number
				for (int emptyBoxNumber = 0; emptyBoxNumber < emptyBoxes.size(); emptyBoxNumber++) {
					for (int missingNumberNum = 0; missingNumberNum < missing.size(); missingNumberNum++) {
						// If the impeding box of the missing number is in its column and not its row
						if (checkColumn(boxes, missing.get(missingNumberNum), emptyBoxes.get(emptyBoxNumber)) && !checkRow(boxes, missing.get(missingNumberNum), emptyBoxes.get(emptyBoxNumber))) {
							// Add the impeding box to a list
							impeding.addAll(impeding(boxes, missing.get(missingNumberNum), emptyBoxes.get(emptyBoxNumber)));
							// Force in the missing number from that box
							boxes.get(emptyBoxes.get(emptyBoxNumber)).setText(missing.get(missingNumberNum));
						}
					}
				}
				
				ArrayList<Integer> columnNumber = new ArrayList<Integer>();
				
				// Gathers the column number from the impeding boxes (0, 1, or 2)
				for (int impedingBoxNumber = 0; impedingBoxNumber < impeding.size(); impedingBoxNumber++) {
					columnNumber.add(columnNum(boxes, impeding.get(impedingBoxNumber)));
				}
				
				// Removes any duplicate column numbers
				for (int leftSideOfArray = 0; leftSideOfArray < columnNumber.size(); leftSideOfArray++) {
					for (int rightSideOfArray = columnNumber.size()-1; rightSideOfArray > leftSideOfArray; rightSideOfArray--) {
						if (columnNumber.get(leftSideOfArray) == columnNumber.get(rightSideOfArray)) {
							columnNumber.remove(rightSideOfArray);
						}
					}
				}

				boolean complete = false;
				int tries = 0;
				while (!complete) {
					// How the grid is manipulated
					int difference = columnNumber.get(columnNumber.size()-1) - columnNumber.get(0);
					// If you can't safely switch the impeding box with the other box
					if (!switchBoxes(boxes, impeding.get(0), impeding.get(0)+difference)) {
						// Force the switch
						forceSwitch(boxes, impeding.get(0), impeding.get(0)+difference);
						
						// Store the boxNumber and contents within variables and remove it so it doesn't search itself
						String text = boxes.get(impeding.get(0)).getText();
						int num = impeding.get(0);
						boxes.get(impeding.get(0)).setText("");
						
						// Check what is now impeding the box after forcing a switch
						impeding = impeding(boxes, text, impeding.get(0));
						
						// Replace the removed box
						boxes.get(num).setText(text);
					} else {
						// Board is complete if you successfully switched the boxes
						complete = true;
					}
					
					// Board is complete if there are no more impeding boxes left
					if (impeding.size() == 0) complete = true;
					tries++;
					// If you tried 100 times you're probably in an infinite loop, reset and try again (extremely rare)
					if (tries == 100) {
						reset(boxes, frame, difficulty, app);
						break;
					}
				}			
			}
			
			/* After the last grid is filled, it checks to see whether or not 
			 * the solution is actually valid, and will manipulate the grid
			 * in order to attempt a fix */
			if (gridNumber == 8) {
				for (int loopThrough = 0; loopThrough < 3; loopThrough++) { // does this 3 times in order to ensure results
					// if the solution is not valid
					if (!solution(boxes)) {
						ArrayList<Integer> impeding = new ArrayList<Integer>();
						
						boolean breakOut = false;
						// Go down each column and find out whether or not there is an impeding box, then start from there
						for (int columnNumber = 0; columnNumber < 3; columnNumber++) {
							for (int goDownColumn = 6+columnNumber; goDownColumn < 81; goDownColumn += 9) {
								// Store box contents into a variable so it doesn't search itself
								String temp = boxes.get(goDownColumn).getText();
								boxes.get(goDownColumn).setText("");
								// If the column contains another box with those same contents
								if (checkColumn(boxes, temp, goDownColumn)) {
									// Find out which box is impeding then breakOut of the loop
									impeding = impeding(boxes, temp, goDownColumn);
									breakOut = true;
								}
								// Reset box
								boxes.get(goDownColumn).setText(temp);
								if (breakOut) break;
							}
							
							if (breakOut) break;
						}	
						
						boolean complete = false;
						int tries = 0;
						while (!complete) {
							// How the grid is manipulated
							int difference = 1;
							// If you can't safely switch the impeding box with the other box
							if (!switchBoxes(boxes, impeding.get(0), impeding.get(0)+difference)) {
								// Force the switch
								forceSwitch(boxes, impeding.get(0), impeding.get(0)+difference);
								
								// Store the boxNumber and contents within variables and remove it so it doesn't search itself
								String text = boxes.get(impeding.get(0)).getText();
								int num = impeding.get(0);
								boxes.get(impeding.get(0)).setText("");
								
								// Check what is now impeding the box after forcing a switch
								impeding = impeding(boxes, text, impeding.get(0));
								
								// Replace the removed box
								boxes.get(num).setText(text);
							} else {
								// Board is complete if you successfully switched the boxes
								complete = true;
							}
							
							// Board is complete if there are no more impeding boxes left
							if (impeding.size() == 0) complete = true;
							tries++;
							// If you tried 100 times you're probably in an infinite loop, reset and try again (extremely rare)
							if (tries == 100) {
								reset(boxes, frame, difficulty, app);
								break;
							}
						}	
					}
				}
				
				/* If for any reason the solution is still not valid, 
				 * reset the board and try again. It's extremely rare to see
				 * this run too */
				if (!solution(boxes)) {
					reset(boxes, frame, difficulty, app);
				}
			}
		}
	}
}