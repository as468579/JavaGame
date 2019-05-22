package Entity;

import TileMap.Tile;
import TileMap.TileMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import Main.GamePanel;

public abstract class MapObject {
	
	// tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;  // map's x coordinate
	protected double ymap;  // map's y coordinate
	
	// position and vector
	protected double x;  // the center of MapObject 's x coordinate on Map
	protected double y;  // the center of MapObject 's y coordinate on Map
	protected double dx;
	protected double dy;
	
	// dimension
	protected int width;  // for reading in the sprite sheets 
	protected int height; // for reading in the sprite sheets
	
	// 
	
	// collision box
	protected int cwidth; // for detect collision
	protected int cheight; // for detect collision
	
	// collision
	protected int currentRow;
	protected int currentCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	// animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	// movement attributes
 	protected double moveSpeed;
 	protected double maxSpeed;
 	protected double stopSpeed;
 	protected double fallSpeed;
 	protected double maxFallSpeed;
 	protected double jumpStart;
 	protected double stopJumpSpeed;
 	
 	// constructor
 	public MapObject(TileMap tm) {
 		tileMap = tm;
 		tileSize = tm.getTileSize();
 	}
 	
 	public boolean intersects(MapObject o) {
 		Rectangle r1 = getRectangle();
 		Rectangle r2 = o.getRectangle();
 		return r1.intersects(r2);
 	}
 	
 	public Rectangle getRectangle() {
 		
 		return new Rectangle(
 				(int)x - cwidth,
 				(int)y - cheight,
 				cwidth,
 				cheight
 		);
 	}
 	
 	public void calculateCorners(double x, double y) {
 		
 		int leftTile = (int)(x - cwidth / 2) / tileSize;
 		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
 		int topTile = (int)(y - cheight / 2) / tileSize;
 		int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
 		
 		int tl = tileMap.getType(topTile, leftTile); // the tile behind topLeft corner getType
 		int tr = tileMap.getType(topTile, rightTile); // the tile behind topRight corner getTyp
 		int bl = tileMap.getType(bottomTile, leftTile); // the tile behind bottomLeft corner getType
 		int br = tileMap.getType(bottomTile, rightTile); // the tile behind bottomRight corner getType
 		
 		topLeft = (tl == Tile.BLOCKED);
 		topRight = (tr == Tile.BLOCKED);
 		bottomLeft = (bl == Tile.BLOCKED);
 		bottomRight = (br == Tile.BLOCKED);
 		
 	}
 	
 	public void checkTileMapCollision() {
 		
 		currentCol = (int)x / tileSize;
 		currentRow = (int)y / tileSize;
 		
 		xdest = x + dx;
 		ydest = y + dy;
 		
 		xtemp = x;
 		ytemp = y;
 		
 		calculateCorners(x,ydest);
 		
 		if(dy < 0) {
 			if(topLeft || topRight) {
 				dy = 0;
 				
 				// 即使下一刻就會撞上blocked tile，目前位置仍可能和blocked tile有極小差距 
 				// 所以算出合理座標帶入
 				ytemp = currentRow * tileSize + cheight / 2;
 				
 			}
 			else {
 				ytemp += dy;
 			}
 			
 		}
 		if(dy > 0) {
 			if(bottomLeft || bottomRight) {
 				dy = 0;
 				falling = false;
 				ytemp = (currentRow + 1) * tileSize - cheight / 2;
 			}
 			else {
 				ytemp += dy;
 			}
 		}
 		
 		calculateCorners(xdest,y);
 		if(dx < 0) {
 			if(topLeft || bottomLeft) {
 				dx = 0;
 				xtemp = currentCol * tileSize + cwidth / 2;
 			}
 			else {
 				xtemp += dx;
 			}
 		}
 		if(dx > 0) {
 			if(topRight || bottomRight) {
 				dx = 0;
 				xtemp = (currentCol + 1) * tileSize - cwidth / 2;
 			}
 			else {
 				xtemp += dx;
 			}
 		}
 		
 		if(!falling) {
 			calculateCorners(x, ydest + 1);
 			if(!bottomLeft && !bottomRight) {
 				falling = true;
 			}
 		}
 	}
 	
 	public int getX() { return (int)x; }
 	public int getY() { return (int)y; }
 	public int getWidth() { return width; }
 	public int getHeight() { return height; }
 	public int getCWidth() { return cwidth; }
 	public int getCHeight() { return cheight; }
 	
 	public void setPosition(double x, double y) {
 		this.x = x;
 		this.y = y;
 	}
 	
 	public void setVector(double dx, double dy) {
 		this.dx = dx;
 		this.dy = dy;
 	}
 	
 	public void setMapPosition() {
 		xmap = tileMap.getX();
 		ymap = tileMap.getY();
 	}
 	
 	public void setUp(boolean b) { up = b; }
 	public void setDown(boolean b) { down = b; }
 	public void setLeft(boolean b) { left = b; }
 	public void setRight(boolean b) {  right = b;}
 	public void setJumping(boolean b) { jumping = b; }
 	
 	public boolean notOnScreen() {
 		return x + xmap + width < 0 || 
 			x + xmap - width > GamePanel.WIDTH ||
 			y + ymap + height < 0 ||
 			y + ymap - height > GamePanel.HEIGHT;
 	}
 	
 	public void draw(Graphics2D g) {
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
		drawCollisionBox(g);
 	}
 	
 	protected abstract void getNextPosition();
 	
 	public void update() {
 		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp,ytemp);
 	}
 	
	public void drawCollisionBox(Graphics2D g) {
		g.setColor(Color.BLUE);
		Rectangle r = getRectangle();
		r.x += ( xmap + cwidth );
		r.y += ( ymap + cheight );
		g.draw(r);
	}
	
}
