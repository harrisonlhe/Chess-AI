package chess;

public class LocationPair {
	private int posX;
	private int posY;
	
	public LocationPair(int a, int b){
		this.posX=a;
		this.posY=b;
	}
	
	public int x() {
		return posX;
	}
	public int y() {
		return posY;
	}
	public boolean onBoard() {
		if(posX<0 || posX>7) {
			return false;
		}
		if(posY<0 || posY>7) {
			return false;
		}
		return true;
	}
}
