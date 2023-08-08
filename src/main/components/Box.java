package main.components;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import main.*;

@SuppressWarnings("serial")
public class Box extends JPanel implements PuzzleMaker, FocusListener, MouseListener, KeyListener {

	private ArrayList<String> possibilities;
	private JLabel text;
	private boolean isEditable = true;
	private boolean isValidating = false;
	private ArrayList<Box> boxes;
	private int boxNumber;
	private JFrame frame;
	private Application app;
	private JMenuBar menuBar;
	private int difficulty = 1;
	
	public Box(ArrayList<Box> boxes, int boxNumber, JFrame frame, JMenuBar menuBar, Application app) {
		construct(boxes, boxNumber, frame, menuBar, app);
		
	}

	public Box(String text, ArrayList<Box> boxes, int boxNumber, JFrame frame, JMenuBar menuBar, Application app) {
		construct(boxes, boxNumber, frame, menuBar, app);
		setText(text);
	}
	
	private void construct(ArrayList<Box> boxes, int boxNumber, JFrame frame, JMenuBar menuBar, Application app) {
		this.boxNumber = boxNumber;
		this.boxes = boxes;
		this.frame = frame;
		this.menuBar = menuBar;
		this.app = app;
		
		text = new JLabel();
		text.setFont(new Font("Arial", Font.BOLD, 42));
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setVerticalAlignment(SwingConstants.CENTER);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.addFocusListener(this);
		this.add(text);
		
		possibilities = new ArrayList<String>();
		for (int i = 1; i <= 9; i++) {
			possibilities.add(String.valueOf(i));
		}
	}
	
	public void refresh() {
		frame.revalidate();
		frame.repaint();
	}
	
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public int getDifficulty() {
		return this.difficulty;
	}
	
	public String getText() {
		return text.getText();
	}
	
	public void setText(String num) {
		this.text.setText(num);
	}
	
	public void setTextColor(Color color) {
		this.text.setForeground(color);
	}
	
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	
	public boolean isEditable() {
		return isEditable;
	}
	
	public void setValidating(boolean isValidating) {
		this.isValidating = isValidating;
	}
	
	public void resetPossibilities() {
		possibilities.removeAll(possibilities);
		for (int i = 1; i <= 9; i++) {
			possibilities.add(String.valueOf(i));
		}
	}
	
	public ArrayList<String> getPossibilities() {
		return possibilities;
	}
	
	public int getPossibilitySize() {
		return possibilities.size();
	}
	
	public void decolour() {
		if (isEditable && !isValidating) {
			this.setBackground(new Color(238, 238, 238));
			text.setForeground(new Color(51, 51, 51));
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (isEditable && !isValidating) {
			this.setBackground(new Color(0, 125, 255));
			text.setForeground(new Color(255, 255, 255));
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		decolour();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && isEditable) {
			this.grabFocus();
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (isEditable) {
			char input = e.getKeyChar();
			if (input > 48 && input < 58) text.setText(String.valueOf(input));
			if (input == 8 || input == 127) text.setText("");
		}
		
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			hide(this.menuBar);
		} else if (e.getKeyCode() == KeyEvent.VK_R) {
			decolour();
			reset(boxes, frame, difficulty, app);
		} else if (e.getKeyCode() == KeyEvent.VK_E) {
			exit(frame);
		} else if (e.getKeyCode() == KeyEvent.VK_C) {
			validate(boxes);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			int addition = 0;
			do {
				addition -= 9;
				if (boxNumber + addition < topOfColumn(boxNumber)) addition = bottomOfColumn(boxNumber) - boxNumber;
			} while (!boxes.get(boxNumber + addition).isEditable);
			boxes.get(boxNumber + addition).grabFocus();
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			int addition = 0;
			do {
				addition += 9;
				if (boxNumber + addition > bottomOfColumn(boxNumber)) addition = topOfColumn(boxNumber) - boxNumber;
			} while (!boxes.get(boxNumber + addition).isEditable);
			boxes.get(boxNumber + addition).grabFocus();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			int addition = 0;
			do {
				addition += 1;
				if (boxNumber + addition > findMaxNum(boxNumber)) addition = findMinNum(boxNumber) - boxNumber;
			} while (!boxes.get(boxNumber + addition).isEditable);
			boxes.get(boxNumber + addition).grabFocus();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			int addition = 0;
			do {
				addition -= 1;
				if (boxNumber + addition < findMinNum(boxNumber)) addition = findMaxNum(boxNumber) - boxNumber;
			} while (!boxes.get(boxNumber + addition).isEditable);
			boxes.get(boxNumber + addition).grabFocus();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}
}