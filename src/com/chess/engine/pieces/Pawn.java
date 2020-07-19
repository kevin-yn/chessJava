package com.chess.engine.pieces;

import java.util.LinkedList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class Pawn extends Piece {
	/**
	 * This may makes it easier to store direction instead of checking its side every time
	 * @param direction
	 */
	private final int direction;
	
	/**
	 * This helps to determine whether Pawn is eligible for two moves
	 * @param moved
	 */

	/** OPTIONAL I can get this info from checking last move
	 * This helps to determine whether Pawn is eligible for en Passant
	 * @param moved two steps
	 */
	
	public Pawn(PlayerSide side, int index) {
		super(side, index);
		if(side == PlayerSide.White) {
			direction = -1;
		} else {
			direction = 1;
		}
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public  PieceType getType() {
		return PieceType.Pawn;
	}

	@Override
	public boolean isAttacking(final Board board, int target_x_cor, int target_y_cor) {
		if(target_y_cor - this.y_cor == direction && Math.abs(target_x_cor - this.x_cor) == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public LinkedList<Move> generatePossibleMoves(final Board board) {
		LinkedList<Move> possibleMoves = new LinkedList<Move>(); 
		// normal one step ahead
		boolean oneStepAheadEmpty = false;
		if(validCor(this.x_cor, this.y_cor + direction) && board.isEmptyAt(this.x_cor, this.y_cor + direction)) {
			oneStepAheadEmpty = true;
			Move newMove = new Move(this.x_cor, this.y_cor, this.x_cor, this.y_cor + direction);
			// check if Pawn Promotion
			if(newMove.getEnd_y() == (this.side == PlayerSide.White ? 0 : 7)) {
				newMove.setPawnPromotion();
			}
			possibleMoves.add(newMove);
		}
		// initial two steps ahead
		if(moveCount == 0 && oneStepAheadEmpty && validCor(this.x_cor, this.y_cor + 2 * direction) && board.isEmptyAt(this.x_cor, this.y_cor + 2 * direction)) {
			possibleMoves.add(new Move(this.x_cor, this.y_cor, this.x_cor, this.y_cor + 2 * direction));
		}
		
		// normal capture move
		if(validCor(this.x_cor + 1, this.y_cor + direction) && board.getPlayerSide(this.x_cor + 1, this.y_cor + direction) == getOppositeSide(side)) {
			Move newMove = new Move(this.x_cor, this.y_cor, this.x_cor + 1, this.y_cor + direction);
			// check if Pawn Promotion
			if(newMove.getEnd_y() == (this.side == PlayerSide.White ? 0 : 7)) {
				newMove.setPawnPromotion();
			}
			possibleMoves.add(newMove);
		}
		if(validCor(this.x_cor - 1, this.y_cor + direction) && board.getPlayerSide(this.x_cor - 1, this.y_cor + direction) == getOppositeSide(side)) {
			Move newMove = new Move(this.x_cor, this.y_cor, this.x_cor - 1, this.y_cor + direction);
			// check if Pawn Promotion
			if(newMove.getEnd_y() == (this.side == PlayerSide.White ? 0 : 7)) {
				newMove.setPawnPromotion();
			}
			possibleMoves.add(newMove);
		}
		
		// en Passnat move TODOTODO!!!!!!!!!
		enPassantMoveHelper(board, possibleMoves, this.x_cor - 1);
		enPassantMoveHelper(board, possibleMoves, this.x_cor + 1);
		return possibleMoves;
	}

	private Move enPassantMoveHelper(Board board, LinkedList<Move> possibleMoves, int x) {
		if(validCor(x, this.y_cor)) {
			// besides me is a pawn of opposite side
			Piece targetPiece = board.getPiece(x, this.y_cor);
			if(targetPiece.getSide() == getOppositeSide(side)) {
				if(targetPiece.getType() == PieceType.Pawn) {
					// his movecount is 1
					if(targetPiece.getMoveCount() == 1) {
						// he is on the specific row
						if((targetPiece.side == PlayerSide.Black && targetPiece.y_cor == 3) || (targetPiece.side == PlayerSide.White && targetPiece.y_cor == 4) ) {
							// he moved here on the last move
							Move lastMovemove = board.getLastMove();
							if(lastMovemove.getEnd_x() == targetPiece.x_cor && lastMovemove.getEnd_y() == targetPiece.y_cor) {
								// his back is empty
								if(board.isEmptyAt(targetPiece.x_cor, targetPiece.y_cor + direction)) {
									Move newMove = new Move(this.x_cor, this.y_cor, x, this.y_cor + direction);
									newMove.setEnPassant();
									possibleMoves.add(newMove);
									return newMove;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public char getLetterSymbol() {
		if(side == PlayerSide.White) {
			return 'p';
		} else {
			return 'P';
		}
	}
}
