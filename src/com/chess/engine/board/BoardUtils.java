package com.chess.engine.board;

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
}
