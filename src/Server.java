import java.net.ServerSocket ;
import java.net.Socket ;

/**
 * 
 */

/**
 * 
 *
 * @author mazurz
 * @version 1.0.0 2020-11-18 Initial implementation
 *
 */
import java.io.*;
public class Server
    {

    /**
     * 
     *
     * @param args
     */
    public static void main( String[] args ) throws IOException{
        char[][] board = new char[3][3];// Server is x since they go first
        boolean[] xWins = new boolean[8];
        boolean[] oWins = new boolean[8];
        ServerSocket s = new ServerSocket(9991);
        Socket s1 = s.accept();
        
        DataInputStream dInput = new DataInputStream (s1.getInputStream());
        DataOutputStream dOutput = new DataOutputStream(s1.getOutputStream());
        BufferedReader bF1 = new BufferedReader(new InputStreamReader(System.in));
        
        String yes = "Y";
        String no = "N";
        String reset = "R";
        String end = "E";
        
        
    }
    
    public static void display(char[][] board) {
    	System.out.printf("%c,%c,%c%n%c,%c,%c%n%c,%c,%c%n", 
    			board[0][0], board[0][1], board[0][2],
    			board[1][0], board[1][1], board[1][2],
    			board[2][0], board[2][1], board[2][2]);
    }
    
    public static void init(char[][] board, boolean[] xWins, boolean oWins[]) {
    	board[0][0] = '1';
    	board[0][1] = '2';
    	board[0][2] = '3';
    	board[1][0] = '4';
    	board[1][1] = '5';
    	board[1][2] = '6';
    	board[2][0] = '7';
    	board[2][1] = '8';
    	board[2][2] = '9';
    	for(int i=0; i<xWins.length;i++) {
    		xWins[i] = true;
    		oWins[i] = true;
    	}
    }
    public static void updateWins(int enemy, boolean[] wins) {
    	wins[0] = (enemy%3==1) ? false : true;
    	wins[1] = (enemy%3==2) ? false : true;
    	wins[2] = (enemy%3==0) ? false : true;
    	wins[3] = (enemy<4) ? false : true;
    	wins[4] = (enemy>3 && enemy < 7) ? false : true;
    	wins[5] = (enemy>6) ? false : true;
    	wins[6] = (enemy==1 || enemy==5 || enemy==7) ? false : true;
    	wins[7] = (enemy==3 || enemy==5 || enemy==9) ? false : true;
    }
 }
	// end class Server