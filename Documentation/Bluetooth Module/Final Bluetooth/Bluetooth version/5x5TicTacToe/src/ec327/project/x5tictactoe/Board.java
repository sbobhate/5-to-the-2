package ec327.project.x5tictactoe;

import android.util.Log;

/*
 * Board class
 * Private Members
 	- The Board sizes
	- The single dimensional board and the double dimensional board
	- The update double dimensional board method to set it equal to the single dimensional board
 * Protected Members
 	- The board constructor that creates a new Game Checker
 	- The get board method that returns the single dimensional board
 	- The clear method to clear the board
 	- The write to board method that adds an 'entry' at a 'position' in the board
 	- The get board status method that returns the status of the game
 */

public class Board {
	
	private static int SIZE = 25;
	private static int HEIGHT = 5;
	private static int WIDTH = 5;
	
	private char singleDimenBoard [] = new char [SIZE];
	private char doubleDimenBoard [][] = new char [HEIGHT][WIDTH];
	
	private static BoardChecker checker;
	
	protected Board ()
	{
		Log.i("Debug", "Board Created.");
		checker = new BoardChecker ();
	}
	
	protected char[] getBoard ()
	{
		return singleDimenBoard;
	}
	
	protected void clear ()
	{
		Log.i("Debug", "Clearing Board.");
		for (int i = 0; i < SIZE; i++)
		{
			singleDimenBoard[i] = '#';
		}
		updateDoubleBoard ();
	}
	
	protected void writeToBoard (int position, char entry)
	{
		Log.i("Debug", "Writing " + entry + " to Board at " + position + ".");
		singleDimenBoard[position] = entry;
		updateDoubleBoard ();
	}
	
	protected char getBoardStatus ()
	{
		Log.i("Debug", "Returning Board Status.");
		return checker.boardStatus(doubleDimenBoard);
	}
	
	protected int[] getAdvancedMoves ()
	{
		return checker.getAdvancedStatus(doubleDimenBoard);
	}
	
	private void updateDoubleBoard ()
	{
		Log.i("Debug", "Updating Double Board.");
		StringBuffer buffer = new StringBuffer ();
		int sizeCounter = 0;
		for (int i = 0; i < HEIGHT; i++)
		{
			for (int j = 0; j < WIDTH; j++)
			{
				doubleDimenBoard[i][j] = singleDimenBoard[sizeCounter];
				sizeCounter ++;
				buffer.append(doubleDimenBoard[i][j]);
			}
			buffer.append('\n');
		}
	}
	
}