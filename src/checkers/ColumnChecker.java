package checkers;
import main.components.*;
import java.util.*;

public interface ColumnChecker extends RowChecker {
	
	default boolean checkColumn(ArrayList<Box> boxes, String find, int boxNumber) {
		return checkColumnUp(boxes, find, boxNumber) || checkColumnDown(boxes, find, boxNumber);
	}
	
	default boolean checkColumnUp(ArrayList<Box> boxes, String find, int boxNumber) {
		if (boxNumber < 0) return false;
		if (boxes.get(boxNumber).getText().equals(find)) return true;
		return checkColumnUp(boxes, find, boxNumber-9);
	}
	
	default boolean checkColumnDown(ArrayList<Box> boxes, String find, int boxNumber) {
		if (boxNumber > 80) return false;
		if (boxes.get(boxNumber).getText().equals(find)) return true;
		return checkColumnDown(boxes, find, boxNumber+9);
	}
	
	default int bottomOfColumn(int boxNumber) {
		if (boxNumber + 9 > 80) return boxNumber;
		return bottomOfColumn(boxNumber+9);
	}
	
	default int topOfColumn(int boxNumber) {
		if (boxNumber - 9 < 0) return boxNumber;
		return topOfColumn(boxNumber - 9);
	}
}