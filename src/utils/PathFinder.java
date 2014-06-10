package utils;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import units.Monster;
import units.MotionObject;

public class PathFinder {
	public final static int WIDTH = 24;
	public final static int HEIGHT = 13;
	public final static short[][] map = new short[HEIGHT][WIDTH];
	public final static Map<Integer, String> CELLTYPES = new HashMap<>();
	public final static Map<Integer, Color> CELLCOLORS = new HashMap<>();
	static {
		init();
		
		CELLTYPES.put(0, "Free Cell");
		CELLTYPES.put(1, "Road");
		CELLTYPES.put(2, "Base");
		
		CELLCOLORS.put(0, Color.GRAY);
		CELLCOLORS.put(1, Color.GRAY.darker());
		CELLCOLORS.put(2, Color.BLUE.darker());
	}
	private static void init() {
		int x = 6;
		int y = 1;
		int i = y;
		int j = 0;
				
		for(; j < x; j++)
			map[y][j] = 1;
		x = j;
		map[y][x] = 4;
		i = y + 1;
		for(; i < (y + 5); i++)
			map[i][x] = 1;
		y = i;
		map[y][x] = 4;
		j = x + 1;
		for(; j < (x + 6); j++)
			map[y][j] = 1;
		x = j;
		map[y][x] = 4;
		i = y + 1;
		for(; i < (y + 5); i++)
			map[i][x] = 1;
		y = i;
		map[y][x] = 4;
		j = x + 1;
		for(; j < (x + 8); j++)
			map[y][j] = 1;
		x = j;
		map[y][x] = 4;
		i = y - 1;
		for(; i > (y - 6); i--)
			map[i][x] = 1;
		y = i;
		map[y][x] = 4;
		j = x + 1;
		for(; j < (x + 4); j++)
			map[y][j] = 1;
		x = j - 1;
		map[y][x] = 2;
	}
	
	public static boolean moveObject(MotionObject obj) {
		int dirX = obj.getDirX();
		int dirY = obj.getDirY();
		
		int x = (int)obj.getPosition().X();
		int y = (int)obj.getPosition().Y();
		
		if(x < 0 || y < 0) {
			return true;
		}
		if(map[y][x] == 2) {
			return false;
		}
		
		if(dirY + y < HEIGHT && dirY != 0) {
			if(map[dirY + y][x] == 0) {
				if(x + 1 < WIDTH && map[y][x + 1] != 0) {
					dirX = 1;
				} else if(x - 1 > 0 && map[y][x - 1] != 0) {
					dirX = -1;
				} else  {
					return false;
				}
				dirY = 0;
				obj.setDirX(dirX);
				//obj.setDirY(dirY);
				return true;
			}
		} 
		if(dirX + x < WIDTH && dirX != 0) {
			if(map[y][dirX + x] == 0) {
				if(y + 1 < HEIGHT && map[y + 1][x] != 0) {
					dirY = 1;
				} else if(y - 1 > 0 && map[y - 1][x] != 0) {
					dirY = -1;
				} else {
					return false;
				}
				dirX = 0;
				//obj.setDirX(dirX);
				obj.setDirY(dirY);
				return true;
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		Monster m = new Monster(0, 1);
		m.setDirX(1);
		m.setDirY(0);
		for(int i = 0; i < HEIGHT ; i++) {
			for(int j = 0; j < WIDTH; j++) {				
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		
		while(PathFinder.moveObject(m)) {
			if(m.getPosition().X() > 0 && m.getPosition().Y() > 0 && (int)m.getPosition().X() < WIDTH && (int) m.getPosition().Y() < HEIGHT)
				map[(int)m.getPosition().Y()][(int)m.getPosition().X()] = 3;
				m.move();
		}
		
		for(int i = 0; i < HEIGHT ; i++) {
			for(int j = 0; j < WIDTH; j++) {				
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
	}
}
