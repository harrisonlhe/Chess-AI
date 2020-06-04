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
        game._original="ORIGINAL";
        
       MinimaxPlayer whiteP=new MinimaxPlayer(game,4);
        //MinimaxPlayer blackP=new MinimaxPlayer(game,4);
        //RandomPlayer blackP=new RandomPlayer(game);
        //RandomPlayer random=new RandomPlayer(game);
        
        
        Timer timerB = new Timer();
        Timer timerW = new Timer();
        //timerB.schedule(new Frame(game,blackP), 0, 10000);
        timerW.schedule(new FrameWhite(game,whiteP), 2500, 5000);
        
          
         
    }
}
