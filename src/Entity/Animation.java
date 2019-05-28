package Entity;

import java.awt.image.BufferedImage;

public class Animation {

	private BufferedImage[] frames;
	private int currentFrame;
	private int numFrames;
	
	private long startTime;
	private long delay;
	
	private boolean playedOnce;  // record whether the animation has already played once 
	private boolean loop;
	
	public Animation() {
		playedOnce = false;
		loop = true;
	}
	
	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;   // reset frames
	}
	
	public void setDelay(long d) { delay = d; }
	public void setCurrentFrame(int i) { currentFrame = i; }
	
	public void update() {
		
		if (delay == -1) return;
		
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if(elapsed > delay) {
			if(playedOnce  && !loop) return;
			currentFrame++;
			startTime = System.nanoTime();
		}
		
		if(currentFrame == frames.length) {
			if(loop)currentFrame = 0;
			else currentFrame--;
			playedOnce = true;
		}
	}
	
	public void setLoop(boolean loop) { this.loop = loop; }
	public int getCurrentFrame() { return currentFrame; }
	public BufferedImage getImage() { return frames[currentFrame]; }
	public boolean hasPlayedOnce() { return playedOnce; }
	
	
	
	

}
