package main.components;
import checkers.*;
import java.util.*;

public interface PossibilityRemover extends AreaChecker {
	
	default void removePossibilities(ArrayList<Box> boxes, String addition, int boxNumber) {
		ArrayList<Integer> boxesInArea = getArea(boxNumber);
		for (int index = 0; index < boxesInArea.size(); index++) {
			if (boxes.get(boxesInArea.get(index)).getPossibilities().contains(addition)) boxes.get(boxesInArea.get(index)).getPossibilities().remove(boxes.get(boxesInArea.get(index)).getPossibilities().indexOf(addition));
		}
	}
	
	default void resetAllPossibilities(ArrayList<Box> boxes, int gridNumber) {
		int[] boxNumbersInGrid = collectGrid(gridNumber, 0);
		for (int boxNumber = 0; boxNumber < boxNumbersInGrid.length; boxNumber++) {
			resetPossibilities(boxes, boxNumbersInGrid[boxNumber]);	
		}
	}
	
	default void resetPossibilities(ArrayList<Box> boxes, int boxNumber) {
		boxes.get(boxNumber).resetPossibilities();
		ArrayList<Integer> area = getArea(boxNumber);
		ArrayList<String> blacklisted = new ArrayList<String>();
		
		for (int boxesInArea = 0; boxesInArea < area.size(); boxesInArea++) {
			if (!boxes.get(area.get(boxesInArea)).getText().equals("")) blacklisted.add(boxes.get(area.get(boxesInArea)).getText());
		}

		for (int blacklistedNumber = 0; blacklistedNumber < blacklisted.size(); blacklistedNumber++) {
			for (int boxPossibility = 0; boxPossibility < boxes.get(boxNumber).getPossibilitySize(); boxPossibility++) {
				if (blacklisted.get(blacklistedNumber).equals(boxes.get(boxNumber).getPossibilities().get(boxPossibility))) boxes.get(boxNumber).getPossibilities().remove(boxPossibility);
			}
		}
	}
	
	default ArrayList<String> findMissingNumbers(ArrayList<Box> boxes, int[] grid) {
		ArrayList<String> missingNumbers = new ArrayList<String>();
		for (int intitialPossibilities = 1; intitialPossibilities <= 9; intitialPossibilities++) {
			missingNumbers.add(String.valueOf(intitialPossibilities));
		}

		for (int boxNumber = 0; boxNumber < grid.length; boxNumber++) {
			for (int initialPossibility = 0; initialPossibility < missingNumbers.size(); initialPossibility++) {
				if (boxes.get(grid[boxNumber]).getText().equals(missingNumbers.get(initialPossibility))) missingNumbers.remove(initialPossibility);
		    }
		}
		return missingNumbers;
	}
	
	default ArrayList<Integer> findEmptyBoxes(ArrayList<Box> boxes, int gridNumber) {
		int[] box = collectGrid(gridNumber, 0);
		ArrayList<Integer> emptyBoxes = new ArrayList<Integer>();
		for (int index = 0; index < box.length; index++) {
			if (boxes.get(box[index]).getText().equals("")) emptyBoxes.add(box[index]);
		}
		return emptyBoxes;
	  }
}