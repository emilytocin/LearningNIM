import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;



public class Game {


	enum userMode {PVP, PVC, CVC};
	boolean player1Turn;
	AI ai = new AI();
	int computerPlayCount = 0;
	boolean player1MovesFirst;
	userMode selectedMode;
	GameState startState = new GameState(3, 5, 7);
	GameState currentState = new GameState();

	Scanner scan = new Scanner(System.in);
	
	
	public void SetupGame(){
		System.out.println("Starting new game.");

		ModeSelection();
		int temp = Base.rand.nextInt(2);
		player1Turn = (temp == 0);
		player1MovesFirst = player1Turn;
		if (ai.thisGameHist.size() > 0){
			ai.thisGameHist.clear();
		}
		currentState.SetTo(startState);
		DrawCurrentState();
	}
	
	
	public void ModeSelection(){
		//Do all this code until they get it right.
		System.out.println("Select game mode: pvp, pvc, cvc");
		selectedMode = null;
		while (selectedMode == null){
			String selection = scan.nextLine();
			selection = selection.toUpperCase();
			selection.replace(" ", "");
			switch (selection){
			case "PVP":
				selectedMode = userMode.PVP;
				break;
			case "PVC":
				selectedMode = userMode.PVC;
				break;
			case "CVP":
				selectedMode = userMode.PVC;
				break;
			case "CVC":
				selectedMode = userMode.CVC;
				System.out.println("How many time should I play myself?");
				computerPlayCount = scan.nextInt();
				break;
			default:
				//System.out.println("Try again.");
				break;
			}
		}
	}
	
	public void GameLoop(){
		while (true){
			if (!currentState.IsGameOver() &&((selectedMode == userMode.CVC && computerPlayCount > 0) || (selectedMode != userMode.CVC))){
				if (player1Turn){
					if (selectedMode != userMode.CVC){
						HumanPlayerTurn();
					}
					else{
						currentState = ai.ComputerPlayerTurn(currentState);
						DrawCurrentState();
						player1Turn = !player1Turn;
					}
				}
				else{
					if (selectedMode == userMode.PVP){
						HumanPlayerTurn();
					}
					else{
						currentState = ai.ComputerPlayerTurn(currentState);
						DrawCurrentState();
						player1Turn = !player1Turn;
					}
				}
				//Store this game state in gameHist;
				ai.StoreCurrentGameState(currentState);
			}
			else{
				if (selectedMode != userMode.CVC || (selectedMode == userMode.CVC && computerPlayCount == 0)){
					//Game is over. Display winner, then restart on enter test
					System.out.println(player1Turn?"Player 1 won.":"Player 2 won.");
					EndGame(player1Turn);
					SetupGame();
				}
				else if (selectedMode == userMode.CVC){
					EndGame(player1Turn);
					SetupAnotherComputerGame();
					computerPlayCount --;
				}
			}
			
		}
	}
	
	public void HumanPlayerTurn(){
		System.out.println(player1Turn?"Player 1's turn. Select row.":"Player 2's turn. Select Row");
		int playerRow = -1;
		while ((playerRow != 1 && playerRow != 2 && playerRow != 3) || currentState.values[playerRow - 1] == 0){
			playerRow = scan.nextInt();
		}
		System.out.println("Select count");
		int playerCount = -1;
		while (playerCount < 1 || playerCount > currentState.values[playerRow-1]){
			playerCount = scan.nextInt();
		}
		
		currentState.values[playerRow - 1] -= playerCount;
		DrawCurrentState();
		
		player1Turn = !player1Turn;
	}
	
	public void DrawCurrentState(){
		for(int i = 0; i < currentState.values[0]; i ++){
			System.out.print("I");
		}
		if (currentState.values[0] == 0){
			System.out.print("X");
		}
		System.out.println();
		for(int i = 0; i < currentState.values[1]; i ++){
			System.out.print("I");
		}
		if (currentState.values[1] == 0){
			System.out.print("X");
		}
		System.out.println();
		for(int i = 0; i < currentState.values[2]; i ++){
			System.out.print("I");
		}
		if (currentState.values[2] == 0){
			System.out.print("X");
		}
		System.out.println("\n\n");
	}
	
	public void SetupAnotherComputerGame(){
		System.out.println("Starting new CVC game.");
		int temp = Base.rand.nextInt(1);
		player1Turn = (temp == 0);
		player1MovesFirst = player1Turn;
		if (ai.thisGameHist.size() > 0){
			ai.thisGameHist.clear();
		}
		currentState.SetTo(startState);
		DrawCurrentState();
	}
	
	public void EndGame(boolean player1Won){
		ai.ScoreAndStoreThisGameHist(player1Won, player1MovesFirst);
	}
}
