package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class Background {

	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	public Background(String s, double ms)
	{
		try {

			image = ImageIO.read(
				getClass().getResourceAsStream(s));
			moveScale = ms;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x, double y){
		this.x = (x * moveScale) % GamePanel.WIDTH;
		//this.y = (y * moveScale) % GamePanel.HEIGHT;
		this.y = 0;
	}
	
	public void setVector(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update(){
		 x += dx;
		 y += dy;
	}
	
	public void draw(Graphics2D g){
		x = x % GamePanel.WIDTH;  // let x always >= 0
		y = y % GamePanel.HEIGHT; // let y always >= 0
		g.drawImage(
				image, 
				(int)x, 
				(int)y, 
				GamePanel.WIDTH, 
				GamePanel.HEIGHT, 
				null);
		if(x == 0) {
			// y == 0 image has already drawn
			
			// draw x == 0 y < 0
			if(y < 0) {
				g.drawImage(
						image, 
						(int)x, 
						(int)y + GamePanel.HEIGHT, 
						GamePanel.WIDTH, 
						GamePanel.HEIGHT, 
						null);
			}
			
			// draw x == 0 y > 0
			else if(y > 0) {
				g.drawImage(
						image, 
						(int)x, 
						(int)y - GamePanel.HEIGHT, 
						GamePanel.WIDTH, 
						GamePanel.HEIGHT, 
						null);
			}
		}
		else if(x < 0) {
			
			// draw x < 0, y == 0
			g.drawImage(
					image,
					(int)x + GamePanel.WIDTH,
					(int)y,
					GamePanel.WIDTH,
					GamePanel.HEIGHT,
					null
			);
			
			// draw x < 0, y < 0
			if(y < 0) {
				g.drawImage(
						image,
						(int)x,
						(int)y + GamePanel.HEIGHT,
						GamePanel.WIDTH,
						GamePanel.HEIGHT,
						null
				);
				g.drawImage(
						image,
						(int)x + GamePanel.WIDTH,
						(int)y + GamePanel.HEIGHT,
						GamePanel.WIDTH,
						GamePanel.HEIGHT,
						null
				);
			}
			
			// draw x < 0, y > 0
			else if(y > 0) {
				g.drawImage(
						image,
						(int)x,
						(int)y - GamePanel.HEIGHT,
						GamePanel.WIDTH,
						GamePanel.HEIGHT,
						null
				);
				g.drawImage(
						image,
						(int)x + GamePanel.WIDTH,
						(int)y - GamePanel.HEIGHT,
						GamePanel.WIDTH,
						GamePanel.HEIGHT,
						null
				);
			}
			
		}
		else if(x > 0){
			
			// draw x < 0, y == 0
			g.drawImage(
					image,
					(int)x - GamePanel.WIDTH,
					(int)y,
					GamePanel.WIDTH,
					GamePanel.HEIGHT,
					null
			);
			
			// draw x < 0, y > 0
			if(y > 0) {
				g.drawImage(
						image,
						(int)x - GamePanel.WIDTH,
						(int)y - GamePanel.HEIGHT,
						GamePanel.WIDTH,
						GamePanel.HEIGHT,
						null
				);
				g.drawImage(
						image,
						(int)x,
						(int)y - GamePanel.HEIGHT,
						GamePanel.WIDTH,
						GamePanel.HEIGHT,
						null
				);
			}
			
			// draw x < 0, y < 0
			else if(y < 0) {
				g.drawImage(
						image,
						(int)x - GamePanel.WIDTH,
						(int)y + GamePanel.HEIGHT,
						GamePanel.WIDTH,
						GamePanel.HEIGHT,
						null
				);
				g.drawImage(
						image,
						(int)x,
						(int)y + GamePanel.HEIGHT,
						GamePanel.WIDTH,
						GamePanel.HEIGHT,
						null
				);				
			}
		}
		
		
		
	}
	
}
	
	
	
	
	
	