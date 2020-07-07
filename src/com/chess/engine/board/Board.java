package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Piece.PlayerSide;
import com.chess.engine.pieces.Piece.PieceType;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Rook;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.EmptySpot;

import java.util.*;


public class Board {
	final private Piece[] PIECES_ARR = new Piece[64];
	final private ArrayList<Piece> whitePiecesArrayList = new ArrayList<Piece>(16);
	final private ArrayList<Piece> blackPiecesArrayList = new ArrayList<Piece>(16);
	final private Stack<Move> moveHistorieStack = new Stack<Move>();
	private PlayerSide currSide;
	
	
	public Board() {
		super();
		initializeBoard();
	}

	private void initializeBoard() {
		setInitialPositions();
		currSide = PlayerSide.White;
	}
	
	private void changeSide() {
		currSide = (currSide == PlayerSide.Black) ? PlayerSide.White: PlayerSide.Black;
	}
	
	private void setInitialPositions() {
		setInitialPositions_Others(0, PlayerSide.Black);
		setInitialPositions_Pawns(1, PlayerSide.Black);
		setInitialPositions_Empties(2);
		setInitialPositions_Empties(3);
		setInitialPositions_Empties(4);
		setInitialPositions_Empties(5);
		setInitialPositions_Pawns(6, PlayerSide.White);
		setInitialPositions_Others(7, PlayerSide.White);
		
	}
	
	// TODO: add pieces to the piecesArrayList
	private void setInitialPositions_Pawns(int y, PlayerSide side) {
		for(int x = 0; x < 8; x++) {
			int index = BoardUtils.getIndex(x, y);
			PIECES_ARR[index] = new Pawn(side);
		}
	}
	
	private void setInitialPositions_Others(int y, PlayerSide side) {
		int index = BoardUtils.getIndex(0, y);
		PIECES_ARR[index + 0] = new Rook(side); // Rook
		PIECES_ARR[index + 1] = new Knight(side); // Knight
		PIECES_ARR[index + 2] = new Bishop(side); // Bishop
		PIECES_ARR[index + 3] = new King(side); // King
		PIECES_ARR[index + 4] = new Queen(side); // Queen
		PIECES_ARR[index + 5] = new Bishop(side); // Bishop
		PIECES_ARR[index + 6] = new Knight(side); // Knight
		PIECES_ARR[index + 7] = new Rook(side); // Rook
	}
	
	private void setInitialPositions_Empties(final int y) {
		for(int x = 0; x < 8; x++) {
			int index = BoardUtils.getIndex(x, y);
			PIECES_ARR[index] = new EmptySpot();
		}
	}
	
	private void printBoard() {
		if(currSide == PlayerSide.White) {
			System.out.println("White Turn");
		} else {
			System.out.println("Black Turn");
		}
		System.out.println("   0  1  2  3  4  5  6  7");
		System.out.println("");
		for(Integer y = 0; y < 8; y++) {
			StringBuffer s = new StringBuffer(y.toString() + "  ");
			for(int x = 0; x < 8; x++) {
				PieceType pieceType = PIECES_ARR[x + y * 8].getType();
				PlayerSide side = PIECES_ARR[x + y * 8].getSide();
				switch (pieceType) {
				case Rook:
					if(side == PlayerSide.Black) {
						s.append('R');
					} else {
						s.append('r');
					}
					break;
				case Bishop:
					if(side == PlayerSide.Black) {
						s.append('B');
					} else {
						s.append('b');
					}
					break;
				case Knight:
					if(side == PlayerSide.Black) {
						s.append('N');
					} else {
						s.append('n');
					}
					break;
				case King:
					if(side == PlayerSide.Black) {
						s.append('K');
					} else {
						s.append('k');
					}
					break;
				case Queen:
					if(side == PlayerSide.Black) {
						s.append('Q');
					} else {
						s.append('q');
					}
					break;
				case Pawn:
					if(side == PlayerSide.Black) {
						s.append('P');
					} else {
						s.append('p');
					}
					break;
				case EmptySpot:
					s.append('O');
					break;
				default:
					break;
				}
				s.append("  ");
			}
			System.out.println(s);
			System.out.println("");
		}
		System.out.println("");
		System.out.println("");
		System.out.println("");
	}
	
	public static void main(String[] args) {
	}
	

	/**
	 * Add a move to moveHistorieStack
	 */
	private void addMoveToHistory(Move move) {
	}
	
	/**
	 * Get the last move made
	 */
	public Move getLastMove() {
		return new Move(0, 0, 0, 0);
	}
	
	/**
	 * Remove the moves that will result in illegal situations (checkmated)
	 */
	public void removeIllegalMoves(ArrayList<Move> possibleMoves) {
		// go through the list
		// remove illegal moves from input list
		// return nothing
	}
	
	/**
	 * Generate all legal moves
	 */
	public ArrayList<Move> generateAllLegalMoves() {
		// go through curr side's all pieces
		// ask them to generate all possible moves
		// use removeIllegalMoves to remove illegal moves
		// return the legal moves in ArrayList
		return new ArrayList<>();
	}

	/** 
	 * Checks whether a spot is under attack by one side
	 */
	public boolean isUnderattack(PlayerSide attackingSide, int x_cor, int y_cor) {
		// TODO
		return false;
	}

	/**
	 * Checks whether a side is being checkmated
	 */
	public boolean isCheckMated(PlayerSide side) {
		return false;
	}
	
	/**
	 * Execute a move without updating whitePiecesArrayList or blackPiecesArrayList
	 */
	public void executeMoveWithOutUpdating(Move move) {
		
	}

	/**
	 * Undo a move without updating whitePiecesArrayList or blackPiecesArrayList
	 */
	public void unDoMoveWithOutUpdating(Move move) {
		
	}

	/**
	 * Execute a move
	 * update the pieceList and moveHistorieStack
	 */
	public void executeMoveComplete(Move move) {
		
	}

	/**
	 * Undo a move
	 * update the pieceList and moveHistorieStack
	 */
	public void unDoMoveComplete(Move move) {
		
	}

	/**
	 * handle Pawn Promotion
	 */
	public void pawnPromotionHandler() {
		
	}
	
	/**
	 * Remove a piece from its piece list
	 */
	private void removePiece(Piece pieceToRemove) {
		
	}
	
	/**
	 * Add a piece from its piece list
	 */
	private void addPiece(Piece pieceToAdd) {
		
	}
}
