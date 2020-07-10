package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.LinkedList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece.PieceType;
import com.chess.engine.pieces.Piece.PlayerSide;

public class Queen extends Piece {

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
			return 'q';
		} else {
			return 'Q';
		}
	}
}
