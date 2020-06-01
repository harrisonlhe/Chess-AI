package chess;

import java.util.List;
import java.util.Timer;

/** Main class of the Chess program.
 * @author Wan Fung Chui
 */

public class Main {

    /** Opens and begins a new game of chess. */
    public static void main(String... dummy) {
        Game game = new Game();
        RandomPlayer random=new RandomPlayer(game);
        
        Timer timer = new Timer();
        timer.schedule(new Frame(game,random), 0, 1000);
        

    }
}
