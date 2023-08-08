package checkers;
import main.components.*;
import java.util.*;

public interface AreaChecker extends ColumnChecker {
	
	default boolean solution (ArrayList<Box> boxes) {
		for (int testNumber = 0; testNumber < 9; testNumber++) {
			int start = 0 + (9*testNumber);
			int total = 0;
			for (int i = 0; i < 9; i++) {
				total += Integer.parseInt(boxes.get(start+i).getText());
			}
			
			if (total != 45) return false;
		}
		
		for (int testNumber = 0; testNumber < 9; testNumber++) {
			int start = 0 + testNumber;
			int total = 0;
			for (int i = 0; i < 9; i++) {
				total += Integer.parseInt(boxes.get(start+(9*i)).getText());
			}
			
			if (total != 45) return false;
		}
		return true;
	}
	
	default boolean checkArea(ArrayList<Box> boxes, String find, int boxNumber) {
		return !checkColumn(boxes, find, boxNumber) && !checkRow(boxes, find, boxNumber) && !checkGrid(boxes, find, boxNumber);
	}

  default ArrayList<Integer> impeding(ArrayList<Box> boxes, String find, int boxNumber) {
    ArrayList<Integer> indexes = new ArrayList<Integer>();
    ArrayList<Integer> box = getArea(boxNumber);
    for (int i = 0; i < box.size(); i++) {
      if (boxes.get(box.get(i)).getText().equals(find)) {
        indexes.add(box.get(i));
      }
    }
    return indexes;
  }
  
  default int columnNum(ArrayList<Box> boxes, int boxNumber) {
	  int minimum = minimumOfGridRow(boxes, boxNumber);
	  if (minimum + 1 == boxNumber) return 1;
	  if (minimum + 2 == boxNumber) return 2;
	  return 0;
  }

  default ArrayList<Integer> getArea(int boxNumber) {
		int[] boxesInColumn = getColumn(boxNumber);
		int[] boxesInRow = getRow(boxNumber);
		int[] boxesInGrid = getGrid(boxNumber);
		ArrayList<Integer> boxesInArray = new ArrayList<Integer>();
		// Add all boxes from column, row, and grid
		for (int bigArrayIndex = 0; bigArrayIndex < 27; bigArrayIndex++) {
			for (int smallArrayIndex = 0; smallArrayIndex < 9; smallArrayIndex++) {
				if (bigArrayIndex < 9) boxesInArray.add(boxesInColumn[smallArrayIndex]);
				if (bigArrayIndex > 8 && bigArrayIndex < 18) boxesInArray.add(boxesInRow[smallArrayIndex]);
				if (bigArrayIndex > 17) boxesInArray.add(boxesInGrid[smallArrayIndex]);
			}
		}
		// Remove duplicates
		for (int leftSideOfArrayList = 0; leftSideOfArrayList < boxesInArray.size(); leftSideOfArrayList++) {
			for (int rightSideOfArrayList = boxesInArray.size()-1; rightSideOfArrayList > leftSideOfArrayList; rightSideOfArrayList--) {
				if (boxesInArray.get(leftSideOfArrayList) == boxesInArray.get(rightSideOfArrayList)) boxesInArray.remove(rightSideOfArrayList);
			}
		}
		return boxesInArray;
	}
	
	default int[] getColumn(int boxNumber) {
		int[] boxesInArea = new int[9];
		
		while (boxNumber - 9 >= 0) {
			boxNumber -= 9;
		}
	
		for (int index = 0; index < 9; index++) {
			boxesInArea[index] = boxNumber;
			boxNumber += 9;
		}
		
		return boxesInArea;
	}
	
	default int[] getRow(int boxNumber) {
		int[] boxesInArea = new int[9];
		int min = findMaxNum(boxNumber) - 8;
		
		while (boxNumber - 1 >= min) {
			boxNumber--;
		}
	
		for (int index = 0; index < 9; index++) {
			boxesInArea[index] = boxNumber;
			boxNumber++;
		}
		
		return boxesInArea;
	}
	
	default int[] getGrid(int boxNumber) {
		int[] boxesInArea = collectGrid(findGrid(boxNumber), 0);
		return boxesInArea;
	}	
}