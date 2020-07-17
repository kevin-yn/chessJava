package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public class Move {
	private final int start_x;
	private final int end_x;
	private final int start_y;
	private final int end_y;
	private boolean isExecuted;
	private Piece captured_piece;
	// special move flag
	private boolean isCastling;
	private boolean isEnPassant;
	
	
	public boolean isEnPassant() {
		return isEnPassant;
	}

	public void setEnPassant() {
		this.isEnPassant = true;
	}

	public void setCastling() {
		isCastling = true;
	}
	
	public boolean isCastling() {
		return isCastling;
	}
	
	public int getStart_x() {
		return start_x;
	}
	
	public int getEnd_x() {
		return end_x;
	}
	
	public int getStart_y() {
		return start_y;
	}
	
	public int getEnd_y() {
		return end_y;
	}
	
	public int getStartIndex() {
		return start_x + start_y * 8;
	}
	 
	public int getEndIndex() {
		return end_x + end_y * 8;
	}

	public Piece getCaptured_piece() {
		return captured_piece;
	}

	public void setCaptured_piece(Piece captured_piece) {
		this.captured_piece = captured_piece;
	}

	public void isExecuted() {
		if(isExecuted) {
			System.err.println("this move is executed twice in a row");
		} else {
			isExecuted = true;
		}
	}
	
	public void isUndone() {
		if(!isExecuted) {
			System.err.println("this move is undone twice in a row");
		} else {
			isExecuted = false;
		}
	}
	
	public Move(int start_x, int start_y, int end_x, int end_y) {
		super();
		this.isExecuted = false;
		this.start_x = start_x;
		this.end_x = end_x;
		this.start_y = start_y;
		this.end_y = end_y;
		if(start_x == end_x && start_y == end_y) {
			System.err.println("illegal instance of move");
		}
		isCastling = false;
		isEnPassant = false;
	}
	
	public Move(int sou_index, int des_index) {
		super();
		this.isExecuted = false;
		this.start_x = sou_index % 8;
		this.start_y = sou_index / 8;
		this.end_x = des_index % 8;
		this.end_y = des_index / 8;
		if(sou_index == des_index) {
			System.err.println("illegal instance of move");
		}
		isCastling = false;
		isEnPassant = false;
	}
}
