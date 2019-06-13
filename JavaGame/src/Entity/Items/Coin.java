package Entity.Items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Item;
import Main.GamePanel;
import TileMap.TileMap;

public class Coin extends Item{
	
	private int money;
	private BufferedImage[] sprites;
	private Font font;
	private Color color;
	private long startTimer;
	private boolean firstTouch;
	
	
	public Coin(TileMap tm) {
		super(tm);
		
		money = 50;
		width = 24;
		height = 24;
		cwidth = 24;
		cheight = 24;
		
		firstTouch  = true;
		
		// set font and color
		font = new Font(
				"Century Gothic",
				Font.BOLD,
				20);
		color = new Color(255,255,0); // yellow
		
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Items/coin.png"
				)
			);
			
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
											i * width,
											0,
											width,
											height
										);
			}
			
			// set animation
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(200);
		}catch(Exception e) {
				e.printStackTrace();
		}
	}
	
	public int getMoney() { return money; }
	
	@Override 
	public void update() {
		
		// update animation and coin will disappear after touching
		if(touched) {
			
			if(firstTouch) {
				startTimer = System.nanoTime();
				firstTouch = false;
			}
			long elapsed = (System.nanoTime() - startTimer) / 1000000;
			if(animation.hasPlayedOnce() && elapsed > 700) {
				remove = true;
			}
		}
		
		animation.update();
	}
	
	
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		// if(notOnScreen) return ;
		
		// cuz bomb is facingLeft in the begining
		if(facingRight) {
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2 + width), // let x be the center of image
					(int)(y + ymap - height /2),         // let y be the center of image
					-width,
					height,
					null
				);
		}
		else{
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2),  // let x be the center of image
					(int)(y + ymap - height / 2), // let y be the center of image
					null
				);
		}
		
		String s= "+" + String.valueOf(money);
		g.setColor(color);
		g.setFont(font);
		if(touched) {
			g.drawString(
					s,
					(int)(x + xmap - (width / 2) - s.length() * 2 ),
					(int)(y + ymap - height));
		}
		
		// draw for test
//		drawCollisionBox(g);
	}
	
	
}
