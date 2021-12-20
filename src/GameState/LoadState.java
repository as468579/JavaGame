package GameState;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class LoadState extends GameState{
	public TileMap tileMap;
	public static BufferedImage[][] Explosion = load("/Sprites/Enemies/explosion.gif", 60, 60);
	public static BufferedImage[][] Fireball = load("/Sprites/Player/fireball.gif", 30, 30);
	public static BufferedImage[][] Bullet = load("/Sprites/Enemies/bullet.png", 20, 8);
	public static BufferedImage[][] Tornado = load("/Sprites/Enemies/tornado.png", 40 ,40);
	public static BufferedImage[][] Alligator = load("/Sprites/Enemies/alligator.png", 48, 48);
	public static BufferedImage[][] Slugger = load("/Sprites/Enemies/slugger.gif", 60, 60);
	public static BufferedImage[][] Snake = load("/Sprites/Enemies/snake.png", 30, 30);
	public static BufferedImage[][] Bat = load("/Sprites/Enemies/bat.png", 50, 30);
	public static BufferedImage[][] IronCannon = load("/Sprites/Enemies/ironCannon.png", 50, 35);
	public static BufferedImage[][] Dragon = load("/Sprites/Player/dragon.png", 60, 30);
	public static BufferedImage[][] Human = load("/Sprites/Player/human.png", 45, 30);
	//public static final int NUMFRAMES = []
			
	private static final int EXPLOSION = 0;
	private static final int FIREBALL = 1;
	private static final int BULLET = 2;
	private static final int TORNADO = 3;
	private static final int ALLIGATOR = 4;
	private static final int SLUGGER = 5;
	private static final int SNAKE = 6;
	private static final int BAT = 7;
	private static final int IRONCANNON = 8;
	private static final int DRAGON = 9;
	private static final int HUMAN = 10;
	
	public static BufferedImage[][] load(String s, int w, int h){
		BufferedImage[][] tmp;
		try {
			BufferedImage spritesheet = ImageIO.read(LoadState.class.getResourceAsStream(s));
			int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;

			tmp = new BufferedImage[height][width];
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					tmp[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return tmp;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
	
	// constructor
	public LoadState(GameStateManager gsm) {
		
		this.gsm = gsm;
		
		// tileMap
		tileMap = new TileMap(16);
		tileMap.loadTile("/Tilesets/mario_tilesets.png", 2, 2);
		tileMap.loadTile("/Tilesets/underground_tiles.png", 1, 0);	
		tileMap.loadMap("res/Maps/Map_test.json");
		tileMap.setPosition(0, 0);
		tileMap.setTween(0.07);
		
		gsm.setStates(GameStateManager.MENUSTATE);
	}
	
	public TileMap getTileMap() {
		return this.tileMap;
	}

	@Override
	public void init() {
	}

	@Override
	public void update() {
	}

	@Override
	public void draw(Graphics2D g) {
	}

	@Override
	public void keyPressed(int k) {
	}

	@Override
	public void keyReleased(int k) {
	}
}
