package com.chess.gui;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.chess.ai_player.AIPlayer;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Board.GameState;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Piece.PieceType;
import com.chess.engine.pieces.Piece.PlayerSide;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;


public class BoardGUI {
	// constants
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
	private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private final static Color lightTileColor = Color.decode("#FFFACD");
    private final static Color darkTileColor = Color.decode("#593E1A");
    private final static Color selectionTileColor = Color.decode("#555555");
    private final static String pieceIconPath = new String("art/pieceIcon/");
    private final static String currentPlayerDisplayString = new String("'s Move");
    private final static int NOT_SELECTED = -1;
    
    
	// fields
	private final JFrame gameFrame;
	private final JLabel displayJLabel;
	private final BoardPanel boardPanel;
	private Board chessBoard;
	private final AIPlayer aiPlayer = new AIPlayer();
	private JPopupMenu pawnPromotionPopupMenu;
	private int sou_index = NOT_SELECTED;
	// Constructor
	public BoardGUI() {
		this.chessBoard = new Board();
		this.gameFrame = new JFrame("Chess Game in Java by Yaning");
		this.gameFrame.setLayout(new BorderLayout());
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		// menu bar
		final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        
        // display winner and current Player
        displayJLabel = new JLabel(chessBoard.getCurrentSideString() + currentPlayerDisplayString, JLabel.CENTER);
        displayJLabel.setVerticalAlignment(JLabel.TOP);
        this.gameFrame.add(displayJLabel, BorderLayout.PAGE_START);
        displayJLabel.setText("Have fun, White's Move");
        // board
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(boardPanel, BorderLayout.CENTER);
        
        
        createPopupMenu();
        
        // this needs to be at the end
        gameFrame.setVisible(true);
        
	}
	// BoardGUI functions
	private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }
    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				undoAMove();
			}
		});
        fileMenu.add(undoMenuItem);
        
        final JMenuItem resetMenuItem = new JMenuItem("Reset");
        resetMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				resetBoard();
			}
		});
        fileMenu.add(resetMenuItem);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }
    // BoardPanel class
    private class BoardPanel extends JPanel {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected final List<TilePanel> BoardTiles;
		
    	public BoardPanel() {
			super(new GridLayout(8, 8));
			this.BoardTiles = new ArrayList<>();
			for(int i = 0; i < 64; i++) {
	    		final TilePanel tilePanel = new TilePanel(this, i);
	    		this.BoardTiles.add(tilePanel);
	    		add(tilePanel);
	    	}
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}

    	protected void updateBoard() {
    		GameState gameState = chessBoard.checkGameState();
    		switch (gameState) {
			case Active:
				displayJLabel.setText(chessBoard.getCurrentSideString() + currentPlayerDisplayString);
				break;
			case Draw:
				displayJLabel.setText("Game Ends DRAW");
				break;
			case WhiteWin:
				displayJLabel.setText("White Wins");
				break;
			case BlackWin:
				displayJLabel.setText("Black Wins");
				break;
			default:
				break;
			}
    		removeAll(); // this only remove stuff inside the BoardPanel
			for(TilePanel tilePanel: BoardTiles) {
				tilePanel.assignTilePieceIcon(chessBoard);
				add(tilePanel);
			}
			validate();
			repaint();
		}
    
    }
    // TilePanel class
    private class TilePanel extends JPanel {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final int tileID;
    	private final int x_cor;
    	private final int y_cor;

    	TilePanel(final BoardPanel boardPanel, final int tileID) {
    		super(new GridBagLayout());
    		x_cor = tileID % 8;
    		y_cor = tileID / 8;
    		this.tileID = tileID;
    		setPreferredSize(TILE_PANEL_DIMENSION);
    		assignTileColor();
    		assignTilePieceIcon(chessBoard);
    		addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					if(isLeftMouseButton(e)) {
						if(sou_index == NOT_SELECTED) {
							sou_index = tileID;
							setBackground(selectionTileColor);
						} else {
							Move move = new Move(sou_index, tileID);
							Move moveMade = chessBoard.makeAMove(move);
							if(moveMade == null) {
								displayJLabel.setText("Illegal Move   Still " + displayJLabel.getText());
								chessBoard.printBoard();
							} else {
								if(moveMade.isPawnPromotion()) {
									pawnPromotionPopupMenu.show(gameFrame, 300, 300);
								}
								boardPanel.updateBoard();
							}
							resetSelection();
							
							// if currentSide is Black, let the AI player makes a move
							if(chessBoard.getCurrSide() == PlayerSide.Black) {
								move = aiPlayer.determine_best_move(chessBoard, 3);
								chessBoard.makeAMove(move);
								boardPanel.updateBoard();
							}
						}
					} else if(isRightMouseButton(e)) {
						resetSelection();
					}
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
    		
    		
    		// at the end
    		validate();
    	}
    	
    	private void assignTilePieceIcon(final Board board) {
    		Piece piece = board.getPiece(tileID);
    		String pathBuffer = new String(pieceIconPath + (piece.getSide() == PlayerSide.White ? 'W' : 'B') + piece.getLetterSymbol() + ".gif");
			this.removeAll();
			if(!piece.isEmpty()) {
				try {
					final BufferedImage image = ImageIO.read(new File(pathBuffer)); 
					add(new JLabel(new ImageIcon(image)));
				}  catch (IOException e) {
                    e.printStackTrace();
                }
					
			}
		}
		protected void assignTileColor() {
			if(y_cor % 2 == 0) {
				setBackground(x_cor % 2 == 0 ? lightTileColor : darkTileColor);
			} else {
				setBackground(x_cor % 2 == 1 ? lightTileColor : darkTileColor);
			}
		}
    }
    
    /**
     * reset selection and reset the selected Tile Color
     */
    protected void resetSelection() {
    	if(sou_index == NOT_SELECTED) {
    		return;
    	}
    	boardPanel.BoardTiles.get(sou_index).assignTileColor();
		sou_index = NOT_SELECTED;
	}
    
	protected void resetBoard() {
    	chessBoard = new Board();
    	boardPanel.updateBoard();
    }
	
	protected void undoAMove() {
		resetSelection();
		chessBoard.undoAMove();
		boardPanel.updateBoard();
	}

	private void createPopupMenu() {
		pawnPromotionPopupMenu = new JPopupMenu("Promote Pawn to");
		pawnPromotionPopupMenu.setLabel("HAHAHAHA");
		pawnPromotionPopupMenu.add(createButton(PieceType.Pawn));
		pawnPromotionPopupMenu.add(createButton(PieceType.Rook));
		pawnPromotionPopupMenu.add(createButton(PieceType.Bishop));
		pawnPromotionPopupMenu.add(createButton(PieceType.Knight));
		pawnPromotionPopupMenu.add(createButton(PieceType.Queen));
		
	}
	/**
	 * create button that appear on pawnpromotionPopupMenu
	 * @return
	 */
	private JButton createButton(final PieceType type) {
		
		JButton button = new JButton();
		
		switch (type) {
		case Pawn:
			button.setText("Pawn");
			break;
		case Rook:
			button.setText("Rook");
			break;
		case Bishop:
			button.setText("Bishop");
			break;
		case Queen:
			button.setText("Queen");
			break;
		case Knight:
			button.setText("Knight");
			break;
		default:
			break;
		}
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Move lastMove = chessBoard.getLastMove();
				Piece pawnPiece = chessBoard.getPiece(lastMove.getEndIndex());
				Piece newPiece = null;
				switch (type) {
				case Pawn:
					newPiece = null;
					break;
				case Rook:
					newPiece = new Rook(pawnPiece.getSide(), pawnPiece.getIndex());
					break;
				case Bishop:
					newPiece = new Bishop(pawnPiece.getSide(), pawnPiece.getIndex());
					break;
				case Queen:
					newPiece = new Queen(pawnPiece.getSide(), pawnPiece.getIndex());
					break;
				case Knight:
					newPiece = new Knight(pawnPiece.getSide(), pawnPiece.getIndex());
					break;
				default:
					break;
				}
				chessBoard.replacePiece(pawnPiece, newPiece);
				pawnPromotionPopupMenu.setVisible(false);
				boardPanel.updateBoard();
			}
		});
		return button;
	}
}
