package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.LinkedList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
	protected boolean alive;
	protected int x_cor;
	protected int y_cor;
	protected PlayerSide side;
	
	public int getX_cor() {
		return x_cor;
	}

	public void setCor(int x_cor, int y_cor) {
		this.x_cor = x_cor;
		this.y_cor = y_cor;
	}

	public int getY_cor() {
		return y_cor;
	}

	public PlayerSide getSide() {
		return side;
	}

	public int getIndex() {
		return x_cor + y_cor * 8;
	}
	
	protected void setSide(PlayerSide side) {
		this.side = side;
	}

	// Constructors
	public Piece(PlayerSide side, int index) {
		x_cor = index % 8;
		y_cor = index / 8;
		this.side = side;
		alive = true;
	}
	public boolean isAlive() {
		return alive;
	}
	
	public boolean setAlive() {
		boolean original = alive;
		alive = true;
		return original;
	}
	
	public boolean setDead() {
		boolean original = alive;
		alive = false;
		return original;
	}
	
	public abstract boolean isEmpty();
	
	public abstract boolean isLegalMove(Move move);
	
	public abstract PieceType getType();
	
	public abstract boolean isAttacking(int x_cor, int y_cor);
	
	public abstract LinkedList<Move> generatePossibleMoves(Board board);
	
	public abstract char getLetterSymbol();
	
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



