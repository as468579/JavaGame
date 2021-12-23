package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import GameState.GameStateManager;

import java.awt.event.*;

public class GamePanel extends JPanel
	implements Runnable, KeyListener{

	// dimensions 
	public static final int WIDTH = 1280 ;
	public static final int HEIGHT = 900 ;
	public static final int MAXSCALE = 4;
	private static int SCALE = 1;
	
	// game thread
	private Thread thread;
	private boolean running;
	private int FPS = 40;
	private long targetTime = 1000 / FPS;
	
	// image
	private BufferedImage image;
	private Graphics2D g;
	
	// game state manager
	private GameStateManager gsm;
	
	public GamePanel() {
		super();
		setPreferredSize(
			new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setFocusable(true);
		requestFocus();
	}
	
	public static int  getScale() { return SCALE; }
	public static void setScale(int s) { 
		if(s < 1 || s > MAXSCALE) return;
		SCALE = s;

	}
	
	@Override
	public void addNotify(){
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		gsm.keyPressed(e.getKeyCode());
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		gsm.keyReleaseed(e.getKeyCode());
		
	}

	@Override
	public void run() {
		init();
		
		long start;
		long elapsed;
		long wait;
		
		//game loop
		while(running){
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) wait = 5;
			try {
				
				Thread.sleep(wait);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void init(){
		image = new BufferedImage(WIDTH, HEIGHT, 
					BufferedImage.TYPE_3BYTE_BGR);
		g = (Graphics2D)image.getGraphics();
		
		running = true;
		
		gsm = new GameStateManager();
	}
	
	private void update(){
		gsm.update();
	}
	
	private void draw(){
		gsm.draw(g);
	}
	
	private void drawToScreen(){
		Graphics g2 = getGraphics();
		g2.drawImage(
				image,
				0,
				0,
				WIDTH * SCALE,
				HEIGHT * SCALE,
				null
		);
		g2.dispose(); // release resource of g
	}
	

	

	
	
	
	
	
	
}
