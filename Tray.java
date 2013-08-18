import java.util.*;

public class Tray implements Comparable <Tray> {
	
	private int myHeight;
	private int myWidth;
	private boolean[][] mySpace;
	public HashSet<Block> myBlocks;
	private HashSet<Block> myGoalBlocks;
	private Tray previousTray;
	public int[] nextMove;
	public int myScore;
	
	public Tray (Tray previous, int[] shift, HashSet<Block> goal) throws SolvedPuzzle {
		// Constructor for a Tray given the previous tray and a move array.
		// Creates a copy of the board and moves the block the given direction.
		myWidth = previous.myWidth;
		myHeight = previous.myHeight;
		mySpace = new boolean[myHeight][myWidth];
		myBlocks = new HashSet<Block>();
		myGoalBlocks = goal;
		previousTray = previous;
		nextMove = shift;
		String blockString;
		
		for (Block currBlock: previous.myBlocks) {
			// Moves the Block.
			if (currBlock.getRow() == nextMove[0] && currBlock.getCol() == nextMove[1]) {
				if (nextMove[0] == nextMove[2]) {
					if (nextMove[1] > nextMove[3]) {
						blockString = "" + nextMove[2] + " " + nextMove[3] + " " + currBlock.getBotRow() + " " + (currBlock.getBotCol()-1);
						currBlock = new Block (blockString);
					}
					else {
						blockString = "" + nextMove[2] + " " + nextMove[3] + " " + currBlock.getBotRow() + " " + (currBlock.getBotCol()+1);
						currBlock = new Block (blockString);
					}
				} else {
					if (nextMove[0] > nextMove[2]) {
						blockString = "" + nextMove[2] + " " + nextMove[3] + " " + (currBlock.getBotRow()-1) + " " + currBlock.getBotCol();
						currBlock = new Block (blockString);
					}
					else {
						blockString = "" + nextMove[2] + " " + nextMove[3] + " " + (currBlock.getBotRow()+1) + " " + currBlock.getBotCol();
						currBlock = new Block (blockString);
					}
				}
			} 
			myBlocks.add(currBlock);
			
			// Adjusts the boolean array to return true on the spaces occupied by the block.
			for (int i = currBlock.getRow(); i <= currBlock.getBotRow(); i++) {
				for (int j = currBlock.getCol(); j <= currBlock.getBotCol(); j++) {
					mySpace[i][j] = true;
				}
			}
		}
		
		// Call scoring method.
		score();
	}
	
	public Tray(InputSource start, HashSet<Block> goal) throws SolvedPuzzle {
		// Tray constructor for the starting board.
		String nextLine = start.readLine();
		String[] size = nextLine.split(" ");
		
		myHeight = Integer.parseInt(size[0]);
		myWidth = Integer.parseInt(size[1]);

		mySpace = new boolean[myHeight][myWidth];
		myBlocks = new HashSet<Block>();
		myGoalBlocks = goal;
		
		// Tray, in the beginning, does not have a previous move.
		previousTray = null;
		nextMove = null;
		
		// Parses the user input and adds the blocks to the Hashset.
		for (nextLine = start.readLine(); nextLine != null; nextLine = start.readLine()) {
			Block currentBlock = new Block(nextLine);
			myBlocks.add(currentBlock);
			// Adjusts the mySpace boolean array to return true on the spaces occupied by a block.
			for (int i = currentBlock.getRow(); i <= currentBlock.getBotRow(); i++) {
				for (int j = currentBlock.getCol(); j <= currentBlock.getBotCol(); j++) {
					mySpace[i][j] = true;
				}
			}
		}
		
		// Call scoring method.
		score();
	}

	public String toString () {
		// Returns a String presentation of the Tray identical to how it would look if it was passed in
		// from a text file.
		String str = "" + myHeight + " " + myWidth + "\n";
		
		// Add each block to the String representation.
		for (Block currBlock: myBlocks) {
			str = currBlock.getRow() + " " + 
				  currBlock.getCol() + " " + 
				  currBlock.getBotRow() + " " + 
				  currBlock.getBotCol() + "\n";
		}
		return str;
	}
	
	public int[][] generateMoves () {
		// Generates all possible moves.
		int[][] moves = new int[3 * myBlocks.size()][];
		int i = 0;
		for (Block b: myBlocks) {
			for (int[] m : b.generateMoves(mySpace)) {
				if (m != null) {
					moves[i] = m;
					i++;
				}
			}
		}
		
		// Return the 2D Array of generated moves.
		return moves;
	}
	
	public void score() throws SolvedPuzzle {
		// Applies a score to the tray and checks for completion of the pzuzle.
		int tempScore = 0;
		myScore = 0;
		
		// If goalChecking stays true through testing all the blocks, a solution has been reached.
		boolean goalCheck = true;
		// True is the current Block is contained within the goal block set.
		boolean goalBlockCheck;
		
		for (Block goalBlock: myGoalBlocks) {
			tempScore = 0;
			goalBlockCheck = false;
			for (Block currBlock: myBlocks) {
				// If the block is equal to the goal block, give a score of 1 and set the goal block indicator
				// to true.
				if (goalBlock.equals(currBlock)) {
					tempScore = 1;
					goalBlockCheck = true;
				}
				// Otherwise, assign a score to the board based on how close the current block is to the goal block.
				else if (currBlock.getWidth() == goalBlock.getWidth() && currBlock.getHeight() == goalBlock.getHeight())
					tempScore = Math.max(Math.abs(currBlock.getCol() - goalBlock.getCol()) + Math.abs(currBlock.getRow() - goalBlock.getRow()), tempScore);
			}
			if (!goalCheck || !goalBlockCheck) {
				goalCheck = false;
			}
			myScore += tempScore;
		}
		
		// Puzzle solved!
		if (goalCheck == true) {
			printPath();
			throw new SolvedPuzzle();
		}
	}

	public int compareTo (Tray other) {
		// Used to implement Comparable interface for PriorityQueue.
		return other.myScore - myScore;
	}
	
	public void printPath () {
		// When a solution is reached, prints the path recursively.
		if (previousTray != null) {
			previousTray.printPath();
			System.out.println(nextMove[0] + " " + nextMove[1] + " " + nextMove[2] + " " + nextMove[3]);
		}
	}

	public boolean isOK() {
		// Checks if Tray is properly initialized with a positive height and width.
		if (myHeight < 0 || myWidth < 0) {
			return false;
		}
		
		// Checks if boolean array is properly initialized.
		if (mySpace.length != myHeight || mySpace[0].length != myWidth) {
			return false;
		}

		// Checks if blocks are properly occupying the boolean array.
		for (Block currBlock: myBlocks) {
			for (int i = currBlock.getCol(); i <= currBlock.getBotCol(); i++) {
				for (int j = currBlock.getRow(); j <= currBlock.getBotRow(); j++) {
					if (!mySpace[i][j]) {
						return false;
					}
				}
			}
		}
		
		// Pass!
		return true;
	}
}