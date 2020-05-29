package chess;

public class MoveValidityPair {
	private Move m;
	private boolean b;
	
	public MoveValidityPair(Move m, boolean b){
		this.m=m;
		this.b=b;
	}
	
	public Move move() {
		return m;
	}
	public boolean validity() {
		return b;
	}
}
