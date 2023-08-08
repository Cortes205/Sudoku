package checkers;
import main.components.Box;
import java.util.*;

public interface GridChecker {
	
	default boolean checkGrid(ArrayList<Box> boxes, String find, int boxNumber) {
		int gridNumber = findGrid(boxNumber);
		int[] indexes = collectGrid(gridNumber, 0);
		for (int i = 0; i < indexes.length; i++) {
			if (boxes.get(indexes[i]).getText().equals(find)) return true;
		}
		return false;
	}
	
	default int findMaxNum(int boxNumber) {
		if ((boxNumber+1) % 9 == 0) return boxNumber;
		return findMaxNum(boxNumber+1);
	}
	
	default int findMinNum(int boxNumber) {
		return findMaxNum(boxNumber) - 8;
	}
	
	default int minimumOfGridRow(ArrayList<Box> boxes, int boxNumber) {
		if (findGrid(boxNumber) - findGrid(boxNumber-1) != 0) return boxNumber;
		return minimumOfGridRow(boxes, boxNumber-1);
	}

	default int minimumOfGridCol(ArrayList<Box> boxes, int boxNumber) {
		if (findGrid(boxNumber) - findGrid(boxNumber-9) != 0) return boxNumber;
		return minimumOfGridCol(boxes, boxNumber-9);
	}
	
	default int findGridRow(int boxNumber) {
		if ((boxNumber+9) % 27 == 0) return ((boxNumber+9) / 27) - 1;
		return findGridRow(boxNumber+9);
	}
	
	default int findGrid(int boxNumber) {
		int minimumNumberInRow = findMaxNum(boxNumber) - 8;
		if ((boxNumber+1) % 3 == 0) return ((((boxNumber+1) - minimumNumberInRow) / 3) - 1) + (3 * findGridRow(minimumNumberInRow));
		return findGrid(boxNumber+1);
	}
	
	default int[] collectGrid(int gridNumber, int multiplier) {
		 // Subtracting a grid number by 3 turns it into the grid above it, then the multiplier represents how many times you have to move down in order to make it the goal grid
		if (gridNumber - 3 >= 0) return collectGrid(gridNumber-3, multiplier+1);
		/* Transformations to be applied to the first grid in order to make into a grid of your choosing 
		 * 3 is how much it takes to move exactly into the grid to the right, while 27 is how much
		 * it takes to move exactly into the grid below. */
		int addition = (3 * gridNumber) + (27 * multiplier);
		int[] indexes = {0, 1, 2, 9, 10, 11, 18, 19, 20}; // Indexes of the boxes within the first grid
		for (int i = 0; i < indexes.length; i++) {
			/* Apply transformations. for example: 
			 * collectGrid(4, 0) -> collectGrid(1, 1) -> returns {30, 31, 32, 39, 40, 41, 48, 49, 50} which are the indexes of the boxes in the fifth grid (grid index 4)
			 * or collectGrid(8, 0) -> collectGrid(5, 1) -> collectGrid(2, 2) -> returns {60, 61, 62, 69, 70, 71, 78, 79, 80} */
			indexes[i] += addition;
		}
		return indexes;
	}
}