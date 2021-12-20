package Entity;

import java.awt.*; 
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Entity.Object.Player;

public class Story {

		private Player player;
		private BufferedImage imagePlayer;
		private Font font = new Font(Font.SERIF, Font.PLAIN, 12);
		private Color color = new Color(0,0,0);
		
		public String content;
		public boolean endStory = false;
		
		public Story(Player p) {
			player = p;
			content = "";
			try {
				imagePlayer = ImageIO.read(
									getClass().getResourceAsStream(
										"/HUD/player.jpg"
									)
							);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		public void draw(Graphics2D g) {
			
			if(!endStory) {
				g.drawImage(imagePlayer, 0 , 130, 95 , 94 , null);
				
				g.setFont(font);
				g.setColor(color);
				g.drawString(
						content,
							100,
							150
					);
			}
			
		}
		
		public void setContent(String content) {
			this.content = content;
		}
		
		public void start() {
			endStory = false;
		}
		public void end() {
			endStory = true;
		}


		public void setFont(Font font) {
			this.font = font;
		}

		public void setColor(Color color) {
			this.color = color;
		}
		
		
		
}
