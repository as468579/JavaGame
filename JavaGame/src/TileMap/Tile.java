package TileMap;

import java.awt.image.BufferedImage;

public class Tile {
	private BufferedImage image;
	
	// tile types
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	
	public Tile(BufferedImage image){
		this.image = image;
	}
	
	public BufferedImage getImage() { return image; }
}
