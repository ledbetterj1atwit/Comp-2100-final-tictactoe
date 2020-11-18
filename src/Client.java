/**
 * 
 *
 * @author mazurz
 * @version 1.0.0 2020-11-18 Initial implementation
 *
 */
import java.net.*;
import java.io.*;
public class Client
    {

    /**
     * 
     *
     * @param args
     */
    public static void main( String[] args ) throws IOException
        {
    	char[][] board = new char[3][3];// Server is x since they go first
        boolean[] xWins = new boolean[8];
        boolean[] oWins = new boolean[8];
        Socket s = new Socket("localhost", 9991);
        System.out.println("Connection Established! Server is X and Client is O");
        
        DataInputStream dInput = new DataInputStream (s.getInputStream());
        DataOutputStream dOutput = new DataOutputStream(s.getOutputStream());
        BufferedReader bF1 = new BufferedReader(new InputStreamReader(System.in));
        
        String yes = "Y";
        String no = "N";
        String reset = "R";
        String end = "E";
        
        int serverMove = 0;
        int clientMove = 0;
        String response = "";
        String serverResponse = "";
        }
    
    /**
     * Output the game board.
     * @param board The board.
     */
    public static void display(char[][] board) {
    	System.out.printf("┏━━━┳━━━┳━━━┓%n┃ %c ┃ %c ┃ %c ┃%n┣━━━╋━━━╋━━━┫%n┃ %c ┃ %c ┃ %c ┃%n┣━━━╋━━━╋━━━┫%n┃ %c ┃ %c ┃ %c ┃%n┗━━━┻━━━┻━━━┛%n", 
    			board[0][0], board[0][1], board[0][2],
    			board[1][0], board[1][1], board[1][2],
    			board[2][0], board[2][1], board[2][2]);
    }
    
    /**
     * Reset board xWins and oWins to initial state.
     * @param board
     * @param xWins
     * @param oWins
     */
    public static void init(char[][] board, boolean[] xWins, boolean oWins[]) {
    	for(int i = 0; i<board.length ;i++) {
    		for(int j = 0; j<board[i].length; j++) {
    			board[i][j] = (char) ((j+1+(3*i))+48);
    		}
    	}
    	for(int i=0; i<xWins.length;i++) {
    		xWins[i] = true;
    		oWins[i] = true;
    	}
    }
    
    /**
     * Update a player's possible wins dependent on opponent move
     * @param move move made by opponent
     * @param wins wins possible by player
     */
    public static void updateWins(int move, boolean[] wins) {
    	wins[0] = (move%3==1) ? false : true;
    	wins[1] = (move%3==2) ? false : true;
    	wins[2] = (move%3==0) ? false : true;
    	wins[3] = (move<4) ? false : true;
    	wins[4] = (move>3 && move < 7) ? false : true;
    	wins[5] = (move>6) ? false : true;
    	wins[6] = (move==1 || move==5 || move==7) ? false : true;
    	wins[7] = (move==3 || move==5 || move==9) ? false : true;
    }
    
    /**
     * Check your wins and opponent wins to see if there has been a tie.
     * @param yourWins
     * @param opponentWins
     * @return
     */
    public static boolean checkTie(boolean[] yourWins, boolean[] opponentWins) {
    	boolean lost = true;
    	boolean opponentLost = true;
    	for(boolean b : yourWins) if(b == true) lost = false;
    	for(boolean b : opponentWins) if(b == true) opponentLost = false;
    	
    	return lost && opponentLost; 
    }
    
    /**
     * Check if a player has won.
     * @param board
     * @param playerWins
     * @param playerSymb the symbol a player uses('x' or 'o')
     * @return
     */
    public static boolean checkWin(char[][] board, boolean[] playerWins, char playerSymb) {
    	if(playerWins[0] && board[0][0]==playerSymb && board[1][0]==playerSymb && board[2][0]==playerSymb) return true;
    	else if(playerWins[1] && board[0][1]==playerSymb && board[1][1]==playerSymb && board[2][1]==playerSymb) return true;
    	else if(playerWins[2] && board[0][2]==playerSymb && board[1][2]==playerSymb && board[2][2]==playerSymb) return true;
    	else if(playerWins[3] && board[0][0]==playerSymb && board[0][1]==playerSymb && board[0][2]==playerSymb) return true;
    	else if(playerWins[4] && board[1][0]==playerSymb && board[1][1]==playerSymb && board[1][2]==playerSymb) return true;
    	else if(playerWins[5] && board[2][0]==playerSymb && board[2][1]==playerSymb && board[2][2]==playerSymb) return true;
    	else if(playerWins[6] && board[0][0]==playerSymb && board[1][1]==playerSymb && board[2][2]==playerSymb) return true;
    	else if(playerWins[7] && board[0][2]==playerSymb && board[1][1]==playerSymb && board[2][0]==playerSymb) return true;
    	return false;
    }

    }
	// end class Client