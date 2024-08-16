package simulator;

/*
 * PROGRESS
 * 
 * Shift to hide GUI
 * 
 * 
 */

/*
 * PLANNED
 * 
 * Customize leaderboard
 * Spiral galaxy shape
 * 
 */

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends Canvas {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3731319023792086485L;

	public static void main(String args[]) {
		new Window(1000,1000,"Galaxy Simulator", new Game());
	}
	
	
	public Window(int width, int height, String title, Game game) {

		JFrame frame = new JFrame(title);
		
		frame.setSize(new Dimension(width,height));
		frame.setPreferredSize(new Dimension(width,height));
		frame.setMaximumSize(new Dimension(width,height));
		frame.setMinimumSize(new Dimension(width,height));
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setFocusable(true);
		frame.requestFocus();

		frame.add(game);
		frame.pack();

		game.start();
		
	}
	

}
