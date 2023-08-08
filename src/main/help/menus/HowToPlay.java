package main.help.menus;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class HowToPlay extends JFrame implements ActionListener, WindowListener, KeyListener{

	// Text area is the top of the window
	private JPanel textArea;
	private JLabel info;
		
	// Button area is the bottom of the window
	private JPanel buttonArea;
	private JButton okay;
	
	public HowToPlay() {
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setTitle("Solution Validator");
		this.setPreferredSize(new Dimension(310, 300));
		this.addWindowListener(this);
		this.addKeyListener(this);
		
		info = new JLabel("<html><body><center>"
				+ "Welcome to Sudoku<br><br>"
				+ "Fill each 3x3 Grid with the numbers 1-9<br><br>"
				+ "Please Note:<br>"
				+ "No number can be the same within a<br>"
				+ "row, column, or grid<br><br>"
				+ "The goal is to have a full board!<br><br>"
				+ "This is a very hard game!<br>"
				+ "Have fun!<br>"
				+ "</center></body></html>");
		info.setFont(new Font("Arial", Font.BOLD, 15));
		
		// Area for text
		textArea = new JPanel();
		textArea.setBackground(Color.GREEN);
		textArea.setOpaque(false);
		textArea.setBounds(0, 0, 300, 225);
		textArea.add(info);
		
		// Okay Button
		okay = new JButton("Okay! (Space)");
		okay.setFocusable(false);
		okay.addActionListener(this);
		
		// Area for Button
		buttonArea = new JPanel();
		buttonArea.setBackground(Color.RED);
		buttonArea.setOpaque(false);
		buttonArea.setBounds(0, 225, 300, 75);
		buttonArea.add(okay);
		
		this.add(buttonArea);
		this.add(textArea);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		this.setVisible(false);		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			this.setVisible(false);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
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
