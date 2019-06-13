package Entity.Enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;

public class DarkKnight extends Enemy{
	
	// flameslash
	private boolean flameslash;
	private int flameSlashDamage;
	
	// earthbreaker
	private boolean earthbreaker;
	private int earthbreakerDamage;
	
	private int count = 0;
	private boolean remove;
	
	// animation actions
	public static final int BORN = 0;
	public static final int IDLE = 1;
	public static final int WALKING = 2;
	public static final int FLAMESLASH = 3;
	public static final int EARTHBREAKER = 4;
	public static final int KB = 5;
	public static final int DYING = 6;
	
	private HashMap<String, AudioPlayer> sfx;
	
	// animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[] NUMFRAMES = {
		33, 8, 6, 22, 25, 1, 16	
	};
	private final int[] FRAMEWIDTHS = {
		286, 86, 86, 198, 127, 86, 100
	};
	
	private final int[] FRAMEHEIGHTS = {
		212, 86, 86, 143, 165, 86, 82
	};
	private final int[] SPRITEDELAYS = {
		80, 40, 40, 80, 115, -1, 80
	};
	
	public DarkKnight(TileMap tm) {
		super(tm);
		
		width = 212;
		height = 286;
		cwidth = 60;
		cheight = 75;
		
		// enemy status
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		
		health = maxHealth = 200;
		flameSlashDamage = 2;
		earthbreakerDamage = 2;
		
		
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/darkKnight.png"
				)
			);
			
			int imageY = 0;
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 7 ; i++) {
				BufferedImage[] bi = new BufferedImage[NUMFRAMES[i]];
				for(int j = 0; j < NUMFRAMES[i]; j++) {
					bi[j] = spritesheet.getSubimage(
							j * FRAMEWIDTHS[i],
							imageY,
							FRAMEWIDTHS[i],
							FRAMEHEIGHTS[i]
					);
				}
				sprites.add(bi);
				imageY += FRAMEHEIGHTS[i];
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		animation = new Animation();
		setAnimation(BORN);
		
		// when the alligator start up, it is going to face left
		right = false;
		left = true;
		facingRight = false;
		
	}
		
	private void setAnimation(int i) {
		currentAction = i;
		if(currentAction == EARTHBREAKER) {
			int length = sprites.get(currentAction).length;
			BufferedImage[] newSprite = new BufferedImage[25];
			newSprite[0] = sprites.get(currentAction)[0];
			
			for(int count = 1; count < 15; count++) {
				newSprite[count] =  sprites.get(currentAction)[(count - 1) % 7 +1];
			}
			
			for(int count = 0; count < 10; count++) {
				newSprite[15 + count] = sprites.get(currentAction)[8 + count];
			}
			
			animation.setFrames(newSprite);
		}
		else {
			animation.setFrames(sprites.get(currentAction));
		}
		animation.setDelay(SPRITEDELAYS[currentAction]);
		width = FRAMEWIDTHS[currentAction];
		height = FRAMEHEIGHTS[currentAction];
	}
	
	private void getNextPosition() {
		
		// cuz a alligator only move left or right
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		
		// cannot move while attacking, except in air
		if(
			currentAction == BORN ||
			currentAction == FLAMESLASH || 
			currentAction == EARTHBREAKER ||
			currentAction == KB ||
			currentAction == DYING
		) {
			dx = 0;
		}
		
	}
	
	@Override
	public int getDamage() {
		switch(currentAction) {
			case BORN:case DYING: return 0;
			case IDLE:case WALKING:case KB: return 1;
			case FLAMESLASH: return flameSlashDamage;
			case EARTHBREAKER: return earthbreakerDamage;
			default: 
				System.out.println("Error Action Number");
				System.exit(1);
		}
		return 0;
	}
	
	@Override
	public void hit(int damage) {
		System.out.println("HP : " + health);
		super.hit(damage);
		flinching = true;
		flinchTimer = System.nanoTime();
		if( damage > 5 ) {
			// lead to change animation
			count++;
			setAnimation(KB);
		}
	}
	
	private void actionCycle() {
		
		// check dead 
		if(dead) {
			if(currentAction != DYING) {
				setAnimation(DYING);
			}
			return;
		}
		
		if(!animation.hasPlayedOnce()) return;
		
		// update count 
		if(count >= 50) count = 0;
		else count++;
		
		// update the cycle of actions
		if(count == 15 || count == 40) { setAnimation(FLAMESLASH); }
		else if(count == 30) { setAnimation(EARTHBREAKER); }
		else { setAnimation(WALKING); }
	}
	
	@Override
	public void update() {
		
		if(currentAction == BORN && animation.hasPlayedOnce()) {
			setAnimation(WALKING);
		}
		
		// update Action
		actionCycle();
		
		// player react time
		if(animation.getCurrentFrame() > 5) {
			if(currentAction == FLAMESLASH) {
				cwidth = 150;
				cheight = 90;
			}
			
			else if(currentAction == EARTHBREAKER) {
				if(animation.getCurrentFrame() < 15) {
					cheight = 100;
				}
				else {  
					cwidth = 100;
					cheight = 180;
				}
			}
		}
		else {
			cwidth = 60;
			cheight = 75;
		}
		
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check flinching
		if(flinching) {
			long elasped = 
					(System.nanoTime() - flinchTimer) / 1000000;
			if(elasped > 100) {
				flinching = false;
			}
		}
		
		// if it hits a wall, go the other direction
		if(
			right && 
			dx == 0 &&
			currentAction != BORN &&
			currentAction != FLAMESLASH &&
			currentAction != EARTHBREAKER &&
			currentAction != KB &&
			currentAction != DYING 
		) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if(
			left && 
			dx == 0 &&
			currentAction != FLAMESLASH &&
			currentAction != EARTHBREAKER &&
			currentAction != KB &&
			currentAction != DYING 
		) {
			right = true;
			left = false;
			facingRight = true;
		}
		
		// update animation
		animation.update();
	}
	public boolean shouldRemove(){
		if(currentAction == DYING && animation.hasPlayedOnce()) return true;
		else return false;
	}
	
	public void drawHP(Graphics2D g) {
		
		// draw HP
		double Rate = (health * 1.0) / maxHealth;
		if( Rate > 0.7 && Rate <= 1 ) {
			g.setColor(Color.GREEN);
		}
		else if( Rate > 0.3 && Rate <= 0.7) {
			g.setColor(Color.ORANGE);
		}
		else {
			g.setColor(Color.RED);
		}

		// inner rectangle
		g.fillRect(
				(int)(x + xmap - cwidth / 2),
				(int)(y + ymap - cheight / 2),
				(int)(60 * Rate) ,
				5
		);
		
		//  draw HP boundary
		g.setColor(Color.BLACK);
		g.drawRect(
				(int)(x + xmap - cwidth / 2),
				(int)((y + ymap - cheight / 2)),
				60,
				5
		);
	}
	
	@Override
	public void draw(Graphics2D g) {
		
		// let slugger.xmap = tileMap.x and slugger.ymap = tileMap.y
		// update the map information saved in mapObject
		setMapPosition();
		
		// test
		//if(notOnScreen()) return;
		
		// cuz alligator is facingLeft in the begining
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
		
		
		// draw HP
		drawHP(g);
		
		// draw for test
		//drawCollisionBox(g);
	}
}
