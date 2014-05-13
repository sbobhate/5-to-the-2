package ec327.project.x5tictactoe;

import android.util.Log;

/*
 * Board Checker Class
 * Private Members
 	- The get row winner method which gets the row winner if exists
 	- The get column winner method which gets the column winner if exists
 	- The get diagonal winner method which gets the diagonal winner if exists
 	- The is tie or continue method which checks if the game is a tie or to be continued
 * Protected Members
 	- The board status method which returns a winner if exist else returns 'n'
 */

public class BoardChecker {
	
	private int[] advancedStatus;
	
	BoardChecker ()
	{
		Log.i("Debug", "Board Checker Created.");
	}
	
	protected int[] getAdvancedStatus (char[][] board)
	{
		advancedStatus = advancedRowIndex(board);
		return advancedStatus;
	}
	
	protected char boardStatus (char[][] board)
	{
		char rowWinner = getRowWinner (board);
		char columnWinner = getColumnWinner (board);
		char diagonalWinner = getDiagonalWinner(board);
		if (rowWinner != 'n')
			return rowWinner;
		else if (columnWinner != 'n')
			return columnWinner;
		else if (diagonalWinner != 'n')
			return diagonalWinner;
		else
			return isTieOrContinue (board);
	}
	
	private char getRowWinner (char[][] board)
	{
		char referenceCell;
	    for (int referenceRow = 0; referenceRow < board.length; referenceRow ++)
	    {
	        int counter = 0;
	        // Need to change 0
	        referenceCell = board[referenceRow][0];
	        for (int k = 1; k < board.length - 1; k++)
	        {
	            if (referenceCell == board[referenceRow][k])
	            {
	                counter += 1;
	            }
	        }
	        if (counter == 3 && referenceCell != '#')
	        {	
	            return referenceCell;
	        }
	        else
	        {
	        	counter = 0;
	        	referenceCell = board[referenceRow][1];
	        	for (int k = 2; k < board.length; k++)
		        {
		            if (referenceCell == board[referenceRow][k])
		            {
		                counter += 1;
		            }
		        }
	        	if (counter == 3 && referenceCell != '#')
		        {	
		            return referenceCell;
		        }
	        }
	    }
	    return 'n';
	}
	
	private char getColumnWinner (char[][] board)
	{
	    char referenceCell;
	    for (int referenceColumn = 0; referenceColumn < board.length; referenceColumn ++)
	    {
	        int counter = 0;
	        referenceCell = board[0][referenceColumn];
	        for (int k = 1; k < board.length - 1; k++)
	        {
	            if (referenceCell == board[k][referenceColumn])
	                counter ++;
	        }
	        if (counter == 3 && referenceCell != '#')
	        {
	            return referenceCell;
	        }
	        else
	        {
	        	counter = 0;
		        referenceCell = board[1][referenceColumn];
		        for (int k = 2; k < board.length; k++)
		        {
		            if (referenceCell == board[k][referenceColumn])
		                counter ++;
		        }
		        if (counter == 3 && referenceCell != '#')
		        {
		            return referenceCell;
		        }
	        }
	    }
	    return 'n';
	}
	
	// Needs fix ***********
	private char getDiagonalWinner (char[][] board)
	{
	    if (((board [0][0] == board [1][1]) && (board [0][0] == board [2][2])) && (board [0][0] == board [3][3]) && (board [0][0] != '#'))
	    {
	        return board [0][0];
	    }
	    if (((board [1][1] == board [2][2]) && (board [1][1] == board [3][3])) && (board [4][4] == board [3][3]) && (board [1][1] != '#'))
	    {
	        return board [1][1];
	    }
	    if (((board [4][0] == board [3][1]) && (board [4][0] == board [2][2])) && board [4][0] == board [1][3] && (board [4][0] != '#') )
	    {
	        return board[4][0];
	    }
	    if (((board [3][1] == board [2][2]) && (board [3][1] == board [1][3])) && board [3][1] == board [0][4] && (board [3][1] != '#') )
	    {
	        return board[3][1];
	    }
	    if (((board [0][1] == board [1][2]) && (board [0][1] == board [2][3])) && board [0][1] == board [3][4] && (board [0][1] != '#') )
	    {
	        return board[0][1];
	    }
	    if (((board [1][0] == board [2][1]) && (board [1][0] == board [3][2])) && board [1][0] == board [4][3] && (board [1][0] != '#') )
	    {
	        return board[1][0];
	    }
	    if (((board [3][0] == board [2][1]) && (board [3][0] == board [1][2])) && board [3][0] == board [0][3] && (board [3][0] != '#') )
	    {
	        return board[3][0];
	    }
	    if (((board [4][1] == board [3][2]) && (board [4][1] == board [2][3])) && board [4][1] == board [1][4] && (board [4][1] != '#') )
	    {
	        return board[4][1];
	    }
	    else
	    {
	        return 'n';
	    } 
	}
	
	private char isTieOrContinue (char[][] board)
	{
	    int counter = 0;
	    for (int row = 0; row < board.length; row ++)
	    {
	        for (int column = 0; column < board.length; column ++)
	        {
	            if (board[row][column] == '#')
	                counter ++;
	        }
	    }
	    if (counter != 0)
	        return 'c';
	    else
	        return 't';
	}
	
	public int[] advancedRowIndex (char[][] board)
	{
		int[] location = {99, 99};
		for (int row = 0; row < board.length; row ++)
		{
			for (int column = 0; column < board.length - 2; column++)
			{
				if(board[row][column] == '#')
					if (board[row][column + 1] == 'x' && board[row][column + 2] == 'x')
					{
						location[0] = row;
						location[1] = column;
						return location;
					}
			}
		}
		for (int row = 0; row < board.length; row ++)
		{
			for (int column = 2; column < board.length; column++)
			{
				if(board[row][column] == '#')
					if (board[row][column - 1] == 'x' && board[row][column - 2] == 'x')
					{
						location[0] = row;
						location[1] = column;
						return location;
					}
			}
		}
		return location;
	}
	
}