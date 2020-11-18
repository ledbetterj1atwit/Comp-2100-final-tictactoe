
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
        Socket s = new Socket("localhost", 9991);
        System.out.println("Connection Established! Server is X and Client is O");
        
        DataInputStream dInput = new DataInputStream (s.getInputStream());
        DataOutputStream dOutput = new DataOutputStream(s.getOutputStream());
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
    
    public static void init(char[][] board) {
    	board[0][0] = '1';
    	board[0][1] = '2';
    	board[0][2] = '3';
    	board[1][0] = '4';
    	board[1][1] = '5';
    	board[1][2] = '6';
    	board[2][0] = '7';
    	board[2][1] = '8';
    	board[2][2] = '9';
    }

    }
	// end class Client