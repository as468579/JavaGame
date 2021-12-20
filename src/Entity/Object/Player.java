package Entity.Object;

import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import Entity.Animation;
import Entity.MapObject;
import Entity.PlayerSave;
import Entity.Object.Enemies.IronCannon;
import Entity.Object.Enemies.Thief;
import Entity.Object.Items.Coin;
import Entity.Object.Items.Shield;
import Entity.Object.Items.Treasurebox;
import Entity.Object.Items.Wings;
import TileMap.Tile;
import TileMap.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends MapObject{

	// player stuff
	private int health;
	private int maxHealth;
	private boolean flinching;
	private int money;
	private long flinchTimer;
	private long time;
	private int score;
	             
	// fireball
	private boolean firing;
	private int fire;    // fire / fireCost = number of bullets
	private int maxFire; // maximum number of bullets
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	
	// scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;
	
	// gliding
	private boolean gliding;
	
	// flying
	private boolean wings;
	
	// double jumping
	private double doubleJumpStart;
	private boolean doubleJump;
	private boolean alreadyDoubleJump;
	
	// shield
	private boolean shield;
	private boolean defending;
	private int defense;
	private int maxDefense;
	private int defenseCost;
	
	
	// knockback
	private boolean knockback;
	
	// roles
	private static final int HUMAN = 0;
	private static final int DRAGON = 1;
	private static final String[] SPRITESNAME = {
			"humansprites.png",
			"dragonsprites.png"
	} ;
	private int currentRole;

	// animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[][] NUMFRAMES = {
		{1, 7, 1, 1, 2, 2, 5, 1, 7, 1, 1},
		{2, 8, 1, 2, 4, 2, 5, 4, 1, 1, 1},
	};
	private final int[][] FRAMEWIDTHS = {
		{30, 30, 30, 30, 30, 60, 45, 40, 30, 30, 30},
		{30, 30, 30, 30, 30, 30, 60, 45, 30, 30, 30}
	};
	
	private final int[][] FRAMEHEIGHTS = {
		{30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30},
		{30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30}
	};
	private final int[][] SPRITEDELAYS = {
		{400, 40, -1, -1, 40, 250, 80, -1, 50, -1, -1},	
		{400, 40, -1, 100, 100, 100, 50, 80, -1, -1, -1}
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING =  4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	private static final int DEFENDING = 7;
	private static final int CLIMBING = 8;
	private static final int KNOCKBACK = 9;
	private static final int DYING = 10;
	
	private HashMap<String, AudioPlayer> sfx;
	public Player(TileMap tm) {
		
		super(tm);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		moveSpeed = 0.18;
		//maxSpeed = 1.6;
		maxSpeed = 3.0;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -3.8;
		stopJumpSpeed = 0.3;
		doubleJumpStart = -3.0;
		money = 0;
		
		facingRight = true;
		
		health = maxHealth = 5;
		
		// shield
		defense = maxDefense = 10000;
		
		// fireBalls
		fire = maxFire = 2500;
		fireCost = 200;
		fireBallDamage = 5;
		fireBalls = new ArrayList<FireBall>();
		
		scratchDamage = 8;
		scratchRange = 30;
		
		wings = false;
		
		// set current role
		String spritesName;
		currentRole = PlayerSave.getCurrentRole();
		spritesName = SPRITESNAME[currentRole];
		
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/" + spritesName
				)
			);
			
			int imageY = 0;
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 11 ; i++) {
				BufferedImage[] bi = new BufferedImage[NUMFRAMES[currentRole][i]];
				for(int j = 0; j < NUMFRAMES[currentRole][i]; j++) {
					bi[j] = spritesheet.getSubimage(
							j * FRAMEWIDTHS[currentRole][i],
							imageY,
							FRAMEWIDTHS[currentRole][i],
							FRAMEHEIGHTS[currentRole][i]
					);
				}
				sprites.add(bi);
				imageY += FRAMEHEIGHTS[currentRole][i];
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		// initialize animation
		animation = new Animation();
		setAnimation(IDLE);
		
		// sprite direction
		correctSpriteDirection = true;
		
		// sfx
		AudioPlayer.load("/SFX/jump.mp3", "jump", AudioPlayer.SFX);
		AudioPlayer.load("/SFX/scratch.mp3", "scratch", AudioPlayer.SFX);
		AudioPlayer.load("/SFX/fireBall.mp3", "fireBall", AudioPlayer.SFX);
		AudioPlayer.load("/SFX/scream.mp3", "scream", AudioPlayer.SFX);		 
		AudioPlayer.load("/SFX/eatMoney.mp3", "money", AudioPlayer.SFX);
		AudioPlayer.setVolume();
	}
	
	
	
	// health
	public void setHealth(int h) { health = h; }
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	
	// fire
	public void setFiring() { 
		if(knockback) return;
 		if(defending) return;
		if(!scratching && !defending) {
			firing = true;
		}
	}
	public int getFire() { return fire; }
	public int getMaxFire() { return maxFire; }
	
	// time
	public void setTime(int t) { time = t; }
	public long getTime() { return time; }
	
	
	// score
	public void increaseScore(int score) { this.score += score; }
	public int getScore() { return score; }
	
	// money
	public int getMoney() { return money; }
	public void setMoney(int money) { this.money = money; }
	
	public void setScratching() { 
		if(knockback) return;
 		if(defending) return;
		if(!firing && !defending) {
			scratching = true;
		}
	}
	
	public void setLeft(boolean b) {
		// cannot move left while climbing
		if(!climbing)
		{
			left = b;
		}
	}
	
	public void setRight(boolean b) {
		// cannot move right while climbing
		if(!climbing) {
			right = b;
		}
	}
	
	public void setUp(boolean b) {
		if(hasWings() && gliding) setFlyingUp(b);
		else setClimbUp(b);
	}
	
	public void setFlyingUp(boolean b) {
		if(b) {
			scratching = firing = false;
		}
		up = b;
	}
	
	public void setClimbUp(boolean b){
		
		// set climbing when tile is climbable
		if(isClimbable()) {
			if(b == true) {
				
				// key releasing doesn't cancel climbing
				climbing = b;
				
				// cancel original movement
				left = right = jumping = falling
				 = scratching = firing = gliding = false;
			}
			up = b;
		}
	}
	
	public void setDown(boolean b) {
		if(hasWings() && gliding) setFlyingDown(b);
		else setClimbDown(b);
	}
	
	public void setFlyingDown(boolean b) {
		if(b) {
			scratching = firing = false;
		}
		down = b;
	}
 	public void setClimbDown(boolean b) {
		
		int x = (int)this.x;
		int y = (int)this.y;
		
		// set climbing when tile is climbable
		if(isClimbable()) {
			if(b == true) {
				
				// key releasing doesn't cancel climbing
				climbing = b;
				
				// cancel original movement
				left = right = jumping = falling
				 = scratching = firing = gliding = false; 
			}
			down = b;
		}
	}
 	
 	// defense
 	
 	public boolean hasShield() { return shield; }
 	public void setShield(boolean b) { shield = b; }
 	
 	public void setDefending(boolean b) {
 		if(!shield) return;
 		if(b) {
	 		if(knockback) return;
	 		if(!firing && !scratching) {
	 			defending = b;
	 		}
 		}
 		else {
 			defending = b;
 		}
 	}
 	
 	public boolean getDefending() {
 		return defending;
 	}
 	
 	public void setDefense(int s) {
 		if(s > maxDefense) defense = maxDefense;
 		else if(s <= 0 ) {
 			defense = 0;
 			defending = false;
 		}
 		else defense = s;
 	}
 	
 	public int getDefense() { return defense; }
 	
 	public int getMaxDefense() { return maxDefense; }
	
	private boolean isClimbable() {
		int row = (int)(x / tileSize);
		int col = (int)(y / tileSize);
		int type = tileMap.getType(col, row);
		if(type == Tile.VINE) { return true; }
		else return false;
	}
	
	public void setJumping(boolean b) {
		if(b && !jumping && falling && !alreadyDoubleJump) {
			doubleJump = true;
		}
		jumping = b;
		// jumping will cancel climbing
		climbing = false;
	}
	
	public void setGliding(boolean b) {
		gliding = b;
	}
	
	public void setWings(boolean wings) {
		this.wings = wings;
	}
	
	public boolean isUp() {
		return up;
	}
	
	public void checkTouch(ArrayList<Item> items) {
		
		// loop items
		for(int i = 0; i < items.size(); i++) {
			
			Item it = items.get(i);
			if(intersects(it)) {
				if(it instanceof Coin && !it.isTouched()) {
					Coin c = (Coin)it;
	 				if(
	 	 					!AudioPlayer.isRunning("money", AudioPlayer.SFX)
	 	 				) {
	 	 					AudioPlayer.play("money", AudioPlayer.SFX);
	 	 			}
					setMoney(getMoney() + c.getMoney());
				}
				else if(it instanceof Treasurebox && !it.isTouched()) {
					Treasurebox t = (Treasurebox)it;
	 				if(
	 	 					!AudioPlayer.isRunning("money", AudioPlayer.SFX)
	 	 				) {
	 	 					AudioPlayer.play("money", AudioPlayer.SFX);
	 	 			}
					setMoney(getMoney() + t.getMoney());
				}
				else if(it instanceof Shield && !it.isTouched()) {
					setShield(true);
				}
				else if(it instanceof Wings && !it.isTouched()) {
					setWings(true);
				}
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
				
				Rectangle scratchBox;
				
				if(facingRight) {
					scratchBox = new Rectangle(
									(int)(x + xmap + cwidth / 2),
									(int)(y + ymap - cheight / 2),
									scratchRange,
									cheight
								);
				}
				else {
					scratchBox = new Rectangle(
									(int)(x + xmap - cwidth / 2) - scratchRange,
									(int)(y + ymap - cheight / 2),
									scratchRange,
									cheight
								);
				}
				if(e.intersects(scratchBox)){
					e.hit(scratchDamage);
				}
			}

			// fireball attack
			for(int j = 0; j < fireBalls.size(); j++) {
				FireBall f = fireBalls.get(j);
				if(f.intersects(e) && !f.getHit()) {
					e.hit(fireBallDamage);
					fireBalls.get(j).setHit();
				}
			}
			
			// chcek enemy collision
			if(intersects(e) && !e.isDead() && !defending) {
				hit(e.getDamage());
			}
			else if(defending && intersects(e)) {
				setDefense(defense - e.getDamage() * 100);
			}
			
			if(e instanceof IronCannon) {
				IronCannon ic = (IronCannon) e;
				ic.checkAttack(this);
			}
			
			if(e instanceof Thief) {
				Thief t = (Thief) e;
				t.checkAttack(this);
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
		fire = maxFire;
		facingRight = true;
		currentAction = IDLE;
		stop();
	}
	
	public void drawScratchBox(Graphics2D g) {
		
		// right scratch box
		Rectangle rsb = new Rectangle(
										(int)(x + xmap + cwidth / 2),
										(int)(y + ymap - cheight / 2),
										scratchRange,
										cheight
									);
		// left scratch box
		Rectangle lsb = new Rectangle(
										(int)(x + xmap - cwidth / 2) - scratchRange,
										(int)(y + ymap - cheight / 2),
										scratchRange,
										cheight
									);
		g.setColor(Color.RED);
		g.draw(rsb);
		g.draw(lsb);
	}
	
	public void stop() {
		left = right = up = down = flinching = climbing = 
			jumping = scratching = firing = gliding =
			defending = knockback =false;
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
			
		if(climbing) {
			
			if(up) {
				dy -= moveSpeed;
				
				if(dy > 0) dy = 0;
				if(dy < -maxSpeed) {
					dy = -maxSpeed;
				}
			}
			else if(down) {
				dy += moveSpeed;
				if(dy < 0) dy = 0;
				if(dy > maxSpeed) {
					dy = maxSpeed;
				}
			}
			else {
				if(dy > 0) {
					dy -= stopSpeed;
					if(dy < 0) {
						dy = 0;
					}
				}
				else if (dy < 0) {
					dy += stopSpeed;
					if(dy > 0) {
						dy = 0;
					}
				}
			}
		}
		
		if(gliding && hasWings()) {
			
			if(up) {
				dy -= moveSpeed;
				if(dy > 0) dy = 0;
				if(dy < -maxSpeed) {
					dy = -maxSpeed;
				}
			}
			else if(down) {
				dy += moveSpeed;
				if(dy < 0) dy = 0;
				if(dy > maxSpeed) {
					dy = maxSpeed;
				}
			}
		}
		
		// cannot move while attacking, climbing, dying except in air
		if(
			(
				currentAction == SCRATCHING ||
				currentAction == FIREBALL ||
				currentAction == CLIMBING ||
				currentAction == DYING
			) && !(jumping || falling)
		) {
			dx = 0;
		}
		
		// jumping
		if(jumping && !falling) {
			//sfx.get("jump").play();
			AudioPlayer.play("jump", AudioPlayer.SFX);
			dy  = jumpStart;
			falling = true;
		}
		
		if(doubleJump) {
			dy = doubleJumpStart;
			alreadyDoubleJump = true;
			doubleJump = false;
		}
		
		if(!falling) alreadyDoubleJump = false;
		
		// falling
		if(falling) {
			if(dy > 0 && gliding && !hasWings()) {
				dy += fallSpeed * 0.1;
			}
			else dy += fallSpeed;
			
			if(dy > 0) jumping = false; 
			
			// the longer press the jump btn then jump higher
			// press jump btn jumping = true, release jump btn or dy > 0 jumping = false
			if(dy < 0 && !jumping && !(gliding && hasWings())) dy += stopJumpSpeed;
			
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(SPRITEDELAYS[currentRole][currentAction]);
		width = FRAMEWIDTHS[currentRole][currentAction];
		height = FRAMEHEIGHTS[currentRole][currentAction];
	}
	
	public void setDead() {
		AudioPlayer.play("scream", AudioPlayer.SFX);
		health = 0;
		stop();
	}
	
	
	@Override
 	public void calculateEdges(double dx, double dy) {
 		
 		super.calculateEdges(dx, dy);
 		
 		int leftTile = (int)((cBox.getMinX() - xmap + dx)) / tileSize;
 		int rightTile = (int)(((cBox.getMaxX() - xmap + dx) - 1))/ tileSize;
 		int topTile = (int)((cBox.getMinY() - ymap + dy)) / tileSize;
 		int bottomTile = (int)(((cBox.getMaxY() - ymap + dy) - 2)) / tileSize;
 		
 		for(int i = leftTile; i <= rightTile; i++) {
 			
 	 		// check top edge
 			int type = tileMap.getType(topTile, i) ;

 			// if encounter coin block
 			if(type == Tile.COIN)  {
 				setMoney(money + 1);
 				tileMap.setFunctionBlockMap(topTile, i, 0);
 			}
 			
 			// if encounter hazard block
 			if(
 				type == Tile.WATER ||
 				type == Tile.DIRTY_WATER ||
 				type == Tile.LAVA ||
 				type == Tile.SPIKE
 			) {

 				hit(1);
 			}

 			
 			// check bottom edge
 			type = tileMap.getType(bottomTile, i) ;

 			// if encounter coin block
 			if(type == Tile.COIN)  {
 				setMoney(money + 1);
 				tileMap.setFunctionBlockMap(bottomTile, i, 0);
 				if(
 					!AudioPlayer.isRunning("money", AudioPlayer.SFX)
 				) {
 					AudioPlayer.play("money", AudioPlayer.SFX);
 				}
 			}
 			
 			// if encounter hazard block
 			if(
 				type == Tile.WATER ||
 				type == Tile.DIRTY_WATER ||
 				type == Tile.LAVA ||
 				type == Tile.SPIKE
 			) {
 				hit(1);
 			}
 			
 			// if encounter transparent block
 			if (tileMap.getType(topTile, i) == Tile.TRANSPARENT || tileMap.getType(bottomTile, i) == Tile.TRANSPARENT) 
 				tileMap.setTransparent(getX(), getY());
 			else tileMap.setTransparent(-1, -1);
 		}
 		
 		for(int i = topTile; i <= bottomTile; i++) {
 			
 	 		// check left edge
 			int type = tileMap.getType(i, leftTile);
 			
 			// if encounter coin block
 			if(type == Tile.COIN)  {
 				setMoney(money + 1);
 				tileMap.setFunctionBlockMap(i, leftTile, 0);
 			}
 			
 			// if encounter hazard block
 			if(
 				type == Tile.WATER ||
 				type == Tile.DIRTY_WATER ||
 				type == Tile.LAVA ||
 				type == Tile.SPIKE
 			) {

 				hit(1);
 			}
 			
 			
 			// check right edge
 			type = tileMap.getType(i, rightTile);
 			
 			// if encounter coin block
 			if(type == Tile.COIN)  {
 				setMoney(money + 1);
 				tileMap.setFunctionBlockMap(i, rightTile, 0);
 			}
 			
 			// if encounter hazard block
 			if(
 				type == Tile.WATER ||
 				type == Tile.DIRTY_WATER ||
 				type == Tile.LAVA ||
 				type == Tile.SPIKE
 			) {

 				hit(1);
 			}
 		}		
 	}
	
	private void fireBallAttack() {
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
		
		if((defense / 100) <= 0 ) defending = false;
		
		// update defense
		setDefense(defense + 5);
		
		// fireBall attack
		fireBallAttack();
		
		
		// check done flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1500) {
				flinching = false;
			}
		}

		// check climbing
		if(!isClimbable()) { climbing = false; }
		
		// set animation 
		if(scratching) {
			//sfx.get("scratch").play();
			AudioPlayer.play("scratch", AudioPlayer.SFX);
			if(currentAction != SCRATCHING) {
				setAnimation(SCRATCHING);
			}
		}
		else if(firing) {
			// sfx.get("fireBall").play();
			AudioPlayer.play("fireBall", AudioPlayer.SFX);
			if(currentAction != FIREBALL) {
				setAnimation(FIREBALL);
			}
		}
		else if(defending) {
			if(currentAction != DEFENDING) {
				setAnimation(DEFENDING);
			}
		}
		else if(hasWings() && gliding && falling) {
			if(currentAction != GLIDING) {
				setAnimation(GLIDING);
			}
		}
		else if(climbing) {
			if(currentAction != CLIMBING) {
				setAnimation(CLIMBING);
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
				setAnimation(DYING);
			}
		}
		else { // idle
			if(currentAction != IDLE) {
				setAnimation(IDLE);
			}
		}
		if(!(currentAction == CLIMBING && dy == 0)) {
			animation.update();
		}
		
		// set direction that the player is facing
		if(
			currentAction != SCRATCHING && 
			currentAction != FIREBALL 
		) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		
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
		
		// draw scratch box
		drawScratchBox(g);
	}
	
	public boolean hasWings() { return wings; }
}
