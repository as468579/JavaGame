package Audio;

import java.util.HashMap;
import javax.sound.sampled.*;

public class AudioPlayer {
	
	private static HashMap<String, Clip>sfx;
	private static HashMap<String, Clip>bgMusic;
	private static int gap;
	private static boolean mute = false;
	
	// type 
	public static int SFX = 0;
	public static int BGMUSIC = 1;
	
	// volume
	private static int sfxVolume;
	private static int bgVolume;
	public static final int maxVolume = 10;
	
	
	public static void init() {
		sfx = new HashMap<String, Clip>();
		bgMusic = new HashMap<String, Clip>();
		gap = 0;
		sfxVolume = 5;
		bgVolume = 5;
	}
	
	public static void load(String s, String n, int type) {
		
		Clip clip;
		try {
			AudioInputStream ais =
				AudioSystem.getAudioInputStream(
					AudioPlayer.class.getResourceAsStream(s)
				);
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				baseFormat.getSampleRate(),
				16,
				baseFormat.getChannels(),
				baseFormat.getChannels() * 2,
				baseFormat.getSampleRate(),
				false
			);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
			if(type == BGMUSIC) {
				bgMusic.put(n, clip);
			}
			else if(type == SFX){
				sfx.put(n, clip);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void play(String s, int type) {
		play(s, gap, type);
	}
	
	public static void play(String s, int i, int type) {
		// if(mute) return;
		Clip c = null;
		if(type == BGMUSIC) {
			c = bgMusic.get(s);
		}
		else if(type == SFX){
			c = sfx.get(s);
		}
		
		if(c == null) return;
		if(c.isRunning()) c.stop();
		c.setFramePosition(i);
		while(!c.isRunning()) c.start();
		// if?
	}
	
	public static void stop(String s, int type) {
		if(type == BGMUSIC) {
			if(bgMusic.get(s) == null) return;
			if(bgMusic.get(s).isRunning()) bgMusic.get(s).stop();
		}
		else if(type == SFX) {
			if(sfx.get(s) == null) return;
			if(sfx.get(s).isRunning()) bgMusic.get(s).stop();
		}
	}
	
	public static boolean isRunning (String s, int type) {
		if(type == BGMUSIC) {
			if(bgMusic.get(s) == null) return false;
			return bgMusic.get(s).isRunning();
		}
		else if(type == SFX) {
			if(sfx.get(s) == null) return false;
			return sfx.get(s).isRunning();
		}
		return false;
	}
	
	public static void resume(String s, int type) {
		// if(mute) return;
		if(type == BGMUSIC) {
			if(bgMusic.get(s).isRunning()) return;
			bgMusic.get(s).start();
		}
		else if(type == SFX) {
			if(sfx.get(s).isRunning()) return;
			sfx.get(s).start();
		}
	}
	
	public static void loop(String s, int type) {
		if(type == BGMUSIC) {
			loop(s, gap, gap, bgMusic.get(s).getFrameLength() - 1, type);
		}
		else if(type == SFX) {
			loop(s, gap, gap, sfx.get(s).getFrameLength() - 1, type);
		}
	}
	
	public static void loop(String s, int frame, int type) {
		if(type == BGMUSIC) {
			loop(s, frame, gap, bgMusic.get(s).getFrameLength() - 1, type);
		}
		else if(type == SFX) {
			loop(s, frame, gap, sfx.get(s).getFrameLength() - 1, type);
		}
	}
	
	public static void loop(String s, int start, int end, int type) {
		loop(s, gap, start, end, type);
	}
	
	public static void loop(String s, int frame, int start, int end, int type) {
		stop(s, type);
		// if(mute) return;
		if(type == BGMUSIC) {
			bgMusic.get(s).setLoopPoints(start, end);
			bgMusic.get(s).setFramePosition(frame);
			bgMusic.get(s).loop(Clip.LOOP_CONTINUOUSLY);
		}
		else if(type == SFX) {
			sfx.get(s).setLoopPoints(start, end);
			sfx.get(s).setFramePosition(frame);
			sfx.get(s).loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	public static void setPosition(String s, int frame, int type) {
		if(type == BGMUSIC) {
			bgMusic.get(s).setFramePosition(frame);
		}
		else if(type == SFX) {
			sfx.get(s).setFramePosition(frame);
		}
	}
	
	public static int getFrames(String s, int type) { 
		if(type == BGMUSIC) {
			return bgMusic.get(s).getFrameLength();
		}
		else if(type == SFX) {
			return sfx.get(s).getFrameLength();
		}
		return -1; 
	}
	public static int getPosition(String s,int type) { 
		if(type == BGMUSIC) {
			return bgMusic.get(s).getFramePosition(); 
		}
		else if(type == SFX) {
			return sfx.get(s).getFramePosition();
		}
		return -1;
	}
	
	public static void close(String s, int type) {
		stop(s, type);
		if(type == BGMUSIC) {
			bgMusic.get(s).close();
		}
		else if(type == SFX) {
			sfx.get(s).close();
		}
	}
	
	public static void setVolumeValue(int volume, int type) {
		
		if(volume < 0 || volume > maxVolume) return ;
		if(type == BGMUSIC) {
			bgVolume = volume;
		}
		else if(type == SFX) {
			sfxVolume = volume;
		}
		setVolume();
	}
	
	public static void setVolume(){

		FloatControl gainControl;
		
		double bgRate = (1.0 * bgVolume) / maxVolume;
		double sfxRate = (1.0 * sfxVolume) / maxVolume;
		
		for(Clip clip : bgMusic.values()) {
			gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); 
			gainControl.setValue(20f * (float) Math.log10(bgRate));
			
		}
		
		for(Clip clip : sfx.values()) {
			gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); 
			gainControl.setValue(20f * (float) Math.log10(sfxRate));

		}
	
	}
	
	public static int getVolumeValue(int type) {
		if(type == BGMUSIC) return bgVolume;
		else if(type == SFX) return sfxVolume;
		return -1;
	}
}
