public class randomPlayer extends ChessPlayer{

public ChessPlayer() {
}

public ChessMove getMove(Game current) {
  			return current.listValidMoves().get((int)(current.listValidMoves().size()*Math.random()));
}

}
