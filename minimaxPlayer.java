package othelloGaming;

import java.util.ArrayList;

public class minimaxPlayer extends OthelloPlayer{
	boolean isO;
//create a move tree... Depth 3. Can generate 
	//starting state, generate moves.
	//move vals associated with each state.
	
	public minimaxPlayer(boolean o) {
		isO = o;
	}

//	@Override
//	public OthelloMove getMove(OthelloState state) {
////		OthelloPlayer p1 = new greedyPlayer(true);
////		OthelloPlayer p2 = new greedyPlayer(false);
//		
//
//		return null;
//	}
	
	//recommended to do it recursively.**************************************
	//if not terminal expand, return the min or max value of children depending
	
	//GOING FOR DEPTH 1
	
	public int getScoreRecurse(OthelloState state) {
		//remember to add null check in moveReturn

		System.out.println(state);
		System.out.println(state.movecount()-4);
		
		if (state.movecount() - Main.getMoveNum() == 6) {
			return state.score();
		}
		
		ArrayList<Integer> moveEval = new ArrayList<Integer>();
		
		
		for (int i=0; i<state.generateMoves().size(); i++) {
			moveEval.add(getScoreRecurse(state.applyMoveCloning(state.generateMoves().get(i))));
		}
		
		if (state.movecount()%2 == 1) {//odd move. Our play.
			int max = Integer.MIN_VALUE;
			int maxIndex = 0;
			for (int p=0; p<state.generateMoves().size(); p++) {
				if (moveEval.get(p) > max) {
					maxIndex = p;
				}
			}
			return max;	
		} else { //other person's turn
			int min = Integer.MAX_VALUE;
			int minIndex = 0;
			
			for (int p=0; p<state.generateMoves().size(); p++) {
				if (moveEval.get(p) < min) {
					minIndex = p;
				}
			}
			return min;
		}
		
		
		
		
		
		//RECURSE and figure out what the best state is. //best state is found by recursing downwards.
	}
	
	public static void main(String[] args) {
		OthelloState start = new OthelloState(8);
		minimaxPlayer test = new minimaxPlayer(true);
		test.getScoreRecurse(start);
	}
	
	
	
	
//	public int moveRecurseVal(OthelloState state) {
//		return (state.applyMoveCloning(state.generateMoves().get(1))).score();
//	}
	
	public OthelloMove getMove(OthelloState state) { //assuming opponent is greedyPlayer looking at the same depth as we are
		ArrayList<OthelloState> storedStates = new ArrayList<OthelloState>();
		ArrayList<OthelloMove> moves = new ArrayList<OthelloMove>(state.generateMoves()); //ONLY WORKS AS P1 rn
		
		if (moves.isEmpty()) {
			return null;
		}
		
		ArrayList<Integer> moveVals = new ArrayList<Integer>();
		
		ArrayList<OthelloState> storedLeafStates = new ArrayList<OthelloState>();
		ArrayList<Integer> storedLeafVals = new ArrayList<Integer>();
		
		for (OthelloMove e : moves) {
			OthelloState clonedState;
			clonedState = state.clone(); //pointer issue fixed
			clonedState.applyMove(e);
			storedStates.add(clonedState);
		}
		
		int min;
		int minIndex;
		
		
		for (int i=0; i<storedStates.size(); i++) { //parsing through storedStates (ply 1)
			
			ArrayList<OthelloMove> posMoves = new ArrayList<OthelloMove>(storedStates.get(i).generateMoves()); //possible moves from each storedState
			
			min = Integer.MAX_VALUE;
			
			for (int k=0; k<posMoves.size(); k++) { //parsing and applying generatedMoves of each stored State
				OthelloState temp = storedStates.get(i).clone();
				temp.applyMove(posMoves.get(k));				
				min = Math.min(min, temp.score()); //stores the min of each branch
			}
			moveVals.add(min);
			//we want to try and maximize. Find mins of tempLeafs in this step and find maxes later.
		}

		int maxIndex = 0;
		int max = Integer.MIN_VALUE; //currently only WORKS TO MAXIMIZE??
		for (int i=0; i<moveVals.size(); i++) {
			if (max < moveVals.get(i)) {
				maxIndex = i;
			}
		}
		
		return state.generateMoves().get(maxIndex);
		
		
		
//		for (int i=0; i<moves.size()-1; i++) {
//			moveVals.add(storedStates.get(i).score());
//			if (isO) {
//				if (moveVals.get(i) >= max) {
//					max = moveVals.get(i);
//					maxIndex = i;
//				}
//			} else {
//				
//				if (moveVals.get(i) <= min) {
//					min = moveVals.get(i);
//					minIndex = i;
//				}
//			}
//			
	}	
}
