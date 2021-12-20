package Entity.Object.Enemies;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import Entity.Animation;
import Entity.Object.Bullet;
import Entity.Object.Enemy;
import Entity.Object.FireBall;
import Entity.Object.Player;
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
		
		// sprite direction
		correctSpriteDirection = true;
		
		// when the alligator start up, it is going to face left
		up = false;
		down = false;
		left = false;
		right = false;
		falling = false;
		facingRight = true;
		
		// SFX
		AudioPlayer.load("/SFX/block.mp3", "block", AudioPlayer.SFX);
		AudioPlayer.setVolume();
	}
	
	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(LoadState.IronCannon[currentAction]);
		animation.setDelay(SPRITEDELAYS[currentAction]);
	}
	
	public void checkAttack(Player player) {
		
	
		// shoot attack
		for(int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			if(b.intersects(player) && !b.getHit()) {
				if(!player.getDefending()) {
					player.hit(bulletDamage);
				}
				else {
					int defense = player.getDefense() - bulletDamage * 1000;
					AudioPlayer.play("block", AudioPlayer.SFX);
					player.setDefense(defense);
				}
				bullets.get(i).setHit();
			}
		}
		
	}
	
	@Override
	public void update() {
		
		// shoot attack
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
		
		super.draw(g);
		
		//draw bullets
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(g);
		}
		
	}
}


