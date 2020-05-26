package cowboysAndAliens;

public class main {
	public static boolean foundGoal = false;
	
	public static void main(String[] args) {
		long startTime = System.nanoTime();
	
		CANode root = new CANode(null, new CAState(3, 3, 1));
		root.solveProblem(root);
		
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
		
	}
}
