package TileMap;

import java.awt.image.BufferedImage;

public class Tile {
	private BufferedImage image;
	
	// tile types
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	
	// function types
	public static final int COIN = 2;
	public static final int VINE = 3;

	// hazard types
	public static final int WATER = 4;
	public static final int DIRTY_WATER = 5;
	public static final int LAVA = 6;
	public static final int SPIKE = 7;
	
	// transparent types
	public static final int TRANSPARENT = 8;
	
	public Tile(BufferedImage image){
		this.image = image;
	}
	
	public BufferedImage getImage() { return image; }
}
