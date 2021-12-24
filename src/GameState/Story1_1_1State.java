package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Main.GamePanel;
import TileMap.Background;

public class Story1_1_1State extends LevelState {

	public Story1_1_1State(GameStateManager gsm) {
		super(gsm);
	}
	
	@Override
	public void init() {
		super.init();
		System.out.println("sotry 1");
		
		bg = new Background("/Backgrounds/story_bg.gif", 0.5);
		
		storyPart = new boolean[] {true, false, false, false};
		basicFont = new Font("Microsoft JhengHei", Font.PLAIN, 30);
		storyFont = new Font("Microsoft JhengHei", Font.PLAIN, 30);
		
		basicColor = new Color(100, 100, 100);
		storyColor = new Color(0, 0, 0);
		
	}
	
	@Override
	public void update() {
		bg.update();
	}
	
	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);
		
		g.setFont(basicFont);
		g.setColor(basicColor);
		g.drawString("<Press X>", GamePanel.WIDTH - 60, GamePanel.HEIGHT-10);
		
		g.setFont(storyFont);
		g.setColor(storyColor);
		
		if(storyPart[0]) {
			g.drawString("宗文是位靠爸的富二代", 10, 20);
		}
		if(storyPart[1]) {
			g.drawString("他爸是鼎鼎大名的考古學家", 10, 50);
			g.drawString("是研究恐龍復活的權威", 10, 70);
			g.drawString("宗文也因此與恐龍有一種不解之緣。", 10, 90);
		}
		
		if(storyPart[2]) {
			g.drawString("然而", 10, 120);
			g.drawString("相較於他爸，宗文每天花天酒地", 10, 140);
			g.drawString("還常常約女生出去玩", 10, 160);
			g.drawString("過著令人羨慕的物質生活。", 10, 180);
		}
		
		if(storyPart[3]) {
			g.drawString("一天，他跟平常一樣約了一位網美出去玩...", 10, 210);
		}
	}
	
	@Override
	public void keyPressed(int k) {
		if( k == KeyEvent.VK_ENTER) {
			endStory();
		}
		if( k == KeyEvent.VK_SPACE || k == KeyEvent.VK_X ) {
			if(!hasAnotherStoryPart(storyPart, storyPart.length))
					endStory();
			
		}
	}
	
	@Override
	public boolean hasAnotherStoryPart(boolean[] storyPart, int partLength) {
		System.out.println(partLength+" "+currentStory);
		currentStory++;
		if(currentStory == partLength) {
			return false;
		}
		storyPart[currentStory] = true;
		return true;
	}
	
	
	private void endStory() {
		
		player.setPosition(100, 300);
		tileMap.setPosition(
				GamePanel.WIDTH / 2 - player.getX(),
				GamePanel.HEIGHT / 2 - player.getY()
		);
		tileMap.setTween(0.07);
		gsm.setStates(GameStateManager.LEVEL1_1STATE);
	}

}
