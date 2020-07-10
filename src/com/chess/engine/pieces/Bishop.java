package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.LinkedList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece.PieceType;

public class Bishop extends Bishop_Rook_Queen{
	
	public Bishop(PlayerSide side, int index) {
		super(side, index);
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public  PieceType getType() {
		return PieceType.Bishop;
	}
	
	@Override
	public boolean isLegalMove(Move move) {
		int start_x = move.getStart_x();
		int start_y = move.getStart_y();
		int end_x = move.getEnd_x();
		int end_y = move.getEnd_y();
		return (end_x - start_x == end_y - start_y);
	}
	
	@Override
	public boolean isAttacking(final Board board, int target_x_cor, int target_y_cor) {
		boolean same_x = (this.x_cor == target_x_cor);
		boolean same_y = (this.y_cor == target_y_cor);
		// check whether same_x or same_y
		if(same_x || same_y) {
			return false;
		}
		// check whether it is on the line
		if(Math.abs(this.x_cor - target_x_cor) != Math.abs(this.y_cor - target_y_cor)) {
			return false;
		}
		int increment_x = target_x_cor > this.x_cor ? 1 : -1;
		int increment_y = target_y_cor > this.y_cor ? 1 : -1;
		return isPathOpen(target_x_cor, target_y_cor, this.x_cor, this.y_cor, increment_x, increment_y, board);
	}
	
	@Override
	public LinkedList<Move> generatePossibleMoves(final Board board) {
		// add diagonal
		LinkedList<Move> possibleMoves = new LinkedList<Move>();
		addPossibleMoves(1, 1, possibleMoves, board);
		addPossibleMoves(1, -1, possibleMoves, board);
		addPossibleMoves(-1, 1, possibleMoves, board);
		addPossibleMoves(-1, -1, possibleMoves, board);
		return possibleMoves;
	}
	
	@Override
	public char getLetterSymbol() {
		if(side == PlayerSide.White) {
			return 'b';
		} else {
			return 'B';
		}
	}
}
