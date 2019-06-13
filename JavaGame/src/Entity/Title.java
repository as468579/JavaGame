package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class Title {

	// image
	private BufferedImage image;
	
	// size
	private int height;
	private int width;
	
	// position
	private double x;
	private double y;
	private double dx;
	
	// status
	private boolean remove;
	private boolean done;
	private int count = 0;
	
	public Title(String s) {
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
			width = image.getWidth();
			height = image.getHeight();
			x = -width;
			done = false;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Title(BufferedImage image) {
		this.image = image;
		width = image.getWidth();
		height = image.getHeight();
		x = -width;
		done = false;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void begin() {
		dx = 10;
	}
	
	public boolean shouldRemove() { return remove;}

	public void update() {
		if(!done) {
			if(x >= (GamePanel.WIDTH - width) / 2) {
				x = (GamePanel.WIDTH - width) / 2;
				count++;
				if(count >= 50) done = true;
			}
			else {
				x += dx;
			}
		}
		else {
			x += dx;
			if(x > GamePanel.WIDTH) remove = true;
		}
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(image, (int)x, (int)y, null);
	}
}
