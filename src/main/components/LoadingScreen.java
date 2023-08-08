package main.components;
import main.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class LoadingScreen extends JFrame implements ActionListener, PuzzleMaker
{

	private JProgressBar loading;
	private Timer loader;
	private boolean reset;
	private ArrayList<Box> boxes;
	private JFrame frame;
	private Application app;
	private int difficulty;
	
	public LoadingScreen(boolean reset) 
	{		
		run(reset);
	}
	
	public LoadingScreen(boolean reset, ArrayList<Box> boxes, JFrame frame, int difficulty, Application app) 
	{		
		this.boxes = boxes;
		this.frame = frame;
		this.difficulty = difficulty;
		this.app = app;
		run(reset);
	}
	
	public void run(boolean reset) {
		this.reset = reset;
		
		loading = new JProgressBar();
		loading.setBounds(0, 0, 300, 75);
		loading.setValue(0);
		loading.setStringPainted(true);
		loading.setVisible(true);
		
		this.setTitle("Loading");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setPreferredSize(new Dimension(300, 75));
		this.add(loading);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
		
		loader = new Timer(25, this);
		loader.setRepeats(true);
		loader.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		loading.setValue(loading.getValue()+5);
		if (loading.getValue() == 100) {
			loader.stop();
			this.setVisible(false);
			if (!reset) new Application();
			if (reset) {
				clear(boxes);
				setPuzzle(boxes, frame, difficulty, app);
			}
		}
	}
}
