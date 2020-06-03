package chess;

import java.util.List;

public class RandomPlayer extends ChessPlayer{
Game state;
public RandomPlayer(Game g) {
	state=g;
}

public Move getMove() {
	List<Move> moves=state.listValidMoves();
  			return moves.get((int)(moves.size()*Math.random()));
}

}