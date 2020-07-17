package com.chess.engine.board;

import java.util.LinkedList;

public class BoardUtils {
	public static int getXCor(int index) {
		return index % 8;
	}
	
	public static int getYCor(int index) {
		return index / 8;
	}
	
	public static int getIndex(int x_cor, int y_cor) {
		return x_cor + y_cor * 8;
	}
	
	public void printMoves(LinkedList<Move> possibleMoves) {
		if(possibleMoves == null) {
			return;
		}
		for(Move move : possibleMoves) {
			System.out.print("From x: ");
			System.out.print(move.getStart_x());
			System.out.print(" y: ");
			System.out.print(move.getStart_y());
			System.out.print("   To x: ");
			System.out.print(move.getEnd_x());
			System.out.print(" y: ");
			System.out.print(move.getEnd_y());
			
		}
	}
}
