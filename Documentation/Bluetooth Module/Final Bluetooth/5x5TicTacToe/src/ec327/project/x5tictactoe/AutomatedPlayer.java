package ec327.project.x5tictactoe;

import java.util.Random;

import android.util.Log;

public class AutomatedPlayer {
	
	Board currentBoard;
	char[] board;
	int index;
	
	AutomatedPlayer (Board b)
	{
		currentBoard = b;
		board = currentBoard.getBoard();
		Log.i("Debug", "Automated Player Created.");
	}
	
	private Random generator = new Random ();
	
	protected void makeDefensiveMove ()
	{
//		int[] advancedMoves = currentBoard.getAdvancedMoves();
//		if (advancedMoves[0] != 99 && advancedMoves[1] != 99)
//		{
//			int position = (5*advancedMoves[0]) + advancedMoves[1];
//			currentBoard.writeToBoard(position, 'o');
//		}
//		else
//		{
			index = getDefensiveMode();
			if (index == 99)
				makeRandomMove ();
			else
				Log.w("Debug", "Defensive Move Made");
//		}
	}
	
	private void makeRandomMove ()
	{
		int size = board.length;
		index = generateRandomNumber(size);
		if (board[index] == '#')
			currentBoard.writeToBoard(index, 'o');
		else
		{
			while (board[index] != '#')
			{
				index = generateRandomNumber(size);
			}
			currentBoard.writeToBoard(index, 'o');
		}
		Log.w("Debug", "Random Move Made.");
	}
	
	protected int getIndex ()
	{
		return index;
	}
	
	private int generateRandomNumber (int n)
	{
		return generator.nextInt(n);
	}
	
	private int getDefensiveMode ()
	{
		for (int i = 0; i < board.length; i++)
		{
			if (board[i] == '#')
			{
				currentBoard.writeToBoard(i, 'x');
				if (currentBoard.getBoardStatus() == 'x') 
				{
					currentBoard.writeToBoard(i, 'o');
					return i;
				}
				else if (currentBoard.getBoardStatus() == 'x')
				{
					currentBoard.writeToBoard(i, 'o');
					return i;
				}
				currentBoard.writeToBoard(i, '#');
			}	
		}
		return 99;
	}
	
}
