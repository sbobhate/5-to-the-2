package ec327.project.x5tictactoe;

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
 * Automated Player Class
 	- Plays against the user in single player modes
 	- Able to make random moves
 	- Able to counter user moves
 	- Does not play offensive moves
 *
 */

import java.util.Random;

import android.util.Log;

@SuppressWarnings("unused")
public class AutomatedPlayer {
	
	Board currentBoard;
	char[] board;
	int index;
	
	AutomatedPlayer (Board b)
	{
		currentBoard = b;
		board = currentBoard.getBoard();
		// Log.i("Debug", "Automated Player Created.");
	}
	
	private Random generator = new Random ();

	protected void makeDefensiveMove ()
	{
		// Log.i("Debug", "Defensive Move Called");
		if (!getDefensiveMove())
			if (!getAdvancedDefensiveMove())
				makeRandomMove();
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
		// Log.i("Debug", "Random Move Made.");
	}
	
	protected int getIndex ()
	{
		return index;
	}
	
	private int generateRandomNumber (int n)
	{
		return generator.nextInt(n);
	}
	
	private boolean getDefensiveMove ()
	{
		for (int i = 0; i < board.length; i++)
		{
			if (board[i] == '#')
			{
				currentBoard.writeToBoard(i, 'x');
				if (currentBoard.getBoardStatus() == 'x')
				{
					currentBoard.writeToBoard(i, 'o');
					// Log.i("Debug", "Normal Defensive Move Made");
					index = i;
					return true;
				}
				currentBoard.writeToBoard(i, '#');
			}	
		}
		// Log.i("Debug", "No Normal Defensive Move To Be Made");
		return false;
	}
	
	private boolean getAdvancedDefensiveMove ()
	{
		for (int i = 0; i < board.length; i++)
		{
			if (board[i] == '#')
			{
				currentBoard.writeToBoard(i, 'x');
				if (currentBoard.getAdvancedBoardStatus(i) == 'x')
				{
					currentBoard.writeToBoard(i, 'o');
					// Log.i("Debug", "Advanced Defensive Move Made");
					index = i;
					return true;
				}
				currentBoard.writeToBoard(i, '#');
			}
		}
		// Log.i("Debug", "No Advanced Defensive Move To Be Made");
		return false;
	}
	
}
