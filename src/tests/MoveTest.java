package tests;

import java.awt.Graphics;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import components.Test;

public class MoveTest extends JPanel {
	private final int TIMESHIFT = 20;
	
	public MoveTest() {
		setFocusable(true);
		setDoubleBuffered(true);
	}
	
	class GameThread implements Runnable {
		MoveTest b;
		
		public GameThread(MoveTest b) {
			this.b = b;
		}
		@Override
		public void run() {
			b.game();
		}
	}
	
	private void iterate() {
		repaint();
	}
	
	private int width = 10;
	private int height = 10;
	private double speed = 2;
	private int x = 200;
	private int y = 200;
	private double xOffset = 0;
	private double yOffset = 0;
	private int angle = 180;
	
	@Override
	public void paint(Graphics g) {
		
	}
	
	public void game() {
		long currentTime = 0;
		long previousTime = System.nanoTime();
		double timeDif = 0.0d;
		while(true) {
			currentTime = System.nanoTime();
			timeDif += (double)(currentTime - previousTime) / 1000000;
			if(timeDif >= TIMESHIFT) {
				iterate();
				timeDif = 0;
			}
			previousTime = currentTime;
		}
	}
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame test = new JFrame();
				test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				test.setSize(500, 500);
				test.setResizable(false);
				test.setLocationRelativeTo(null);
				test.setAlwaysOnTop(true);
				test.setVisible(true);
				MoveTest testPanel = new MoveTest();
				test.add(testPanel);
				
				ExecutorService exec = Executors.newCachedThreadPool();
				exec.execute(testPanel.new GameThread(testPanel));
				exec.shutdown();
			}
		});
	}
}
