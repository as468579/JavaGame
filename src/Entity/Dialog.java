package Entity;

import java.awt.*; 
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Main.GamePanel;

public class Dialog {

		private BufferedImage imagePlayer;
		private Font font = new Font("Microsoft JhengHei", Font.PLAIN, 15);
		private Color color = new Color(0,0,0);
		
		public String content;
		public boolean endStory = false;
		
		private String[] dialog1_1;
		private String[] dialog1_2;
		private String[] dialog1_2_1;
		private String[] dialog1_3;
		private String[] dialog1_3_1;
		private String[] dialog1_3_2;
		private String[] dialog1_4;
		private String[] dialog1_4_1;
		private String[] dialog1_5;
		private String[] dialog1_6;
		private String[] dialog1_6_1;
		private String[] dialog1_6_2;
		
		private int contentX;
		private int contentY;
		
		public Dialog() {
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
			
			writeDialog();
			contentX=100;
			contentY=155;
		}
		
		public void draw(Graphics2D g) {
			
			if(!endStory) {
				//draw dialog background
				g.setColor(new Color(200, 200, 200, 200));
				g.fillRect(5, 137, GamePanel.WIDTH-10, GamePanel.HEIGHT-140);
				g.setColor(new Color(0, 0, 0));
				g.drawRect(5, 137, GamePanel.WIDTH-10, GamePanel.HEIGHT-140);
				
				//draw player's icon
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
				g.drawImage(imagePlayer, 10 , 140, 75 , 95 , null);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
				
				//draw content
				g.setFont(font);
				g.setColor(color);
				drawString(g, content, contentX, contentY);
				
				g.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 11));
				g.setColor(new Color(0,0,0));
				g.drawString("<Press X >", GamePanel.WIDTH-70, GamePanel.HEIGHT-10);
			}
			
		}
		
		private void drawString(Graphics g, String text, int x, int y) {
            for (String line : text.split("\n")) {
                g.drawString(line, x, y);
                y += g.getFontMetrics().getHeight();
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
		
		public void setContentX(int contentX) {
			this.contentX = contentX;
		}
		
		public void setContentY(int contentY) {
			this.contentY = contentY;
		}
		
		
		
		public String[] getDialog1_1() { return dialog1_1; }

		public String[] getDialog1_2() { return dialog1_2; }

		public String[] getDialog1_2_1() { return dialog1_2_1; }

		public String[] getDialog1_3() { return dialog1_3; }
		
		public String[] getDialog1_3_1() { return dialog1_3_1; }
		
		public String[] getDialog1_3_2() { return dialog1_3_2; }

		public String[] getDialog1_4() { return dialog1_4; }
		
		public String[] getDialog1_4_1() { return dialog1_4_1; }

		public String[] getDialog1_5() { return dialog1_5; }

		public String[] getDialog1_6() { return dialog1_6; }
		
		public String[] getDialog1_6_1() { return dialog1_6_1; }
		
		public String[] getDialog1_6_2() { return dialog1_6_2; }

		
		public void setDialog1_1(String[] dialog1_1) { this.dialog1_1 = dialog1_1; }

		public void setDialog1_2(String[] dialog1_2) { this.dialog1_2 = dialog1_2; }

		public void setDialog1_2_1(String[] dialog1_2_1) { this.dialog1_2_1 = dialog1_2_1; }

		public void setDialog1_3(String[] dialog1_3) { this.dialog1_3 = dialog1_3; }
		
		public void setDialog1_3_1(String[] dialog1_3_1) { this.dialog1_3_1 = dialog1_3_1; }
		
		public void setDialog1_3_2(String[] dialog1_3_2) { this.dialog1_3_2 = dialog1_3_2; }

		public void setDialog1_4(String[] dialog1_4) { this.dialog1_4 = dialog1_4; }
		
		public void setDialog1_4_1(String[] dialog1_4_1) { this.dialog1_4_1 = dialog1_4_1; }

		public void setDialog1_5(String[] dialog1_5) { this.dialog1_5 = dialog1_5; }

		public void setDialog1_6(String[] dialog1_6) { this.dialog1_6 = dialog1_6; }
		
		public void setDialog1_6_1(String[] dialog1_6_1) { this.dialog1_6_1 = dialog1_6_1; }
		
		public void setDialog1_6_2(String[] dialog1_6_2) { this.dialog1_6_2 = dialog1_6_2; }

		private void writeDialog() {
			
			dialog1_1 = new String[] {
					"今天天氣真好",
					"不知道對方到了沒，趕快去找她\n吧!",
					" ( ←→ 移動，空白鍵跳躍 ) ",
					" ( w 滑翔，e、r 可以攻擊) "
			};
			
			dialog1_2 = new String[] {
					"噢！好痛",
					"這裡是哪裡(慌張",
					"糟糕，好像掉到下水道了",
					"總之先想辦法離開吧",
					"這裡的水好髒，碰到感覺會\n生病 ><"
			};
			
			dialog1_2_1 = new String[] {
					"WOW! 這棵樹在地底下也長得\n很茂盛呢",
					"看來這附近的土地很營養"
			};
			
			dialog1_3 = new String[] {
					"感覺越走越深了呢...",
					".........",
					"等等!!!",
					"前面看起來沒路了...",
					".........",
					"疑?那些藤蔓看起來很堅固",
					"爬上去應該沒問題",
					"( 在藤蔓上，↑↓ 可以攀爬 )"
			};
			
			dialog1_3_1 = new String[] {
					"啊啊啊啊阿", 
					"前面的這個怪物看起來好強", 
					"他好像在守著甚麼重要的東西...?"
			};
			
			dialog1_3_2 = new String[] {
					"是盾牌!!!", 
					"有了這個就不用擔心怪物的攻擊了", 
					"(按 f 可以防禦\n"
					+ "護盾有耐久值，碰到怪物會減少\n"
					+ "耐久值會慢慢恢復)"
			};
			
			dialog1_4 = new String[] {
					"嗚哇哇哇～～好冷",
					"下水道怎麼會有這種地方啦\nQQ",
					"哈啾!!",
					"嗚嗚～～趕快離開吧"
			};
			
			dialog1_5 = new String[] {
					"好像到了很深的地方了...",
					"跟剛剛比起來，這裡暖和多了",
					"...",
					"不對，這裡好熱啊啊啊"
			};
			
			dialog1_6 = new String[] {
					"呼...終於離開了熱死人的地方",
					"...",
					"剛離開礦洞...",
					"怎麼突然進入這麼正常的關卡呀",
					"該不會是要迎接最終BOSS吧",
					"......",
					"怎麼可能嘛哈哈哈哈"
			};
			
			dialog1_6_1 = new String[] {
					"...",
					".....................",
					"放馬過來吧!!!"
			};
			
			dialog1_6_2 = new String[] {
					"是翅膀!!!",
					"有了這個就可以離開這該死的\n地方了吧",
					"!!!!!!",
					"該不會是啟動了甚麼機關吧...",
					"不行!要趕快逃出去了",
					"( 現在按 w 改為飛行，可以\n使用方向鍵控制方向 )"
			};
			
			dialog1_4_1 = new String[] {
					"啊啊啊啊好晃啊",
					"!!! 怎麼怪物變多了",
					"難道是地震激怒了這邊的生物",
					"算了別再想這些了，趕快走到\n出口要緊"
			};
		}
		
}
