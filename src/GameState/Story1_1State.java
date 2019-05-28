package GameState;

import java.awt.Color;
import java.awt.Font;

/*
 * player enter sewer
 * */

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Entity.Enemy;
import Entity.Explosion;
import Entity.Player;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Story1_1State extends LevelState{
	
	private int current = 1;
	private boolean part[] = {true, false, false, false};
	private Font basicFont;
	private Font storyFont;
	private Color basicColor;
	private Color storyColor;
	
	
	public Story1_1State(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	@Override
	public void init() {
		super.init();
		System.out.println("sotry 1");
		
		bg = new Background("/Backgrounds/story_bg.gif",0.5);
		
		basicFont = new Font(Font.DIALOG, Font.PLAIN, 10);
		storyFont = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
		
		basicColor = new Color(0, 0, 0);
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
		g.drawString("'space'", GamePanel.WIDTH - 40, GamePanel.HEIGHT-10);
		
		g.setFont(storyFont);
		g.setColor(storyColor);
		
		if(part[0]) {
			g.drawString("宗文是個靠爸的人", 0, 30);
		}
		if(part[1]) {
			g.drawString("他爸有錢，它是富二代", 0, 60);
		}
		if(part[2]) {
			g.drawString("一天，他跟人約喝茶...", 0, 90);
		}
	}
	
	@Override
	public void keyPressed(int k) {
		if( k == KeyEvent.VK_ENTER) {
			endStory();
		}
		if( k == KeyEvent.VK_SPACE) {
			goAnotherPart();
		}
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
	
	private void goAnotherPart() {
		if(current == 3) {
			endStory();
			return;
		}
		
		part[current] = true;
		System.out.println(current);
		current++;
		return;
		
	}
}
