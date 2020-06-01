package chess;

public class RandomPlayer extends ChessPlayer{
Game state;
public RandomPlayer(Game g) {
	state=g;
}

public Move getMove() {
  			return state.listValidMoves().get((int)(state.listValidMoves().size()*Math.random()));
}

}
