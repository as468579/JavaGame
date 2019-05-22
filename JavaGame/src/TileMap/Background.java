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
		this.y = (y * moveScale) % GamePanel.HEIGHT;
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
		if(x < GamePanel.WIDTH * -1)
		{
			x = 0;
		}
		
		if(y < GamePanel.HEIGHT * -1)
		{
			y = 0; 
		}
		
		g.drawImage(
				image, 
				(int)x, 
				(int)y, 
				GamePanel.WIDTH, 
				GamePanel.HEIGHT, 
				null);
		
		if(x < 0){
			g.drawImage(
					image,
					(int)x + GamePanel.WIDTH,
					(int)y,
					GamePanel.WIDTH,
					GamePanel.HEIGHT,
					null
			);
			
		}
		if(x > 0){
			g.drawImage(
					image,
					(int)x - GamePanel.WIDTH,
					(int)y,
					GamePanel.WIDTH,
					GamePanel.HEIGHT,
					null
			);
		}

		
		
		
	}
	
}
	
	
	
	
	
	