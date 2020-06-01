package chess;

import java.util.List;

/** Main class of the Chess program.
 * @author Wan Fung Chui
 */

public class Main {

    /** Opens and begins a new game of chess. */
    public static void main(String... dummy) {
        Game game = new Game();
        ChessPlayer random=new RandomPlayer(game);
        List<Move> validMoves=game.listValidMoves();
        int x=0;
        /*while(true) {
        	x++;
        	if(x%10000==0) {
        	for(Move m : validMoves)
        		System.out.println(m.movedPiece().type().abbrev());
        		System.out.println("BREAK BREAK BREAK BREAK");
        		System.out.println("");
        		System.out.println("");
        	}
        }*/
        while(true) {
        	System.out.println(game.turn().abbrev());
        	if(game.turn().string().contentEquals("BLACK")) {
	        	Move m=random.getMove();
	        	game.makeMove(m);
	        	System.out.println("moved");
        	}
        }

    }
}
