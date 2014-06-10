package scenes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;

import components.Board;

public class Menu implements Scene {
	Board b;
	BufferStrategy bs;
	Graphics2D g;
	
	private int qubeSize = 40;
	
	private final String GAME = "Game";
	private final String SETTINGS = "Settings";
	private final String EXIT = "Exit";
	
	private int width;
	private int height;
	
	public Menu(Board b, BufferStrategy bs, int width, int height) {
		this.bs = bs;
		this.b = b;
		g = (Graphics2D)this.bs.getDrawGraphics();
		this.width = width;
		this.height = height;
		this.b.setKeyAdapter(null);
		this.b.setMouseAdapter(new MM());
		this.b.setMouseMotionListener(null);
		System.out.println("Menu Width = " + width + " height = " + height);
	}
	
	private class MM extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			int mX = e.getX();
			int mY = e.getY();
			
			int x = width / 2 - 5 * qubeSize;
			int y = height / 2 - qubeSize;
			
			if(mX >= x && mX <= x + qubeSize * 10 && mY >= y && mY <= y + qubeSize) {
				b.setScene(new GameScene(bs, width, height, b));
				System.out.println("GAME");
				return;
			}
			if(mX >= x && mX <= x + qubeSize * 10 && mY >= y + 2 * qubeSize && mY <= y + 2 * qubeSize + qubeSize) {
				System.out.println("SETTINGS");
			}
			if(mX >= x && mX <= x + qubeSize * 10 && mY >= y + 4 * qubeSize && mY <= y + 4 * qubeSize + qubeSize) {
				b.stop();
				return;
			}
		}
	}
	
	private void draw() {
		g = (Graphics2D)bs.getDrawGraphics();
		g.clearRect(0, 0, width, height);
		g.setBackground(Color.gray);
		int x = width / 2 - 5 * qubeSize;
		int y = height / 2 - qubeSize;
		
		g.drawRect(x, y, qubeSize * 10, qubeSize);
		g.drawRect(x, (y + 2 * qubeSize), qubeSize * 10, qubeSize);
		g.drawRect(x, (y + 4 * qubeSize), qubeSize * 10, qubeSize);
		
		g.drawString(GAME, x + 4 * qubeSize, y + qubeSize / 2);
		g.drawString(SETTINGS, x + 4 * qubeSize, y + 2 * qubeSize + qubeSize / 2);
		g.drawString(EXIT, x + 4 * qubeSize, y + 4 * qubeSize + qubeSize / 2);
		g.dispose();
		bs.show();
	}

	@Override
	public void update(int timeShift) {
		
		
	}

	@Override
	public void redraw(float interpolation) {
		draw();
	}
}
