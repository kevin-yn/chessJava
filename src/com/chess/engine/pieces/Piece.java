package com.chess.engine.pieces;

import java.util.ArrayList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
	private PlayerSide side;


	public PlayerSide getSide() {
		return side;
	}

	protected void setSide(PlayerSide side) {
		this.side = side;
	}

	// Constructors
	public Piece(PlayerSide side) {
		this.side = side;
	}
	
	// Utility Functions
	public abstract boolean isEmpty();
	
	public abstract boolean isLegalMove(Move move);
	
	public abstract PieceType getType();
	
	public abstract boolean isAttacking(int x_cor, int y_cor);
	
	public abstract ArrayList<Move> generatePossibleMoves(Board board);
	
	public enum PieceType {
		King, 
		Queen,
		Rook,
		Bishop,
		Knight,
		Pawn,
		EmptySpot
	}
	
	public enum PlayerSide {
		Black,
		White,
		EmptySpot
	}
}



