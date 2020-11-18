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
        char[][] board = new char[3][3];
    	// Server is x since they go first
        ServerSocket s = new ServerSocket(9991);
        Socket s1 = s.accept();
        
        DataInputStream dInput = new DataInputStream (s1.getInputStream());
        DataOutputStream dOutput = new DataOutputStream(s1.getOutputStream());
        BufferedReader bF1 = new BufferedReader(new InputStreamReader(System.in));


    }
    
    public static void display(char[][] board) {
    	System.out.printf("%c,%c,%c%n%c,%c,%c%n%c,%c,%c%n", 
    			board[0][0], board[0][1], board[0][2],
    			board[1][0], board[1][1], board[1][2],
    			board[2][0], board[2][1], board[2][2]);
    }
 }
	// end class Server