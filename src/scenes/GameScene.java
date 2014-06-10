package scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import components.Board;

import specialEffects.SpecialEffect;
import towers.AbstractTower;
import towers.GunTower;
import towers.LaserTower;
import towers.OrdinaryTower;
import towers.PoisonTower;
import towers.SlowTower;
import units.AbstractMonster;
import units.GameObject;
import units.Monster;
import utils.Animator;
import utils.Generator;
import utils.ImageCollection;
import utils.PathFinder;
import utils.Position;
import utils.SpriteSheet;

public class GameScene implements Scene {
	private Graphics2D g;
	private BufferStrategy bs;
	
	private GameObject base;
	
	private boolean isPaused, isGameOver;
	private boolean isObjectSelected, isTowerPlacing;
	
	private int reward = 10000;
	private int tempReward = 0;
	private int startPositionX = 0;
	private int startPositionY = 1;
	private int generatorIndex = 0;
	private float selectedX = 0;
	private float selectedY = 0;
	private int mouseX = -1;
	private int mouseY = -1;
	
	private int width = 0;
	private int height = 0;
	
	private Rectangle recMenu;
	private Rectangle recSubMenu1;
	private Rectangle recSubMenu2;
	private Rectangle recSubMenu3;
	private Rectangle gameMenu;
	private Rectangle gameMenuSub1;
	private Rectangle gameMenuSub2;

	private Rectangle menuButton;
	
	private GameObject currentySelectedObject = new GameObject(-1, -1);
	
	private List<AbstractMonster> monsters = Collections.synchronizedList(new ArrayList<AbstractMonster>());
	private List<AbstractMonster> deathMonster = Collections.synchronizedList(new ArrayList<AbstractMonster>());
	private List<AbstractTower> towers = new ArrayList<AbstractTower>();
	private List<AbstractTower> towersOnField = new ArrayList<AbstractTower>();
	private List<AbstractTower> towersOnConstruction = new ArrayList<>();
	private List<Generator<AbstractMonster>> generators = new ArrayList<Generator<AbstractMonster>>();
	private List<SpecialEffect> specialEffects = new ArrayList<>();
	
	private Board b;
	
	private class KeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			
			if(keyCode == KeyEvent.VK_P)
				isPaused = !isPaused;
		}
	}
	
	private class MAdapter extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			
			if(e.getButton() == MouseEvent.BUTTON1) {
				Point p = e.getPoint();
				float x = (float)(p.getX() / Board.SQUAREWIDTH);
				float y = (float)(p.getY() / Board.SQUAREHEIGHT);			
				if(isPaused) {
					if(gameMenuSub1.x < p.x && (gameMenuSub1.x + gameMenuSub1.width) > p.x
							&& gameMenuSub1.y < p.y && (gameMenuSub1.y + gameMenuSub1.height) > p.y) {
						isPaused = false;
					}
					if(gameMenuSub2.x < p.x && (gameMenuSub2.x + gameMenuSub2.width) > p.x
							&& gameMenuSub2.y < p.y && (gameMenuSub2.y + gameMenuSub2.height) > p.y) {
						b.setScene(new Menu(b, bs, width, height));
					}
				}
				
				if(menuButton.x < p.x && (menuButton.x + menuButton.width) > p.x
						&& menuButton.y < p.y && (menuButton.y + menuButton.height) > p.y) {
					isPaused = true;
				}
				
				if(y < Board.HEIGHT - 2) {
					selectedX = x;
					selectedY = y;
					if(isTowerPlacing) {
						int index = PathFinder.map[(int)y][(int)x];
						if(index == 0) {
							AbstractTower tower = ((AbstractTower)currentySelectedObject).createTower();
							tower.setPosition(new Position((int)x,(int)y));
							towersOnConstruction.add(tower);
							isTowerPlacing = false;
							return;
						}
					}
				} else {
					/*if((x > rMenu.x && x < (rMenu.x + rMenu.width)) && (y > rMenu.y && y < (rMenu.y + rMenu.height))) {
						b.setScene(new Menu(b, bs, width, height));
						System.out.println("Menu");
						return;
					}*/
					for(AbstractTower t : towers) {
						float tX = t.getPosition().X();
						float tY = t.getPosition().Y();
						float tX2 = tX + (float)t.getSize().getWidth()/Board.SQUAREWIDTH;
						float tY2 = tY + (float)t.getSize().getHeight()/Board.SQUAREHEIGHT;
						if(x > tX && x < tX2 && y > tY && y < tY2 && t.getCost() <= reward) {
							currentySelectedObject.setSelected(false);
							t.setSelected(true);
							currentySelectedObject = t;
							isObjectSelected = true;
							mouseX = e.getPoint().x;
							mouseY = e.getPoint().y;
							isTowerPlacing = true;
							tempReward = t.getCost();
							reward -= tempReward;
							break;
						}
					}
				}
			}
			else if(e.getButton() == MouseEvent.BUTTON3) {
				selectedX = -1;
				selectedY = -1;
				mouseX = -1;
				mouseY = -1;
				
				if(isTowerPlacing) {
					reward += tempReward;
					tempReward = 0;
				}
				isObjectSelected = false;
				isTowerPlacing = false;
				currentySelectedObject.setSelected(false);
			}
		}
	}
	
	private class MMListener implements MouseMotionListener {
		@Override
		public void mouseMoved(MouseEvent e) {
			if(isObjectSelected) {
				if(currentySelectedObject instanceof AbstractTower) {
					mouseX = e.getPoint().x;
					mouseY = e.getPoint().y;
				}
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {}
	}
	
	public GameScene(BufferStrategy bs, int width, int height, Board b) {
		this.bs = bs;
		this.g = (Graphics2D)this.bs.getDrawGraphics();
		this.width = width;
		this.height = height;		
		this.b = b;
		
		try {
			BufferedImage baseImage = ImageIO.read(new File("images//base.png"));
			SpriteSheet baseSheet = new SpriteSheet(baseImage, 150, 50);
			base = new GameObject();
			base.setAnimator(new Animator(baseSheet));
			base.setHealth(10);
		} catch (IOException e) {
			System.out.println("Couldn't load base.png\n" + e);
		}
		towers.add(new OrdinaryTower(0.25f, Board.HEIGHT - 0.25f - (float)Board.SQUAREHEIGHT/2/Board.SQUAREHEIGHT));
		towers.add(new GunTower(1.25f, Board.HEIGHT - 0.25f - (float)Board.SQUAREHEIGHT/2/Board.SQUAREHEIGHT));
		towers.add(new SlowTower(2.25f, Board.HEIGHT - 0.25f - (float)Board.SQUAREHEIGHT/2/Board.SQUAREHEIGHT));
		towers.add(new PoisonTower(3.25f, Board.HEIGHT - 0.25f - (float)Board.SQUAREHEIGHT/2/Board.SQUAREHEIGHT));
		towers.add(new LaserTower(4.25f, Board.HEIGHT - 0.25f - (float)Board.SQUAREHEIGHT/2/Board.SQUAREHEIGHT));
		generators.add(new Monster());
		
		this.b.setKeyAdapter(new KeyListener());
		this.b.setMouseAdapter(new MAdapter());
		this.b.setMouseMotionListener(new MMListener());
		
		System.out.println("Game Width: " + width + " height: " + height);
		
		recMenu = new Rectangle(0, (Board.HEIGHT - 1) * Board.SQUAREHEIGHT, width, height - (Board.HEIGHT - 1) * Board.SQUAREHEIGHT);
		recSubMenu1 = new Rectangle(0, (Board.HEIGHT - 1) * Board.SQUAREHEIGHT, (int)width/3, height - (Board.HEIGHT - 1) * Board.SQUAREHEIGHT);
		recSubMenu2 = new Rectangle((int)width/3, (Board.HEIGHT - 1) * Board.SQUAREHEIGHT, (int)width/2, height - (Board.HEIGHT - 1) * Board.SQUAREHEIGHT);
		recSubMenu3 = new Rectangle((int)width/3 + (int)width/2, (Board.HEIGHT-1) * Board.SQUAREHEIGHT, width - ((int)width/3 + (int)width/2), height - (Board.HEIGHT - 1) * Board.SQUAREHEIGHT);
		gameMenu = new Rectangle((Board.WIDTH/2 - 3) * Board.SQUAREWIDTH, (Board.HEIGHT/2 - 4) * Board.SQUAREHEIGHT, 6 * Board.SQUAREWIDTH, 8 * Board.SQUAREHEIGHT);
		gameMenuSub1 = new Rectangle(gameMenu.x + (int)(0.5 * Board.SQUAREWIDTH), gameMenu.y + Board.SQUAREHEIGHT, gameMenu.width - Board.SQUAREWIDTH, Board.SQUAREHEIGHT);
		gameMenuSub2 = new Rectangle(gameMenu.x + (int)(0.5 * Board.SQUAREWIDTH), gameMenu.y + (int)(2.5 * Board.SQUAREHEIGHT), gameMenu.width - Board.SQUAREWIDTH, Board.SQUAREHEIGHT);
		
		menuButton = new Rectangle(recSubMenu3.x + (int)(0.5f * Board.WIDTH), recSubMenu3.y + recSubMenu3.height/2 - Board.SQUAREHEIGHT/2, Board.SQUAREWIDTH, Board.SQUAREHEIGHT);
	}

	private int health = 1;
	private int waveLenght = 5;
	private void createWave(Generator<AbstractMonster> g, int dirX, int dirY) {
		float x = startPositionX - 1.5f * waveLenght;
		float y = startPositionY;
		for(int i = 0; i < 1; i++) {
			AbstractMonster m = g.next(x, y);
			m.setDirX(dirX);
			m.setDirY(dirY);
			m.setHealth(health);
			monsters.add(m);
			y += 1.5f * dirY;
			x += 1.5f * dirX;
		}
		health++;
		
		if(health % 3 == 0) {
			waveLenght += 5;
		}
	}
	
	private void iterate(int timeShift) {
		Iterator<AbstractMonster> mIterator = this.monsters.iterator();
		while(mIterator.hasNext()) {
			AbstractMonster m = mIterator.next();
			if(PathFinder.moveObject(m)) {
				m.move();
			}
			else {
				m.damage(m.getHealth());
				mIterator.remove();
				base.getAnimator().setTicks(4);
				base.getAnimator().setTimeShift(40);
				base.getAnimator().setYIndex(1);
				base.setHealth(base.getHealth() - 1);
				if(base.getHealth() == 0) {
					isGameOver = true;
				}
				continue;
			}
			if(m.getHealth() <= 0) {
				reward += m.getReward();
				m.killMonster();
				deathMonster.add(m);
				mIterator.remove();
			} else {
				m.applyEffects();
			}
		}
		
		Iterator<AbstractTower> constructions = towersOnConstruction.iterator();
		
		while(constructions.hasNext()) {
			AbstractTower tower = constructions.next();
			if(tower.constructed(timeShift)) {
				constructions.remove();
				towersOnField.add(tower);
			}
		}
		
		for(AbstractTower t : towersOnField)
			if(t.isReadyToAttack(timeShift))
				t.attack(monsters);
		
		Iterator<AbstractMonster> deathMonstersIter = deathMonster.iterator();
		while(deathMonstersIter.hasNext()) {
			AbstractMonster m = deathMonstersIter.next();
			if(m.getAnimator().getTicks() == 0) {
				deathMonstersIter.remove();
			}
		}
		
		Iterator<SpecialEffect> effects = specialEffects.iterator();
		while(effects.hasNext()) {
			SpecialEffect effect = effects.next();
			if(effect.getAnimator().getTicks() == 0) {
				effects.remove();
			}
		}
	}

	private void render(float interpolation) {
		g = (Graphics2D)bs.getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
		drawMap();
		draw(base, g);
		for(AbstractMonster m : monsters) {
			if(m.getPosition().X() >= 0 || m.getPosition().Y() >= 0)
				drawMonster(m, g);//				draw(m, g, 0,0);
			float x = m.getPosition().X();
			float y = m.getPosition().Y();
			float x2 = x + m.getSize().width/Board.SQUAREWIDTH;
			float y2 = y + m.getSize().height/Board.SQUAREHEIGHT;
			if(selectedX > x && selectedX < x2 && selectedY > y && selectedY < y2) {
				currentySelectedObject.setSelected(false);
				m.setSelected(true);
				currentySelectedObject = m;
				selectedX = -1;
				selectedY = -1;
			}
		}
		
		for(AbstractMonster m : deathMonster)
			draw(m, g, 0,0);
		for(AbstractTower t : towersOnField) {
			draw(t, g);
			float x = t.getPosition().X();
			float y = t.getPosition().Y();
			float x2 = x + t.getSize().width/Board.SQUAREWIDTH;
			float y2 = y + t.getSize().height/Board.SQUAREHEIGHT;
			if(selectedX > x && selectedX < x2 && selectedY > y && selectedY < y2) {
				currentySelectedObject.setSelected(false);
				t.setSelected(true);
				currentySelectedObject = t;
				selectedX = -1;
				selectedY = -1;
			}
		}
		
		for(AbstractTower t : towersOnConstruction) {
			draw(t, g);
			float x = t.getPosition().X();
			float y = t.getPosition().Y();
			float x2 = x + t.getSize().width/Board.SQUAREWIDTH;
			float y2 = y + t.getSize().height/Board.SQUAREHEIGHT;			
			if(selectedX > x && selectedX < x2 && selectedY > y && selectedY < y2) {
				currentySelectedObject.setSelected(false);
				t.setSelected(true);
				currentySelectedObject = t;
				selectedX = -1;
				selectedY = -1;
			}
		}
		
		for(SpecialEffect effect : specialEffects) {
			draw(effect, g);
		}
		drawSelectedObject();
		
		drawMenu();
		g.dispose();
		bs.show();
	}
	
	private void drawSelectedObject() {
		if(currentySelectedObject.isSelected()) {
			if(currentySelectedObject instanceof AbstractTower) {
				AbstractTower tower = (AbstractTower)currentySelectedObject;
				if(isTowerPlacing)
					g.drawImage(tower.getAnimator().getImage(), mouseX, mouseY,
							tower.getAnimator().getImage().getWidth(), tower.getAnimator().getImage().getHeight(), null);
				else {
					Ellipse2D.Double circle = new Ellipse2D.Double(tower.getPosition().X() * Board.SQUAREWIDTH - Board.SQUAREWIDTH * (tower.getRadius() + 0.5) + Board.SQUAREWIDTH / 2,
							tower.getPosition().Y() * Board.SQUAREHEIGHT - Board.SQUAREWIDTH * (tower.getRadius() + 0.5) + Board.SQUAREWIDTH / 2,
							Board.SQUAREWIDTH * (tower.getRadius() * 2 + 1),
							Board.SQUAREWIDTH * (tower.getRadius() * 2 + 1));
					g.draw(circle);
					g.setColor(new Color(1f, 0f, 0f, 0.2f));
					g.fill(circle);
				}
			}
			if(currentySelectedObject instanceof AbstractMonster) {
				if(((AbstractMonster)currentySelectedObject).getHealth() <= 0)
					return;
			}
			drawSelectedObject(currentySelectedObject, g);
		}
	}
	
	private void drawMenu() {
		g.setColor(Color.BLACK);
		g.drawRect(recMenu.x, recMenu.y, recMenu.width, recMenu.height);
		g.drawRect(recSubMenu1.x, recSubMenu1.y, recSubMenu1.width, recSubMenu1.height);
		g.drawRect(recSubMenu2.x, recSubMenu2.y, recSubMenu2.width, recSubMenu2.height);
		g.drawRect(recSubMenu3.x, recSubMenu3.y, recSubMenu3.width, recSubMenu3.height);
		g.drawRect(menuButton.x, menuButton.y, menuButton.width, menuButton.height);
		
		{
			FontRenderContext frc = g.getFontRenderContext();
			FontMetrics fms = g.getFontMetrics();
			int width = fms.stringWidth("Menu");
			int height = fms.getAscent();
			String value = "Menu";
			Font font = new Font(value, Font.BOLD, 20);
			TextLayout tl = new TextLayout(value, font, frc);
			tl.draw(g, menuButton.x + menuButton.width/2 - (int)(1.5 * width / 2), menuButton.y + menuButton.height/2 + height);	
		}
		
		int xShift = 10;
		for(AbstractTower t : towers) {
			draw(t, g, xShift, 0);
			xShift += 5;
		}
		
//		g.drawImage(ImageCollection.getSpriteForName("heart"), recSubMenu2.x + 3 * Board.SQUAREWIDTH, recSubMenu2.y + recSubMenu2.height/2 - 40, 80, 80, null);
//		g.drawImage(ImageCollection.getSpriteForName("armor"), recSubMenu2.x + 6 * Board.SQUAREWIDTH, recSubMenu2.y + recSubMenu2.height/2 - 40, 80, 80, null);
		if(isPaused) {
			g.setColor(Color.GRAY.darker());
			g.fillRect(gameMenu.x, gameMenu.y, gameMenu.width, gameMenu.height);
			g.setColor(Color.BLACK);
			g.drawRect(gameMenuSub1.x, gameMenuSub1.y, gameMenuSub1.width, gameMenuSub1.height);
			g.drawRect(gameMenuSub2.x, gameMenuSub2.y, gameMenuSub2.width, gameMenuSub2.height);

			FontRenderContext frc = g.getFontRenderContext();
			FontMetrics fms = g.getFontMetrics();
			int width = fms.stringWidth("Resume");
			int height = fms.getAscent();
			String value = "Resume";
			Font font = new Font(value, Font.BOLD, 40);
			TextLayout tl = new TextLayout(value, font, frc);
			tl.draw(g, gameMenuSub1.x + gameMenuSub1.width/2 - width * 2, gameMenuSub1.y + gameMenuSub1.height/2 + height);
			value = "Main menu";
			tl = new TextLayout(value, font, frc);
			tl.draw(g, gameMenuSub2.x + gameMenuSub2.width/2 - width * 2, gameMenuSub2.y + gameMenuSub2.height/2 + height);
		}
	}
	
	private void drawMap() {
		Image image = new ImageIcon("images//cells//road.png").getImage();
		Image background = new ImageIcon("images//background.png").getImage();

		g.drawImage(background, 0, 0, width, height, null);
		g.setBackground(Color.GRAY.darker());
//		System.out.println("Width: " + width + " Height: " + height);
//		System.out.println("S Width: " + Board.SQUAREWIDTH + " S Height: " + Board.SQUAREHEIGHT);
		short[][] map = PathFinder.map;
		int width = Board.SQUAREWIDTH;
		int height = Board.SQUAREHEIGHT;
		g.setColor(Color.GRAY.darker());
		for(int i = 0; i < PathFinder.HEIGHT; i++) {
			for(int j = 0; j < PathFinder.WIDTH; j++) {
				if(map[i][j] == 0) {
				}
				else if(map[i][j] == 1 || map[i][j] == 4) {
					g.drawImage(image, j * width, i * height, width, height, null);
				}
				else if(map[i][j] == 2) {
					base.setAngle(270);
					base.setPosition(new Position(j, i));
				}
			}
		}
	}
	
	public void draw(GameObject o, Graphics2D g) {
		draw(o, g, 0, 0);
	}
	
	public void drawMonster(AbstractMonster o, Graphics2D g) {
		double x = o.getPosition().X() * Board.SQUAREWIDTH;
		double y = o.getPosition().Y() * Board.SQUAREHEIGHT;

		BufferedImage image = o.getAnimator().getImage();
		AffineTransform transform = new AffineTransform();
		transform.setToTranslation(x, y);
		int height = image.getHeight()/2;
		int width = image.getWidth()/2;
/*		if(o.getAngle() > 0 && o.getAngle() < 90) {
			width = image.getWidth()/2;
			height = image.getHeight();
			System.out.println(1 + " dir = " + o.rotateDirection);
		}
		if(o.getAngle() > 90 && o.getAngle() < 180) {
			width = image.getWidth();
			height = image.getHeight()/2;
			System.out.println(2 + " dir = " + o.rotateDirection);
		}
		if(o.getAngle() > 180 && o.getAngle() < 270)
			System.out.println(3 + " dir = " + o.rotateDirection);
		if(o.getAngle() > 270 && o.getAngle() < 360)
			System.out.println(4 + " dir = " + o.rotateDirection);*/
		transform.rotate(Math.toRadians(o.getAngle()), width,height);
		g.drawImage(image, transform, null);
	}
	
	public void draw(GameObject o, Graphics2D g, int shiftX, int shiftY) {
		double x = o.getPosition().X() * Board.SQUAREWIDTH;
		double y = o.getPosition().Y() * Board.SQUAREHEIGHT;

		BufferedImage image = o.getAnimator().getImage();
		AffineTransform transform = new AffineTransform();
		transform.setToTranslation(x + shiftX, y + shiftY);
		transform.rotate(Math.toRadians(o.getAngle()), image.getWidth()/2,image.getHeight()/2);
		Point2D.Double point = (Double) transform.transform(new Point2D.Double(image.getWidth(),0), null);
		g.drawImage(image, transform, null);
		
		for(SpecialEffect effect : o.getEffects()) {
			BufferedImage effectImage = effect.getAnimator().getImage();
			transform = new AffineTransform();
			transform.setToTranslation(point.x, 
										point.y);
			transform.rotate(Math.toRadians(o.getAngle()), 0, 0);
			g.drawImage(effectImage, transform, null);
		}
	}
		
	public void drawSelectedObject(GameObject o, Graphics2D g) {
		BufferedImage image = o.getAnimator().getImage();
		int x = recSubMenu2.x;
		int y = recSubMenu2.y;
		Position p = o.getPosition();
		
		g.setColor(Color.green.brighter());
		g.drawRect(	(int)(p.X() * Board.SQUAREWIDTH + Board.SQUAREWIDTH / 2 - image.getWidth()/2), 
					(int)(p.Y() * Board.SQUAREHEIGHT + Board.SQUAREHEIGHT / 2 - image.getHeight()/2), 
					image.getWidth(), image.getHeight());
		
		g.setColor(Color.BLACK);
		g.drawImage(o.getAnimator().getImage(), x + (int)Board.SQUAREWIDTH/2, y + (int)Board.SQUAREHEIGHT/3, 
				o.getAnimator().getImage().getWidth(), o.getAnimator().getImage().getWidth(), null);
		
		FontRenderContext frc = g.getFontRenderContext();
		Font font = new Font("Helvetica", Font.BOLD, 18);
		TextLayout tl = null;
		
		int shiftX = x + (int)Board.SQUAREWIDTH/2 + 5 + image.getWidth();
		int shiftY = y + (int)Board.SQUAREHEIGHT/2;
		for(String str : o.getDetails()) {
			tl = new TextLayout(str, font, frc);
			tl.draw(g, shiftX, shiftY);
			shiftY += 20;
		}
	}

	private final int waveTime = 5000;
	private int currentWaveTime = waveTime;
	
	@Override
	public void update(int timeShift) {
		if(monsters.size() == 0) {
			currentWaveTime -= timeShift;
			if(currentWaveTime == 0) {
				currentWaveTime = waveTime;
				createWave(generators.get(generatorIndex++ % generators.size()), 1, 0);
			}
		}
		if(!isGameOver && !isPaused)
			iterate(timeShift);
	}

	@Override
	public void redraw(float interpolation) {
		render(interpolation);
	}
	
}
