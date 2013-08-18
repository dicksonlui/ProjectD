
public class Block {
	
	private int myRow;
	private int myCol;
	private int myBotRow;
	private int myBotCol;
	private int myHeight;
	private int myWidth;
	
	public Block (String s) {
		// Block constructor. Takes in a Block object given the text file line input.
		String[] corners = s.split(" ");
		
		// Checks if the line input has four arguments passed in for the myRow myCol corner
		// and this.getBotRow() this.getBotCol() corner. If not, throw an error.
		if (corners.length != 4) {
			throw new IllegalArgumentException("Invalid number of arguments: " +
					"Board construction requires FOUR integers to be passed in");
		}
		
		myRow = Integer.parseInt(corners[0]);
		myCol = Integer.parseInt(corners[1]);
		myBotRow = Integer.parseInt(corners[2]);
		myBotCol = Integer.parseInt(corners[3]);
		
		// Height calculation.
		myHeight = myBotRow - myRow + 1;
		myWidth = myBotCol - myCol + 1;
	}
	
	public int getRow () {
		// Returns the top-most row of the block.
		return myRow;
	}
	
	public int getCol () {
		// Returns the left-most column of the block.
		return myCol;
	}
	
	public int getBotRow () {
		// Returns bottom-most row of the block.
		return myBotRow;
	}
	
	public int getBotCol () {
		// Returns right-most column of the block.
		return myBotCol;
	}
	
	public int getHeight () {
		// Returns the height (top to bottom) of the block.
		return myHeight;
	}
	
	public int getWidth () {
		// Returns the width (left to right) of the block.
		return myWidth;
	}
	
	public int hashCode() {
		// Overrides default Object hashcode method.
		return (myRow)*1337 + (myCol)*337 + (myBotRow)*37 + (myBotCol)*7;
	}
	
	public String toString() {
		// Returns a String representation of the Block the same way Blocks were passed in.
		return "" + myRow + " " + myCol +" " + myBotRow + " " + myBotCol;
	}
	
	public boolean equals (Object other) {
		// Overrides default Object equals method.
		if (myRow == ((Block) other).myRow &&
			myCol == ((Block) other).myCol &&
			myBotRow == ((Block) other).myBotRow &&
			myBotCol == ((Block) other).myBotCol) {
			return true;
		}
		return false;
	}
	
	public int[][] generateMoves (boolean[][] space) {
		// When called on a block, generates all possible moves.
		int[][] moves = new int[4][];
		
		// Checks each direction.
		if (canMoveUp(space)) {
			moves[0] = new int[] {myRow, myCol, myRow - 1, myCol};
		}
		if (canMoveDown(space)) {
			moves[1] = new int[] {myRow, myCol, myRow + 1, myCol};
		}
		if (canMoveRight(space)) {
			moves[2] = new int[] {myRow, myCol, myRow, myCol + 1};
		}
		if (canMoveLeft(space)) {
			moves[3] = new int[] {myRow, myCol, myRow, myCol - 1};
		}
		return moves;
	}
	
	public boolean canMoveUp(boolean[][] space) { 
		// If the block is touching the top row, return false. 
		if (myRow == 0) {
			return false;
		}
		
		// Otherwise, check each space above the block in the boolean array.
		for (int i = myCol; i <= myBotCol; i++) {
			if (space[myRow-1][i]) {
				return false;
			}
		}
		return true;
	}
	
	public boolean canMoveLeft(boolean[][] space) { 
		// If the block is touching the left column, return false.
		if (myCol == 0) {
			return false;
		}
		
		// Otherwise, check each space to the left of the block in the boolean array.
		for (int i = myRow; i <= myBotRow; i++) {
			if (space[i][myCol-1]) {
				return false;
			}
		}
		return true;
	}
	
	public boolean canMoveDown(boolean[][] space) { 

		if (myBotRow == space.length-1) {
			return false;
		}
		
		// Otherwise, check each space below the block in the boolean array.
		for (int i = myCol; i <= myBotCol; i++) {	
			if (space[myBotRow+1][i]) {
				return false;
			}
		}
		return true;
	}

	public boolean canMoveRight (boolean[][] space) { 
		// If the block is touching the right column, return false.
		if (myBotCol == space[0].length-1) {
			return false;
		}
		
		// Otherwise, check each space to the left of the block in the boolean array.
		for (int i = myRow; i <= myBotRow; i++) {	
			if (space[i][myBotCol+1]) {
				return false;
			}
		}
		return true;
	}
}
	

