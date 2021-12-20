package Entity;

import java.awt.*; 
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Entity.Object.Player;
import Main.GamePanel;

public class HUD {

		private Player player;
		private BufferedImage heartIcon;
		private BufferedImage fireIcon;
		private BufferedImage moneyIcon;
		private BufferedImage shieldIcon;
		private BufferedImage wingsIcon;
		private Font font;
		
		public HUD(Player p) {
			player = p;
			try {
				heartIcon = ImageIO.read(
						getClass().getResourceAsStream(
							"/HUD/heart.png"
						)
					);
				fireIcon = ImageIO.read(
						getClass().getResourceAsStream(
							"/HUD/fire.png"
						)
					);
				moneyIcon = ImageIO.read(
						getClass().getResourceAsStream(
							"/HUD/money.png"
						)
					);
				shieldIcon = ImageIO.read(
						getClass().getResourceAsStream(
							"/HUD/shield.png"
						)
					);
				wingsIcon = ImageIO.read(
						getClass().getResourceAsStream(
							"/HUD/wings.png"
						)
					);
				font = new Font("Arial", Font.BOLD, 14);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		public void draw(Graphics2D g) {
			
			// draw bg
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, GamePanel.WIDTH - 1, (int)(GamePanel.HEIGHT / 5));
			
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			g.setColor(Color.DARK_GRAY);
			g.drawRect(0, 0, GamePanel.WIDTH - 1, (int)(GamePanel.HEIGHT / 5));
			
			// draw player health
			for(int i = 0 ; i < player.getHealth() ; i++) {
					g.drawImage(heartIcon, i*20 , 7, 20 , 15 , null);
			}
			
			g.setFont(font);
			g.setColor(Color.BLACK);
			
			// draw fire
			g.drawImage(fireIcon, 0 , 22, 22 , 20 , null);
			g.drawString(
				": " + player.getFire() / 100 + "/" + player.getMaxFire() / 100,
					24,
					38
			);
			
			// draw shield 
			if(player.hasShield()) {
				g.drawImage(shieldIcon, 70, 22, 20 , 20 , null);
				g.drawString(
					": " + player.getDefense() / 100 + "/" + player.getMaxDefense() / 100,
						91,
						38
				);
			}
			
			// draw wings
			if(player.hasWings()) {
				g.drawImage(wingsIcon, 160, 25, 40 , 20 , null);
			}
			
			// draw money
			g.drawImage(moneyIcon ,230 ,3 , null);
			g.drawString(": " + player.getMoney(), 255, 20);
			

		}
		
}
