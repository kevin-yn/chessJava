package com.chess.engine.pieces;

import java.util.ArrayList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece.PieceType;

public class EmptySpot extends Piece {

	public EmptySpot() {
		super(PlayerSide.EmptySpot);
	}
	
	@Override
	public boolean isEmpty() {
		return true;
	}
	
	@Override
	public  PieceType getType() {
		return PieceType.EmptySpot;
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
