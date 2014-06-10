package components;

import java.awt.Canvas;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import scenes.Scene;
import scenes.Menu;
import utils.PathFinder;



public class Board extends Canvas implements Runnable {
	
	private static final long serialVersionUID = -8759813409014074654L;
	
	public  final static int TIMESHIFT = 10;
	public static final int WIDTH = PathFinder.WIDTH;
	public static final int HEIGHT = PathFinder.HEIGHT;
	public static final int TOTALHEIGHT = HEIGHT;
	public static final int WAVESHIFT = 5000;
	public static int SQUAREWIDTH = 0;
	public static int SQUAREHEIGHT = 0;
	
	private KeyAdapter keyAdapter;
	private MouseAdapter mouseAdapter;
	private MouseMotionListener mmListener;
	
	Scene scene;
	
	JFrame frame;
	
	private int fps;
	private int currentFps;
	private boolean running;
	
	public Board() {
		frame = new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.pack();
		frame.validate();
		frame.setTitle("Game Tests");
		frame.setVisible(true);
		requestFocus();
	}
	
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	public void start() {
		running = true;
		createBufferStrategy(3);
		new Thread(this).start();
	}
	
	public void stop() {
		running = false;
		frame.setVisible(false);
		frame.dispose();
	}
	
	public void update(int ticks) {
		if(SQUAREWIDTH == 0) {
			setSquareParameters();
		}
		if(scene != null && SQUAREWIDTH != 0)
			scene.update(ticks);
	}
	
	public void render(float interpolation) {
		if(scene != null && SQUAREWIDTH != 0)
			scene.redraw(interpolation);
	}
	
	private void setSquareParameters() {
		int tempWidth = getWidth() / WIDTH;
		int tempHeight = getHeight() / TOTALHEIGHT;
		
		SQUAREWIDTH = Math.min(tempWidth, tempHeight);
		SQUAREHEIGHT = SQUAREWIDTH;
		
		System.out.println("S_Width = " + SQUAREWIDTH);
		System.out.println("S_Height = " + SQUAREHEIGHT);
	}
	
	public void run() {
		final int TICKS_PER_SECOND = 100;
		final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
		final int MAX_FRAMESKIP = 5;
	    
	    float interpolation = 0.0f;	    
		long nextGameTick = System.currentTimeMillis();
		int second = 0;
		
		int renderCount = 0;
		int renderFPS = 0;
		while(running) {
			int loops = 0;
			long now = System.currentTimeMillis();
			while(now - nextGameTick > SKIP_TICKS && loops < MAX_FRAMESKIP) {
				nextGameTick += SKIP_TICKS;
				loops++;
				update(SKIP_TICKS);
				currentFps++;
			}
			if ( now - nextGameTick > SKIP_TICKS) {
               nextGameTick = now - SKIP_TICKS;
            }
			second += loops * SKIP_TICKS;
			if(second >= 1000) {
				fps = currentFps;
				currentFps = 0;
				renderFPS = renderCount;
				second = 0;
				renderCount = 0;
				System.out.println("FPS = " + fps + ". Render = " + renderFPS);
			}
			renderCount++;
			interpolation = Math.min(1.0f, (float)(now - nextGameTick) / SKIP_TICKS);
			render(interpolation);
		}
	}
	
	public void setKeyAdapter(KeyAdapter key) {
		removeKeyListener(keyAdapter);
		keyAdapter = key;
		addKeyListener(keyAdapter);
	}
	
	public void setMouseAdapter(MouseAdapter mouse) {
		removeMouseListener(mouseAdapter);
		mouseAdapter = mouse;
		addMouseListener(mouse);
	}
	
	public void setMouseMotionListener(MouseMotionListener mouseMotion) {
		removeMouseMotionListener(mmListener);
		mmListener = mouseMotion;
		addMouseMotionListener(mmListener);
	}
	
	public static void main(String[] args) {
		Board b = new Board();
		b.start();		
		b.setScene(new Menu(b, b.getBufferStrategy(), b.getWidth(), b.getHeight()));
	}
}
