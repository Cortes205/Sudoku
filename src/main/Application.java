package main;
import main.components.*;
import main.components.Box;
import main.help.menus.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Application extends JFrame implements PuzzleMaker, WindowListener, ActionListener, KeyListener {
	
	private ArrayList<Grid> grids;
	private ArrayList<Box> boxes;
	private int hints = 10;
	
	private JMenuBar menuBar;
	
	private JMenu game;
	private JMenuItem submit;
	private JMenuItem giveUp;
	private JMenuItem reset;
	private JMenuItem exit;
	
	private JMenu difficulty;
	private int diff = 1;
	private JMenuItem easy;
	private JMenuItem medium;
	private JMenuItem hard;
	private JMenuItem impossible;
	
	private JMenu view;
	private JMenuItem hideBar;
	
	private JMenu help;
	private JMenuItem hint;
	private JMenuItem howToPlay;
	private JMenuItem controls;
	
	public Application() {
		
		controls = new JMenuItem("Controls");
		controls.addActionListener(this);
		
		howToPlay = new JMenuItem("How To Play");
		howToPlay.addActionListener(this);
		
		hint = new JMenuItem("Get A Hint");
		hint.addActionListener(this);
		
		help = new JMenu("Help");
		help.add(hint);
		help.add(howToPlay);
		help.add(controls);
		
		hideBar = new JMenuItem("Hide Menu Bar (CTRL)");
		hideBar.addActionListener(this);
		
		view = new JMenu("View");
		view.add(hideBar);
		
		easy = new JMenuItem("Easy");
		easy.addActionListener(this);
		
		medium = new JMenuItem("Medium ✓");
		medium.addActionListener(this);
		
		hard = new JMenuItem("Hard");
		hard.addActionListener(this);
		
		impossible = new JMenuItem("Impossible");
		impossible.addActionListener(this);
		
		difficulty = new JMenu("Difficulty");
		difficulty.add(easy);
		difficulty.add(medium);
		difficulty.add(hard);
		difficulty.add(impossible);
		
		submit = new JMenuItem("Check Solution (C)");
		submit.addActionListener(this);
		
		giveUp = new JMenuItem("Give Up");
		giveUp.addActionListener(this);
		
		reset = new JMenuItem("Reset (R)");
		reset.addActionListener(this);
		
		exit = new JMenuItem("Exit (E)");
		exit.addActionListener(this);
		
		game = new JMenu("Game");
		game.add(submit);
		game.add(giveUp);
		game.add(reset);
		game.add(exit);
		
		menuBar = new JMenuBar();
		menuBar.add(game);
		menuBar.add(difficulty);
		menuBar.add(view);
		menuBar.add(help);
		
		this.grids = new ArrayList<Grid>();
		this.boxes = new ArrayList<Box>();
		
		for (int gridNumber = 0; gridNumber < 9; gridNumber++) {
			grids.add(new Grid());
		}
		
		int numberOfGrid = 0;
		for (int boxNumber = 0; boxNumber < 81; boxNumber++) {
			boxes.add(new Box(boxes, boxNumber, this, this.menuBar, this));
			grids.get(numberOfGrid).add(boxes.get(boxNumber));
			
			if ((boxNumber+1) % 3 == 0) {
				numberOfGrid++;
				if (numberOfGrid > 2 && boxNumber < 26) numberOfGrid = 0;
				if (numberOfGrid > 5 && boxNumber < 53) numberOfGrid = 3;
				if (numberOfGrid > 8 && boxNumber < 80) numberOfGrid = 6;
			}
		}
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
		this.addKeyListener(this);
		this.setTitle("Sudoku");
		this.setLayout(new GridLayout(3, 3));
		this.setPreferredSize(new Dimension(600, 600));
		this.setJMenuBar(menuBar);
		
		for (int gridNum = 0; gridNum < 9; gridNum++) {
			this.add(grids.get(gridNum));
		}
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
		
		setPuzzle(boxes, this, diff, this);	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		if (e.getSource() == submit) {
			validate(boxes);
		} else if (e.getSource() == giveUp) {
			printSolution(boxes, this);
		} else if (e.getSource() == reset) {
			reset(boxes, this, diff, this);
		} else if (e.getSource() == exit) {
			exit(this);
		}
		
		if (e.getSource() == easy) {
			int temp = getDifficulty();
			setDifficulty(0);
			if (reset(boxes, this, diff, this)) {
				easy.setText("Easy ✓");
				medium.setText("Medium");
				hard.setText("Hard");
				impossible.setText("Impossible");
				hints = 15;
			} else {setDifficulty(temp);}
			
		} else if (e.getSource() == medium) {
			int temp = getDifficulty();
			setDifficulty(1);
			if (reset(boxes, this, diff, this)) {
				easy.setText("Easy");
				medium.setText("Medium ✓");
				hard.setText("Hard");
				impossible.setText("Impossible");
				hints = 10;
			} else {setDifficulty(temp);}
		} else if (e.getSource() == hard) {
			int temp = getDifficulty();
			setDifficulty(2);
			if (reset(boxes, this, diff, this)) {
				easy.setText("Easy");
				medium.setText("Medium");
				hard.setText("Hard ✓");
				impossible.setText("Impossible");
				hints = 5;
			} else {setDifficulty(temp);}
		} else if (e.getSource() == impossible) {
			int temp = getDifficulty();
			setDifficulty(3);
			if (reset(boxes, this, diff, this)) {
				easy.setText("Easy");
				medium.setText("Medium");
				hard.setText("Hard");
				impossible.setText("Impossible ✓");
				hints = 0;
			} else {setDifficulty(temp);}
		}
		
		if (e.getSource() == hideBar) {
			hide(this.menuBar);
		}
		
		if (e.getSource() == hint) {
			hints = giveHint(boxes, this, hints, diff);
		} else if (e.getSource() == howToPlay) {
			new HowToPlay();
		} else if (e.getSource() == controls) {
			new Controls();
		}		
	}
	
	public void setHints(int hints) {
		this.hints = hints;
	}
	
	public void setDifficulty(int difficulty) {
		this.diff = difficulty;
		for (int boxNumber = 0; boxNumber < 81; boxNumber++) {
			boxes.get(boxNumber).setDifficulty(difficulty);
		}
	}
	
	public int getDifficulty() {
		return this.diff;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			hide(this.menuBar);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_E) {
			exit(this);
		} else if (e.getKeyCode() == KeyEvent.VK_R) {
			reset(boxes, this, diff, this);
		} else if (e.getKeyCode() == KeyEvent.VK_C) {
			validate(boxes);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			int box = 72;
			while (!boxes.get(box).isEditable()) {
				box -= 9;
				if (box < topOfColumn(box)) box = bottomOfColumn(box) - box;
			} 
			boxes.get(box).grabFocus();
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			int box = 9;
			while (!boxes.get(box).isEditable()) {
				box += 9;
				if (box > bottomOfColumn(box)) box = topOfColumn(box) - box;
			} 
			boxes.get(box).grabFocus();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			int box = 1;
			while (!boxes.get(box).isEditable()) {
				box += 1;
				if (box > findMaxNum(box)) box = findMinNum(box) - box;
			} 
			boxes.get(box).grabFocus();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			int box = 8;
			while (!boxes.get(box).isEditable()) {
				box -= 1;
				if (box < findMinNum(box)) box = findMaxNum(box) - box;
			} 
			boxes.get(box).grabFocus();
		}
	}
	
	@Override
	public void windowClosing(WindowEvent arg0) {
		exit(this);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}
}