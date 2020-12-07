
/**
 * 
 *
 * @author mazurz
 * @version 1.0.0 2020-11-18 Initial implementation
 *
 */
import java.net.*;
import java.io.*;


public class Client {
	
	static final String yes = "Y"; // constants
	static final String  no = "N";
	static final String reset = "R";
	static final String end = "E";
	static char[][] board = new char[3][3];// Server is x since they go first
	static boolean[] xWins = new boolean[8];
	static boolean[] oWins = new boolean[8];
	/**
	 * 
	 *
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		
		init();
		Socket s = new Socket("localhost", 9991);
		System.out.println("Connection Established! Server is X and Client is O");

		DataInputStream dInput = new DataInputStream(s.getInputStream());
		DataOutputStream dOutput = new DataOutputStream(s.getOutputStream());
		BufferedReader bF1 = new BufferedReader(new InputStreamReader(System.in));

		String serverMove = "";
		String clientMove = "";
		String response = "";
		String serverResponse = "";

		System.out.println("Ready to Play? Y for yes and N for no.");

		response = bF1.readLine();
		dOutput.writeUTF(response);
		serverResponse = dInput.readUTF();
		if ((serverResponse.equals(yes)) && (response.contentEquals(yes))) {
			boolean playing = true;
			while (playing) { // game loop
				System.out.println("Server will make the first move as X. Enter \"E\" to exit on your turn.");
				init();
				display();
				int checkState = 0;
				while (true) { // round loop
					// x turn
					if(!xTurn(dInput)) {
						playing = false;
						break;
					}
					checkState = check('X', bF1, dInput, dOutput);
					if(checkState == 1) {
						break;
					}
					else if(checkState == -1) {
						playing = false;
						break;
					}
					// o turn
					if(!oTurn(bF1, dOutput)) {
						playing = false;
						break;
					}
					checkState = check('O', bF1, dInput, dOutput);
					if(checkState == 1) {
						break;
					}
					else if(checkState == -1) {
						playing = false;
						break;
					}
				}
			}
			System.out.printf("Thanks for playing%n", args);

		}
		dInput.close();
		dOutput.close();
		s.close();
	}

	/**
	 * Output the game board.
	 * 
	 * @param board The board.
	 */
	public static void display() {
		System.out.printf(
				"┏━━━┳━━━┳━━━┓%n┃ %c ┃ %c ┃ %c ┃%n┣━━━╋━━━╋━━━┫%n┃ %c ┃ %c ┃ %c ┃%n┣━━━╋━━━╋━━━┫%n┃ %c ┃ %c ┃ %c ┃%n┗━━━┻━━━┻━━━┛%n",
				board[0][0], board[0][1], board[0][2], board[1][0], board[1][1], board[1][2], board[2][0], board[2][1],
				board[2][2]);
	}

	/**
	 * Reset board xWins and oWins to initial state.
	 * 
	 * @param board
	 * @param xWins
	 * @param oWins
	 */
	public static void init() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = (char) ((j + 1 + (3 * i)) + 48);
			}
		}
		for (int i = 0; i < xWins.length; i++) {
			xWins[i] = true;
			oWins[i] = true;
		}
	}

	/**
	 * Update a player's possible wins dependent on opponent move
	 * 
	 * TODO: Make sure player cant place over already taken spot. (Player will loose turn if they try!)
	 * 
	 * @param move move made by opponent
	 * @param wins wins possible by player
	 */
	public static void updateWins(int move, boolean[] wins) {
		wins[0] = (move % 3 == 1) ? false : wins[0];
		wins[1] = (move % 3 == 2) ? false : wins[1];
		wins[2] = (move % 3 == 0) ? false : wins[2];
		wins[3] = (move < 4) ? false : wins[3];
		wins[4] = (move > 3 && move < 7) ? false : wins[4];
		wins[5] = (move > 6) ? false : wins[5];
		wins[6] = (move == 1 || move == 5 || move == 7) ? false : wins[6];
		wins[7] = (move == 3 || move == 5 || move == 9) ? false : wins[7];
	}

	/**
	 * Check your wins and opponent wins to see if there has been a tie.
	 * Does not find ties if a player could win two turns from now when only one free space remains.
	 * EX: X:1,2,7,8 O:3,4,5,9 (its a tie on x's turn but o could still win if x does not put into the only free space left)
	 * Fix: Make player automatically make the last available move of a game. TODO
	 * 
	 * @param yourWins
	 * @param opponentWins
	 * @return
	 */
	public static boolean checkTie(boolean[] yourWins, boolean[] opponentWins) {
		boolean lost = true;
		boolean opponentLost = true;
		for (boolean b : yourWins)
			if (b == true)
				lost = false;
		for (boolean b : opponentWins)
			if (b == true)
				opponentLost = false;

		return lost && opponentLost;
	}

	/**
	 * Check if a player has won.
	 * 
	 * @param board
	 * @param playerWins
	 * @param playerSymb the symbol a player uses('x' or 'o')
	 * @return
	 */
	public static boolean checkWin(char[][] board, boolean[] playerWins, char playerSymb) {
		if (playerWins[0] && board[0][0] == playerSymb && board[1][0] == playerSymb && board[2][0] == playerSymb)
			return true;
		else if (playerWins[1] && board[0][1] == playerSymb && board[1][1] == playerSymb && board[2][1] == playerSymb)
			return true;
		else if (playerWins[2] && board[0][2] == playerSymb && board[1][2] == playerSymb && board[2][2] == playerSymb)
			return true;
		else if (playerWins[3] && board[0][0] == playerSymb && board[0][1] == playerSymb && board[0][2] == playerSymb)
			return true;
		else if (playerWins[4] && board[1][0] == playerSymb && board[1][1] == playerSymb && board[1][2] == playerSymb)
			return true;
		else if (playerWins[5] && board[2][0] == playerSymb && board[2][1] == playerSymb && board[2][2] == playerSymb)
			return true;
		else if (playerWins[6] && board[0][0] == playerSymb && board[1][1] == playerSymb && board[2][2] == playerSymb)
			return true;
		else if (playerWins[7] && board[0][2] == playerSymb && board[1][1] == playerSymb && board[2][0] == playerSymb)
			return true;
		return false;
	}

	/**
	 * update the board with move as an index of sorts.
	 * TODO: Make sure player cant place over already taken spot. (Player will loose turn if they try!)
	 * 
	 * @param move  int 1-9
	 * @param symb  symbol to set to
	 * @param board
	 */
	public static void updateBoard(int move, char symb) {
		board[(move % 3 == 0) ? (move / 3) - 1 : move / 3][(move + 2) % 3] = symb;
		display();
	}
	
	/**
	 * Handle x's turn.
	 * @param dInput
	 * @param board
	 * @param oWins
	 * @return true to continue false to quit
	 * @throws IOException
	 */
	public static boolean xTurn(DataInputStream dInput) throws IOException {
		System.out.printf("X's Turn%n");
		String serverMove = dInput.readUTF(); // Wait for server.
		if (serverMove.contentEquals(end)) { // Check for quit.
			System.out.printf("%nOther player quit!%n");
			return false;
			//playing = false;
			//break;
		}
		updateBoard(Integer.parseInt(serverMove), 'X'); // Update
		updateWins(Integer.parseInt(serverMove), oWins);
		return true;
	}
	
	/** 
	 * Handle o's turn.
	 * @param bF1
	 * @param dOutput
	 * @param board
	 * @param xWins
	 * @return true to continue false to quit
	 * @throws IOException
	 */
	public static boolean oTurn(BufferedReader bF1, DataOutputStream dOutput) throws IOException {
		System.out.printf("O's Turn: ");
		String clientMove = bF1.readLine(); // Get player input
		if (clientMove.contentEquals(end)) {
			dOutput.writeUTF(clientMove);
			return false;
		}
		dOutput.writeUTF(clientMove);  // Send move
		updateBoard(Integer.parseInt(clientMove), 'O'); // update
		updateWins(Integer.parseInt(clientMove), xWins);
		return true;
	}
	
	/**
	 *  Check for win state/tie
	 * @param playerChar
	 * @param wins
	 * @param bF1
	 * @param dInput
	 * @param dOutput
	 * @return 0 for round continue, 1 for round end, -1 for quit
	 * @throws IOException
	 */
	public static int check(char playerChar, BufferedReader bF1, DataInputStream dInput, DataOutputStream dOutput) throws IOException {
		String response;
		boolean[] playerWins, opponentWins;
		if (playerChar == 'X') {
			playerWins = xWins;
			opponentWins = oWins;
		}
		else {
			playerWins = oWins;
			opponentWins = xWins;
		}
		
		if (checkWin(board, playerWins, playerChar)) { // check client win
			System.out.printf("%c has won! Do you wish to play again?(Y/N)%n", playerChar);
			if (!bF1.readLine().contentEquals(yes)) {
				dOutput.writeUTF(end);
				return -1;
			} 
			else {
				dOutput.writeUTF(reset);
				response = dInput.readUTF();
				if (response.contentEquals(end)) {
					System.out.printf("%nOther player quit!%n");
					return -1;
				}
				return 1;
			}
		} 
		else if (checkTie(playerWins, opponentWins)) { // check tie
			System.out.println("Its a tie! Do you wish to play again?(Y/N)");
			if (!bF1.readLine().contentEquals(yes)) {
				dOutput.writeUTF(end);
				return -1;
				
			} 
			else {
				dOutput.writeUTF(reset);
				response = dInput.readUTF();
				if (response.contentEquals(end)) {
					System.out.printf("%nOther player quit!%n");
					return -1;
				}
				return 1;
			}
		}
		return 0;
	}
}
// end class Client