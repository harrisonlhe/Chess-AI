public class randomPlayer extends ChessPlayer{

public ChessPlayer() {
}

public ChessMove getMove(Game current) {
  			return game.listValidMoves().get((int)(game.listValidMoves().size()*Math.random()));
}

}
