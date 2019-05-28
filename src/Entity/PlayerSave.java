package Entity;

public class PlayerSave {

	//private static int lives = 3;
	private static int health = 5;
	private static long time = 0;
	
	public static void init() {
		//lives = 3;
		health = 5;
		time = 0;
	}
	
	//public static int getLives() { return lives; }
	//public static void setLives(int l) { lives = l; }
	
	public static int getHealth() { return health;}
	public static void setHealth(int h) { health = h; }
	
	public static long getTime() { return time; }
	public static void setTime(long t) { time = t; }
}

//°Ñ¦Ò¨Ó·½ : 
//ForeignGuyMike. 2013. Java 2D Game Programming - Platformer Tutorial 
//https://www.youtube.com/playlist?list=PL-2t7SM0vDfcIedoMIghzzgQqZq45jYGv