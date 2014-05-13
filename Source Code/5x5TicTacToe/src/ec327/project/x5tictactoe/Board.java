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
 
 * Board Class
 	- The board object
 	- Contains the digital board and the functions to makes make operations on the board
 	- Communicates with the board checker class
 * 
 * Private Members
 	- The Board sizes
	- The single dimensional board and the double dimensional board
	- The update double dimensional board method to set it equal to the single dimensional board
	- The Board Checker object checker
	- The update double board method that updates the 2 dimensional board
 *
 * Protected Members
 	- The board constructor that creates a new Game Checker
 	- The get board method that returns the single dimensional board
 	- The clear method to clear the board
 	- The write to board method that adds an 'entry' at a 'position' in the board
 	- The get board status method that returns the status of the game
 	- The get advanced board status method that returns the advanced status of a game
 *
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
		// Log.i("Debug", "Board Created.");
		checker = new BoardChecker ();
	}
	
	protected char[] getBoard ()
	{
		return singleDimenBoard;
	}
	
	protected void clear ()
	{
		// Log.i("Debug", "Clearing Board.");
		for (int i = 0; i < SIZE; i++)
		{
			singleDimenBoard[i] = '#';
		}
		updateDoubleBoard ();
	}
	
	protected void writeToBoard (int position, char entry)
	{
		// Log.i("Debug", "Writing " + entry + " to Board at " + position + ".");
		singleDimenBoard[position] = entry;
		updateDoubleBoard ();
	}
	
	protected char getBoardStatus ()
	{
		// Log.i("Debug", "Returning Board Status.");
		return checker.boardStatus(doubleDimenBoard);
	}
	
	protected char getAdvancedBoardStatus (int position)
	{
		// Log.i("Debug", "Returning Advanced Board Status.");
		return checker.advancedBoardStatus(doubleDimenBoard, position);
	}
	
	private void updateDoubleBoard ()
	{
		// Log.i("Debug", "Updating Double Board.");
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