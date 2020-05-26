package cowboysAndAliens;

import java.util.ArrayList;


public class CAState {
	private int cLeft, cRight, aLeft, aRight;
	
	private int boatOnLeft; //1 if on left, 0 if on right
	private int isValid; //1 if is, 0 if failed
	//private boolean foundGoal = false;
	//define constructor(s)
	public CAState() {
	}
	//ADD THE ROOT TO THE ARRAYLIST!!!
	public CAState(int a, int b, int c, int d, int e) {
		setcLeft(a);
		cRight = b;
		setaLeft(c);
		aRight = d;
		boatOnLeft = e;
		isValid = 1;
	}
	public CAState(int a, int b, int c) {
		setcLeft(a);
		cRight = 3-a;
		setaLeft(b);
		aRight = 3-b;
		boatOnLeft = c;
		isValid = 1;
	}
	public CAState(String state) {
		setcLeft(state.charAt(0));
		setaLeft(state.charAt(1));
		cRight = state.charAt(2);
		aRight = state.charAt(3);
		boatOnLeft = state.charAt(4);
		isValid = state.charAt(5);
	}
	
	public CAState(CAState currentState) {
		setcLeft(currentState.getcLeft());
		setaLeft(currentState.getaLeft());
		cRight = currentState.cRight;
		aRight = currentState.aRight;
		boatOnLeft = currentState.boatOnLeft;
		isValid = currentState.isValid;
		// TODO Auto-generated constructor stub
	}
	//methods
	public void ValidSet() {
		if ((getcLeft() > getaLeft() && getaLeft() > 0) || (cRight > aRight && aRight > 0)
				|| (getcLeft() < 0 || cRight < 0 || getaLeft() < 0 || aRight < 0)) {
			this.isValid = 0;
		} else {
		this.isValid = 1;
		}
	}
	
	public boolean isValidBool() {
		if ((((getcLeft() > getaLeft()) && getaLeft() > 0) || (cRight > aRight && aRight > 0)) || 
		(getcLeft() < 0 || cRight < 0 || getaLeft() < 0 || aRight < 0)) {
			return false;
		}
		return true;
	}
	
//	public CAState applyActionReturn(CAState other, int moveCow, int moveAl) {
//		if (other.boatOnLeft==0) {
//			other.cRight -= moveCow;
//			other.cLeft += moveCow;
//			other.aRight -= moveAl;
//			other.aLeft += moveAl;
//			other.boatOnLeft = 1;
//		} else {
//			other.cRight += moveCow;
//			other.cLeft -= moveCow;
//			other.aRight += moveAl;
//			other.aLeft -= moveAl;
//			other.boatOnLeft = 0;
//		}
//		other.ValidSet();
//		if (other.isGoal()) {
//			setFoundGoal(true);
//		}
//		
//		if ((other.toString()).charAt(5)==1 && !visitedStates.contains(other.toString())) {
//			visitedStates.add(other.toString());
//		}
//		return (CAState)other;
//	}
	
	public CAState applyActionReturn(CAState other, int moveCow, int moveAl) {
		if (other.boatOnLeft==0) {
			other.cRight -= moveCow;
			other.setcLeft(other.getcLeft() + moveCow);
			other.aRight -= moveAl;
			other.setaLeft(other.getaLeft() + moveAl);
			other.boatOnLeft = 1;
		} else {
			other.cRight += moveCow;
			other.setcLeft(other.getcLeft() - moveCow);
			other.aRight += moveAl;
			other.setaLeft(other.getaLeft() - moveAl);
			other.boatOnLeft = 0;
		}
		other.ValidSet();
//			if (other.isGoal()) {
//				setFoundGoal(true);
//			}
			
//			if ((other.toString()).charAt(5)==("1".charAt(0)) && !visitedStates.contains(other.toString())) {
//				visitedStates.add(other.toString());
//			}
			System.out.println(other);
//			System.out.println("VISITED: " + visitedStates);
			return other;
	}
	
	public void applyAction(CAState other, int moveCow, int moveAl) {
		if (other.boatOnLeft==0) {
			other.cRight -= moveCow;
			other.setcLeft(other.getcLeft() + moveCow);
			other.aRight -= moveAl;
			other.setaLeft(other.getaLeft() + moveAl);
			other.boatOnLeft = 1;
		} else {
			other.cRight += moveCow;
			other.setcLeft(other.getcLeft() - moveCow);
			other.aRight += moveAl;
			other.setaLeft(other.getaLeft() - moveAl);
			other.boatOnLeft = 0;
		}
		other.ValidSet();
//			if (other.isGoal()) {
//				setFoundGoal(true);
//			}
			
//			if ((other.toString()).charAt(5)==1 && !visitedStates.contains(other.toString())) {
//				visitedStates.add(other.toString());
//			}
			System.out.println(other);
	}
	
	public boolean isGoal() {
		if (cRight == 3 && aRight == 3) {
			return true;
		}
		return false;
	}
		
	//return true if this state is identical to parameter state o
	@Override
	public boolean equals(Object o) {
		CAState other = (CAState)o;
		//might need to add getters?
		if (this.getcLeft() == other.getcLeft() && this.getaLeft() == other.getaLeft() && this.boatOnLeft == other.boatOnLeft) {
			return true;
		}
		return false;
	}
	
	//return a string-ified version of this state 
	@Override
	public String toString() {
		return "" + getcLeft() + getaLeft() + cRight + aRight + boatOnLeft + isValid;
	}
	
	public CAState toState(String state) {
		CAState n = new CAState();
		n.setcLeft(state.charAt(0));
		n.setaLeft(state.charAt(1));
		n.cRight = state.charAt(2);
		n.aRight = state.charAt(3);
		n.boatOnLeft = state.charAt(4);
		n.isValid = state.charAt(5);
		
		return n;
	}
//	public boolean isFoundGoal() {
//		return foundGoal;
//	}
//	public void setFoundGoal(boolean foundGoal) {
//		this.foundGoal = foundGoal;
//	}
	public int getcLeft() {
		return cLeft;
	}
	public void setcLeft(int cLeft) {
		this.cLeft = cLeft;
	}
	public int getaLeft() {
		return aLeft;
	}
	public void setaLeft(int aLeft) {
		this.aLeft = aLeft;
	}
	
	//other methods?
}
