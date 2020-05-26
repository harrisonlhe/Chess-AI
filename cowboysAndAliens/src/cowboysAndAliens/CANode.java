package cowboysAndAliens;

import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

public class CANode{
	private CANode parent;
	public CAState currentState;
	ArrayList<CANode> children = new ArrayList<CANode>();
	Queue<CANode> q = new LinkedList<CANode>();
	public ArrayList<String> visitedStates = new ArrayList<String>();

	public CANode(CANode par, CAState current) {
		parent = par;
		currentState = current;		
	}
	
	public CANode(CANode caNode) { //attempt at copying separate failed?
		this.parent = caNode.parent;
		this.currentState = caNode.currentState;
	}
	
	public CANode(CANode par, CAState current, ArrayList<String> sad) {
		parent = par;
		currentState = current;
		visitedStates = sad;
	}

	public void generateChildren() {//add goal check here
		for (int a=0; a<3; a++) {
			for (int c=0; c<3; c++) {
				if (a+c<3 && a+c>0) {
					CANode origNode = new CANode(this.parent, new CAState(this.currentState));//creates copy that doesn't get changed
				
					System.out.println("Original origNode" + origNode);
					CAState orig = new CAState(this.currentState);
					System.out.println("Aliens moved: " + a);
					System.out.println("Cowboys moved: " + c);
					//changing currentState. Need it not to. Now just resets it?
					System.out.println("Original origNode right before applyActionReturn" + origNode);
					CAState temp = currentState.applyActionReturn(currentState, c, a);
					System.out.println("Original origNode after applyActionReturn" + origNode);
					//stop this from editing currentState!!
					System.out.println("act applied");
//					if (temp.isGoal()) {
//						children.add(new CANode(origNode, temp));
//						System.out.println(temp);
//						System.out.println(origNode);
//						System.out.println("WE FOUND IT!");
//						printHistory();
//						return;
//					} //possibly integrate better, two goal checks.
					System.out.println("Temp is " + temp.toString());
//					if (temp.isValidBool()) {
//						children.add(new CANode(origNode, temp));
//					}
					System.out.println("Checking");
					if (temp.toString().charAt(temp.toString().length()-1)==("1".charAt(0)) && !visitedStates.contains(temp.toString())) {
						children.add(new CANode(origNode, temp));
						System.out.println();
						System.out.println();
						System.out.println("TEMP ADDED TO CHILDREN ARRAY!");
						System.out.println("The assigned parent is: " + origNode);
						System.out.println(children.get(children.size()-1).parent);
						visitedStates.add(temp.toString());
						System.out.println("VISITED: " + visitedStates);
					}
					this.currentState = orig;
					System.out.println("This should be same (from orig): " + currentState);
				}
			}
		}
		System.out.println("CHILDREN ARRAYLIST!" + children);
	}
	
	@Override
	public String toString() {
		return (currentState.toString());
	}
	
	public void printHistory() {
		main.foundGoal = true;
		
		//System.out.println(getHistory());
		
		System.out.println("Let's have a history lesson!");
		StringBuffer historyLesson = new StringBuffer();
		CANode historyTime = new CANode(this.parent, new CAState(this.currentState)); //edited to detach from currentState
		System.out.println(this.parent);
		System.out.println("Honored above");
		while (!historyTime.toString().equals("330011")) {
			System.out.println(historyTime);
			historyLesson.append(historyTime.toString() + ", ");
			historyTime = historyTime.parent;
		System.out.println(historyTime + "?");
		}
		System.out.println(historyTime);
		historyLesson.append(historyTime.toString() + ", ");
		historyTime = historyTime.parent;
		System.out.println(historyTime + "?");
		for (int i=0; i<5; i++) {
			System.out.println();
		}
		historyLesson.deleteCharAt(historyLesson.length()-1);
		historyLesson.deleteCharAt(historyLesson.length()-1);
		System.out.println("Below are a list of run through states that got this program to the goal.");
		System.out.println("The notation used is a string with cowboys on left, aliens on left, cowboys on right, aliens on right, boat on left (1 if true, 0 if not), validity check (1 if true, 0 if not).");
		System.out.println("If multiple members of the list all have the same number for the boat on the left checker (5th digit) then they are all states that could be worked on the way to the goal node.");
		System.out.println();
		System.out.println(historyLesson);
		System.out.println("Improper assignment of parents? Seemed to work properly in the generateChildren but not in the solveProblem. Not sure why I can't get a single line of parenthood since everything seemed to be assigned correctly in my debugging.");
		System.out.println("Will resubmit if I solve the issue but I checked most places I suspected there to be problems.");
	}
	
//	public boolean hasParent() {
//		if (this.toString().equals("330011")) {
//			return false;
//		}
//	}
//	
//	public void printHistory() {
//		StringBuffer history = new StringBuffer();
//		CANode orphan = this;
//		history.append(orphan);
//		System.out.println("Successfully generated first orphan");
//		while (orphan.hasParent()) {
//			orphan = orphan.parent;
//			history.append(orphan);
//		}
//		System.out.println(history);
//	}
//	
//	public ArrayList<CANode> getHistory() {//SOME PROBLEM HERE! HISTORY DOES NOT PRINT ACCEPTABLE PATH THOUGH I manually followed its actions and it worked
//		//copied from Harrison
//		ArrayList<CANode> hist = new ArrayList<CANode>();
//		ArrayList<CANode> tempn =  new ArrayList<CANode>();
//		hist.add(this);
//		if (parent!=null) {
//			tempn = parent.getHistory();
//			for (int i=0; i<tempn.size(); i++) {
//				hist.add(tempn.get(i));
//			}
//		}
//		return hist;
//	}
	
//	public String getParent() {
//		if (this.parent.toString().length()>0) {
//		return (this.parent.toString());
//		}
//		return "Hi";
//	}
//	
//	public boolean hasParent() {
//		if (this.parent.toString().length() > 0) {
//			return true;
//		}
//		return false;
//	}
	
	public void setQueueWide(CANode root) { //BFS search since implementation seemed easier
		for (CANode e: root.children) {
			q.add(e);
		}	
	}
	
	public void solveProblem(CANode root) {
		visitedStates.add(new CANode(null, new CAState(3, 3, 1)).toString());
		int i=0;
		while (!root.currentState.isGoal()) {
			System.out.println(currentState.toString());
			System.out.println("Generate children step start");
			root.generateChildren();
			System.out.println("Generate children step finished");
			System.out.println(currentState.toString());
			setQueueWide(root);
			System.out.println("QUEUE SUPPOSEDLY UPDATED: " + q);
			System.out.println(currentState.toString());
			System.out.println();
			System.out.println("QUEUE IS: " + q);
			System.out.println();
			if (children.size() > 1) {
				
			}//PARENT ASSIGNING INCORRECTLY!
			
			root = new CANode(root, q.poll().currentState, visitedStates);
//			for (String s: root.parent.visitedStates) {
//				System.out.println("This ran");
//				root.visitedStates.add(s);
//			}
			System.out.println("Inherited visit: " + root.visitedStates);
			System.out.println("ROOT is: " + root.currentState.toString());
			System.out.println("QUEUE IS: " + q);
			System.out.println("AHHHHHHHHHH!" + i);
			i++;
		}
		root.printHistory();
	}	
	//depth first, use stack. Recurse through tree?
	
	
}
