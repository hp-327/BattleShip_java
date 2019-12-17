package package_p1;

public class BattleShipGameClass
{
	public static final char AIRCRAFT_CARRIER = 'A';
	private final int AIRCRAFT_CARRIER_LENGTH = 5;
	private int arrayHeight;
	private int arrayWidth;
	public static final char BATTLESHIP = 'B';
	private final int BATTLESHIP_LENGTH = 4;
	public static final int COMPUTER = 103;
	private ShipClass[][] computerArray;
	public static final char CRUISER = 'C';
	private final int CRUISER_LENGTH = 3;
	public static final char DESTROYER = 'D';
	private final int DESTROYER_LENGTH = 2;
	public static final char FRIGATE = 'F';
	private final int FRIGATE_LENGTH = 1;
	public static final char HORIZONTAL = 'H';
	public static final int HUMAN = 102;
	private ShipClass[][] humanArray;
	private final char SPACE = ' ';
	public static final char VERTICAL = 'V';

	/**
	 * Configures BattleShip playing board with height and width
	 *
	 * Initializes array of ShipClass objects
	 *
	 * @param height - integer value for number of rows in array
	 * @param width  - integer value for number of columns in array
	 */
	public BattleShipGameClass(int height, int width)
	{
		humanArray = new ShipClass[height][width];
		computerArray = new ShipClass[height][width];
		arrayHeight = height;
		arrayWidth = width;
		int row;
		int column;
		for (row = 0; row < arrayHeight; row++)
		{
			for (column = 0; column < humanArray[row].length; column++)
			{
				humanArray[row][column] = new ShipClass();
				computerArray[row][column] = new ShipClass();
			}
		}
	}

	/**
	 * Creates copy of other battleship Object
	 *
	 * @param copied - Battleship object to be copied
	 */

	public BattleShipGameClass(BattleShipGameClass copied)
	{
		int row;
		int column;
		arrayHeight = copied.arrayHeight;
		arrayWidth = copied.arrayWidth;

		humanArray = new ShipClass[arrayHeight][arrayWidth];
		computerArray = new ShipClass[arrayHeight][arrayWidth];

		for (row = 0; row < arrayHeight; row++)
		{
			for (column = 0; column < humanArray[row].length; column++)
			{
				humanArray[row][column] = new ShipClass(copied.humanArray[row][column]);
				computerArray[row][column] = new ShipClass(copied.computerArray[row][column]);
			}
		}

	}

	/**
	 *
	 * @param testLetter - character letter representing a ship; letter is compared
	 *                   to all possible ship letters Note: no if/else/else if
	 *                   statements may be used in this method
	 * @return: Boolean result of ship letter test
	 */

	private boolean checkShipLetter(char testLetter)
	{
		return testLetter == AIRCRAFT_CARRIER || testLetter == BATTLESHIP || testLetter == CRUISER
				|| testLetter == DESTROYER || testLetter == FRIGATE;
	}

	/**
	 * Computer generates random values within the field and fires missile using
	 * fireMissile method Note: Must check player array to see if the location found
	 * has already been missed, must use isMissInArray Note: This method can be
	 * significantly improved to make the game more interesting
	 *
	 * @return: success of missile fire
	 */
	public boolean computerFiresMissile()
	{
		int xPos;
		int yPos;
		do
		{
			xPos = (getRandBetween(0, arrayWidth - 1));
			yPos = (getRandBetween(0, arrayHeight - 1));
		} 
		while (isMissInArray(HUMAN, xPos, yPos));
		return fireMissile(COMPUTER, xPos, yPos);

	}

	/**
	 * Displays player and computer screens
	 * 
	 * @param displayState- integer indication as to showing fields; EXPOSED_STATE
	 *        only shows exposed items; HIDDEN_STATE shows all hidden items
	 */

	public void displayFields(int displayState)
	{
		int row;
		int column;
		printChars(8, SPACE);
		printCentered("HUMAN", arrayWidth);
		printChars(4, SPACE);
		printCentered("COMPUTER", arrayHeight);
		System.out.println();
		for (row = 0; row < arrayHeight; row++)
		{
			printChars(8, SPACE);
			for (column = 0; column < arrayWidth; column++)
			{
				System.out.print(humanArray[row][column].getLocationLetter(displayState));
			}
			printChars(4, SPACE);
			for (column = 0; column < arrayWidth; column++)
			{
				System.out.print(computerArray[row][column].getLocationLetter(displayState));
			}
			System.out.println();
		}
	}

	/**
	 * Accepts player (human/computer) and fires missile at location; if location
	 * had a ship, updates ShipClass object to show ship letter and returns true;
	 * otherwise returns false
	 *
	 * @param player - integer value identifying human or computer who is firing the
	 *               missile; the missile will be fired at the other player's array
	 * @param xPos   - integer x position missile landing location
	 * @param yPos   - integer y position missile landing location
	 * @return: Boolean result of ship hit
	 */
	public boolean fireMissile(int player, int xPos, int yPos)
	{
		ShipClass[][] workingArray;

		if (player == HUMAN)
		{
			workingArray = computerArray;

		} 
		else if (player == COMPUTER)
		{
			workingArray = humanArray;
		} 
		else
		{
			return false;
		}
		if (workingArray[yPos][xPos].getLocationLetter(ShipClass.HIDDEN_STATE) != ShipClass.BACKGROUND_LETTER)
		{
			workingArray[yPos][xPos].updateLocationState(ShipClass.EXPOSED_STATE);
			return true;
		}

		workingArray[yPos][xPos].updateLocationState(ShipClass.EXPOSED_STATE, ShipClass.MISSED_LETTER);
		return false;
	}

	/**
	 * Creates random value between a low and high value inclusive Note: Math.random
	 * and Math.round may be used in this method
	 *
	 * @param low  - integer low limit of random value
	 * @param high - integer high limit of random value
	 * @return: integer random value as specified
	 */
	private int getRandBetween(int low, int high)
	{
		int rand = (int) (Math.random() * (high - low+1));
		return rand+low;
	}

	/**
	 * Finds score of requested player (human or computer)
	 *
	 * @param player - constant HUMAN or COMPUTER
	 * @return: score found from player
	 */

	public int getScore(int player)
	{
		ShipClass[][] board = null;
		int score = 0;
		if (player == HUMAN)
		{
			board = computerArray;
		}
		if (player == COMPUTER)
		{
			board = humanArray;
		}
		for (int row = 0; row < arrayHeight; row++)
		{
			for (int column = 0; column < arrayWidth; column++)
			{
				if (checkShipLetter((board[row][column].getLocationLetter(ShipClass.EXPOSED_STATE))))

				{

					score++;
				}
			}
		}
		return score;
	}

	/**
	 * Finds ship length from letter given Note: Must use constants for ship letters
	 * and for ship lengths
	 *
	 * @param typeOfShip - character letter used to find ship length
	 * @returns: integer length of ship
	 */

	private int getShipLength(char typeOfShip)
	{
		int shipLength;
		switch (typeOfShip)
		{
		case AIRCRAFT_CARRIER:
			shipLength = AIRCRAFT_CARRIER_LENGTH;
			break;
		case BATTLESHIP:
			shipLength = BATTLESHIP_LENGTH;
			break;
		case COMPUTER:
			shipLength = CRUISER_LENGTH;
			break;
		case DESTROYER:
			shipLength = DESTROYER_LENGTH;
			break;
		default:
			shipLength = FRIGATE_LENGTH;
			break;
		}
		return shipLength;
	}

	/**
	 * Finds ship name from letter given Note: Must use constants for ship letters
	 *
	 * @param typeOfShip - character letter used to find ship name
	 * @return: String type of ship
	 */

	public String getShipName(char typeOfShip)
	{
		typeOfShip = toUpper(typeOfShip);
		String shipName;
		switch (typeOfShip)
		{
		case AIRCRAFT_CARRIER:
			shipName = "AIRCRAFT_CARRIER";
			break;
		case BATTLESHIP:
			shipName = "BATTLESHIP";
			break;
		case CRUISER:
			shipName = "CRUISER";
			break;
		case DESTROYER:
			shipName = "DESTROYER";
			break;
		default:
			shipName = "FRIGATE";
			break;
		}
		return shipName;
	}

	/**
	 * Reports true if a miss is found in the given array
	 *
	 * @param player - integer value identifying which array to look in
	 * @param xPos   - integer x position to test for miss character
	 * @param yPos   - integer y position to test for miss character
	 * @return: Boolean result of specified test
	 */

	private boolean isMissInArray(int player, int xPos, int yPos)
	{
		char testChar;
		if (player == HUMAN)
		{

			testChar = humanArray[yPos][xPos].getLocationLetter(ShipClass.EXPOSED_STATE);
			return testChar == ShipClass.MISSED_LETTER;
		} 
		else
		{
			testChar = computerArray[yPos][xPos].getLocationLetter(ShipClass.EXPOSED_STATE);
			return testChar == ShipClass.MISSED_LETTER;
		}

	}

	/**
	 * Verifies that a ship will fit on the board and that no other ship is
	 * occupying the proposed space, then places ship and returns true; otherwise
	 * returns false Note: Maximum four loops in the method
	 *
	 * @param shipType    - character value representing ship; letter must be
	 *                    verified as upper case and/or converted to upper case
	 * @param player      - integer constant indicating which player ship to place
	 * @param xPos        - integer x position to place bow of ship
	 * @param yPos        - integer y position to place bow of ship
	 * @param orientation - character 'H' for horizontal or 'V' for vertical; letter
	 *                    must be verified as upper case and/or converted to upper
	 *                    case
	 * @return: Boolean result of attempt to place ship
	 */
	public boolean placeShip(char shipType, int player, int xPos, int yPos, char orientation)
	{
		ShipClass[][] workingArray;
		char shipName = toUpper(shipType);
		int size = getShipLength(shipName);
		char orientationCap = toUpper(orientation);
		int index;
		if (checkShipLetter(shipName) && xPos >= 0 && yPos >= 0)
		{
			if (player == HUMAN)
			{
				workingArray = humanArray;
			} 
			else
			{
				workingArray = computerArray;
			}
			if (orientationCap == VERTICAL && (yPos + size) <= arrayHeight)
			{
				for (index = yPos; index < yPos + size; index++)
				{
					if (workingArray[index][xPos]
							.getLocationLetter(ShipClass.HIDDEN_STATE) != ShipClass.BACKGROUND_LETTER)
					{
						return false;
					}

				}
				for (index = yPos; index < yPos + size; index++)
				{
					workingArray[index][xPos].updateLocationState(ShipClass.HIDDEN_STATE, shipName);
				}
				return true;
			} 
			else if (orientationCap == HORIZONTAL && (xPos + size) <= arrayWidth)
			{
				for (index = xPos; index < xPos + size; index++)
				{
					if (workingArray[yPos][index]
							.getLocationLetter(ShipClass.HIDDEN_STATE) != ShipClass.BACKGROUND_LETTER)
					{
						return false;
					}

				}
				for (index = xPos; index < xPos + size; index++)
				{
					workingArray[yPos][index].updateLocationState(ShipClass.HIDDEN_STATE, shipName);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Prints given string centered within the block size it is given Example:
	 * "Bill" with a blockSize of 10 => SSSBillSSS where S is a space Note: The
	 * String utility .length may be used in this method
	 *
	 * @param toPrint   - String object to be printed
	 * @param blockSize - integer size within which the string is to be centered
	 */
	private void printCentered(java.lang.String toPrint, int blockSize)
	{

		int preSpaces = (blockSize - toPrint.length()) / 2;
		int postSpaces = (blockSize - toPrint.length() - preSpaces);

		printChars(preSpaces, SPACE);
		System.out.print(toPrint);
		printChars(postSpaces, SPACE);

	}

	/**
	 * Recursively prints a given number of a specified character; used in
	 * printCentered for printing spaces
	 *
	 * @param numChars - integer number of characters to be printed
	 * @param outChar  - character value to be repeatedly printed
	 */
	private void printChars(int numChars, char outChar)
	{
		if (numChars > 0)
		{
			System.out.print(outChar);
			printChars(numChars - 1, outChar);
		}

	}

	/**
	 * Sets the computer ships randomly given the number of ships to set Note: Ship
	 * letters (A,B,C,D,F), ship orientation (V/H), and ship x and y positions
	 * (within array) are all randomly generated Uses placeShip to make sure ship
	 * fits on board and it is not on top of another ship
	 *
	 * @param numShips - integer number of ships to set
	 */
	public void setComputerShips(int numShips)
	{
		char shipChar = 0;
		int shipNum;
		int shipIndex;
		int xPos;
		int yPos;
		int orientationIndex;
		char orientation;

		for (shipIndex = 0; shipIndex < numShips; shipIndex++)
		{
			xPos = (getRandBetween(0, arrayWidth - 1));
			yPos = (getRandBetween(0, arrayHeight - 1));
			shipNum = (getRandBetween(0, 4));

			if (shipNum == 0)
			{
				shipChar = AIRCRAFT_CARRIER;
			} 
			else if (shipNum == 1)
			{
				shipChar = BATTLESHIP;
			} 
			else if (shipNum == 2)
			{
				shipChar = CRUISER;
			} 
			else if (shipNum == 3)
			{
				shipChar = DESTROYER;
			} 
			else if (shipNum == 4)
			{
				shipChar = FRIGATE;
			}

			orientationIndex = (getRandBetween(0, 1));
			if (orientationIndex == 0)
			{
				orientation = VERTICAL;
			} 
			else
			{
				orientation = HORIZONTAL;
			}

			placeShip(shipChar, COMPUTER, xPos, yPos, orientation);

		}

	}

	/**
	 * If lower case letter is provided to method it is converted to upppercase;
	 * otherwise no action is taken
	 *
	 * @param testLetter - character letter to be converted as specified
	 * @return: character letter, converted as specified
	 */

	private char toUpper(char testLetter)
	{
		if (testLetter >= 'a' && testLetter <= 'z')
		{
			testLetter = (char) (testLetter - 'a' + 'A');

		}
		return testLetter;

	}

}