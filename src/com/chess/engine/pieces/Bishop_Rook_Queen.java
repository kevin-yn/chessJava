package com.chess.engine.pieces;

import java.util.LinkedList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

// an additional abstract class layer that provides helper functions for Bishop, Rook, Queen
public abstract class Bishop_Rook_Queen extends Piece {

	public Bishop_Rook_Queen(PlayerSide side, int index) {
		super(side, index);
	}

	protected void addPossibleMoves(int x_increment, int y_increment, LinkedList<Move> possibleMoves, Board board) {
		int x = this.x_cor + x_increment;
		int y = this.y_cor + y_increment;
		while(true) {
			if(!validCor(x, y)) {
				break;
			}
			PlayerSide thisSide = board.getPlayerSide(x, y);
			if(thisSide == this.side) {
				break;
			} else if(thisSide == PlayerSide.EmptySpot) {
				possibleMoves.add(new Move(this.x_cor, this.y_cor, x, y));
				x += x_increment;
				y += y_increment;
			} else { // this is a capture move
				possibleMoves.add(new Move(this.x_cor, this.y_cor, x, y));
				break;
			}
		}
	}
	
	/**
	 * Exclusive of both ends
	 * @param end_x
	 * @param end_y
	 * @param start_x
	 * @param start_y
	 * @param increment_x
	 * @param increment_y
	 * @param board
	 * @return
	 */
	protected boolean isPathOpen(int end_x, int end_y, int start_x, int start_y, int increment_x, int increment_y, Board board) {
		System.out.println("isPathOpen called");
		start_x += increment_x;
		start_y += increment_y;
		while(start_x != end_x || start_y != end_y) {
			if(!board.isEmptyAt(start_x, start_y)) {
				return false;
			}
			start_x += increment_x;
			start_y += increment_y;
		}
		return true;
	}
}
