package com.chess.ai_player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Board.GameState;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Piece.PieceType;
import com.chess.engine.pieces.Piece.PlayerSide;

/** 
 * AIPlayer -- currently only as white player
 * @author calvinwang
 *
 */
public class AIPlayer {
	private static final int DEFAULT_VALUE = 100000000;
	private static int CONSTANT_PAWN = 1;
	private static int CONSTANT_ROOK = 5;
	private static int CONSTANT_KNIGHT = 4;
	private static int CONSTANT_BISHOP = 5;
	private static int CONSTANT_QUEEN = 10;
	private static int CONSTANT_PIECE_POSSESSION = 3;
	private static int CONSTANT_PAWN_ADVANCEMENT = 1;
	private static int CONSTANT_PIECE_MOBILITY = 1;
	private static int CONSTANT_PIECE_THREATS = 2;
	private static int CONSTANT_PIECE_PROTECTS = 1;
	public Move bestMove = null;
	
	/**
	 * White: positive
	 * Evaluates the board based on:
	 * 		piece possession
	 * 		pawn advancement
	 * 		piece mobility
	 * 		piece threats
	 * 		piece protects
	 */
	private int evaluate_board(Board board) {
		// first check whether the game has ended
		if(board.getGameState() == GameState.WhiteWin) {
			return DEFAULT_VALUE;
		} else if(board.getGameState() == GameState.BlackWin) {
			// board.printBoard();
			return -1 * DEFAULT_VALUE;
		}
		
		
		int score = 0;
		// piece possession
		score += evaluate_piece_possession(board);
		// pawn advancement
		score += evaluate_pawn_advancement(board);
		// piece mobility & piece threats & piece protects
		board.resetIsProtected(); // this is for evalue pieces protected
		LinkedList<Move> legalMoves = board.compileAllLegalMoves();
		score += evaluate_piece_mobility(legalMoves);
		score += evaluate_piece_threats(legalMoves);
		score += evaluate_piece_protects(board);
		return board.getCurrSide() == PlayerSide.White ? score : score * (-1);
	}
	
	/**
	 * Calculate the value of piece possession
	 */
	private int evaluate_piece_possession(Board board) {
		ArrayList<Piece> pieces = board.getCurrentSidePieces();
		int possesion_value = 0;
		for(Piece piece: pieces) {
			if(!piece.isAlive()) {
				continue;
			}
			switch (piece.getType()) {
				case Rook:
					possesion_value += CONSTANT_ROOK;
					break;
				case Knight:
					possesion_value += CONSTANT_KNIGHT;
					break;
				case Bishop:
					possesion_value += CONSTANT_BISHOP;
					break;
				case Queen:
					possesion_value += CONSTANT_QUEEN;
					break;
				case Pawn:
					possesion_value += CONSTANT_PAWN;
					break;
				default:
					break;
			}
		}
		return possesion_value * CONSTANT_PIECE_POSSESSION;
	}
	
	/**
	 * Calculate the value of pawn advancement
	 */
	private int evaluate_pawn_advancement(Board board) {
		ArrayList<Piece> pieces = board.getCurrentSidePieces();
		int advancement = 0;
		int start_pt = board.getCurrSide() == PlayerSide.White ? 6 : 1;
		for(Piece piece : pieces) {
			if(piece.getType() == PieceType.Pawn) {
				advancement = piece.getY_cor() - start_pt;
			}
		}
		return Math.abs(advancement) * CONSTANT_PAWN_ADVANCEMENT;
	}
	
	/**
	 * Calculate the value of piece mobility
	 */
	private int evaluate_piece_mobility(LinkedList<Move> legalMoves) {
		return CONSTANT_PIECE_MOBILITY * legalMoves.size();
	}
	
	/**
	 * Calculate the value of piece threats
	 */
	private int evaluate_piece_threats(LinkedList<Move> legalMoves) {
		// generate all possible moves
		// check how many moves are capturedPieces
		// maybe use a hashtable
		boolean threated_pieces[] = new boolean[64];
		for(Move move : legalMoves) {
			if(move.isCaptureMove()) {
				threated_pieces[move.getEndIndex()] = true;
			}
		}
		int num_pieces_threated = 0;
		for(int i = 0; i < 64; i++) {
			if(threated_pieces[i]) {
				num_pieces_threated++;
			}
		}
		return num_pieces_threated * CONSTANT_PIECE_THREATS;
	}
	
	/**
	 * Calculate the value of piece protects
	 *  when generating all possible moves, indicate whether a piece is protected
	 *  To use this function, call resetIsProtected before compileAllLegalMoves
	 */
	private int evaluate_piece_protects(Board board) {
		ArrayList<Piece> pieces = board.getCurrentSidePieces();
		int num_protected = 0;
		for(Piece piece : pieces) {
			if(piece.isProtected()) {
				num_protected++;
			}
		}
		return num_protected * CONSTANT_PIECE_PROTECTS;
	}

	
	private int determine_best_move(Board board, int depth, int currentDepth, int alpha_beta) {
		// base case
		if(depth == currentDepth) {
			int result = evaluate_board(board);
			return result;
		}
		Move bestMoveSoFar = null;
		int bestScoreSoFar = board.getCurrSide() == PlayerSide.White ? -DEFAULT_VALUE : DEFAULT_VALUE;
		LinkedList<Move> legalMoves = board.compileAllLegalMoves();
		PlayerSide currSide = board.getCurrSide();
		for(Move move : legalMoves) {
			// execute each move, and go deeper
			board.executeMoveComplete(move);
			int score = determine_best_move(board, depth, currentDepth + 1, bestScoreSoFar);
			// first check alpha_beta value
			if((currSide == PlayerSide.White && score > alpha_beta) 
					|| (currSide == PlayerSide.Black && score < alpha_beta)) {
				board.undoMoveComplete();
				return score;
			}
			
			
			if((currSide == PlayerSide.White && score > bestScoreSoFar) 
					|| (currSide == PlayerSide.Black && score < bestScoreSoFar)) {
				bestMoveSoFar = move;
				bestScoreSoFar = score;
			}
			board.undoMoveComplete();
		}
		
		if(currentDepth == 0) {
			bestMove = bestMoveSoFar;
		}
		return bestScoreSoFar;
	}
	
	public Move determine_best_move(Board board, int depth) {
		int alpha_beta = board.getCurrSide() == PlayerSide.White ? DEFAULT_VALUE : -DEFAULT_VALUE;
		determine_best_move(board, depth, 0, alpha_beta);
		return bestMove;
	}
	
	public static void main(String[] args) {
		Board board = new Board();
		AIPlayer player = new AIPlayer();
		player.evaluate_board(board);
		player.determine_best_move(board, 5);
		System.out.println(" finished ");
		player.bestMove.printMove();
	}
}
