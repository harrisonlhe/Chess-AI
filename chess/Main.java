package chess;

/** Main class of the Chess program.
 * @author Wan Fung Chui
 */

public class Main {

    /** Opens and begins a new game of chess. */
    public static void main(String... dummy) {
        Game game = new Game();
        List<Move> validMoves=game.listValidMoves();
        for(Move m : validMoves) {
        	System.out.println(m.movedPiece().type().abbrev());
        }
    }

}
