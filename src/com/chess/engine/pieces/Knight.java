package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece.PieceType;
import com.chess.engine.pieces.Piece.PlayerSide;

import java.lang.Math;
import java.util.ArrayList;
import java.util.LinkedList;

public class Knight extends Piece {

	public Knight(PlayerSide side, int index) {
		super(side, index);
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
			return 'n';
		} else {
			return 'N';
		}
	}
}
