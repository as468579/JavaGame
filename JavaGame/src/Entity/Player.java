package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends MapObject{

	// player stuff
	private int health;
	private int maxHealth;
	private int fire;    // fire / fireCost = number of bullets
	private int maxFire; // maximum number of bullets
	private boolean flinching;
	private long flinchTimer;
	private long time;
	private int score;
	             
	// fireball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	
	// scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;
	
	// gliding
	private boolean gliding;
	
	// animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[] NUMFRAMES = {
		2, 8, 1, 2, 4, 2, 5	
	};
	private final int[] FRAMEWIDTHS = {
		30, 30, 30, 30, 30, 30, 60	
	};
	
	private final int[] FRAMEHEIGHTS = {
		30,	30, 30, 30, 30, 30, 30
	};
	private final int[] SPRITEDELAYS = {
		400, 40, -1, 100, 100, 100, 50
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	private static final int DYING = 7;
	
	private HashMap<String, AudioPlayer> sfx;
	public Player(TileMap tm) {
		
		super(tm);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = maxHealth = 5;
		fire = maxFire = 2500;
		
		fireCost = 200;
		fireBallDamage = 5;
		fireBalls = new ArrayList<FireBall>();
		
		scratchDamage = 8;
		scratchRange = 40;
		
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/playersprites.gif"
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
		setAnimation(IDLE);
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("jump",new AudioPlayer("/SFX/jump.mp3"));
		sfx.put("scratch", new AudioPlayer("/SFX/scratch.mp3"));
	}
	
	// health
	public void setHealth(int h) { health = h; }
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	
	// fire
	public void setFire(int f) { fire = f; }
	public int getFire() { return fire; }
	public int getMaxFire() { return maxFire; }
	
	// time
	public void setTime(int t) { time = t; }
	public long getTime() { return time; }
	
	
	// score
	public void increaseScore(int score) { this.score += score; }
	public int getScore() { return score; }
	
	
	public void setFiring() { firing = true;}
	
	public void setScratching() { scratching = true;}
	
	public void setGliding(boolean b) { gliding = b; }
	
	public void checkTouch(ArrayList<Item> items) {
		
		// loop items
		for(int i = 0; i < items.size(); i++) {
			
			Item it = items.get(i);
			if(intersects(it)) {
				it.setTouched();
			}
		}
	}
	
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			
			// while scratching, player's attack range is out of player's width
			// So we can not use intersects method 
			// scratch attack
			if(scratching) {
				if(facingRight) {
					if( 
						e.getX() > x &&
						e.getX() < x + scratchRange &&
						e.getY() > y - height / 2   &&
						e.getY() < y +  height / 2
					){
						e.hit(scratchDamage);
					}
				}
				else {
					if(
						e.getX() < x &&
						e.getX() > x - scratchRange &&
						e.getY() > y - height / 2   &&
						e.getY() < y + height / 2 
					) {
						e.hit(scratchDamage);
					}
				}
			}
			
			// fireball attack
			for(int j = 0; j < fireBalls.size(); j++) {
				if(fireBalls.get(j).intersects(e)) {
					e.hit(fireBallDamage);
					fireBalls.get(j).setHit();
				}
			}
			
			// chcek enemy collision
			if(intersects(e)) {
				hit(e.getDamage());
			}
		}
		
	}
	
	public void hit(int damage) {
		if(flinching) return ;
		health -= damage;
		if(health < 0) health = 0;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void reset() {
		health = maxHealth;
		facingRight = true;
		currentAction = IDLE;
		stop();
	}
	
	public void stop() {
		left = right = up = down = flinching =
			jumping = scratching = firing = gliding = false;
	}
	
	private void getNextPosition() {
		
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
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if (dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// cannot move while attacking, except in air
		if(
		(currentAction == SCRATCHING || currentAction == FIREBALL) &&
		!(jumping || falling)) {
			dx = 0;
		}
		
		// jumping
		if(jumping && !falling) {
			sfx.get("jump").play();
			dy  = jumpStart;
			falling = true;
		}
		
		// falling
		if(falling) {
			if(dy > 0 && gliding) dy += fallSpeed * 0.1;
			else dy += fallSpeed;
			
			if(dy > 0) jumping = false; 
			
			// the longer press the jump btn then jump higher
			// press jump btn jumping = true, release jump btn or dy > 0 jumping = false
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(SPRITEDELAYS[currentAction]);
		width = FRAMEWIDTHS[currentAction];
		height = FRAMEHEIGHTS[currentAction];
	}
	
	public void setDead() {
		health = 0;
		stop();
	}
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp,ytemp);
		
		
		// check attack has stopped
		if(currentAction == SCRATCHING) {
			if(animation.hasPlayedOnce()) scratching = false;
		}
		
		if(currentAction == FIREBALL) {
			if(animation.hasPlayedOnce()) firing = false;
		}
		
		// fireball attack
		fire += 1; // add 1/200 bullet
		if(fire > maxFire) fire = maxFire;
		if(firing && currentAction != FIREBALL) {
			if(fire > fireCost) {
				fire -= fireCost;
				FireBall fb = new FireBall(tileMap,facingRight);
				fb.setPosition(x,y);
				fireBalls.add(fb);
			}
		}
		
		// update fireBalls
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}
		
		// check done flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
				flinching = false;
			}
		}
		
		// set animation 
		if(scratching) {
			sfx.get("scratch").play();
			if(currentAction != SCRATCHING) {
				setAnimation(SCRATCHING);
			}
		}
		else if(firing) {
			if(currentAction != FIREBALL) {
				setAnimation(FIREBALL);
			}
		}
		else if(dy > 0) {
			if(gliding) {
				if(currentAction != GLIDING) {
					setAnimation(GLIDING);
				}
			}
			else{
				if(currentAction != FALLING) {
					setAnimation(FALLING);
				}
			}
		}
		else if(dy < 0){
			if(currentAction != JUMPING) {
				setAnimation(JUMPING);
			}
			
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				setAnimation(WALKING);
			}
		}
		else if(health == 0) {
			if(currentAction != DYING) {
				// setAnimation(DYING);
				
				currentAction = DYING;
				//animation.setFrames(sprites.get(DYING));
				//animation.setDelay(40);
				//width = 30;
			}
		}
		else { // idle
			if(currentAction != IDLE) {
				setAnimation(IDLE);
			}
		}
		animation.update();
		
		// set direction that the player is facing
		if(currentAction != SCRATCHING && currentAction != FIREBALL) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		
		// let player.xmap = tileMap.x and player.ymap = tileMap.y
		// update the map information saved in player
		setMapPosition(); 
		
		//draw fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).draw(g);
		}
		
		// draw flinching player
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return ;
			}
		}
		super.draw(g);
	}
	
}
