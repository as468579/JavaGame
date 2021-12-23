package Entity;

import TileMap.Tile;
import TileMap.TileMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import Entity.Object.FireBall;
import Entity.Object.Player;
import Entity.Object.Enemies.SkullWarrior;
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
	
	
	// collision box
	// cheight and cwidth must be even, or it will embedded in the floors.
	protected int cwidth; // for detect collision
	protected int cheight; // for detect collision
	protected Rectangle2D cBox;
	
	// collision
	protected int currentRow;
	protected int currentCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected double dxtemp;
	protected double dytemp;
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
	protected boolean climbing;
	
	// movement attributes
 	protected double moveSpeed;
 	protected double maxSpeed;
 	protected double stopSpeed;
 	protected double fallSpeed;
 	protected double maxFallSpeed;
 	protected double jumpStart;
 	protected double stopJumpSpeed;
 	
 	// four corners
 	protected boolean topCollided;
 	protected boolean bottomCollided;
 	protected boolean leftCollided;
 	protected boolean rightCollided;
 	
 	// sprite direction
 	protected boolean correctSpriteDirection;
 	
 	// constructor
 	public MapObject(TileMap tm) {
 		tileMap = tm;
 		tileSize = tm.getTileSize();
 		
		// collision box
		cBox = getRectangle();
 	}
 	
 	public boolean intersects(MapObject o) {
 		Rectangle2D c2 = o.getCBox();
 		return cBox.intersects(c2);
 	}
 	
 	public boolean intersects(Rectangle c2) {
 		return cBox.intersects(c2);
 	}
 	
 	public Rectangle2D getCBox() {
 		return cBox;
 	}
 	
 	public Rectangle2D getRectangle() {
 		// Retangle2D:
 		//   minX: x
 		//   maxX: x + width
 		//   minY: y
 		//   maxY: y + height
 		return new Rectangle2D.Double(
 				(x - (cwidth / 2.0)),  // The X coordinate of the upper-left corner of the newly constructed Rectangle2D
 				(y - (cheight / 2.0)), // The Y coordinate of the upper-left corner of the newly constructed Rectangle2D
 				cwidth,
 				cheight
 		);
 	}
 	
 	public void calculateEdges(double dx, double dy) {
 		
 		/*
 		int leftTile = (int)(x - cwidth / 2) / tileSize;
 		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
 		int topTile = (int)(y - cheight / 2) / tileSize;
 		int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
 		
 		System.out.println("dx : " + dx + ", dy : " + dy);
 		*/

 		// cBox.getMaxX = cBox.x + width;
 		int leftTile = (int)((cBox.getMinX() + dx)) / tileSize;
 		int rightTile = (int)(((cBox.getMaxX() + dx) - 1))/ tileSize;
 		int topTile = (int)((cBox.getMinY()  + dy)) / tileSize;
 		double a = (cBox.getMinX());
 		double c = (cBox.getMinY());
 		double d = (cBox.getMaxX());
 		double e = (cBox.getMaxY());
 		int b = (int)(((cBox.getMaxY() + dy) - 1));
 		int bottomTile = (int)(((cBox.getMaxY() + dy) - 1)) / tileSize;
 		
 		if(this instanceof SkullWarrior) {
 			System.out.println("x: " + x + ", y: " + y + ", dx: " + dx + ", dy: " + dy + ", falling: " + falling + ", climbing: " + climbing + ",  bottomTile: " + bottomTile);
 		}
 	
 		// testing 	
 		// System.out.println("");
 		// System.out.println("dx : " + dx + ", dy : " + dy);
 		// System.out.println("x : " + x + ", y : " + y);
 		// System.out.println("cBox.x : " + cBox.getMinX());
 		// System.out.println("xmap : " + xmap);
 		// System.out.println("dx : " + dx);
 		// System.out.println(((cBox.getMinX() - xmap + dx) + cwidth - 1) + ", " + ((cBox.getMinY() - ymap + dy) + cheight - 1));
 		// System.out.println("leftTile :" + leftTile);
 		// System.out.println("rightTile :" + rightTile);
 		// System.out.println("topTile :" + topTile);
 		// System.out.println("bottomTile :" + bottomTile);
 		
 		// initialize
 		topCollided = false;
 		bottomCollided = false;
 		leftCollided = false;
 		rightCollided = false;
 		
 		
 		for(int i = leftTile; i <= rightTile; i++) {
 			
 	 		// check top edge
 			if (tileMap.getType(topTile, i) == Tile.BLOCKED)  {
 				System.out.println(topTile + ", " + i + ": TopCollided");
 				topCollided = true;
 			}
 			
 			// check bottom edge
 			if(tileMap.getType(bottomTile, i) == Tile.BLOCKED) {
 				System.out.println(bottomTile + ", " + i + ": BottomCollided");
 				bottomCollided = true;
 			}
 		}
 		
 		for(int i = topTile; i <= bottomTile; i++) {
 			
 			if(this instanceof Player) {
 				System.out.println(i + ", " + leftTile + ", tileMap.getType(i, leftTile): " + tileMap.getType(i, leftTile));
 				System.out.println(i + ", " + rightTile + ", tileMap.getType(i, rightTile): " + tileMap.getType(i, rightTile));
 			}
 			
 	 		// check left edge
 			if (tileMap.getType(i, leftTile) == Tile.BLOCKED)  {
 				System.out.println(i + ", " + leftTile + ": LeftCollided");
 				leftCollided = true;
 			 }
 			
 			// check right edge
 			if(tileMap.getType(i, rightTile) == Tile.BLOCKED) {
 				System.out.println(i + ", " + rightTile + ": RightCollided");
 				rightCollided = true;
 			}
 		}
 		
 		/*
 		System.out.println("Top : " + topCollided);
 		System.out.println("Bottom : " + bottomCollided);
 		System.out.println("Left : " + leftCollided);
 		System.out.println("Right : " + rightCollided);
 		if(tileMap.getType(leftTile, bottomTile) == Tile.BLOCKED) {
 			
 		}
 		*/
 	}
 	
 	/*
 	public void checkTileMapCollision() {
 		
 		currentCol = (int)x / tileSize;
 		currentRow = (int)y / tileSize;
 	
 		xdest = x + dx;
 		ydest = y + dy;
 		
 		xtemp = x;
 		ytemp = y;
 		
 		System.out.println("calculateEdges(x,ydest)");
 		calculateEdges(x,ydest);
 
 		if(dy < 0) {
 			if(topCollided) {
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
 			if(bottomCollided) {
 				dy = 0;
 				falling = false;
 				climbing = false;
 			    // if cheight /2 > tileSize , the MapObject will bounce 
 				ytemp = (currentRow + 1) * tileSize - cheight / 2;
 			}
 			else {
 				ytemp += dy;
 			}
 		}
 		
 		System.out.println("calculateEdges(xdest,y)");
 		calculateEdges(xdest,y);
 		if(dx < 0) {
 			if(leftCollided) {
 				dx = 0;
 				xtemp = currentCol * tileSize + cwidth / 2;
 			}
 			else {
 				xtemp += dx;
 			}
 		}
 		if(dx > 0) {
 			if(rightCollided) {
 				dx = 0;
 				xtemp = (currentCol + 1) * tileSize - cwidth / 2;
 			}
 			else {
 				xtemp += dx;
 			}
 		}
 		
 		if(!falling && !climbing) {
 			System.out.println("calculateEdges(x, ydest + 1)");
 			calculateEdges(x, ydest + 1);
 			if(!bottomCollided) {
 				falling = true;
 			}
 		}
 	}
 	*/
 	public void checkTileMapCollision() {

 		if(this instanceof SkullWarrior) {
 	 		System.out.println("checkTileMapCollision");
 		}

 		currentCol = (int)x / tileSize;
 		currentRow = (int)y / tileSize;
 	
 		xdest = x + dx;
 		ydest = y + dy;
 		
 		dxtemp = dx;
 		dytemp = dy;
 		
 		xtemp = x;
 		ytemp = y;
 		
 		//System.out.println("dx : " + dx + ", dy : " + dy);
 		calculateEdges(0, dy);
 
 		if(dy < 0) {
 			if(topCollided) {
 				dytemp = 0;
 				
 				// 即使下一刻就會撞上blocked tile，目前位置仍可能和blocked tile有極小差距 
 				// 所以算出合理座標帶入
 				ytemp = currentRow * tileSize + cheight / 2.0;
 				
 			}
 			else {
 				ytemp += dy;
 			}
 			
 		}
 		if(dy > 0) {
 			if(bottomCollided) {
 					System.out.println("bottomCollided");
 	 				System.out.println("y = " + y + ", (currentRow) * tileSize - cheight = " + ((currentRow) * tileSize - cheight));
 					dytemp = 0;
 	 				falling = false;
 	 				climbing = false;
 	 			    
 	 				currentRow = (int)(((cBox.getMaxY() + dy) - 1)) / tileSize;
 	 				ytemp = (currentRow) * tileSize - cheight / 2.0; 	
 			}
 			else {
 				ytemp += dy;
 			}
 		}

 		calculateEdges(dx, 0);
 		if(dx < 0) {
 			if(leftCollided) {
 				dxtemp = 0;
 				currentCol = (int)((cBox.getMinX() + dx)) / tileSize;
 				xtemp = (currentCol + 1) * tileSize +  cwidth / 2.0;
 			}
 			else {
 				xtemp += dx;
 			}
 		}
 		if(dx > 0) {
 			if(rightCollided) {
 				dxtemp = 0;
 				currentCol = (int)((cBox.getMaxX() + dx) - 1) / tileSize;
 				xtemp = (currentCol) * tileSize - cwidth / 2.0;
 			}
 			else {
 				xtemp += dx;
 			}
 		}
 		
 		

 		if(!falling && !climbing) {
 			calculateEdges(0, dy + 1);
 			if(!bottomCollided) {
 				falling = true;
 			}
 		}
 		
 		dx = dxtemp;
 		dy = dytemp;
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
 		this.cBox = getRectangle();
 	}
 	
 	public void setVector(double dx, double dy) {
 		this.dx = dx;
 		this.dy = dy;
 	}
 	
 	public void setMapPosition() {
 		xmap = tileMap.getX();
 		ymap = tileMap.getY();
 	}
 	
	public void setFacingRight(boolean b) {
		facingRight = b;
	}
	
 	public void setUp(boolean b) { up = b; }
 	public void setDown(boolean b) { down = b; }
 	public void setLeft(boolean b) { left = b; }
 	public void setRight(boolean b) {  right = b;}
 	public void setJumping(boolean b) { 
 		jumping = b;
 	}
 	
 	public boolean notOnScreen() {
 		return x + xmap + (width / 2) < 0 || 
 			x + xmap - (width / 2) > GamePanel.WIDTH ||
 			y + ymap + (height / 2) < 0 ||
 			y + ymap - (height / 2) > GamePanel.HEIGHT;
 	}
 	
 	public void draw(Graphics2D g) {
 		
		// let MapObject.xmap = tileMap.x and MapObject.ymap = tileMap.y
		// update the map information saved in MapObject
		setMapPosition();
 		
		// update collisionBox cuz xmap and ymap is changed
		updateCollisionBox();
 		
		
		// if(notOnScreen) return;
		
 		if(correctSpriteDirection) {
			if(facingRight) {
				// The image is drawn with its top-left corner at (x, y) in this graphics context's coordinate space
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
 		}else {
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
 		}
		
		// draw for test
		 drawCollisionBox(g);
 	}
 	
 	
 	public void updateCollisionBox() {
 		cBox = getRectangle();
 	}
 	
	public void drawCollisionBox(Graphics2D g) {
		
		Rectangle2D r1 = new Rectangle2D.Double(
 				(x + xmap - (cwidth / 2.0)),
 				(y + ymap - (cheight / 2.0)),
 				cwidth,
 				cheight
 		);
		g.setColor(Color.BLUE);
		
		g.draw(r1);
	}
	
}
