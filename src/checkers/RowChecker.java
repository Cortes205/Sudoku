package checkers;
import main.components.*;
import java.util.*;

public interface RowChecker extends GridChecker {
	
	default boolean checkRow(ArrayList<Box> boxes, String find, int boxNumber) {
		return checkRowLeft(boxes, find, boxNumber) || checkRowRight(boxes, find, boxNumber);
	}
	
	default boolean checkRowLeft(ArrayList<Box> boxes, String find, int boxNumber) {
		int minimumNumberInRow = findMaxNum(boxNumber) - 8;
		if (boxNumber == minimumNumberInRow) if (boxes.get(boxNumber).getText().equals(find)) return true;
		if (boxNumber <= minimumNumberInRow) return false;
		if (boxes.get(boxNumber).getText().equals(find)) return true;
		return checkRowLeft(boxes, find, boxNumber-1);
	}
	
	default boolean checkRowRight(ArrayList<Box> boxes, String find, int boxNumber) {
		int maximumNumberInRow = findMaxNum(boxNumber);
		if (boxNumber == maximumNumberInRow) if (boxes.get(boxNumber).getText().equals(find)) return true;
		if (boxNumber >= maximumNumberInRow ) return false;
		if (boxes.get(boxNumber).getText().equals(find)) return true;
		return checkRowRight(boxes, find, boxNumber+1);
	}
}