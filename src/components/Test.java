package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import utils.Animator;
import utils.Position;
import utils.SpriteSheet;

public class Test extends JPanel {
	private static final long serialVersionUID = -1231267545949988128L;

	private final int TIMESHIFT = 20;
	
	private BufferedImage image;
	private BufferedImage image2;
	
	private float angle = 0;
	Animator a;
	Animator a2;
	AffineTransform transform = new AffineTransform();	
	List<Position> bullets = new ArrayList<>();
	List<Integer> delays = new ArrayList<>();
	
	public Test() {
		setFocusable(true);
		setDoubleBuffered(true);
		try {
			image = ImageIO.read(new File("images//bullet.png"));
			image2 = ImageIO.read(new File("images//towers//ordinary.png"));
			a = new Animator(new SpriteSheet(image, 50, 3));
			a2 = new Animator(new SpriteSheet(image2, 50, 50));
			a2.setTicks(10000);
			a2.setTimeShift(10);
		} catch(IOException e) {}
		
		for(int i = 0; i < 1000; i++) {
			bullets.add(new Position(0, 0));
			delays.add(i * 100);
		}
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
	
	private void iterate() {
		repaint();
	}
	
	int x1 = 100;
	int y1 = 100;
	int x2 = 60;
	int y2 = 50;
	
	double startX = 80;
	double startY = 80;
	
	int shiftX = 0;
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
//		angle = (angle + 1) % 360;
//		BufferedImage image = a2.getImage();
//		BufferedImage image2 = a.getImage();
//		
//		AffineTransform transform = new AffineTransform();
//		transform.translate(100,100);
//		transform.rotate(Math.toRadians(angle), image.getWidth()/2, image.getHeight()/2);
//		Point2D.Double point = (Double) transform.transform(new Point2D.Double(x1,0), null);
//		g2.drawLine((int)point.x, (int)point.y, (int)point.x, (int)point.y);
//		g2.drawImage(image, transform, this);
//		
//		transform = new AffineTransform();
//		transform.translate(point.x, point.y);
//		transform.rotate(Math.toRadians(angle), 0, 0);
//		g2.drawImage(image2, transform, this);
//		
//		g2.drawLine(0, 0, getWidth(), getHeight());
//		g2.drawLine(getWidth(), 0, 0, getHeight());
		
		//g2.rotate(Math.toRadians(11));
		//angle = 0;
		transform = new AffineTransform();
		transform.translate(getWidth()/2, getHeight()/2);
		transform.rotate(Math.toRadians(angle), a2.getImage().getWidth()/2, a2.getImage().getHeight()/2);
		Point2D.Double point = (Double) transform.transform(new Point2D.Double(a2.getImage().getWidth(), 0), null);
		g2.drawImage(a2.getImage(), transform, this);
		transform = new AffineTransform();
		transform.translate(point.x, point.y + a2.getImage().getHeight()/2 - image.getHeight()/2);
		transform.rotate(Math.toRadians(angle), 0, -a2.getImage().getHeight()/2 + image.getHeight()/2);
		transform.translate(shiftX, 0);
		
		for(int i = 0; i < bullets.size(); i++) {
			if(delays.get(i) <= 0) {
				transform.translate(bullets.get(i).X(), 0);
				g2.drawImage(image, transform, this);
				transform.translate(-bullets.get(i).X(), 0);
				bullets.get(i).setX(bullets.get(i).X() + 50);
			}
			else {
				delays.set(i, delays.get(i) - TIMESHIFT);
			}
		}
		
		Iterator<Position> positions = bullets.iterator();
		int index = 0;
		while(positions.hasNext()) {
			Position pos = positions.next();
			if(pos.X() > 100) {
				positions.remove();
				delays.remove(index);
			}
			index++;
		}
		
		if(shiftX > 150)
			shiftX = 0;
		x1 = 100;		
		angle += 0.2;
		//shiftX += 5;
	}

	class GameThread implements Runnable {
		Test b;
		
		public GameThread(Test b) {
			this.b = b;
		}
		@Override
		public void run() {
			b.game();
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ExecutorService exec = Executors.newCachedThreadPool();
				Test b = new Test();
				JFrame g = new JFrame();
				g.setTitle("Tower Defence");
				g.setSize(350, 300);
				//setResizable(false);
				g.setLocationRelativeTo(null);
				g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				g.setVisible(true);
				g.add(b);
				exec.execute(b.new GameThread(b));
				exec.shutdown();
			}
		});
	}
	
}
