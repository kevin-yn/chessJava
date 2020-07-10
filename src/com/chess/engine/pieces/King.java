package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.LinkedList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece.PieceType;
import com.chess.engine.pieces.Piece.PlayerSide;

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
	public boolean isLegalMove(Move move) {
		return false;
	}

	@Override
	public boolean isAttacking(int x_cor, int y_cor) {
		return false;
	}
	
	@Override
	public LinkedList<Move> generatePossibleMoves(Board board) {
		return null;
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
