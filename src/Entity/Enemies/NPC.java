package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;

public class NPC extends Enemy {

	// scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;

	// animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[] NUMFRAMES = { 2, 8, 1, 2, 4, 2, 5 };
	private final int[] FRAMEWIDTHS = { 30, 30, 30, 30, 30, 30, 60 };

	private final int[] FRAMEHEIGHTS = { 30, 30, 30, 30, 30, 30, 30 };
	private final int[] SPRITEDELAYS = { 400, 40, -1, 100, 100, 100, 50 };

	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	private static final int DYING = 7;
	private static final int CLIMBING = 8;

	private HashMap<String, AudioPlayer> sfx;

	public NPC(TileMap tm) {
		super(tm);

		moveSpeed = 0.7;
		maxSpeed = 5.0;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		stopSpeed = 0.4;

		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;

		facingRight = true;

		health = maxHealth = 999;
		damage = 0;

		scratchDamage = 0;
		scratchRange = 40;

		// load sprites
		try {

			BufferedImage spritesheet = ImageIO
					.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.gif"));

			sprites = new ArrayList<BufferedImage[]>();
			
			int imageY = 0;
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

		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		setAnimation(IDLE);

		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
		sfx.put("scratch", new AudioPlayer("/SFX/scratch.mp3"));

		facingRight = true;

	}

	public void setScratching(boolean scratch) {
		this.scratching = scratch;
	}

	public void setLeft(boolean b) {
		left = b;
	}

	public void setRight(boolean b) {
		right = b;
	}
	
	public void stop() {
		left = right = up = down = flinching = climbing = 
			jumping = scratching  = false;
	}

	private void getNextPosition() {
		
		//TODO
		System.out.println("L: "+left+"R: "+right+" dx: "+dx);	

		// only move left or right
		// movement
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
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

		// falling
		if (falling) {
			dy += fallSpeed;
		}
	}

	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(SPRITEDELAYS[currentAction]);
		width = FRAMEWIDTHS[currentAction];
		height = FRAMEHEIGHTS[currentAction];
	}

	@Override
	public void update() {

		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		// check flinching
		if (flinching) {
			long elasped = (System.nanoTime() - flinchTimer) / 1000000;
			if (elasped > 400) {
				flinching = false;
			}
		}
		
		// check attack has stopped
		if(currentAction == SCRATCHING) {
			if(animation.hasPlayedOnce()) scratching = false;
		}

		// set animation
		if (scratching) {
			sfx.get("scratch").play();
			if (currentAction != SCRATCHING) {
				setAnimation(SCRATCHING);
			}
		} else if (dy < 0) {
			if (currentAction != JUMPING) {
				setAnimation(JUMPING);
			}

		} else if (left || right) {
			if (currentAction != WALKING) {
				setAnimation(WALKING);
			}
		} else if (health == 0) {
			if (currentAction != DYING) {
				// setAnimation(DYING);

				currentAction = DYING;
				// animation.setFrames(sprites.get(DYING));
				// animation.setDelay(40);
				// width = 30;
			}
		} else { // idle
			if (currentAction != IDLE) {
				setAnimation(IDLE);
			}
		}

		// update animation
		animation.update();

		// set direction that the NPC is facing
		if (currentAction != SCRATCHING && currentAction != FIREBALL) {
			if (right)
				facingRight = true;
			if (left)
				facingRight = false;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		
		// let slugger.xmap = tileMap.x and slugger.ymap = tileMap.y
		// update the map information saved in slugger
		setMapPosition();

		// test
		// if(notOnScreen()) return;

		// draw image and collision box
		super.draw(g);
	}
}
