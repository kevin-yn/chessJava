package com.chess.engine.pieces;

import java.util.LinkedList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class King extends Piece {
	/**
	 * This helps to determine whether King is eligible for Castling
	 * @param moved
	 */

	public King(PlayerSide side, int index) {
		super(side, index);
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public  PieceType getType() {
		return PieceType.King;
	}

	@Override
	public boolean isAttacking(final Board board, int target_x_cor, int target_y_cor) {
		return false;
	}
	
	@Override
	public LinkedList<Move> generatePossibleMoves(final Board board) {
		LinkedList<Move> possibleMoves = new LinkedList<>();
		// normal moves
		generateOneMove(this.x_cor + 1, this.y_cor + 0, board, possibleMoves);
		generateOneMove(this.x_cor + 1, this.y_cor + 1, board, possibleMoves);
		generateOneMove(this.x_cor + 1, this.y_cor - 1, board, possibleMoves);
		generateOneMove(this.x_cor + 0, this.y_cor + 1, board, possibleMoves);
		generateOneMove(this.x_cor + 0, this.y_cor - 1, board, possibleMoves);
		generateOneMove(this.x_cor - 1, this.y_cor + 0, board, possibleMoves);
		generateOneMove(this.x_cor - 1, this.y_cor + 1, board, possibleMoves);
		generateOneMove(this.x_cor - 1, this.y_cor - 1, board, possibleMoves);
		// Castling
		if(board.isCastlingLegal(this.side, true)) {
			Move move = new Move(this.x_cor, this.y_cor, 2, this.y_cor);
			move.setCastling();
			System.out.println("Left Castling is legal");
			possibleMoves.add(move);
		} else {
			System.out.println("Left Castling is NOT legal");
		}
		
		if(board.isCastlingLegal(this.side, false)) {
			Move move = new Move(this.x_cor, this.y_cor, 6, this.y_cor);
			move.setCastling();
			System.out.println("Right Castling is legal");
			possibleMoves.add(move);
		} else {
			System.out.println("Right Castling is NOT legal");
		}
		return possibleMoves;
	}
	
	private void generateOneMove(int target_x, int target_y, Board board, LinkedList<Move> possibleMoves) {
		if(!validCor(target_x, target_y)) { // check isValid Coordinates
			return;
		}
		if(board.getPlayerSide(target_x, target_y) == this.side) { // check if ally piece 
			return;
		}
		possibleMoves.add(new Move(this.x_cor, this.y_cor, target_x, target_y));
	}
	
	
	@Override
	public char getLetterSymbol() {
		if(side == PlayerSide.White) {
			return 'k';
		} else {
			return 'K';
		}
	}

}
