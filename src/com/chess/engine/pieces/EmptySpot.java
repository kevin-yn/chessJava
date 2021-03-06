package com.chess.engine.pieces;

import java.util.LinkedList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class EmptySpot extends Piece {

	public EmptySpot(int index) { // special Constructor for EmptySpot
		super(PlayerSide.EmptySpot, index);
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
	public boolean isAttacking(final Board board, int target_x_cor, int target_y_cor) {
		return false;
	}
	
	@Override
	public LinkedList<Move> generatePossibleMoves(final Board board) {
		return null;
	}
	
	@Override
	public char getLetterSymbol() {
		return '0';
	}
}
