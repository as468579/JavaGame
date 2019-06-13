package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Bullet;
import Entity.Enemy;
import Entity.FireBall;
import Entity.Player;
import GameState.LoadState;
import TileMap.TileMap;

public class IronCannon extends Enemy{
	
	// cannon stuff
	private int fire;
	private int maxFire;
	
	
	// shoot
	private boolean firing;
	private int fireCost;
	private int bulletDamage;
	private ArrayList<Bullet> bullets;
	
	// sprites
	private BufferedImage[] sprites;
	
	// animation actions
	private static final int ACTIVATION = 0;
	private static final int BROKEN = 1;
	
	private final int[] SPRITEDELAYS = {
		40, -1
	};
	public IronCannon(TileMap tm) {
		super(tm);
	
		width = 50;
		height = 35;
		cwidth = 50;
		cheight = 35;
		
		health = maxHealth = 10;
		fire = maxFire = fireCost = 20;
		
		damage = bulletDamage = 1;
		bullets = new ArrayList<Bullet>();
		firing = true;

		// set animation
		animation = new Animation();
		setAnimation(ACTIVATION);
		
		// when the alligator start up, it is going to face left
		up = false;
		down = false;
		left = false;
		right = false;
		falling = false;
		facingRight = true;
	}
	
	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(LoadState.IronCannon[currentAction]);
		animation.setDelay(SPRITEDELAYS[currentAction]);
	}
	
	public void setFacingRight(boolean b) {
		facingRight = b;
	}
	
	
	public void checkAttack(Player player) {
		
	
		// shoot attack
		for(int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			if(b.intersects(player) && !b.getHit()) {
				player.hit(bulletDamage);
				bullets.get(i).setHit();
			}
		}
		
	}
	
	@Override
	public void update() {
		
		// fireball attack
		fire += 1; 
		if(fire > maxFire) fire = maxFire;
		if(firing && currentAction == ACTIVATION) {
			if(fire >= fireCost) {
				fire -= fireCost;
				Bullet b = new Bullet(tileMap,facingRight);
				b.setPosition(x,y);
				bullets.add(b);
			}
		}
		
		// update bullets
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
			if(bullets.get(i).shouldRemove()) {
				bullets.remove(i);
				i--;
			}
		}
		
		// check flinching
		if(flinching) {
			long elasped = 
					(System.nanoTime() - flinchTimer) / 1000000;
			if(elasped > 400) {
				flinching = false;
			}
		}
		
		// update animation
		animation.update();
		
		if(dead) {
			if(currentAction != BROKEN) {
				setAnimation(BROKEN);
				cwidth = 0;
				cheight = 0;
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		
		// let slugger.xmap = tileMap.x and slugger.ymap = tileMap.y
		// update the map information saved in slugger
		setMapPosition();
		
		// test
		//if(notOnScreen()) return;
		
		//draw bullets
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(g);
		}
		
		if(facingRight) {
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2),  // let x be the center of image
					(int)(y + ymap - height / 2), // let y be the center of image
					null
				);
		}
		else{
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2 + width), // let x be the center of image
					(int)(y + ymap - height /2),         // let y be the center of image
					-width,
					height,
					null
				);
		}
		
		// draw for test
//		drawCollisionBox(g);
	}
}


