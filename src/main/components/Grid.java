package main.components;
import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Grid extends JPanel {
	
	public Grid() {
		this.setLayout(new GridLayout(3,3));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		this.setOpaque(false);
	}

}