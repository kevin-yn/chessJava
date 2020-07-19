package com.chess.engine.pieces;

import java.util.LinkedList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class Queen extends Bishop_Rook_Queen {

	public Queen(PlayerSide side, int index) {
		super(side, index);
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public  PieceType getType() {
		return PieceType.Queen;
	}

	@Override
	public boolean isAttacking(final Board board, int target_x_cor, int target_y_cor) {
		boolean same_x = (this.x_cor == target_x_cor);
		boolean same_y = (this.y_cor == target_y_cor);
		if(same_x ^ same_y) { // same vertical or horizontal line
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
		} else if(Math.abs(this.x_cor - target_x_cor) == Math.abs(this.y_cor - target_y_cor)) { // same diagonal line
			System.out.println("Queen might attack");
			int increment_x = target_x_cor > this.x_cor ? 1 : -1;
			int increment_y = target_y_cor > this.y_cor ? 1 : -1;
			return isPathOpen(target_x_cor, target_y_cor, this.x_cor, this.y_cor , increment_x, increment_y, board);
		} else { // not attackable
			return false;
		}
	}
	
	@Override
	public LinkedList<Move> generatePossibleMoves(final Board board) {
		LinkedList<Move> possibleMoves = new LinkedList<Move>();
		// add diagonal
		addPossibleMoves(1, 1, possibleMoves, board);
		addPossibleMoves(1, -1, possibleMoves, board);
		addPossibleMoves(-1, 1, possibleMoves, board);
		addPossibleMoves(-1, -1, possibleMoves, board);
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
			return 'q';
		} else {
			return 'Q';
		}
	}
}
