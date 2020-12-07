
/**
 * 
 *
 * @author mazurz
 * @version 1.0.0 2020-11-18 Initial implementation
 *
 */
import java.io.*;
import java.net.*;

public class Server {

	/**
	 * 
	 *
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		char[][] board = new char[3][3];// Server is x since they go first
		boolean[] xWins = new boolean[8];
		boolean[] oWins = new boolean[8];

		ServerSocket s = new ServerSocket(9991);
		Socket s1 = s.accept();

		DataInputStream dInput = new DataInputStream(s1.getInputStream());
		DataOutputStream dOutput = new DataOutputStream(s1.getOutputStream());
		BufferedReader bF1 = new BufferedReader(new InputStreamReader(System.in));

		String yes = "Y";
		String no = "N";
		String reset = "R";
		String end = "E";

		String serverMove = "";
		String clientMove = "";
		String response = "";
		String clientResponse = "";

		System.out.println("Ready to Play? Y for yes and N for no.");

		response = bF1.readLine();
		dOutput.writeUTF(response);
		clientResponse = dInput.readUTF();
		if ((clientResponse.equals(yes)) && (response.contentEquals(yes))) {
			boolean playing = true;
			while (playing) { // Game loop
				System.out.println("Server will make the first move as X. Enter \"E\" to exit on your turn.");
				init(board, xWins, oWins);
				display(board);
				while (true) // round loop
				{
					System.out.printf("X's Turn: ");
					serverMove = bF1.readLine();
					if (serverMove.contentEquals(end)) {
						dOutput.writeUTF(serverMove);
						playing = false;
						break;
					}
					dOutput.writeUTF(serverMove);
					updateBoard(Integer.parseInt(serverMove), 'X', board);
					updateWins(Integer.parseInt(serverMove), oWins);
					if (checkWin(board, xWins, 'X')) {
						System.out.println("X has won! Do you wish to play again?(Y/N)");
						if (!bF1.readLine().contentEquals(yes)) {
							playing = false;
							dOutput.writeUTF(end);
							break;
						} else {
							dOutput.writeUTF(reset);
							clientResponse = dInput.readUTF();
							if (clientResponse.contentEquals(end))
								playing = false;
							break;
						}
					} else if (checkTie(oWins, xWins)) {
						System.out.println("Its a tie! Do you wish to play again?(Y/N)");
						if (!bF1.readLine().contentEquals(yes)) {
							playing = false;
							dOutput.writeUTF(end);
							break;
						} else {
							dOutput.writeUTF(reset);
							clientResponse = dInput.readUTF();
							if (clientResponse.contentEquals(end))
								playing = false;
							break;
						}
					}
					System.out.printf("O's Turn%n");
					clientMove = dInput.readUTF();
					if (clientMove.contentEquals(end)) {
						System.out.printf("%nOther player quit!%n");
						playing = false;
						break;
					}
					updateBoard(Integer.parseInt(clientMove), 'O', board);
					updateWins(Integer.parseInt(clientMove), xWins);
					if (checkWin(board, oWins, 'O')) {
						System.out.println("O has won! Do you wish to play again?(Y/N)");
						if (!bF1.readLine().contentEquals(yes)) {
							playing = false;
							dOutput.writeUTF(end);
							break;
						} else {
							dOutput.writeUTF(reset);
							clientResponse = dInput.readUTF();
							if (clientResponse.contentEquals(end))
								playing = false;
							break;
						}
					} else if (checkTie(oWins, xWins)) {
						System.out.println("Its a tie! Do you wish to play again?(Y/N)");
						if (!bF1.readLine().contentEquals(yes)) {
							playing = false;
							dOutput.writeUTF(end);
							break;
						} else {
							dOutput.writeUTF(reset);
							clientResponse = dInput.readUTF();
							if (clientResponse.contentEquals(end))
								playing = false;
							break;
						}
					}
				}
			}
			System.out.printf("Thanks for playing%n", args);

		}
		dInput.close();
		dOutput.close();
		s.close();
		s1.close();
	}

	/**
	 * Output the game board.
	 * 
	 * @param board The board.
	 */
	public static void display(char[][] board) {
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
	public static void init(char[][] board, boolean[] xWins, boolean[] oWins) {
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
	 * 
	 * @param move  int 1-9
	 * @param symb  symbol to set to
	 * @param board
	 */
	public static void updateBoard(int move, char symb, char[][] board) {
		board[(move % 3 == 0) ? (move / 3) - 1 : move / 3][(move + 2) % 3] = symb;
		display(board);
	}
}
// end class Server