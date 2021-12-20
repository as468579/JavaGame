package Main;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Game {

	public static void main(String[] args){
		JFrame window = new JFrame("Java Game");
		window.setLayout(new BorderLayout());
		//window.setContentPane(new GamePanel());
		window.add(new GamePanel(),BorderLayout.CENTER);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}
}
