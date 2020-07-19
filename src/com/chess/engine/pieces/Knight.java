package com.chess.engine.pieces;

import java.util.LinkedList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

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
	public boolean isAttacking(final Board board, int target_x_cor, int target_y_cor) {
		return (Math.pow(this.x_cor - target_x_cor, 2) + Math.pow(this.y_cor - target_y_cor, 2) == 5);
	}
	
	@Override
	public LinkedList<Move> generatePossibleMoves(final Board board) {
		LinkedList<Move> possibleMoves = new LinkedList<>();
		generateOneMove(this.x_cor + 1, this.y_cor + 2, board, possibleMoves);
		generateOneMove(this.x_cor + 2, this.y_cor + 1, board, possibleMoves);
		generateOneMove(this.x_cor + 1, this.y_cor - 2, board, possibleMoves);
		generateOneMove(this.x_cor + 2, this.y_cor - 1, board, possibleMoves);
		generateOneMove(this.x_cor - 1, this.y_cor + 2, board, possibleMoves);
		generateOneMove(this.x_cor - 2, this.y_cor + 1, board, possibleMoves);
		generateOneMove(this.x_cor - 1, this.y_cor - 2, board, possibleMoves);
		generateOneMove(this.x_cor - 2, this.y_cor - 1, board, possibleMoves);
		return possibleMoves;
	}
	
	private void generateOneMove(int target_x, int target_y, Board board, LinkedList<Move> possibleMoves) {
		if(!validCor(target_x, target_y)) { // check isValid Coordinates
			return;
		}
		if(board.getPlayerSide(target_x, target_y) == this.side) { // check if ally piece 
			return;
		}
		possibleMoves.add(new Move(this.x_cor, this.y_cor, target_x, target_y));
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
