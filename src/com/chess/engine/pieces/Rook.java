package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.LinkedList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece.PieceType;
import com.chess.engine.pieces.Piece.PlayerSide;

public class Rook extends Bishop_Rook_Queen {
	/**
	 * This helps to determine whether Rook is eligible for Castling
	 * @param moved
	 */

	public Rook(PlayerSide side, int index) {
		super(side, index);
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public  PieceType getType() {
		return PieceType.Rook;
	}
	
	@Override
	public boolean isLegalMove(Move move) {
		return false;
	}

	@Override
	public boolean isAttacking(final Board board, int target_x_cor, int target_y_cor) {
		boolean same_x = (this.x_cor == target_x_cor);
		boolean same_y = (this.y_cor == target_y_cor);
		// check whether either same_x or same_y
		if(!(same_x ^ same_y)) {
			return false;
		}
		// check whether it is on the vertical line or horizontal line
		int increment_x = 0;
		int increment_y = 0;
		if(same_x) {
			if(target_y_cor > y_cor) {
				increment_y = 1;
			} else {
				increment_y = -1;
			}
		} else {
			if(target_x_cor > x_cor) {
				increment_x = 1;
			} else {
				increment_x = -1;
			}
		}
		return isPathOpen(target_x_cor, target_y_cor, x_cor, y_cor, increment_x, increment_y, board);
	}
	
	@Override
	public LinkedList<Move> generatePossibleMoves(final Board board) {
		LinkedList<Move> possibleMoves = new LinkedList<Move>();
		// add vertical
		addPossibleMoves(1, 0, possibleMoves, board);
		addPossibleMoves(-1, 0, possibleMoves, board);
		addPossibleMoves(0, 1, possibleMoves, board);
		addPossibleMoves(0, -1, possibleMoves, board);
		return possibleMoves;
	}
	
	
	@Override
	public char getLetterSymbol() {
		if(side == PlayerSide.White) {
			return 'r';
		} else {
			return 'R';
		}
	}
}
