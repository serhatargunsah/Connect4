package argunsah.ai.connect4;
//1. WAY
/*public class PrimitiveEvaluation extends StateFunction {
	@Override
	public double calculate(State state) {
		Connect4 c4 = (Connect4) state;
		int status = c4.getWinStatus();
		if (status == Connect4.X)
			return Double.POSITIVE_INFINITY;
		if (status == Connect4.O)
			return Double.NEGATIVE_INFINITY;		
		//return 0.0;
		
		// stupid preferation of middle columns
//		double h = 0.0;
//		for (int i = 0; i < Connect4.n; i++)
//			for (int j = 0; j < Connect4.m; j++) {
//				double middleReward = (Connect4.m - 1.0) * 0.5; 
//				if (c4.getBoard()[i][j] == Connect4.X)
//					h += middleReward - Math.abs(middleReward - j);
//				else if (c4.getBoard()[i][j] == Connect4.O)
//					h -= middleReward - Math.abs(middleReward - j);				
//			}

		final int n = Connect4.n;
		final int m = Connect4.m;
		byte[][] board = c4.getBoard();
		
		int counter5=0;
		int counter6=0;
		int counter7=0;
		int s=0;
		int f=1;
		int something=1;
		
		

		int h = 0;
		while(s<n) {
			while(f+4-1<m) {
				for(int t=0; t<4; t++) {
					int ft=f+t;
					if(board[s][ft]==Connect4.E || board[s][ft]==Connect4.X) {
						counter5++;
					}
				}
				if(counter5==4) {
					counter6=counter6+something;
					counter5=0;
					something++;
				}
				else {
					counter5=0;
					something=0;
				}
				f++;
			}
			s++;
		}
		
		while(f + 4 - 1 <m) {
			while(s<n) {
				for(int t=0; t<4; t++) {
					int st=s+t;
					if(board[st][f]==Connect4.E || board[st][f]==Connect4.X) {
						counter5++;
					}
				}
				if(counter5==4) {
					counter6=counter6+something;
					counter5=0;
					something++;
				}
				else {
					counter5=0;
					something=0;
				}
				s++;
			}
			f++;
		}
		
		while(s<n) {
			while(f<m) {
				for(int t=0; t<4; t++) {
					int ft=f+t;
					int st=s+t;
					if(board[st][ft]==Connect4.E || board[st][ft]==Connect4.X) {
						counter5++;
					}
				}
				if(counter5==4) {
					counter6=counter6+something;
					counter5=0;
					something++;
				}
				else {
					counter5=0;
					something=0;
				}
				f++;
			}
			s++;
		}
		
		while(s<n) {
			while(f<m) {
				for(int t=0; t<4; t++) {
					int ft=f+t;
					if(board[s][ft]==Connect4.E || board[s][ft]==Connect4.O) {
						counter5++;
					}
				}
				if(counter5==4) {
					counter7=counter7+something;
					counter5=0;
					something++;
				}
				else {
					counter5=0;
					something=0;
				}
				f++;
			}
			s++;
		}
		
		while(f<m) {
			while(s<n) {
				for(int t=0; t<4; t++) {
					int st=s+t;
					if(board[st][f]==Connect4.E || board[st][f]==Connect4.O) {
						counter5++;
					}
				}
				if(counter5==4) {
					counter7=counter7+something;
					counter5=0;
					something++;
				}
				else {
					counter5=0;
					something=0;
				}
				s++;
			}
			f++;
		}
	
		while(s<n) {
			while(f<m) {
				for(int t=0; t<4; t++) {
					int ft=f+t;
					int st=s+t;
					if(board[st][ft]==Connect4.E || board[st][ft]==Connect4.X) {
						counter5++;
					}
				}
				if(counter5==4) {
					counter7=counter7+something;
					counter5=0;
					something++;
				}
				else {
					counter5=0;
					something=0;
				}
				f++;
			}
			s++;
		}
		h = counter6 - counter7; 
		
		
		return h;		
		//System.out.println(board[1][1]);
	}
}*/
//2. WAY
/*import sac.State;
import sac.StateFunction;

public class PrimitiveEvaluation extends StateFunction{
	public double calculate(State state) {
		Connect4 c4 = (Connect4) state;
		int status = c4.getWinStatus();
		byte[][] board = c4.getBoard();
		if(status == Connect4.X)
			return Double.POSITIVE_INFINITY;
		if(status == Connect4.O)
			return Double.NEGATIVE_INFINITY;
		//return 0.0;
		
		// STUPID
		double h = 0.0;
		for(int i=0; i<Connect4.n; i++)
			for(int j=0; j<Connect4.m; j++) {
				double middleReward = (Connect4.m - 1.0) * 0.5;
				if (c4.getBoard()[i][j] == Connect4.X)
					h += middleReward - Math.abs(middleReward - j);
				else if (c4.getBoard()[i][j] == Connect4.O)
					h -= middleReward - Math.abs(middleReward - j);
			}
		//return h;
		
		// SMART
		
		int counterX = 0;
		int counterE = 0;
		int counterO = 0;
		int counterDortluGrupXd = 0;
		int counterDortluGrupXy = 0;
		int counterDortluGrupE = 0;
		for(int i=0, j=0; i<Connect4.n-4; i++){
			counterDortluGrupXd = 0;
			if(board[i][j] == Connect4.X) {
				counterX++;
			}
			if(counterX >= 1) {
				for(int k=0, l=0; k<4 && i+k<Connect4.n; k++) {
					if((board[k][l] == Connect4.X) || (board[k][l] == Connect4.E)) {
						counterDortluGrupXd++;
						if(Connect4.X + Connect4.E < 4) {
							j++;
						}
					}
				}
			}
			
			if(board[i][j] == Connect4.E) {
				counterE++;
			}
			if(counterE >= 1) {
				for(int k=0, l=0; k<4 && i+k<Connect4.n; k++) {
					if(board[k][l] == Connect4.E) {
						counterDortluGrupE++;
						/*if(Connect4.X + Connect4.E < 4) {
							j++;
						}*/
					/*}
				}
			}
		}
		for(int i=0, j=0; j<Connect4.m-4; j++){
			counterDortluGrupXy = 0;
			if(board[i][j] == Connect4.X) {
				counterX++;
			}
			if(counterX >= 1) {
				for(int k=0, l=0; l<4 && j+l<Connect4.m; l++) {
					if((board[k][l] == Connect4.X) || (board[k][l] == Connect4.E)) {
						counterDortluGrupXy++;
						if(Connect4.X + Connect4.E < 4) {
							l++;
						}
					}
				}
			}
			
			if(board[i][j] == Connect4.E) {
				counterE++;
			}
			if(counterE >= 1) {
				for(int k=0, l=0; l<4 && j+l<Connect4.m; l++) {
					if(board[k][l] == Connect4.E) {
						counterDortluGrupE++;
						/*if(Connect4.X + Connect4.E < 4) {
							j++;
						}*/
					/*}
				}
			}
		}
		
		//yatay
		
		
		return counterDortluGrupXd+counterDortluGrupXy;
		
	}
}*/

//3.WAY
import sac.State;
import sac.StateFunction;

public class PrimitiveEvaluation extends StateFunction {
    @Override
    public double calculate(State state) {
        Connect4 c4 = (Connect4) state;
        int status = c4.getWinStatus();
        if (status == Connect4.X)
            return Double.POSITIVE_INFINITY;
        if (status == Connect4.O)
            return Double.NEGATIVE_INFINITY;

        double h = 0.0;
        for (int i = 0; i < Connect4.n; i++)
            for (int j = 0; j < Connect4.m; j++) {
                double middleReward = (Connect4.m - 1.0) * 0.5;
                if (c4.getBoard()[i][j] == Connect4.X)
                    h += middleReward - Math.abs(middleReward - j);
                else if (c4.getBoard()[i][j] == Connect4.O)
                    h -= middleReward - Math.abs(middleReward - j);
            }
        return h;
    }
}
