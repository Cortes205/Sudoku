package main.help.menus;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Controls extends JFrame implements ActionListener, WindowListener, KeyListener{

	// Text area is the top of the window
	private JPanel textArea;
	private JLabel info;
		
	// Button area is the bottom of the window
	private JPanel buttonArea;
	private JButton okay;
	
	public Controls() {
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setTitle("Solution Validator");
		this.setPreferredSize(new Dimension(310, 300));
		this.addWindowListener(this);
		this.addKeyListener(this);
		
		info = new JLabel("<html><body><center>"
				+ "Controls<br><br>"
				+ "WASD/Arrow Keys/Mouse1 - Select boxes<br><br>"
				+ "CTRL - Hide/show menu bar<br><br>"
				+ "C - Check your solution<br><br>"
				+ "R - Reset the game<br><br>"
				+ "E - Exit the game<br><br>"
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
