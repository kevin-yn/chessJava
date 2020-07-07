package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece.PieceType;

import java.lang.Math;
import java.util.ArrayList;

public class Knight extends Piece {

	public Knight(PlayerSide side) {
		super(side);
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public  PieceType getType() {
		return PieceType.Knight;
	}

	@Override
	public boolean isLegalMove(Move move) {
		return Math.pow(move.getEnd_x() - move.getStart_x(), 2) + Math.pow(move.getEnd_y() - move.getStart_y(), 2) == 5;
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
