package chess;

public class Location {
	private int posX;
	private int posY;
	
	public Location(int a, int b){
		this.posX=a;
		this.posY=b;
	}
	
	public int x() {
		return posX;
	}
	public int y() {
		return posY;
	}
}
