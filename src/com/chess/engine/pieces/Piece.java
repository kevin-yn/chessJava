package com.chess.engine.pieces;

import java.util.LinkedList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
	protected boolean alive;
	protected int x_cor;
	protected int y_cor;
	protected PlayerSide side;
	protected int moveCount; // to record the number of times this Piece is moved
	protected boolean isProtected;
	
	public boolean isProtected() {
		return isProtected;
	}

	public void setProtected(boolean isProtected) {
		this.isProtected = isProtected;
	}

	public void moveTo(int x_cor, int y_cor) {
		this.x_cor = x_cor;
		this.y_cor = y_cor;
		moveCount++;
	}
	
	public void returnTo(int x_cor, int y_cor) {
		this.x_cor = x_cor;
		this.y_cor = y_cor;
		moveCount--;
	}
	
	public boolean isMoved() {
		return (moveCount > 0);
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
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

	protected int getLargerInt(int a, int b) {
		return (a > b) ? a : b; 
	}
	
	protected int getSmallerInt(int a, int b) {
		return (a <= b) ? a : b; 
	}
	
	protected boolean validCor(int x, int y) {
		return x >= 0 && x <= 7 && y >= 0 && y <= 7;
	}

	// Constructors
	
	public Piece(PlayerSide side, int index) {
		x_cor = index % 8;
		y_cor = index / 8;
		this.side = side;
		alive = true;
		moveCount = 0;
		isProtected = false;
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
	
	public abstract PieceType getType();
	
	/**
	 * This does not include En Passant, because this function is only used to checkmate and castling
	 * @param x_cor
	 * @param y_cor
	 * @return
	 */
	public abstract boolean isAttacking(final Board board, int target_x_cor, int target_y_cor);
	
	public abstract LinkedList<Move> generatePossibleMoves(final Board board);
	
	public abstract char getLetterSymbol();
	
	public static PlayerSide getOppositeSide(PlayerSide side) {
		return side == PlayerSide.Black ? PlayerSide.White : PlayerSide.Black;
	}
	
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



