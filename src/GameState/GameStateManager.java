package GameState;

import java.awt.Graphics2D;
import java.util.ArrayList;

import Audio.AudioPlayer;
import Main.GamePanel;

public class GameStateManager {
	private GameState[] gameStates;
	private int currentState;
	
	public static final int NUMGAMESTATES = 16;
	public static final int LOADSTATE = 0;
	public static final int MENUSTATE = 1;
	public static final int LEVEL1_1STATE = 2;
	public static final int LEVEL1_2STATE = 3;
	public static final int LEVEL1_3STATE = 4;
	public static final int LEVEL1_4STATE = 5;
	public static final int LEVEL1_5STATE = 6;
	public static final int LEVEL1_6STATE = 7;
	public static final int LEVEL1_7STATE = 8;
	public static final int STORY1_1_1STATE = 9;
	public static final int STORY1_1_2STATE = 10;
	public static final int STORY1_2STATE = 11;
	public static final int ENDING_STATE_1 = 12;
	public static final int ENDING_STATE_2 = 13;
	public static final int ENDING_STATE_3 = 14;
	public static final int SETTINGSTATE = 15;

	
	public GameStateManager(){
		
		AudioPlayer.init();
		
		gameStates = new GameState[NUMGAMESTATES];
		
		currentState = LOADSTATE;
		loadState(currentState);
	}
	
	private void loadState(int state) {
	
		if(state == LOADSTATE) {
			gameStates[state] = new LoadState(this);
		}
		else if(state == MENUSTATE) {
			gameStates[state] = new MenuState(this);
		}
		else if(state == LEVEL1_1STATE) {
			gameStates[state] = new Level1_1State(this);
		}
		else if(state == LEVEL1_2STATE) {
			gameStates[state] = new  Level1_2State(this);
		}
		else if(state == LEVEL1_3STATE) {
			gameStates[state] = new Level1_3State(this);
		}
		else if(state == LEVEL1_4STATE) {
			gameStates[state] = new Level1_4State(this);
		}
		else if(state == LEVEL1_5STATE) {
			gameStates[state] = new Level1_5State(this);
		}
		else if(state == LEVEL1_6STATE) {
			gameStates[state] = new Level1_6State(this);
		}
		else if(state == LEVEL1_7STATE) {
			gameStates[state] = new Level1_7State(this);
		}
		else if(state == STORY1_1_1STATE) {
			gameStates[state] = new  Story1_1_1State(this);
		}
		else if(state == STORY1_1_2STATE) {
			gameStates[state] = new  Story1_1_2State(this);
		}
		else if(state == STORY1_2STATE) {
			gameStates[state] = new  Story1_2State(this);
		}
		else if(state == ENDING_STATE_1) {
			gameStates[state] = new  Ending1State(this);
		}
		else if(state == ENDING_STATE_2) {
			gameStates[state] = new  Ending2State(this);
		}
		else if(state == ENDING_STATE_3) {
			gameStates[state] = new  Ending3State(this);
		}
		else if(state == SETTINGSTATE) {
			gameStates[state] = new  SettingState(this);
		}
	}
	
	private void unloadState(int state) {
		if(state == LOADSTATE) {
			return;
		}
		gameStates[state] = null;
	}
	
	public void setStates(int state){
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}
	
	public void update(){
		try {
			if(gameStates[currentState] != null)
				gameStates[currentState].update();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g){
		try {
			if(gameStates[currentState] != null) 
				gameStates[currentState].draw(g);
			else {
				g.setColor(java.awt.Color.BLACK);
				g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public LoadState getLoadState() {
		if(gameStates[LOADSTATE] instanceof LoadState) {
			LoadState ls = (LoadState)gameStates[LOADSTATE];
			return ls;
		}
		return null;
	}
	
	public void keyPressed(int k){
		gameStates[currentState].keyPressed(k);
	}
	
	public void keyReleaseed(int k){
		gameStates[currentState].keyReleased(k);
	}
	
	
	
	
}
