package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.LinkedList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece.PieceType;
import com.chess.engine.pieces.Piece.PlayerSide;

public class Pawn extends Piece {
	/**
	 * This may makes it easier to store direction instead of checking its side every time
	 * @param direction
	 */

	
	/**
	 * This helps to determine whether Pawn is eligible for two moves
	 * @param moved
	 */

	/** OPTIONAL I can get this info from checking last move
	 * This helps to determine whether Pawn is eligible for en Passant
	 * @param moved two steps
	 */
	
	public Pawn(PlayerSide side, int index) {
		super(side, index);
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public  PieceType getType() {
		return PieceType.Pawn;
	}

	@Override
	public boolean isLegalMove(Move move) {
		return false;
	}

	@Override
	public boolean isAttacking(final Board board, int target_x_cor, int target_y_cor) {
		return false;
	}
	
	@Override
	public LinkedList<Move> generatePossibleMoves(final Board board) {
		return null;
	}
	
	@Override
	public char getLetterSymbol() {
		if(side == PlayerSide.White) {
			return 'p';
		} else {
			return 'P';
		}
	}
}
