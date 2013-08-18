import java.util.*;

public class Solver {
	
	private HashSet<HashSet<Block>> visited;
	public HashSet<Block> myGoal;
	private boolean[] debugOptions;
	private int numberofMoves = 0;
	
	private PriorityQueue<Tray> fringe;
	
	public Solver(InputSource start, InputSource end, boolean[] debug) throws SolvedPuzzle {
		debugOptions = debug;
		myGoal = new HashSet<Block>();
		
		// Goal block storage.
		for (String line = end.readLine(); line != null; line = end.readLine()) {
			Block x = new Block(line);
			myGoal.add(x);
		}
		fringe = new PriorityQueue<Tray>();
		visited = new HashSet<HashSet<Block>>();
		
		// Adds tray as long as tray has not been visited previously.
		Tray myTray = new Tray(start, myGoal);
		if (visited.add(myTray.myBlocks)) {
			fringe.add(myTray);
		}
	}
	
	public void makeMove() throws SolvedPuzzle {
		Tray previous = fringe.poll();
		for (int[] move : previous.generateMoves()) {
			// If no moves exist, return.
			if (move == null) {
				return;
			}
			
			// Debugging options.
			if (debugOptions[0]) {
				System.out.print("Move:");
				for (int i = 0; i < 4; i++)
					System.out.print(" " + move[i]);
					System.out.println();
			}
			if (debugOptions[1]) {
				numberofMoves++;
			}
			
			// If tray has not already been visited, add it to the queue.
			Tray newTray = new Tray(previous, move, myGoal);
			if (visited.add(newTray.myBlocks)) {
				fringe.add(newTray);
			}
		}
	}
	
	public static void main (String[] args) {
		Long time = System.currentTimeMillis();
		InputSource start;
		InputSource goal;
		Solver mySolver = null;
		boolean[] debugOptions = new boolean[3];
		if (args.length == 3) {
			String debug = args[0];
			
			if (!debug.startsWith("-o")) {
				throw new IllegalArgumentException(
				"Your debugging argument must start with -o followed by corresponding codes. For help, please use -ooptions as your debugging argument.");
			}
			
			// Checking the debugging options.
			String option = debug.substring(2);
			
			if (option.equals("options")) {
				System.out.println("Debugging Options:");
				System.out.println("***********************************");
				System.out.println("Your debugging options are:");
				System.out.println("m: Prints all of the moves that were considered.");
				System.out.println("n: Prints the number of moves taken.");
				System.out.println("r: Show the runtime of the solver");
				System.out.println("***********************************");
				System.out.println("How to use the debugging options:");
				System.out.println("Pass in debugging argument as follows:");
				System.out.println("If you want to print all of the moves considered,");
				System.out.println("the number of moves taken, and the runtime, simply pass in:");
				System.out.println("-omnr");
				System.out.println("The order of the letters following the o do no matter.");

			} else {
				for (int i = 0; i < option.length(); i++) {
					String current = option.substring(i, i + 1);
					if (current.equals("m")) {
						// How to handle printing all moves considered.
						debugOptions[0] = true;
					}
					if (current.equals("n")) {
						// How to handle printing all moves considered.
						debugOptions[1] = true;
					}
					if (current.equals("r")) {
						// How to handle runtime calculation.
						debugOptions[2] = true;
					} else {
						System.out.println("No such debug option: " + current + ".");
					}
				}
			}
			
			start = new InputSource(args[1]);
			goal = new InputSource (args[2]);
		}
		
		else {
			start = new InputSource (args[0]);
			goal = new InputSource (args[1]);
		}
		
		try {
			// Start the solver.
			mySolver = new Solver(start, goal, debugOptions);
			while (mySolver.fringe.size() > 0) {
				mySolver.makeMove();
			}
		} catch (SolvedPuzzle ex) {
			if (debugOptions[1]) {
				System.out.println("This puzzle required " + mySolver.numberofMoves + " moves to solve!");
			}
			if (debugOptions[2]) {
				System.out.println("This puzzle took " + (System.currentTimeMillis() - time) + "ms to solve.");
			}
			return;
		}
		
		return;
	}
}
