package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public class Move {
	private final int start_x;
	private final int end_x;
	private final int start_y;
	private final int end_y;
	private boolean isExecuted;
	private Piece captured_piece;
	
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
	}
}
