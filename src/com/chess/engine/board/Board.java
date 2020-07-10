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
	final public Piece[] boardPiecesArray = new Piece[64];
	final private ArrayList<Piece> whitePiecesArrayList = new ArrayList<Piece>(16);
	final private ArrayList<Piece> blackPiecesArrayList = new ArrayList<Piece>(16);
	private Piece whiteKingPiece;
	private Piece blackKingPiece;
	final private Stack<Move> moveHistorieStack = new Stack<Move>();
	//final private LinkedList<Move> allLegalMoves = new LinkedList<>();
	private PlayerSide currSide;
	
	
	public Board() {
		super();
		initializeBoard();
	}

	/**
	 * Initialize the Board, the piecesArrayList
	 */
	private void initializeBoard() {
		setInitialPositions();
		whiteKingPiece = boardPiecesArray[59];
		blackKingPiece = boardPiecesArray[3];
		currSide = PlayerSide.White;
	}
	
	/**
	 * switch the currSide (the player to make the next move)
	 */
	private void changeSide() {
		currSide = (currSide == PlayerSide.Black) ? PlayerSide.White: PlayerSide.Black;
	}
	
	/**
	 * dispatch function to set all initial positions
	 */
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
	
	/**
	 * set the initial pawns for a whole row
	 * @param y the row number
	 * @param side
	 */
	private void setInitialPositions_Pawns(int y, PlayerSide side) {
		for(int x = 0; x < 8; x++) {
			int index = BoardUtils.getIndex(x, y);
			Piece newPiece = new Pawn(side, index);
			boardPiecesArray[index] = newPiece;
			addPiece(newPiece);
		}
	}
	
	/**
	 * set the other pieces for a whole row
	 * @param y the row number
	 * @param side
	 */
	private void setInitialPositions_Others(int y, PlayerSide side) {
		int index = BoardUtils.getIndex(0, y);
		boardPiecesArray[index + 0] = new Rook(side, index + 0); // Rook
		boardPiecesArray[index + 1] = new Knight(side, index + 1); // Knight
		boardPiecesArray[index + 2] = new Bishop(side, index + 2); // Bishop
		boardPiecesArray[index + 3] = new King(side, index + 3); // King
		boardPiecesArray[index + 4] = new Queen(side, index + 4); // Queen
		boardPiecesArray[index + 5] = new Bishop(side, index + 5); // Bishop
		boardPiecesArray[index + 6] = new Knight(side, index + 6); // Knight
		boardPiecesArray[index + 7] = new Rook(side, index + 7); // Rook
		// add the pieces
		for(int i = 0; i < 8; i++) {
			addPiece(boardPiecesArray[index + i]);
		}
	}
	
	/**
	 * set the empty_spots for a whole row
	 * @param y the row number
	 */
	private void setInitialPositions_Empties(final int y) {
		for(int x = 0; x < 8; x++) {
			int index = BoardUtils.getIndex(x, y);
			boardPiecesArray[index] = new EmptySpot(index);
		}
	}
	
	
	/**
	 * Add a move to moveHistorieStack
	 */
	private void addMoveToHistory(Move move) {
		moveHistorieStack.push(move);
	}
	
	/**
	 * @return the Removed last move made
	 */
	private Move removeLastMove() {
		return moveHistorieStack.pop();
	}
	/**
	 * Get the last move made
	 */
	public Move getLastMove() {
		return moveHistorieStack.peek();
	}
	
	
	/**
	 * check whether a move is legal, this assumes that the currSide is current!
	 */
	private boolean isLegalMove(Move move) {
		executeMoveWithOutUpdatingHistory(move);
		boolean isCheckMate = isCheckMated(currSide);
		undoMoveWithOutUpdatingHistory(move);
		return !isCheckMate; // if checkMated, then illegal
	}
	
	/**
	 * select the legal moves from the possible moves returned by the pieces 
	 * Add them to allLegalMoves
	 */
	public void selectLegalMoves(LinkedList<Move> possibleMoves, LinkedList<Move> legalMovesList) {
		for(Move currMove : possibleMoves) {
			if(isLegalMove(currMove)) {
				legalMovesList.add(currMove);
			}
		}
	}
	
	/**
	 * Go through the pieces, get the pieces to generate possible moves, select legal ones, add to allLegalMoves
	 */
	public LinkedList<Move> compileAllLegalMoves() {
		// go through curr side's all pieces
		// ask them to generate all possible moves
		// use selectLegalMoves to add only legal ones
		LinkedList<Move> legalMovesList = new LinkedList<>();
		ArrayList<Piece> picesList = (currSide == PlayerSide.White) ? whitePiecesArrayList : blackPiecesArrayList;
		for(Piece currPiece : picesList) {
			selectLegalMoves(currPiece.generatePossibleMoves(this), legalMovesList);
		}
		return legalMovesList;
	}

	/** 
	 * Checks whether a spot is under attack by one side
	 * @ineffectivePiece is one Piece that can not attack at this moment
	 */
	public boolean isUnderattack(PlayerSide attackingSide, int x_cor, int y_cor) {
		ArrayList<Piece> list = (attackingSide == PlayerSide.White) ? whitePiecesArrayList : blackPiecesArrayList;
		for(Piece piece : list) {
			if(piece.isAlive() && piece.isAttacking(this, x_cor, y_cor)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether a side is being checkmated
	 * When this function is used while checking legal moves, capturedPiece is the piece that is already captured but still exists in the pieceList
	 */
	public boolean isCheckMated(PlayerSide defendingSide) {
		PlayerSide attackingSide;
		int x_cor;
		int y_cor;
		if(defendingSide == PlayerSide.White) {
			attackingSide = PlayerSide.Black;
			x_cor = whiteKingPiece.getX_cor();
			y_cor = whiteKingPiece.getY_cor();
		} else {
			attackingSide = PlayerSide.White;
			x_cor = blackKingPiece.getX_cor();
			y_cor = blackKingPiece.getY_cor();
		}
		return isUnderattack(attackingSide, x_cor, y_cor);
	}
	
	/**
	 * Execute a move without saving the move the movehistoryStack
	 */
	public void executeMoveWithOutUpdatingHistory(Move move) {
		int endIndex = move.getEndIndex();
		int startIndex = move.getStartIndex();
		Piece capturedPiece = boardPiecesArray[endIndex]; // get capturedPiece
		move.setCaptured_piece(capturedPiece); // record the capturedPiece
		boolean original_status_debug = capturedPiece.setDead(); // set capturedPiece as dead
		if(!original_status_debug) { // DEBUG!!!
			System.err.println("executeMoveWithOutUpdating ERROR");
			return;
		}
		Piece movedPiece = boardPiecesArray[startIndex]; // get movedPiece
		movedPiece.setCor(move.getEnd_x(), move.getEnd_y()); // update movedPiece position
		// update Board Array
		boardPiecesArray[startIndex] = new EmptySpot(startIndex);
		boardPiecesArray[endIndex] = movedPiece;
	}

	/** TODO
	 * Undo a move without updating the @alive of Piece
	 */
	public void undoMoveWithOutUpdatingHistory(Move move) {
		int endIndex = move.getEndIndex();
		int startIndex = move.getStartIndex();
		Piece capturedPiece = move.getCaptured_piece();
		boolean original_status_debug = capturedPiece.setAlive(); // set capturedPiece as dead
		// DEBUG
		if(original_status_debug || move.getEnd_x() != capturedPiece.getX_cor() || move.getEnd_y() != capturedPiece.getY_cor()) {
			System.err.println("undoMoveWithOutUpdating ERROR");
		}
		Piece movedPiece = boardPiecesArray[endIndex];
		movedPiece.setCor(move.getStart_x(), move.getStart_y());
		// update Board Array
		boardPiecesArray[startIndex] = movedPiece;
		boardPiecesArray[endIndex] = capturedPiece;
	}
	
	/** TODO
	 * Execute a move
	 * updating the moveHistorieStack AND CURR_SIDE
	 */
	public void executeMoveComplete(Move move) {
		executeMoveWithOutUpdatingHistory(move);
		addMoveToHistory(move);
		changeSide();
	}

	/** TODO
	 * Undo the last move only
	 * updating the moveHistorieStack
	 */
	public void undoMoveComplete() {
		undoMoveWithOutUpdatingHistory(removeLastMove());
		changeSide();
	}

	/** TODO
	 * handle Pawn Promotion
	 */
	public void pawnPromotionHandler() {
		
	}
	
	/**
	 * return the PlayerSide of the piece at input coordinate
	 */
	public PlayerSide getPlayerSide(int x_cor, int y_cor) {
		return boardPiecesArray[x_cor + y_cor * 8].getSide();
	}
	
	/**
	 * check whether the board is empty at input coordiante
	 */
	public boolean isEmptyAt(int x_cor, int y_cor) {
		return boardPiecesArray[x_cor + y_cor * 8].isEmpty();
	}
	
	/**
	 * get the piece at input coordinate
	 */
	public Piece getPiece(int x_cor, int y_cor) {
		return boardPiecesArray[x_cor + y_cor * 8];
	}
	
	// ==================================================================================================================
	// 											DEBUG FUNCTIONS
	// ==================================================================================================================
	
	/**
	 * print the board and the current player for debug purposes
	 */
	public void printBoard() {
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
				s.append(boardPiecesArray[y * 8 + x].getLetterSymbol() + "  ");
			}
			System.out.println(s);
			System.out.println("");
		}
		System.out.println("");
		System.out.println("");
		System.out.println("");
	}
	
	/** for debug purposes
	 * print the pieces of one sides ALIVE OR DEAD
	 */
	public void printAlivePieces(PlayerSide side, boolean isAlive) {
		ArrayList<Piece> list = (side == PlayerSide.White) ? whitePiecesArrayList : blackPiecesArrayList;
		System.out.println(side + ": " + list.size());
		for(Piece piece : list) {
			if(piece.isAlive() == isAlive) {
				System.out.println(piece.getLetterSymbol());
			}
		}
	}
	
	/**
	 * check whether the board info is in sync with the coordinate info in Piece
	 */
	public boolean checkBoard() {
		boolean correct = true;
		for(int i = 0; i < 8 * 8; i++) {
			if(i != boardPiecesArray[i].getIndex()) {
				System.err.print("wrong index at ");
				System.err.println(i);
				System.err.println(boardPiecesArray[i].getIndex());
				correct = false;
			}
		}
		return correct;
	}
	
	// ==================================================================================================================
	// 											USELESS FUNCTIONS
	// ==================================================================================================================
	
	
	
	
	/**
	 * Remove a piece from its piece list
	 */
	private void removePiece(Piece pieceToRemove) {
		ArrayList<Piece> list = (pieceToRemove.getSide() == PlayerSide.White) ? whitePiecesArrayList : blackPiecesArrayList;
		list.remove(pieceToRemove);
	}
	
	/**
	 * Add a piece from its piece list
	 */
	private void addPiece(Piece pieceToAdd) {
		ArrayList<Piece> list = (pieceToAdd.getSide() == PlayerSide.White) ? whitePiecesArrayList : blackPiecesArrayList;
		list.add(pieceToAdd);
	}
}
