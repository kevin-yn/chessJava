package com.chess.engine.board;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.EmptySpot;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Piece.PlayerSide;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;


public class Board {
	final public Piece[] boardPiecesArray = new Piece[64];
	final private ArrayList<Piece> whitePiecesArrayList = new ArrayList<Piece>(16);
	final private ArrayList<Piece> blackPiecesArrayList = new ArrayList<Piece>(16);
	final private Stack<Move> moveHistorieStack = new Stack<Move>();
	private Piece whiteKingPiece;
	private Piece blackKingPiece;
	private PlayerSide currSide;
	private GameState gameState;
	
	public Board() {
		super();
		initializeBoard();
		whiteKingPiece = boardPiecesArray[60];
		blackKingPiece = boardPiecesArray[4];
		currSide = PlayerSide.White;
		gameState = GameState.Active;
	}

	
	
	public PlayerSide getCurrSide() {
		return currSide;
	}



	/**
	 * switch the currSide (the player to make the next move)
	 */
	private void changeSide() {
		currSide = (currSide == PlayerSide.Black) ? PlayerSide.White: PlayerSide.Black;
	}
	
	/**
	 * Set all initial positions and add them to piecesArrayList
	 */
	private void initializeBoard() {
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
			int index = getIndex(x, y);
			Piece newPiece = new Pawn(side, index);
			boardPiecesArray[index] = newPiece;
			addPiece(newPiece);
		}
	}

	/**
	 * Add a piece from its piece list
	 */
	private void addPiece(Piece pieceToAdd) {
		ArrayList<Piece> list = (pieceToAdd.getSide() == PlayerSide.White) ? whitePiecesArrayList : blackPiecesArrayList;
		list.add(pieceToAdd);
	}
	
	/**
	 * set the other pieces for a whole row
	 * @param y the row number
	 * @param side
	 */
	private void setInitialPositions_Others(int y, PlayerSide side) {
		int index = getIndex(0, y);
		boardPiecesArray[index + 0] = new Rook(side, index + 0); // Rook
		boardPiecesArray[index + 1] = new Knight(side, index + 1); // Knight
		boardPiecesArray[index + 2] = new Bishop(side, index + 2); // Bishop
		boardPiecesArray[index + 3] = new Queen(side, index + 3); // King
		boardPiecesArray[index + 4] = new King(side, index + 4); // Queen
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
			int index = getIndex(x, y);
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
	 * check whether a move is legal, this assumes that the currSide is current!
	 */
	private boolean isLegalMove(Move move) {
		executeMoveWithOutUpdatingHistory(move);
		boolean isCheckMate = isCheckMated(currSide);
		undoMoveWithOutUpdatingHistory(move);
		return !isCheckMate; // if checkMated, then illegal
	}
	
	public void replacePiece(Piece oldPiece, Piece newPiece) {
		if(oldPiece == newPiece || newPiece == null) {
			return;
		}
		ArrayList<Piece> arrayList = oldPiece.getSide() == PlayerSide.White ? whitePiecesArrayList : blackPiecesArrayList;
		int index = arrayList.indexOf(oldPiece);
		arrayList.set(index, newPiece);
		boardPiecesArray[oldPiece.getIndex()] = newPiece;
	}


	/** 
	 * Checks whether a spot is under attack by one side
	 * @ineffectivePiece is one Piece that can not attack at this moment
	 */
	private boolean isUnderattack(PlayerSide attackingSide, int x_cor, int y_cor) {
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
	private boolean isCheckMated(PlayerSide defendingSide) {
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
//		printBoard();
		return isUnderattack(attackingSide, x_cor, y_cor);
	}


	private int getIndex(int x, int y) {
		return x + y * 8;
	}


	public Move getLastMove() {
		return moveHistorieStack.peek();
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
		for(int i = 0; i < picesList.size(); i++) {
			Piece currPiece = picesList.get(i);
			if(!currPiece.isAlive()) {
				continue;
			}
			selectLegalMoves(currPiece.generatePossibleMoves(this), legalMovesList);
		}
		return legalMovesList;
	}
	
	/**
	 * check whether the game has ended
	 */
	public GameState checkGameState() {
		LinkedList<Move> legalMoves = compileAllLegalMoves();
		if(legalMoves.isEmpty()) {
			// printBoard();
			if(isCheckMated(currSide)) {
				gameState = currSide == PlayerSide.White ? GameState.BlackWin : GameState.WhiteWin;
			} else {
				gameState = GameState.Draw;
			}
		} else {
			gameState = GameState.Active;
		}
		return gameState;
	}

	/**
	 * Execute a move without saving the move the movehistoryStack
	 * @return the piece moved
	 */
	public Piece executeMoveWithOutUpdatingHistory(Move move) {
		int endIndex = move.getEndIndex();
		int startIndex = move.getStartIndex();
		Piece capturedPiece = boardPiecesArray[endIndex]; // get capturedPiece
		move.setCaptured_piece(capturedPiece); // record the capturedPiece
		boolean original_status_debug = capturedPiece.setDead(); // set capturedPiece as dead
		if(!original_status_debug) { // DEBUG!!!
			System.err.println("executeMoveWithOutUpdating ERROR");
			return null;
		}
		Piece movedPiece = boardPiecesArray[startIndex]; // get movedPiece
		movedPiece.moveTo(move.getEnd_x(), move.getEnd_y()); // update movedPiece position and moveCount
		// update Board Array
		boardPiecesArray[startIndex] = new EmptySpot(startIndex);
		boardPiecesArray[endIndex] = movedPiece;
		
		
		// special Castling move
		if(move.isCastling()) {
			if(move.getEnd_x() < move.getStart_x()) { // left side castling
				Piece rookPiece = (movedPiece.getSide() == PlayerSide.White) ? boardPiecesArray[56] : boardPiecesArray[0];
				rookPiece.moveTo(3, movedPiece.getY_cor());
				boardPiecesArray[3 + movedPiece.getY_cor() * 8] = rookPiece;
				boardPiecesArray[movedPiece.getY_cor() * 8] = new EmptySpot(movedPiece.getY_cor() * 8);
			} else { // right side castling
				Piece rookPiece = (movedPiece.getSide() == PlayerSide.White) ? boardPiecesArray[63] : boardPiecesArray[7];
				rookPiece.moveTo(5, movedPiece.getY_cor());
				boardPiecesArray[5 + movedPiece.getY_cor() * 8] = rookPiece;
				boardPiecesArray[7 + movedPiece.getY_cor() * 8] = new EmptySpot(7 + movedPiece.getY_cor() * 8);
			}
		} else if(move.isEnPassant()) {
			int captured_cor = move.getEnd_x() + move.getStart_y() * 8;
			Piece capturedPawn = boardPiecesArray[captured_cor];
			capturedPawn.setDead();
			move.setCaptured_piece(capturedPawn);
			boardPiecesArray[captured_cor] = new EmptySpot(captured_cor);
		} else if(move.isPawnPromotion() ) {
//			Piece newPiece = new Queen(movedPiece.getSide(), movedPiece.getIndex());
//			replacePiece(movedPiece, newPiece);
//			movedPiece = newPiece;
		}
		return movedPiece;
	}

	/** 
	 * Undo a move without poping the movehistoryStack
	 * @return the piece moved
	 */
	public Piece undoMoveWithOutUpdatingHistory(Move move) {
		int endIndex = move.getEndIndex();
		int startIndex = move.getStartIndex();
		Piece capturedPiece = move.getCaptured_piece();

		
		boolean original_status_debug = capturedPiece.setAlive(); // set capturedPiece as alive
		// DEBUG
		if(original_status_debug) {
			System.err.println("undoMoveWithOutUpdating ERROR");
			System.err.println(move.getStartIndex());
			System.err.println(move.getEndIndex());
		}
		Piece movedPiece = boardPiecesArray[endIndex];
		movedPiece.returnTo(move.getStart_x(), move.getStart_y());
		// update Board Array
		boardPiecesArray[startIndex] = movedPiece;
		if(move.isEnPassant()) {
			boardPiecesArray[endIndex] = new EmptySpot(endIndex);
		} else {
			boardPiecesArray[endIndex] = capturedPiece;
		}
		
		// speical Castling move
		if(move.isCastling()) {
			
			if(move.getEnd_x() < move.getStart_x()) { // left side castling
				Piece rookPiece = (movedPiece.getSide() == PlayerSide.White) ? boardPiecesArray[59] : boardPiecesArray[3];
				rookPiece.returnTo(0, movedPiece.getY_cor());
				boardPiecesArray[0 + movedPiece.getY_cor() * 8] = rookPiece;
				boardPiecesArray[3 + movedPiece.getY_cor() * 8] = new EmptySpot(movedPiece.getY_cor() * 8);
			} else { // right side castling
				Piece rookPiece = (movedPiece.getSide() == PlayerSide.White) ? boardPiecesArray[61] : boardPiecesArray[5];
				rookPiece.returnTo(7, movedPiece.getY_cor());
				boardPiecesArray[7 + movedPiece.getY_cor() * 8] = rookPiece;
				boardPiecesArray[5 + movedPiece.getY_cor() * 8] = new EmptySpot(movedPiece.getY_cor() * 8);
			}
		} else if(move.isEnPassant()) {
			int captured_cor = move.getEnd_x() + move.getStart_y() * 8;
			Piece capturedPawn = move.getCaptured_piece();
			capturedPawn.setAlive();
			boardPiecesArray[captured_cor] = capturedPawn;
		} else if(move.isPawnPromotion()) {
			Piece oldPawnPiece = new Pawn(movedPiece.getSide(), movedPiece.getIndex());
			replacePiece(movedPiece, oldPawnPiece);
		}
		return movedPiece;
	}
	
	/** 
	 * Execute a move
	 * updating the moveHistorieStack AND CURR_SIDE and GameState
	 */
	public void executeMoveComplete(Move move) {
		executeMoveWithOutUpdatingHistory(move);
		addMoveToHistory(move);
		changeSide();
		checkGameState();
	}

	public GameState getGameState() {
		return gameState;
	}

	/**
	 * Undo the last move only
	 * updating the moveHistorieStack and CURR_SIDE
	 */
	public void undoMoveComplete() {
		undoMoveWithOutUpdatingHistory(moveHistorieStack.pop());
		changeSide();
		checkGameState();
	}

	/** 
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
	 * return the PlayerSide of the piece at input coordinate
	 */
	public PlayerSide getPlayerSide(int index) {
		return boardPiecesArray[index].getSide();
	}
	
	/**
	 * check whether the board is empty at input coordinate
	 */
	public boolean isEmptyAt(int x_cor, int y_cor) {
		return boardPiecesArray[x_cor + y_cor * 8].isEmpty();
	}
	
	/**
	 * check whether the board is empty at input index
	 */
	public boolean isEmptyAt(int index) {
		return boardPiecesArray[index].isEmpty();
	}
	
	/**
	 * get the piece at input coordinate
	 */
	public Piece getPiece(int x_cor, int y_cor) {
		return boardPiecesArray[x_cor + y_cor * 8];
	}
	
	/**
	 * get the piece at input coordinate
	 */
	public Piece getPiece(int index) {
		return boardPiecesArray[index];
	}
	
	/**
	 * Check whether it is legal to perform Castling for side
	 */
	public boolean isCastlingLegal(PlayerSide side, boolean leftSide) {
		/**
		 * Castling
		 * 1. King and Rook is not moved yet
		 * 2. All space in between is empty
		 * 3. King's whole path is not under attack
		 */
		
		// 1. check King is unmoved
		Piece kingPiece = (side == PlayerSide.White) ? whiteKingPiece: blackKingPiece;
		if(kingPiece.isMoved()) {
			return false;
		}
		// 1. check rook is unmoved
		Piece rookPiece;
		if(leftSide) {
			rookPiece = (side == PlayerSide.White) ? boardPiecesArray[56] : boardPiecesArray[0];
		} else {
			rookPiece = (side == PlayerSide.White) ? boardPiecesArray[63] : boardPiecesArray[7];
		}
		if(rookPiece.isMoved()) {
			return false;
		}
		
		// 2. check all space is empty
		int y = (side == PlayerSide.White) ? 7 : 0;
		if(leftSide) {
			for(int x = 1; x <= 3; x++) {
				if(!boardPiecesArray[x + y * 8].isEmpty()) {
					return false;
				}
			}
		} else {
			for(int x = 5; x <= 6; x++) {
				if(!boardPiecesArray[x + y * 8].isEmpty()) {
					return false;
				}
			}
		}

		// 3. check the route is safe
		PlayerSide attackingSide = Piece.getOppositeSide(side);
		if(leftSide) {
			for(int x = 2; x <= 3; x++) {
				if(isUnderattack(attackingSide, x, y)) {
					return false;
				}
			}
		} else {
			for(int x = 5; x <= 6; x++) {
				if(isUnderattack(attackingSide, x, y)) {
					return false;
				}
			}
		}
		return true;
	}
 	
	public String getCurrentSideString() {
		return currSide == PlayerSide.White ? "White" : "Black";
	}
	
	/**
	 * make a move function used by the GUI
	 * @param move
	 * @return whether the move is legal and successfully executed
	 */
	public Move makeAMove(Move move) {
		// check whether move is legal
			// check whether the moved piece belongs to the currSide
		Piece movedPiece = boardPiecesArray[move.getStartIndex()];
		if(movedPiece.getSide() != currSide) {
			return null;
		}
			// check whether this move belongs to the list of possible moves generated by this piece
		boolean found = false;
		LinkedList<Move> possibleMoves = movedPiece.generatePossibleMoves(this);
		if(possibleMoves == null) {
			System.err.println("possibleMoves is Null");
			return null;
		}
		for(Move currMove: possibleMoves) {
			if(currMove.getEndIndex() == move.getEndIndex() && currMove.getStartIndex() == move.getStartIndex()) {
				found = true;
				move = currMove;
				break;
			}
		}
		if(!found) {
			return null;
		}
			// check whether this move is legal (based on checkmate)
		if(!isLegalMove(move)) {
			return null;
		}
		// execute the move complete
		executeMoveComplete(move);
		printBoard();
		return move;
	}
	
	/**
	 * undo a move function used by the GUI
	 */
	public boolean undoAMove() {
		if(moveHistorieStack.isEmpty()) {
			return false;
		}
		undoMoveComplete();
		return true;
	}
	
	/**
	 * resets isProtected of all the pieces of the current side to be false
	 */
	public void resetIsProtected() {
		ArrayList<Piece> pieces = getCurrentSidePieces();
		for(Piece piece : pieces) {
			piece.setProtected(false);
		}
	}

	public ArrayList<Piece> getCurrentSidePieces() {
		return (currSide == PlayerSide.White) ? whitePiecesArrayList : blackPiecesArrayList;
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
	
	public enum GameState {
		Active,
		WhiteWin,
		BlackWin,
		Draw
	}
}
