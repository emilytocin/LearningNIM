import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Base {

	public static Random rand = new Random(12323);
	
	
	public static void main (String args[]){
		System.out.println("Learning NIM\nBy Group 6");
		Game game = new Game();
		game.SetupGame();
		game.GameLoop();
	
	}
}
