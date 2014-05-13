package ec327.project.x5tictactoe;

import android.util.Log;

/*
 * @author Shantanu Bobhate
 * EC327, Spring 2014
 * Boston University, Boston, MA
 * 
 * Copyright (C) 2014 Shantanu Bobhate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Board Checker Class
 	- The class that communicates with Board class
 	- Gives feedback to the Board on the status of the game
 	- Used by the automated player to make moves wisely
 * 
 * Private Members
 	- The get row winner method which gets the row winner if exists
 	- The get column winner method which gets the column winner if exists
 	- The get diagonal winner method which gets the diagonal winner if exists
 	- The is tie or continue method which checks if the game is a tie or to be continued
 	- The get advanced row winner method which predicts a row winner
 	- The get advanced column winner method which predicts a column winner
 	- The get advanced diagonal winner method which predicts a diagonal winner
 * Protected Members
 	- The board status method which returns a winner if exist else returns 'n'
 	- The advanced board method which predicts a winner otherwise returns 'n'
 *
 */

@SuppressWarnings("unused")
public class BoardChecker {
	
	BoardChecker ()
	{
		// Log.i("Debug", "Board Checker Created.");
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
	
	protected char advancedBoardStatus (char[][] board, int position)
	{
		char advancedRowWinner = getAdvancedRowWinner (board, position);
		char advancedColumnWinner = getAdvancedColumnWinner (board, position);
		char advancedDiagonalWinner = getAdvancedDiagonalWinner(board);
		if (advancedRowWinner != 'n')
			return advancedRowWinner;
		else if (advancedColumnWinner != 'n')
			return advancedColumnWinner;
		else if (advancedDiagonalWinner != 'n')
			return advancedDiagonalWinner;
		else
			return isTieOrContinue(board);
	}
	
	private char getAdvancedRowWinner (char[][] board, int position)
	{
		int referenceRow = (int) Math.ceil(position / 5);
		char referenceCell;
        int counter = 0;
        // Need to change 0
        referenceCell = board[referenceRow][0];
        for (int k = 1; k < board.length - 2; k++)
        {
            if (referenceCell == board[referenceRow][k])
            {
                counter += 1;
            }
        }
        if (counter == 2 && referenceCell != '#')
        {	
            return referenceCell;
        }
        else
        {
        	counter = 0;
        	referenceCell = board[referenceRow][1];
        	for (int k = 2; k < board.length - 1; k++)
	        {
	            if (referenceCell == board[referenceRow][k])
	            {
	                counter += 1;
	            }
	        }
        	if (counter == 2 && referenceCell != '#')
	        {	
	            return referenceCell;
	        }
        	else
	        {
	        	counter = 0;
	        	referenceCell = board[referenceRow][2];
	        	for (int l = 3; l < board.length; l++)
		        {
		            if (referenceCell == board[referenceRow][l])
		            {
		                counter += 1;
		            }
		        }
	        	if (counter == 2 && referenceCell != '#')
		        {	
		            return referenceCell;
		        }
	        }
        }
	    return 'n';
	}
	
	private char getAdvancedColumnWinner (char[][] board, int position)
	{
		int referenceColumn = position % 5;
	    char referenceCell;
        int counter = 0;
        referenceCell = board[0][referenceColumn];
        for (int k = 1; k < board.length - 2; k++)
        {
            if (referenceCell == board[k][referenceColumn])
                counter ++;
        }
        if (counter == 2 && referenceCell != '#')
        {
            return referenceCell;
        }
        else
        {
        	counter = 0;
	        referenceCell = board[1][referenceColumn];
	        for (int k = 2; k < board.length - 1; k++)
	        {
	            if (referenceCell == board[k][referenceColumn])
	                counter ++;
	        }
	        if (counter == 2 && referenceCell != '#')
	        {
	            return referenceCell;
	        }
	        else
	        {
	        	counter = 0;
		        referenceCell = board[2][referenceColumn];
		        for (int k = 3; k < board.length; k++)
		        {
		            if (referenceCell == board[k][referenceColumn])
		                counter ++;
		        }
		        if (counter == 2 && referenceCell != '#')
		        {
		            return referenceCell;
		        }
	        }
        }
	    return 'n';
	}
	
	private char getAdvancedDiagonalWinner (char[][] board)
	{
		if ((board[4][0] == board [3][1] && board[4][0] == board [2][2] && board[4][0] != '#') || 
			(board[3][1] == board [2][2] && board[3][1] == board [1][3] && board[3][1] != '#') ||
			(board[2][2] == board [1][3] && board[2][2] == board [0][4] && board[2][2] != '#') ||
			(board[0][0] == board [1][1] && board[0][0] == board [2][2] && board[0][0] != '#') ||
			(board[1][1] == board [2][2] && board[1][1] == board [3][3] && board[1][1] != '#') ||
			(board[2][2] == board [3][3] && board[2][2] == board [4][4] && board[2][2] != '#'))
			return 'x';
		else return 'n';
	}
	
}