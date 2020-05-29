public class randomPlayer extends ChessPlayer{

public ChessPlayer() {
}

public ChessMove getMove(Game current) {
  			return game.generateMoves().get((int)(game.generateMoves().size()*Math.random()));
}

}
