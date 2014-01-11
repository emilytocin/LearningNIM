import java.util.ArrayList;


public class AI {

	ArrayList<GameState> table = new ArrayList<GameState>();
	ArrayList<GameState> thisGameHist = new ArrayList<GameState>();
	
	public GameState ComputerPlayerTurn(GameState currentState){
		System.out.println("Computer taking turn");
		GameState bestMove = new GameState();
		bestMove.score = 1;
		boolean moreThanOne = false;
		ArrayList<GameState> possibleList = new ArrayList<GameState>();
		for (int i = 0; i < 3; i ++){
			for (int j = 1; j <= currentState.values[i]; j ++){
				GameState curPossible = new GameState(currentState.values[0],
						currentState.values[1],
						currentState.values[2]);
				if (i == 0){
					curPossible = new GameState(currentState.values[0]-j,
						currentState.values[1],
						currentState.values[2]);
				}
				else if (i == 1){
					curPossible = new GameState(currentState.values[0],
						currentState.values[1]-j,
						currentState.values[2]);
				}
				else if (i == 2){
					curPossible = new GameState(currentState.values[0],
						currentState.values[1],
						currentState.values[2]-j);
				}
				else{
					System.out.println("Break in if construct for computerplayermove()");
					System.exit(1);
				}
				float possibleScore = CheckScoreOf(curPossible);
				if (possibleScore < bestMove.score){
					bestMove.SetTo(curPossible);
					bestMove.score = possibleScore;
					moreThanOne = false;
				}
				else if (possibleScore == bestMove.score){
					possibleList.add(new GameState(curPossible.values[0], curPossible.values[1], curPossible.values[2]));
					moreThanOne = true;
				}
			}
		}
		if (moreThanOne){
			int tempIndex = Base.rand.nextInt(possibleList.size());
			currentState.SetTo(possibleList.get(tempIndex));
		}
		else{
			currentState.SetTo(bestMove);
		}
		
		return currentState;
	}
	
	public void StoreCurrentGameState(GameState currentState){
		thisGameHist.add(new GameState(currentState.values[0], currentState.values[1], currentState.values[2]));
	}
	
	public float CheckScoreOf(GameState state){
		float result = 0.0f;
		
		for (int i = 0; result == 0.0f && i < table.size() - 1; i ++){
			if (GameState.Compare(state, table.get(i))){
				result = table.get(i).score;
			}
		}
		return result;
	}
	
	public int GetTableIndexOfState(GameState state){
		int result = -1;
		for (int i = 0; result == -1 && i < table.size() - 1; i ++){
			if (GameState.Compare(state, table.get(i))){
				result = i;
			}
		}
		return result;
	}
	
	public void ScoreAndStoreThisGameHist(boolean player1Won, boolean player1MovesFirst){
		System.out.println("Getting smarter");
		int numberOfTurns = thisGameHist.size();
		int numberOfWinningMoves = (int) ((player1Won == player1MovesFirst) ? 
				Math.ceil(numberOfTurns/2) :
				Math.floor(numberOfTurns / 2));
		int numberOfLosingMoves = numberOfTurns - numberOfWinningMoves;
		//For each move, if that move led to a win, score it as move
		
		//Winning Moves
		//Score the first score and every other score as a win.
		for (int i = 0; i < numberOfTurns; i ++){
			//If numofMoves - i is even, score was a loss. Multiply by -1. That should do it.
			boolean thisWasAWin = ((numberOfTurns - i) % 2 != 0);
			//score the state at table[0] as (i / 2 + 1)/(numberOfWinningMoves).
			thisGameHist.get(i).score = ((float)i / 2.0f + 1.0f)/(float)(thisWasAWin?numberOfWinningMoves:numberOfLosingMoves);
			//Make negative if it was a lose.
			thisGameHist.get(i).score *= (thisWasAWin?1:-1);
			//Then add it to table...
			//if it already exists in the table, just modify the score.
			int tableIndex = GetTableIndexOfState(thisGameHist.get(i));
			if (tableIndex != -1){
				table.get(tableIndex).score =
						((table.get(tableIndex).freq *
						table.get(tableIndex).score) + 
						thisGameHist.get(i).score) / 
						(table.get(tableIndex).freq + 1);
				table.get(tableIndex).freq ++;
			}
			//else, add to end.
			else{
				thisGameHist.get(i).freq = 1;
				
				table.add(new GameState(thisGameHist.get(i).values[0], thisGameHist.get(i).values[1], thisGameHist.get(i).values[2]));
				table.get(table.size() - 1).score = thisGameHist.get(i).score;
				table.get(table.size() - 1).freq = 1;
			}
		}
	}
}
