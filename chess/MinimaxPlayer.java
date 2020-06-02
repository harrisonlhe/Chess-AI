package chess;

import java.util.List;

public class MinimaxPlayer extends ChessPlayer{
Game state;
int depth;
public MinimaxPlayer(Game g, int d) {
	state=g;
	depth=d;
}

public static double MinimaxValue(Game s, int d) {
	if(d==0) return s.simpleEval1();
	List<Move> l=s.listValidMoves();
	if(l.size()==0) return s.simpleEval1();
	if(s.turn().abbrev().equals("w")) { //return max of minimax values
		double max=-Double.MAX_VALUE;
		for(Move m : l) {
			Game child=s.applyMoveCloning(m);
			max=Math.max(max, MinimaxValue(child,d-1));
		}
		return max;
	}
	else{ //return min of minimax values}
		double min=Double.MAX_VALUE;
		for(Move m : l) {
			Game child=s.applyMoveCloning(m);
			min=Math.min(min, MinimaxValue(child,d-1));
		}
		return min;
	}
	
}

public Move getMove(){
	List<Move> l=state.listValidMoves();
	if(l.size()==0) return null;
	Game temp=state.applyMoveCloning(l.get(0));
	Double bestScore=MinimaxValue(temp,depth-1);
	int bestPos=0;
	boolean isWhite=state.turn().abbrev().contentEquals("w") ? true:false;
	for(int i=0; i<l.size(); i++) {
		temp=state.applyMoveCloning(l.get(i));
		Double mScore=MinimaxValue(temp,depth-1);
		if(isWhite) {
			if(mScore>=bestScore) {
				bestScore=mScore;
				bestPos=i;
			}
		}
		else {
			if(mScore<=bestScore) {
				bestScore=mScore;
				bestPos=i;
			}
		}
	}
	return l.get(bestPos);
}

}
