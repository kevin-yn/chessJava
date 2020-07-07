package com.chess.engine.pieces;

import java.util.ArrayList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece.PieceType;

public class Rook extends Piece {
	/**
	 * This helps to determine whether Rook is eligible for Castling
	 * @param moved
	 */

	public Rook(PlayerSide side) {
		super(side);
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
	public boolean isAttacking(int x_cor, int y_cor) {
		return false;
	}
	
	@Override
	public ArrayList<Move> generatePossibleMoves(Board board) {
		return new ArrayList<Move>();
	}
}