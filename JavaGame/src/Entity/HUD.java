package Entity;

import java.awt.*; 
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Main.GamePanel;

public class HUD {

		private Player player;
		private BufferedImage imageHeart;
		private BufferedImage imageFire;
		private BufferedImage imageMoney;
		private Font font;
		
		public HUD(Player p) {
			player = p;
			try {
				imageHeart = ImageIO.read(
						getClass().getResourceAsStream(
							"/HUD/heart.png"
						)
					);
				imageFire = ImageIO.read(
						getClass().getResourceAsStream(
							"/HUD/fire.png"
						)
					);
				imageMoney = ImageIO.read(
						getClass().getResource(
							"/HUD/money.png"
						)
					);
				
				font = new Font("Arial", Font.BOLD, 14);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		public void draw(Graphics2D g) {
			
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, GamePanel.WIDTH - 1, (int)(GamePanel.HEIGHT / 5));
			
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			g.setColor(Color.DARK_GRAY);
			g.drawRect(0, 0, GamePanel.WIDTH - 1, (int)(GamePanel.HEIGHT / 5));
			
			// draw player health
			for(int i = 0 ; i < player.getHealth() ; i++) {
					g.drawImage(imageHeart, i*20 , 7, 20 , 15 , null);
			}
			
			g.setFont(font);
			g.setColor(Color.BLACK);
			
			// draw fire
			g.drawImage(imageFire, 0 , 22, 22 , 20 , null);
			g.drawString(
				": " + player.getFire() / 100 + "/" + player.getMaxFire() / 100,
					24,
					38
			);
			
			// draw money
			g.drawImage(imageMoney ,230 ,3 , null);
			g.drawString(": " + player.getMoney(), 255, 20);

		}
		
}
