package chess;

import java.util.List;

public class MinimaxPlayer extends ChessPlayer{
Game state;
int depth;
int counter=0;
public MinimaxPlayer(Game g, int d) {
	state=g;
	depth=d;
}

public static double MinimaxValue(Game s, int d) {
	if(d==0) {
		double sc=s.complexEval();
		//if(sc==2.4) { s.printBoard(); System.out.println(); s.checkPhantom(false);}
		return s.complexEval();
	}
	List<Move> l=s.listValidMoves();
	if(l.size()==0) return s.complexEval();
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
	counter++;
	List<Move> l=state.listValidMoves();
	if(l.size()==0) {System.out.println("L HAS SIZE 0!!!! ERRORRRR!"); return null; }
	int bestPos=0;
	Game temp=state.applyMoveCloning(l.get(0));
	Double bestScore=MinimaxValue(temp,depth-1);
	Double mScore=0.0;
	boolean isWhite=state.turn().abbrev().contentEquals("w") ? true:false;
	for(int i=0; i<l.size(); i++) {
		temp=state.applyMoveCloning(l.get(i));
		mScore=MinimaxValue(temp,depth-1);
		//System.out.println("Possible choice: "+mScore);
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
	System.out.println("Minimax Value it chose: "+bestScore);
	System.out.println("GETMOVE COUNTER IS: "+counter);
	//System.out.println(state.listValidMoves());
	return l.get(bestPos);
}

}