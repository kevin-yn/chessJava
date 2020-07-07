package com.chess.engine.pieces;

import java.util.ArrayList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece.PieceType;

public class Bishop extends Piece{
	
	public Bishop(PlayerSide side) {
		super(side);
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
	public boolean isAttacking(int x_cor, int y_cor) {
		return false;
	}
	
	@Override
	public ArrayList<Move> generatePossibleMoves(Board board) {
		return new ArrayList<Move>();
	}
	
}
