package Entity.Object.Enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import Entity.Animation;
import Entity.Object.Bullet;
import Entity.Object.Enemy;
import Entity.Object.FireBall;
import Entity.Object.Missile;
import Entity.Object.Player;
import Entity.Object.Tornado;
import TileMap.TileMap;

public class Thief extends Enemy{
	
	// dragonSpear
	private boolean dragonSpear;
	private int dragonSpearDamage;
	
	// EMP
	private boolean firing;
	private ArrayList<Missile> missiles;
	private int empDamage;
	private int fire;
	private int maxFire;
	private int fireCost;
	private boolean firstShoot;
	
	// earthbreaker
	private boolean earthbreaker;
	private int earthbreakerDamage;
	
	
	// tornados
	private ArrayList<Tornado> tornados;
	private int tornadoDamage;
	private int magic;
	private int maxMagic;
	private int tornadoCost;
	
	// walk
	private boolean walking;
	
	private int count = 0;
	private boolean remove;
	
	// animation actions
	public static final int BORN = 0;
	public static final int IDLE = 1;
	public static final int WALKING = 2;
	public static final int DRAGONSPEAR = 3;
	public static final int EMP = 4;
	public static final int EARTHBREAKER = 5;
	public static final int KB = 6;
	public static final int DYING = 7;
	
	private static final int RESPONSETIME = 3;
	
	// animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[] NUMFRAMES = {
		33, 8, 6, 15, 21, 28, 1, 16
	};
	
	private final int[] FRAMEWIDTHS = {
		286, 62, 64, 234, 132, 117, 67, 76
	};
	
	private final int[] FRAMEHEIGHTS = {
		212, 84, 84, 114, 86, 156, 81, 81
	};
	private final int[] SPRITEDELAYS = {
		80, 40, 40, 80, 115, 100, -1, 80,
	};
	
	public Thief(TileMap tm) {
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
		
		health = maxHealth = 300;
		
		dragonSpearDamage = 2;
		earthbreakerDamage = 3;
		
		// EMP
		missiles = new ArrayList<Missile>();
		fire = maxFire = fireCost = 1;
		empDamage = 2;
		firstShoot = true;
		
		// tornados
		tornados = new ArrayList<Tornado>();
		magic = maxMagic = tornadoCost = 15;
		tornadoDamage = 1;
		
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/thief.png"
				)
			);
			
			int imageY = 0;
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 8 ; i++) {
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
		
		// sprite direction
		correctSpriteDirection = false;
		
		// when the alligator start up, it is going to face left
		right = false;
		left = true;
		facingRight = false;
		
		// SFX
		AudioPlayer.load("/SFX/block.mp3", "block", AudioPlayer.SFX);
		AudioPlayer.setVolume();
	}
		
	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
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
			currentAction == DRAGONSPEAR ||
			currentAction == EMP || 
			currentAction == EARTHBREAKER ||
			currentAction == KB ||
			currentAction == DYING
		) {
			dx = 0;
		}
		
	}
	
	public void checkAttack(Player player) {
		
		
		// tornado attack
		for(int i = 0; i < tornados.size(); i++) {
			Tornado t = tornados.get(i);
			if(t.intersects(player)) {
				if(!player.getDefending()) {
					player.hit(tornadoDamage);
				}
				else {
					int defense = player.getDefense() - tornadoDamage * 1000;
					AudioPlayer.play("block", AudioPlayer.SFX);
					player.setDefense(defense);
				}
				tornados.get(i).setHit();
			}
		}
		
		// emp attack
		for(int i = 0; i < missiles.size(); i++) {
			
			Missile m = missiles.get(i);
			if(m.intersects(player)) {
				if(!player.getDefending()) {
					player.hit(empDamage);
				}
				else {
					int defense = player.getDefense() - empDamage * 1000;
					AudioPlayer.play("block", AudioPlayer.SFX);
					player.setDefense(defense);
				}
				missiles.get(i).setHit();
			}
		}
		
	}
	
	@Override
	public int getDamage() {
		
		switch(currentAction) {
			case BORN:case DYING: return 0;
			case IDLE:case WALKING:case KB: return 1;
			case DRAGONSPEAR: return dragonSpearDamage;
			case EMP: return empDamage;
			case EARTHBREAKER: return earthbreakerDamage;
			default: 
			System.out.println("Error Action Number");
				System.exit(1);
		}
		return 1;
	}
	
	@Override
	public void hit(int damage) {
		
		// Give player response time
		if(animation.getCurrentFrame() < RESPONSETIME) return;
		
		System.out.println("HP : " + health);
		super.hit(damage);
		flinching = true;
		flinchTimer = System.nanoTime();
		if( damage > 5 ) {
			// lead to change animation
			count++;
			dragonSpear = false;
			firing = false;
			firstShoot = true;
			earthbreaker = false;
			setAnimation(KB);
		}
	}
	
	private void actionCycle() {
		
		if(!animation.hasPlayedOnce()) return;
		
		// update count 
		if(count >= 130) count = 0;
		else count++;
		
		System.out.println("count : " + count);
		
		// update the cycle of actions
		if(count % 50 == 0) { earthbreaker = true; }
		else if(
			count == 45 ||
			count == 90 ||
			count == 135
		) { firing = true; }
		else if(count % 10 == 0) {
			dragonSpear = true;
		}
		else {
			setAnimation(WALKING);
		}
	}
	
	public void checkAnimation() {
		
		if(dead) {
			if(currentAction != DYING) {
				setAnimation(DYING);
			}	
		}
		else if(dragonSpear) {
			// AudioPlayer.play("scratch", AudioPlayer.SFX);
			if(currentAction != DRAGONSPEAR) {
				setAnimation(DRAGONSPEAR);
			}
		}
		else if(firing) {
			// AudioPlayer.play("fireBall", AudioPlayer.SFX);
			if(currentAction != EMP) {
				setAnimation(EMP);
			}
		}
		else if(earthbreaker) {
			if(currentAction != EARTHBREAKER) {
				setAnimation(EARTHBREAKER);
			}
		}
		
		animation.update();
	}
	
	@Override
	public void updateCollisionBox() {

		if(currentAction == DRAGONSPEAR) {
			cwidth = 200;
			if(facingRight) {
				cBox = new Rectangle2D.Double(
							((x + 20) + xmap - (cwidth / 2)),
							(y + ymap - (cheight / 2)),
							cwidth,
							cheight
						);
			}
			else {
				cBox = new Rectangle2D.Double(
						((x - 20) + xmap - (cwidth / 2)),
						(y + ymap - (cheight / 2)),
						cwidth,
						cheight
					);
			}
		}
		else if(currentAction == EARTHBREAKER) { 
			cwidth = 90;
			cheight = 150;
			cBox = new Rectangle2D.Double(
					(int)(x  + xmap - (cwidth / 2)),
					(int)(y + ymap - (cheight / 2)),
					cwidth,
					cheight
				);
		}
		// EMP and other actions
		else {
			cwidth = 60;
			cheight = 75;
			cBox = getRectangle();
		}

	}
	
	public void missileAttack() {
		
		fire += 1;
		if(fire > maxFire) fire = maxFire;
		if(
			firing && 
			currentAction == EMP &&
			animation.getCurrentFrame() == 12 &&
			firstShoot
		) {
			if(fire >= fireCost) {
				fire -= fireCost;
				Missile m = new Missile(tileMap,facingRight);
				m.setPosition(x,y);
				missiles.add(m);
				firstShoot = false;
			}
		}
		
		// update missiles
		for(int i = 0; i < missiles.size(); i++) {
			missiles.get(i).update();
			if(missiles.get(i).shouldRemove()) {
				missiles.remove(i);
				i--;
			}
		}
	}
	
	public void tornadoAttack() {
		
		magic += 1;
		if(magic > maxMagic) magic = maxMagic;
		if(
			currentAction == EARTHBREAKER &&
			animation.getCurrentFrame() < 12
		) {
			if(magic >= maxMagic) {
				
				magic -= tornadoCost;
				
				// right
				Tornado t1 = new Tornado(tileMap, true);
				t1.setPosition(x, y + 20);
				tornados.add(t1);
				
				// left
				Tornado t2 = new Tornado(tileMap, false);
				t2.setPosition(x, y + 20);
				tornados.add(t2);
			}
		}
		
		// update tornados
		for(int i = 0; i < tornados.size(); i++) {
			tornados.get(i).update();
			if(tornados.get(i).shouldRemove()) {
				tornados.remove(i);
				i--;
			}
		}
	}
	
	@Override
	public void update() {
		
		if(currentAction == BORN && animation.hasPlayedOnce()) {
			setAnimation(WALKING);
		}
		
		if(
			dragonSpear && 
			animation.hasPlayedOnce()
		) dragonSpear = false;
		
		if(
			firing && 
			animation.hasPlayedOnce()
		) {
			firing = false;
			firstShoot = true;
		}
		
		if(
			earthbreaker && 
			animation.hasPlayedOnce()
		) earthbreaker = false;
	
		// update Action
		actionCycle();
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// tornado attack
		tornadoAttack();
		
		// missile attack
		missileAttack();
		
		// check flinching
		if(flinching) {
			long elasped = 
					(System.nanoTime() - flinchTimer) / 1000000;
			if(elasped > 300) {
				flinching = false;
			}
		}
		
		checkAnimation();
		
		// the cBox update after this function,
		// if walking after earthbreaker will inherit large cBox which lead to tileMapCollision 
		// So earthbreaker still lead to turn another direction.
		// if it hits a wall, go the other direction
		if(
			right && 
			dx == 0 &&
			currentAction == WALKING
		) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if(
			left && 
			dx == 0 &&
			currentAction == WALKING
		) {
			right = true;
			left = false;
			facingRight = true;
		}
		
	}
	
	public boolean shouldRemove(){
		if(currentAction == DYING && animation.hasPlayedOnce()) return true;
		else return false;
	}
	
	public void drawHP(Graphics2D g) {
		
		// calculate center of HP bar
		double HPx = (cBox.getCenterX());
		double HPy = (cBox.getMinY());
		double barWidth = 60;
		double barHeight = 5;
		
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
		
		// while dragonSpear
		if(currentAction == DRAGONSPEAR) {
			if(facingRight) {
				HPx = cBox.getMinX() + barWidth;
			}
			else{
				HPx = cBox.getMaxX() - barWidth;
			}
		}
		
		// inner rectangle
		g.fillRect(
				(int)(HPx - barWidth / 2),
				(int)(HPy - barHeight / 2),
				(int)(barWidth * Rate) ,
				(int)barHeight
		);
		
		//  draw HP boundary
		g.setColor(Color.BLACK);
		g.drawRect(
				(int)(HPx - barWidth / 2),
				(int)(HPy - barHeight / 2),
				(int)barWidth ,
				(int)barHeight
		);
	}
	
	@Override
	public void draw(Graphics2D g) {
		
		super.draw(g);
		
		// draw HP
		drawHP(g);
		
		// draw tornados
		for(int i = 0; i < tornados.size(); i++) {
			tornados.get(i).draw(g);
		}
		
		// draw missiles
		for(int i = 0; i < missiles.size(); i++) {
			missiles.get(i).draw(g);
		}
	}
}
