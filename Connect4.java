package argunsah.ai.connect4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import sac.game.AlphaBetaPruning;
import sac.game.GameSearchAlgorithm;
import sac.game.GameSearchConfigurator;
import sac.game.GameState;
import sac.game.GameStateImpl;
import sac.game.MinMax;

public class Connect4 extends GameStateImpl {
	
	public static final byte E = 0; // empty
	public static final byte X = 1; // MAX player
	public static final byte O = 2; // MIN player
	private static final String[] SYMBOL_NAMES = { ".", "X", "O" };

	public static final int n = 6; // no. of rows
	public static final int m = 7; // no. of columns

	private byte[][] board = null;

	public Connect4() {
		board = new byte[n][m];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < m; j++)
				board[i][j] = E;
	}
	
	public Connect4(Connect4 parent) {
		board = new byte[n][m];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < m; j++)
				board[i][j] = parent.board[i][j];
		setMaximizingTurnNow(parent.isMaximizingTurnNow());
	}
	
	public byte[][] getBoard() {
		return board;		
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append("|");
			for (int j = 0; j < m; j++) {
				sb.append(SYMBOL_NAMES[board[i][j]]);
				sb.append("|");
			}
			sb.append("\n");
		}
		sb.append("|");
		for (int j = 0; j < m; j++) {
			sb.append(j);
			sb.append("|");
		}
		return sb.toString();
	}

	public boolean makeMove(int column) {
		for (int i = n - 1; i >= 0; i--)
			if (board[i][column] == E) {
				board[i][column] = isMaximizingTurnNow() ? X : O;  
				setMaximizingTurnNow(!isMaximizingTurnNow());
				return true;
			}
		return false;
	}

	@Override
	public List<GameState> generateChildren() {
		List<GameState> children = new ArrayList<>();
		for (int i = 0; i < m; i++) {
			Connect4 child = new Connect4(this);
			if (child.makeMove(i)) {
				children.add(child);
				child.setMoveName(Integer.toString(i)); // naming the child by column index
			}			
		}			
		return children;
	}
	
	@Override
	public int hashCode() {
		byte[] flat = new byte[m * n];
		int k = 0;
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				flat[k++] = board[i][j];
		return Arrays.hashCode(flat);
	}

	public int getWinStatus()
	{
		int[] di = new int[] {-1, 0, +1, +1};
		int[] dj = new int[] {+1, +1, +1, 0};
		for (int i = 0; i < n; i++)
			for (int j = 0; j < m; j++) 
				for (int k = 0; k < 4; k++) { // loop over 4 directions: top-right, right, bottom-right, bottom
					int sumAlongDirection = 0;
					for (int q = 0; q < 4; q++) { // loop along one particular direction
						int coordI = i + q * di[k];
						int coordJ = j + q * dj[k];
						if ((coordI >= 0) && (coordI < n) && (coordJ >= 0) && (coordJ < m)) {
							if (board[coordI][coordJ] == X)
								sumAlongDirection += 1;
							else if (board[coordI][coordJ] == O)
								sumAlongDirection -= 1;
						}
					}
					if (sumAlongDirection == 4)
						return X; // meaning: Xs won! (MAX player)
					else if (sumAlongDirection == -4) 
						return O; // meaning: Os won! (MIN player)	
				}
		return E; // meaning: noone won!
	}
	
//	@Override
//	public boolean isQuiet() {
//		// TODO Auto-generated method stub
//		return super.isQuiet();
//	}
	
	public static void main(String[] args) {
		boolean PLAYER_MAX_HUMAN = true;
		boolean PLAYER_MIN_HUMAN = false;
		GameSearchConfigurator conf = new GameSearchConfigurator();
		conf.setDepthLimit(4.5);		
		GameSearchAlgorithm algo = new MinMax();
		algo.setConfigurator(conf);
		Connect4.setHFunction(new PrimitiveEvaluation());
		
		Scanner scanner = new Scanner(System.in);				
		System.out.println("CONNECT 4...");		
		Connect4 c4 = new Connect4();		
		System.out.println(c4);
		int moves = 0;
//		int counter5=0;
//		int counter6=0;
//		int counter7=0;
//		int s=0;
//		int f=0;
		while (true) {
			if ((c4.isMaximizingTurnNow()) && (PLAYER_MAX_HUMAN)) {
				System.out.println("PICK COLUMN: ");
				String column = scanner.nextLine();
				boolean moveResult = c4.makeMove(Integer.valueOf(column));
				if (!moveResult)
					System.out.println("ILLEGAL COLUMN (ALREADY FULL), PICK COLUMN AGAIN...");
			}
			else if ((!c4.isMaximizingTurnNow()) && (PLAYER_MIN_HUMAN)) {
				System.out.println("PICK COLUMN: ");
				String column = scanner.nextLine();
				boolean moveResult = c4.makeMove(Integer.valueOf(column));
				if (!moveResult)
					System.out.println("ILLEGAL COLUMN (ALREADY FULL), PICK COLUMN AGAIN...");
			}			
			else {
				System.out.println("AI...");
				algo.setInitial(c4);
				algo.execute();				
				System.out.println("AI DONE. [TIME: " + algo.getDurationTime() + " ms]");
				System.out.println("STATES ANALYZED: " + algo.getClosedStatesCount());
				System.out.println("GENERAL DEPTH: " + algo.getConfigurator().getDepthLimit());
				System.out.println("MAX DEPTH (DUE TO QUEIESCENCE): " + algo.getDepthReached());
				System.out.println("TRANSPOSITION TABLE HITS: " + algo.getTranspositionTable().getUsesCount());
				System.out.println("MOVE SCORES: " + algo.getMovesScores()); // Map<String, Double>
				String bestMove = algo.getFirstBestMove();
				System.out.println("BEST MOVE: " + bestMove);
				c4.makeMove(Integer.valueOf(bestMove)); //Integer.parseInt(bestMove)	
			}
			
			
			System.out.println(c4);
			int status = c4.getWinStatus();
			if (status == X) {
				System.out.println("Xs WON!!!");
				break;
			}
			else if (status == O) {
				System.out.println("Os WON!!!");
				break;
			}			
			moves++;
			if (moves == Connect4.m * Connect4.n) {
				System.out.println("GAME ENDED WITH A DRAW.");
				break;				
			}
				
		}
		scanner.close();
		System.out.println("DONE.");
	}
}