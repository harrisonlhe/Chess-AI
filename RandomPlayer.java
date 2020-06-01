import chess.*;

public class RandomPlayer extends ChessPlayer{

public RandomPlayer() {
}

public Move getMove(Game current) {
  			return current.listValidMoves().get((int)(current.listValidMoves().size()*Math.random()));
}

}
