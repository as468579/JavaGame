package Entity;

public class PlayerSave {

	//private static int lives = 3;
	private static int health = 5;
	private static long time = 0;
	private static int money = 0;
	
	// saved role
	public static final int HUMAN = 0;
	public static final int DRAGON = 1;
	private static int currentRole = 0;
	
	// saved items
	private static boolean shield = false;
	private static boolean wings = false;
	
	// saved position
	private static double xPos = 100;
	private static double yPos = 300;
	
	// level
	private static boolean lvl1_1;
	private static boolean lvl1_2;
	private static boolean lvl1_3;
	private static boolean lvl1_4;
	private static boolean lvl1_5;
	private static boolean lvl1_6;
	private static boolean lvl1_7;
	
	private static boolean flying;
	
	public static void init() {

		// player status
		// lives = 3;
		health = 5;
		time = 0;
		money = 0;

		// items
		wings = false;
		shield = false;
		
		// position
		xPos = 100;
		yPos = 300;
		
		// level
		lvl1_1 = false;
		lvl1_2 = false;
		lvl1_3 = false;
		lvl1_4 = false;
		lvl1_5 = false;
		lvl1_6 = false;
		lvl1_7 = false;
		
		flying = false;
	}
	
	//public static int getLives() { return lives; }
	//public static void setLives(int l) { lives = l; }
	
	public static int getHealth() { return health;}
	public static void setHealth(int h) { health = h; }
	
	public static void setTime(long t) { time = t; }
	
	public static int getMoney() { return money; }
	public static void setMoney(int m) { money = m; }
	
	public static boolean hasWings() { return wings; }
	public static void setWings(boolean w) { wings = w; }
	
	public static boolean hasShield() { return shield; }
	public static void setShield(boolean b) { shield = b; }
	
	public static double getX() { return xPos; }
	public static void setX(double x) { xPos = x; }
	
	public static double getY() { return yPos; }
	public static void setY(double y) { yPos = y; }

	public static int getCurrentRole() { return currentRole; }
	public static void setCurrentRole(int i) { currentRole = i; }

	public static boolean enteredLvl1_1() {
		return lvl1_1;
	}

	public static void setLvl1_1(boolean lvl1_1) {
		PlayerSave.lvl1_1 = lvl1_1;
	}

	public static boolean enteredLvl1_2() {
		return lvl1_2;
	}

	public static void setLvl1_2(boolean lvl1_2) {
		PlayerSave.lvl1_2 = lvl1_2;
	}

	public static boolean enteredLvl1_3() {
		return lvl1_3;
	}

	public static void setLvl1_3(boolean lvl1_3) {
		PlayerSave.lvl1_3 = lvl1_3;
	}

	public static boolean enteredLvl1_4() {
		return lvl1_4;
	}

	public static void setLvl1_4(boolean lvl1_4) {
		PlayerSave.lvl1_4 = lvl1_4;
	}

	public static boolean enteredLvl1_5() {
		return lvl1_5;
	}

	public static void setLvl1_5(boolean lvl1_5) {
		PlayerSave.lvl1_5 = lvl1_5;
	}

	public static boolean enteredLvl1_6() {
		return lvl1_6;
	}

	public static void setLvl1_6(boolean lvl1_6) {
		PlayerSave.lvl1_6 = lvl1_6;
	}

	public static boolean enteredLvl1_7() {
		return lvl1_7;
	}

	public static void setLvl1_7(boolean lvl1_7) {
		PlayerSave.lvl1_7 = lvl1_7;
	}

	public static boolean isFlying() {
		return flying;
	}

	public static void setFlying(boolean flying) {
		PlayerSave.flying = flying;
	}
	
	
	
}


//�ѦҨӷ� : 
//ForeignGuyMike. 2013. Java 2D Game Programming - Platformer Tutorial 
//https://www.youtube.com/playlist?list=PL-2t7SM0vDfcIedoMIghzzgQqZq45jYGv