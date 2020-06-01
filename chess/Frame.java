package chess;

import java.util.TimerTask;

public class Frame extends TimerTask {
	Game game;
	RandomPlayer random;
	public Frame(Game g, RandomPlayer r) {
		game=g;
		random=r;
	}
	public void run() {
		if(game.turn().string().contentEquals("BLACK")) {
        	Move m=random.getMove();
        	if(m.isDouble()) {
        		DoubleMove mD=(DoubleMove)m;
        		System.out.println(m.movedPiece().color().toString()+" "+m.movedPiece().type().toString()+" at "+m.movedPiece().getLocation()+" to "+(mD.move1().x2())+","+(mD.move1().y2())+" AND ALSO: "+mD.move2().movedPiece().color().toString()+" "+mD.move2().movedPiece().type().toString()+" at "+mD.move2().movedPiece().getLocation()+" to "+(mD.move2().x2())+","+(mD.move2().y2()));

        	}
        	else {
        		SingleMove mS=(SingleMove)m;
        		System.out.println(mS.movedPiece().color().toString()+" "+mS.movedPiece().type().toString()+" at "+mS.movedPiece().getLocation()+" to "+(mS.x2())+","+(mS.y2()));
        	}
        	//game.makeMove(m);
        	//System.out.println("moved");
    	}
	}
}
