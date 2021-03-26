package chess;

import java.util.*;
import java.io.*;
import pieces.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import static pieces.King.castledK;
import static pieces.King.castledQ;


public class Chess {
	public static Board chessBoard;
	static boolean whiteTurn = true;

	public static void main(String[] args) {
		setUpGame();
		Scanner sc = new Scanner(System.in);
		String input = "";

		//TODO Check for checkmate
		while (true) { //while not checkmate
			
			if (whiteTurn) {
				System.out.println();
				boolean check=isKingInCheck(0,findKingPosition(0));
				if (check) {
					boolean checkmate=isCheckMate(0);
					if (checkmate) {
						System.out.println("Checkmate");
						System.out.println("Black Wins");
						System.exit(1);
					}
					else {
						System.out.println("Check");
					}
				}
				System.out.print("White's move: ");
				
			}
			else {
				System.out.println();
				boolean check=isKingInCheck(1,findKingPosition(1));
				if (check) {
					boolean checkmate=isCheckMate(1);
					if (checkmate) {
						System.out.println("Checkmate");
						System.out.println("White Wins");
						System.exit(1);
					}
					else {
						System.out.println("Check");
					}
				}
				
				System.out.print("Black's move: ");
			}

			
			input = sc.nextLine();
			newBoardState(input);
		}
		
	}
	public static void setUpGame() {
		chessBoard = new Board();
		chessBoard.drawBoard();
	}
	//TODO Capturing & board updates **DONE**
	//TODO weird board update bug due to piece naming **DONE**
	public static void newBoardState(String input) {
		String[] tokens = input.trim().toLowerCase().split("\\s+");
		if (tokens.length == 0 || tokens.length > 3) {
			System.out.println("Illegal move, try again");
			if (whiteTurn) {
				System.out.println("White's move: ");
			}
			else {
				System.out.println("Black's move: ");
			}	
		}
		else if (tokens.length == 1) {
			if (tokens[0].equals("resign")) {
				if (whiteTurn) {
					System.out.println("Black wins");
				}
				else {
					System.out.println("White wins");
				}
				System.exit(1);
			}
		}
		else { //correct inputs
			//move tokens[0] to tokens[1] if a valid move
			int xfrom = Math.abs('a'-tokens[0].charAt(0));
			int yfrom = Math.abs(8-Character.getNumericValue(tokens[0].charAt(1)));
			int xto = Math.abs('a'-tokens[1].charAt(0));
			int yto = Math.abs(8-Character.getNumericValue(tokens[1].charAt(1)));
			int currColor = whiteTurn ? 0 : 1;
			Spot currSpot = chessBoard.grid[xfrom][yfrom];
			Spot destSpot = chessBoard.grid[xto][yto];

			if (currSpot.isEmpty()){
				System.out.println("Illegal move, try again (current spot is empty)");
			}else if(currSpot.getPiece().getColor() == currColor && currSpot.getPiece().validMoveWithoutCheck(chessBoard, currSpot, destSpot) && chessBoard.isPathEmpty(currSpot, destSpot)) {
				boolean pawnPromo = false;
				//valid move
				//CASTLING
				if (castledK) {
					if (whiteTurn){
						//move rook only as king moving is handled below
						chessBoard.grid[5][7].setPiece(chessBoard.grid[7][7].getPiece());
						chessBoard.grid[7][7].setPiece(null);
					}else{
						chessBoard.grid[5][0].setPiece(chessBoard.grid[7][0].getPiece());
						chessBoard.grid[7][0].setPiece(null);
					}
					castledK = false;
				}
				if (castledQ) {
					if (whiteTurn){
						//same thing move rook only
						chessBoard.grid[3][7].setPiece(chessBoard.grid[0][7].getPiece());
						chessBoard.grid[0][7].setPiece(null);
					}else{
						chessBoard.grid[3][0].setPiece(chessBoard.grid[0][0].getPiece());
						chessBoard.grid[0][0].setPiece(null);
					}
					castledQ = false;
				}
				//remove piece from old Spot
				System.out.println("selected piece and color: " + currSpot.getPiece().getPieceName() + " " + currSpot.getPiece().getColor());
				ChessPiece mover = currSpot.getPiece();

				if (mover.getPieceName().substring(1).equals("p")){ //pawn promo
					pawnPromo = true;
					if (whiteTurn){
						if (currSpot.getYCoordinate() == 1){
							pawnPromotion(currSpot, destSpot, tokens[2].charAt(0), 0);
							System.out.println("white's pawn has been promoted to " + tokens[2]);
						}
					}else{
						if (currSpot.getYCoordinate() == 6){
							pawnPromotion(currSpot, destSpot, tokens[2].charAt(0), 1);
							System.out.println("black's pawn has been promoted to " + tokens[2]);
						}
					}
				}
				currSpot.setPiece(null);

				//check new Spot for enemy Piece, if so then remove
				if (destSpot.getPiece() != null && !pawnPromo){
					destSpot.piece.Dead();
					System.out.println("\nBAM! " + destSpot.getPiece().getPieceName() + " was captured by " + mover.getPieceName() + " @ (" + xto + ", " + yto +")");
				}
				if (!pawnPromo){destSpot.setPiece(mover);}
				chessBoard.drawBoard();
				whiteTurn = whiteTurn ? false : true; //switch colors
			} else {
				System.out.println("selected piece and color: " + currSpot.getPiece().getPieceName() + " " + (currSpot.getPiece().getColor() == 0 ? "white" : "black"));
				System.out.println("attempted move: " + currSpot.getXCoordinate() + "," + currSpot.getYCoordinate() + " to " + destSpot.getXCoordinate() + "," + destSpot.getYCoordinate());
				System.out.println("Illegal move, try again (illegal move)");
				
			}

		}
	}
	public static void pawnPromotion(Spot curr, Spot dest, char promo, int color){
		switch (promo){
			case 'R':
				dest.setPiece(new Rook(color));
				break;
			case 'N':
				dest.setPiece(new Knight(color));
				break;
			case 'B':
				dest.setPiece(new Bishop(color));
				break;
			default: //dont have to do case Q because default covers that
				dest.setPiece(new Queen(color));
		}
	}
   	public static boolean isKingInCheck(int color, Spot kingPosition) {
		if (color==0) {   // White's move
	
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				if (chessBoard.grid[j][i].getPiece()!=null && chessBoard.grid[j][i].getPiece().getColor()==1 && chessBoard.grid[j][i].getPiece().validMove(chessBoard, chessBoard.grid[j][i], kingPosition ) && chessBoard.isPathEmpty(chessBoard.grid[j][i], kingPosition)) {
					return true;
				}
			}
		}
		}
		else {   // Black's move
			
			for (int i=0;i<8;i++) {
				for (int j=0;j<8;j++) {
					if (chessBoard.grid[j][i].getPiece()!=null && chessBoard.grid[j][i].getPiece().getColor()==0 && chessBoard.grid[j][i].getPiece().validMove(chessBoard, chessBoard.grid[j][i], kingPosition ) && chessBoard.isPathEmpty(chessBoard.grid[j][i], kingPosition)) {
						return true;
					}
				}
			}
	}
		return false;
	}
	public static boolean isCheckMate(int color) {
		Spot kingPosition=findKingPosition(color);
	
		int[] xDirection=new int[] {-1,0,1,-1,1,-1,0,1};
		int[] yDirection=new int[] {-1,-1,-1,0,0,1,1,1};
		
		//if (isKingInCheck(color,kingPosition)) {
			for (int i=0;i<xDirection.length;i++) {
				int newX=kingPosition.getXCoordinate()+xDirection[i];
				int newY=kingPosition.getYCoordinate()+yDirection[i];
				if (newX<0 || newX>7 || newY<0 || newY>7) {
					continue;
				}
				Spot endPosition=chessBoard.grid[newX][newY];
				if (kingPosition.getPiece().validMoveWithoutCheck(chessBoard, kingPosition, endPosition)) {
					if (!isKingInCheck(color,endPosition)) {
						return false;
				}
			}
		}
			if (!validMoves(color)) {
				return true;
			}

		return false;
	}
	public static Spot findKingPosition(int color) {
		Spot kingPosition=null;
		if (color==0) {
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				if (chessBoard.grid[j][i].getPiece()!=null && chessBoard.grid[j][i].getPiece().getPieceName().equals("wK")) {
					kingPosition=chessBoard.grid[j][i];
					break;
				}
					
			}
		}
		}
		else if (color==1) {
			for (int i=0;i<8;i++) {
				for (int j=0;j<8;j++) {
					if (chessBoard.grid[j][i].getPiece()!=null && chessBoard.grid[j][i].getPiece().getPieceName().equals("bK")) {
						kingPosition=chessBoard.grid[j][i];
						break;
					}
						
				}
			}
		}
		return kingPosition;
	}
	
	public static boolean validMoves(int color) {
		String pieceToExclude="";
		if (color==0) {
			pieceToExclude="wK";
		}
		else {
			pieceToExclude="bK";
		}
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				if (chessBoard.grid[j][i].getPiece()!=null && chessBoard.grid[j][i].getPiece().getColor()==color && !chessBoard.grid[j][i].getPiece().getPieceName().equals(pieceToExclude)) {
					for (int a=0;a<8;a++) {
						for (int b=0;b<8;b++) {
							Spot newPosition=chessBoard.grid[b][a];
							ChessPiece newPositionPiece=newPosition.getPiece();
							ChessPiece current=chessBoard.grid[j][i].getPiece();
							if (chessBoard.grid[j][i].getPiece().validMoveWithoutCheck(chessBoard, chessBoard.grid[j][i], newPosition) ) {
								
								
								newPosition.setPiece(current);
								chessBoard.grid[j][i].setPiece(null);
								if (!isKingInCheck(color,findKingPosition(color))) {
									chessBoard.grid[j][i].setPiece(current);
									newPosition.setPiece(newPositionPiece);
									return true;
								}
							}
							chessBoard.grid[j][i].setPiece(current);
							newPosition.setPiece(newPositionPiece);
						}
					}
				}
			}
		}
		
		return false;
	}
	
	
	
	
	
	/*  0  1  2  3  4  5  6  7  
	 *|bR|bN|bB|bQ|bK|bB|bN|bR| 0  j
	 *|bP|bP|bP|bP|bP|bP|bP|bP| 1
	 *|  |##|  |##|  |##|  |##| 2
	 *|##|  |##|  |##|  |##|  | 3
	 *|  |##|  |##|  |##|  |##| 4
	 *|##|  |##|  |##|  |##|  | 5
	 *|wP|wP|wP|wP|wP|wP|wP|wP| 6
	 *|wR|wN|wB|wQ|wK|wB|wN|wR| 7  */
	
}
