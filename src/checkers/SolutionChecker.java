package checkers;
import main.components.Box;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class SolutionChecker extends JFrame implements ActionListener, WindowListener, KeyListener {
	
	private ArrayList<Box> boxes;
	private String[] board;
	private boolean win;
	
	// Text area is the top of the window
	private JPanel textArea;
	private JLabel info;
	
	// Button area is the bottom of the window
	private JPanel buttonArea;
	private JButton okay;
	
	public SolutionChecker(ArrayList<Box> boxes, String[] board) {		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setTitle("Solution Validator");
		this.setPreferredSize(new Dimension(310, 300));
		this.addWindowListener(this);
		this.addKeyListener(this);
		this.boxes = boxes;
		this.board = board;
		win = true;
		
		info = new JLabel("<html><body><center>"
				+ "This is the solution validator menu!<br><br>"
				+ "You may move this window to the side<br>"
				+ "in order to review the game board.<br><br>"
				+ "Once you close this window you will<br>"
				+ "no longer be able to see the validity<br>"
				+ "of your solution so far.<br><br>"
				+ "Take your time, you got this!<br>"
				+ "I hope you're having fun!"
				+ "</center></body></html>");
		info.setFont(new Font("Arial", Font.BOLD, 15));
		
		// Area for text
		textArea = new JPanel();
		textArea.setBackground(Color.GREEN);
		textArea.setOpaque(false);
		textArea.setBounds(0, 0, 300, 225);
		textArea.add(info);
		
		// Okay Button
		okay = new JButton("Done! (Space)");
		okay.setFocusable(false);
		okay.addActionListener(this);
		
		// Area for Button
		buttonArea = new JPanel();
		buttonArea.setBackground(Color.RED);
		buttonArea.setOpaque(false);
		buttonArea.setBounds(0, 225, 300, 75);
		buttonArea.add(okay);
		
		for (int boxNumber = 0; boxNumber < board.length; boxNumber++) {
			if (boxes.get(boxNumber).isEditable()) {
				boxes.get(boxNumber).decolour();
				boxes.get(boxNumber).setValidating(true);
				if (boxes.get(boxNumber).getText().equals(board[boxNumber])) {
					boxes.get(boxNumber).setBackground(Color.GREEN);
				} else {
					if (!boxes.get(boxNumber).getText().equals("")) boxes.get(boxNumber).setBackground(Color.RED);
					win = false;
				}
				boxes.get(boxNumber).refresh();
			}
		}
		
		if (win) {
			info.setText("<html><body><center>"
				+ "Congratulations! You won!<br><br>"
				+ "You may move this window to the side<br>"
				+ "in order to review the game board.<br><br>"
				+ "Once you close this window you will<br>"
				+ "no longer be able to see the validity<br>"
				+ "of your solution.<br><br>"
				+ "To play again, close this window and<br>"
				+ "press 'R' or reset in the game menu!"
				+ "</center></body></html>");
		}
		
		this.add(buttonArea);
		this.add(textArea);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public void exit() {
		for (int boxNumber = 0; boxNumber < board.length; boxNumber++) {
			if (boxes.get(boxNumber).isEditable()) {
				boxes.get(boxNumber).setValidating(false);
				boxes.get(boxNumber).decolour();
			}
		}
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		exit();
		this.setVisible(false);		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			exit();
			this.setVisible(false);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		exit();
		this.setVisible(false);
	}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}
}