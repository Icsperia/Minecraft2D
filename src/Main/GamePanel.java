package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import Tile.TileManager;
import entity.Player;

public class GamePanel extends JPanel implements Runnable{
	final int originalTileSize = 16;  //32 * 32
	final int scale = 2;
	
	public final int tileSize = originalTileSize * scale; //64 * 64
	public final int maxScreenCol = 32;
	public final int maxScreenRow = 18;
	final int screenWidth = tileSize * maxScreenCol; // 768px
	final int screenHeight = tileSize * maxScreenRow; // 565px
	
	int FPS = 60;
	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	Player player = new Player(this, keyH);
	public CollisionChecker cCollision = new CollisionChecker(this);
	//player default position
	
//	int playerX = 100;
//	int playerY = tileSize*(maxScreenRow - 1);
//	int playerSpeed = 3;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.decode("#81A0FF"));
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		double drawInterval = 1000000000/FPS;  //0,01666 sec
		double nextDrawTime = System.nanoTime() + drawInterval;
		
		while(gameThread != null) {
		
			update();
			
			repaint();
		
			try {
				double remainingTime = nextDrawTime - System.nanoTime();				
				remainingTime = remainingTime / 1000000;
				
				if(remainingTime < 0)
					remainingTime = 0;
				
				Thread.sleep((long) remainingTime);
				
				nextDrawTime += drawInterval; 
			} catch(InterruptedException e){
				e.printStackTrace();
			}
			
		}
	}
	
	public void update() {
		
		player.update();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics g2 = (Graphics2D)g;
		tileM.draw(g2);
		player.draw(g2);
		
		g2.dispose();
	}
	
}