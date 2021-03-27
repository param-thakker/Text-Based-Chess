package pieces;
import chess.Board;
import chess.Spot;

/**
 * The King class is an extension of the ChessPiece class and creates a King Piece.
 * @author Param Thakker
 * @author Jonathan Lu
 *
 */
public class King extends ChessPiece{
	
	String pieceName="K";
	public static boolean castledQ = false;
	public static boolean castledK = false;
	int pieceColor=0;
	int previousChange=0;
	public King(int color) {
		this.setColor(color);
		this.pieceColor=color;
	}

	public King(int color, int previousChange) {
		this.setColor(color);
		this.pieceColor=color;
		this.previousChange = previousChange;
	}

	public int getPreviousChange() {
		return previousChange;
	}

	@Override
	public String getPieceName() {
		
		if (pieceColor==0) {
			this.pieceName="wK";
		}
		else {
			this.pieceName="bK";
		}
		return this.pieceName;
	}

	@Override
	//TODO check if move puts king into Check, if so then not valid move
	//TODO implement castling **DONE**
	public boolean validMove(Board board, Spot startPosition, Spot endPosition) {
		//white castling
		if (color == 0){
			//kings side
			if (startPosition.getXCoordinate() == 4  && startPosition.getYCoordinate() == 7 && endPosition.getXCoordinate() == 6 && endPosition.getYCoordinate() == 7 && first && !board.grid[7][7].isEmpty() && board.grid[7][7].getPiece().getPieceName().equals("wR") && board.grid[7][7].getPiece().first && board.grid[5][7].isEmpty() && board.grid[6][7].isEmpty()){
				//System.out.println("**white castle kings side**");
				castledK = true;
				first = false;
				return true;
			}
			//queens side
			if (startPosition.getXCoordinate() == 4 && startPosition.getYCoordinate() == 7 && endPosition.getXCoordinate() == 2 && endPosition.getYCoordinate() == 7 && first && !board.grid[0][7].isEmpty() && board.grid[0][7].getPiece().getPieceName().equals("wR") && board.grid[0][7].getPiece().first && board.grid[1][7].isEmpty() && board.grid[2][7].isEmpty() && board.grid[3][7].isEmpty()){
				//System.out.println("**white castle queens side**");
				castledQ = true;
				first = false;
				return true;
			}
		}
		//black castling
		if (color == 1) {
			//kings side
			if (startPosition.getXCoordinate() == 4 && startPosition.getYCoordinate() == 0 && endPosition.getXCoordinate() == 6 && endPosition.getYCoordinate() == 0 && first && !board.grid[7][0].isEmpty() && board.grid[7][0].getPiece().getPieceName().equals("bR") && board.grid[7][0].getPiece().first && board.grid[5][0].isEmpty() && board.grid[6][0].isEmpty()){
				//System.out.println("**black castle kings side**");
				castledK = true;
				first = false;
				return true;
			}
			//queens side
			if (startPosition.getXCoordinate() == 4 && startPosition.getYCoordinate() == 0 && endPosition.getXCoordinate() == 2 && endPosition.getYCoordinate() == 0 && first && !board.grid[0][0].isEmpty() && board.grid[0][0].getPiece().getPieceName().equals("bR") && board.grid[0][0].getPiece().first && board.grid[1][0].isEmpty() && board.grid[2][0].isEmpty() && board.grid[3][0].isEmpty()){
				//System.out.println("**black castle queens side**");
				castledQ = true;
				first = false;
				return true;
			}

		}
		int yChange=Math.abs(endPosition.getYCoordinate()-startPosition.getYCoordinate());
		int xChange=Math.abs(endPosition.getXCoordinate()-startPosition.getXCoordinate());

		if ((xChange == 1 && yChange ==1) || (xChange ==1 && yChange == 0) || (xChange ==0 && yChange == 1)){
			previousChange = yChange;
			return true;
		}
		return false;
		
	}


	@Override
	public boolean validMoveWithoutCheck(Board board, Spot startPosition, Spot endPosition) {
		if (!endPosition.isEmpty() && endPosition.getPiece().getColor()==this.getColor()) {
			return false;
		}
		//white castling
		if (color == 0){
			//kings side
			if (startPosition.getXCoordinate() == 4  && startPosition.getYCoordinate() == 7 && endPosition.getXCoordinate() == 6 && endPosition.getYCoordinate() == 7 && first && board.grid[7][7].getPiece().getPieceName().equals("wR") && board.grid[7][7].getPiece().first && board.grid[5][7].isEmpty() && board.grid[6][7].isEmpty()){
				//System.out.println("**white castle kings side**");
				castledK = true;
				first = false;
				return true;
			}
			//queens side
			if (startPosition.getXCoordinate() == 4 && startPosition.getYCoordinate() == 7 && endPosition.getXCoordinate() == 2 && endPosition.getYCoordinate() == 7 && first && board.grid[0][7].getPiece().getPieceName().equals("wR") && board.grid[0][7].getPiece().first && board.grid[1][7].isEmpty() && board.grid[2][7].isEmpty() && board.grid[3][7].isEmpty()){
				//System.out.println("**white castle queens side**");
				castledQ = true;
				first = false;
				return true;
			}
		}
		//black castling
		if (color == 1){
			//kings side
			if (startPosition.getXCoordinate() == 4 && startPosition.getYCoordinate() == 0 && endPosition.getXCoordinate() == 6 && endPosition.getYCoordinate() == 0 && first && board.grid[7][0].getPiece().getPieceName().equals("bR") && board.grid[7][0].getPiece().first && board.grid[5][0].isEmpty() && board.grid[6][0].isEmpty()){
				//System.out.println("**black castle kings side**");
				castledK = true;
				first = false;
				return true;
			}
			//queens side
			if (startPosition.getXCoordinate() == 4 && startPosition.getYCoordinate() == 0 && endPosition.getXCoordinate() == 2 && endPosition.getYCoordinate() == 0 && first && board.grid[0][0].getPiece().getPieceName().equals("bR") && board.grid[0][0].getPiece().first && board.grid[1][0].isEmpty() && board.grid[2][0].isEmpty() && board.grid[3][0].isEmpty()){
				//System.out.println("**black castle queens side**");
				castledQ = true;
				first = false;
				return true;
			}
		}
		int yChange=Math.abs(endPosition.getYCoordinate()-startPosition.getYCoordinate());
		int xChange=Math.abs(endPosition.getXCoordinate()-startPosition.getXCoordinate());

		if ((xChange == 1 && yChange ==1) || (xChange ==1 && yChange == 0) || (xChange ==0 && yChange == 1)){
			previousChange = yChange;
			return true;
		}
		return false;
	}

}