
public class GameState {

	int values[] = new int[3];
	float score;
	int freq;
	
	public GameState(){
		
		
	}
	public GameState(int i, int j, int k){
		values[0] = i;
		values[1] = j;
		values[2] = k;
		score = 0;
		freq = 0;
	}
	
	public static boolean Compare(GameState firstState, GameState secondState){
			return (firstState.values[0] == secondState.values[0] &&
					firstState.values[1] == secondState.values[1] &&
					firstState.values[2] == secondState.values[2]);
	}
	
	public boolean IsGameOver(){
		return ( values[0] == 0 && values[1] == 0 && values[2] == 0);
	}
	
	public static boolean WorseThan(GameState firstState, GameState secondState){
		return (firstState.score > secondState.score);
	}
	
	public void SetTo(GameState state){
		this.values = state.values.clone();
		this.score = state.score;
		this.freq = state.freq;
	}
}
