package chess;
import static chess.PieceColor.BLACK;
import static chess.PieceColor.WHITE;
import static chess.PieceType.BISHOP;
import static chess.PieceType.KING;
import static chess.PieceType.KNIGHT;
import static chess.PieceType.PAWN;
import static chess.PieceType.QUEEN;
import static chess.PieceType.ROOK;

import java.util.ArrayList;
import java.util.List;

/** Represents a game of chess with a board and GUI.
 *  @author Wan Fung Chui
 */

public class Game {

    /** A game of 2-player chess, displayed in a GUI. */
    Game() {
        _moves = new ArrayList<Move>();
        _gui = new ChessGUI("Chess", this);
        newGame();
    }
    
    Game(Game nG){ //deep clones g
    	_moves = new ArrayList<Move>();
    	//_gui=null;
        newGame();
        
    }
    
    public void clonePieces(Game g, Game nG) { //g is old game, this is new game
    	_turn=g.turn().abbrev().contentEquals("b") ? BLACK : WHITE;
        _selectedX=g._selectedX;
        _selectedY=g._selectedY;
    	for(int x=0; x<8; x++) {
        	for(int y=0; y<8; y++) {
        		if(g._board[x][y]!=null)
            	_board[x][y]=g.board()[x][y].dclone(nG);
            }
        }
    	/** Stores the black king. */
        _blackKi=(King)g._blackKi.dclone(nG);

        /** Stores the white king. */
        _whiteKi=(King)g._whiteKi.dclone(nG);
        for(int i=0; i<g._moves.size(); i++) {
        	SingleMove m=(SingleMove) g._moves.get(i);
        	_moves.add(m.dclone(nG));
        }
    }

    /** Clears the game and starts a new one. */
    public void newGame() {
        initializeBoard();
        _moves.clear();
        _turn = WHITE;
        _selectedX = -1;
        _selectedY = -1;
    }
    
    public Game applyMoveCloning(Move move) {
    	Game nG=new Game(this); //this is old game
    	nG.clonePieces(this, nG); //nG is new game
    	SingleMove m=(SingleMove)move;
    	m=(SingleMove)m.dclone(nG);
    	nG.makeMove(m);
    	//System.out.println("created game clone");
    	return nG;
    }

    /** Quits the game. */
    public void quit() {
        System.exit(0);
    }

    /** Undoes the last move in the game. */
    public void undoMove() {
        if (_moves.size() > 0) {
            Move lastMove = _moves.remove(_moves.size() - 1);
            makeMove(lastMove.undoMove());
            _moves.remove(_moves.size() - 1);
        }
        //System.out.println("undone");
    }

    /** Makes the move MOVE on the board. Assumes that it is valid. */
    public void makeMove(Move move) {
    	if(move!=null) {
        _moves.add(move);
        if (!move.isDouble()) {
            SingleMove singlemove = (SingleMove) move;
            executeMove(singlemove);
        } else {
            DoubleMove doublemove = (DoubleMove) move;
            executeMove(doublemove.move1());
            executeMove(doublemove.move2());
        }
        _turn = _turn.opposite();
    	}
    	else {
    		System.out.println("MOVE WAS NULL");
    	}
    }
    
    public int simpleEval1() {
    	int score = 0;
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (!(_board[r][c] == null)) {
					if (_board[r][c].type().abbrev().equals("pa")) {
						if (_board[r][c].color().abbrev().equals("w")) {
							score++;
						} else {
							score--;
						}
					}

					if (_board[r][c].type().abbrev().equals("bi") || _board[r][c].type().abbrev().equals("kn")) {
						if (_board[r][c].color().abbrev().equals("w")) {
							score += 3;
						} else {
							score -= 3;
						}
					}

					if (_board[r][c].type().abbrev().equals("ro")) {
						if (_board[r][c].color().abbrev().equals("w")) {
							score += 5;
						} else {
							score -= 5;
						}
					}

					if (_board[r][c].type().abbrev().equals("qu")) {
						if (_board[r][c].color().abbrev().equals("w")) {
							score += 9;
						} else {
							score -= 9;
						}
					}
				}
			}
		}
    
    public double complexEval() {
    	double score = 0;
    	for (int r = 0; r < 8; r++) {
    		for (int c = 0; c < 8; c++) {
    			if (!(_board[r][c] == null)) {
    				//Pawns controlling the center is good!
    				//If pawns control e4, d4, e5, or d5, at +0.3 to the score
    				if (_board[2][5] instanceof Pawn || _board[4][5] instanceof Pawn || _board[3][5] instanceof Pawn || _board[5][5] instanceof Pawn) {
    					if (_board[2][5].color().abbrev() == "w" || _board[4][5].color().abbrev() == "w" || _board[3][5].color().abbrev() == "w" || _board[5][5].color().abbrev() == "w") {
    						score += 0.3;
    					}
    					else {
    						score -= 0.3;
    					}
    				}
    				if (_board[4][4] instanceof Pawn || _board[5][4] instanceof Pawn) {
    					if (_board[4][4].color().abbrev() == "w" || _board[5][4].color().abbrev() == "w") {
    						score += 0.6;
    					}
    					else {
    						score -= 0.6;
    					}
    				}
    				if (_board[r][c] instanceof Pawn) {
    					if (_board[r][c].color().abbrev() == "w") {
    						score++;
    					}
    					else {
    						score--;
    					}
    				}
    				//Queens
    				if (_board[r][c] instanceof Queen) {
    					if (_board[r][c].color().abbrev() == "w") {
    						score += 9;
    					}
    					else {
    						score -= 9;
    					}
    				}
    				//It is valuable to put the rooks in the center of the board!
    				if (_board[3][0] instanceof Rook || _board[4][0] instanceof Rook) {
    					if (_board[3][0].color().abbrev() == "w" || _board[4][0].color().abbrev() == "w") {
    						score += 0.5;
    					}
    				}
    				if (_board[3][7] instanceof Rook || _board[4][7] instanceof Rook) {
    					if (_board[3][7].color().abbrev() == "b" || _board[4][7].color().abbrev() == "b") {
    						score -= 0.5;
    					}
    				}
    				if (_board[r][c] instanceof Rook) {
    					if (_board[r][c].color().abbrev() == "w") {
    						score += 5;
    					}
    					else {
    						score -= 5;
    					}
    				}
    				//Minor pieces
    				if (_board[r][c] instanceof Knight) {
    					if (_board[r][c].color().abbrev() == "w") {
    						score += 3.2;
    					}
    					else {
    						score -= 3.2;
    					}
    				}
    				if (_board[1][6] instanceof Bishop || _board[6][6] instanceof Bishop) {
    					if (_board[1][6].color().abbrev() == "w" || _board[6][6].color().abbrev() == "w") {
    						score += 0.3;
    					}
    				}
    				if (_board[1][1] instanceof Bishop || _board[6][1] instanceof Bishop) {
    					if (_board[1][1].color().abbrev() == "b" || _board[6][1].color().abbrev() == "b") {
    						score -= 0.3;
    					}
    				}
    				if (_board[r][c] instanceof Bishop) {
    					if (_board[r][c].color().abbrev() == "w") {
    						score += 3;
    					}
    					else {
    						score -= 3;
    					}
    				}
    			}	
    		}
    	}
    	return score;
    }

			

    /** Executes the single move MOVE on the board. */
    public void executeMove(SingleMove move) {
        _board[move.x1()][move.y1()] = move.replace();
        if (move.replace() != null) {
            move.replace().setLocation(move.x1(), move.y1());
            //System.out.println("replace WASN'T null for some reason");
        }
        _board[move.x2()][move.y2()] = move.selected();
        if (move.selected() != null) {
            move.selected().setLocation(move.x2(), move.y2());
        }
        if (move.target() != null) {
            move.target().setLocation(-1, -1);
        }
        SingleMove mS=(SingleMove)move;
		//System.out.println(mS.movedPiece().color().toString()+" "+mS.movedPiece().type().toString()+" at "+mS.x1()+","+mS.y1()+" to "+(mS.x2())+","+(mS.y2()));

    }

    /** Returns whether COLOR king is being checked by the opponent. */
    public boolean inCheck(PieceColor color) {
        int x = kingX(color);
        int y = kingY(color);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = get(i, j);
                if (p != null && p.color() == color.opposite()
                    && p.canCapture(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Returns whether the player to play has no valid moves. */
    public boolean noMoves() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = get(i, j);
                if (p != null && p.color() == _turn) {
                    if (p.hasMove()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /** Returns whether the position (X, Y) is being guarded by an opposing
      * piece. Used to check if a king can castle. */
    public boolean guarded(int x, int y) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = get(i, j);
                if (p != null && p.color() == _turn.opposite()
                    && p.canCapture(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Returns the piece present at column I and row J. */
    public Piece get(int i, int j) {
        return _board[i][j];
    }

    /** Returns the last piece that moved in the game. */
    public Piece lastMover() {
        return _moves.get(_moves.size() - 1).movedPiece();
    }

    /** Initializes the board to begin play. */
    private void initializeBoard() {
        Piece blackRo0 = new Rook(BLACK, this, 0, 0);
        Piece blackKn0 = new Knight(BLACK, this, 1, 0);
        Piece blackBi0 = new Bishop(BLACK, this, 2, 0);
        Piece blackQu0 = new Queen(BLACK, this, 3, 0);
        _blackKi = new King(BLACK, this, 4, 0);
        Piece blackBi1 = new Bishop(BLACK, this, 5, 0);
        Piece blackKn1 = new Knight(BLACK, this, 6, 0);
        Piece blackRo1 = new Rook(BLACK, this, 7, 0);
        Piece blackPa0 = new Pawn(BLACK, this, 0, 1);
        Piece blackPa1 = new Pawn(BLACK, this, 1, 1);
        Piece blackPa2 = new Pawn(BLACK, this, 2, 1);
        Piece blackPa3 = new Pawn(BLACK, this, 3, 1);
        Piece blackPa4 = new Pawn(BLACK, this, 4, 1);
        Piece blackPa5 = new Pawn(BLACK, this, 5, 1);
        Piece blackPa6 = new Pawn(BLACK, this, 6, 1);
        Piece blackPa7 = new Pawn(BLACK, this, 7, 1);
        Piece whiteRo0 = new Rook(WHITE, this, 0, 7);
        Piece whiteKn0 = new Knight(WHITE, this, 1, 7);
        Piece whiteBi0 = new Bishop(WHITE, this, 2, 7);
        Piece whiteQu0 = new Queen(WHITE, this, 3, 7);
        _whiteKi = new King(WHITE, this, 4, 7);
        Piece whiteBi1 = new Bishop(WHITE, this, 5, 7);
        Piece whiteKn1 = new Knight(WHITE, this, 6, 7);
        Piece whiteRo1 = new Rook(WHITE, this, 7, 7);
        Piece whitePa0 = new Pawn(WHITE, this, 0, 6);
        Piece whitePa1 = new Pawn(WHITE, this, 1, 6);
        Piece whitePa2 = new Pawn(WHITE, this, 2, 6);
        Piece whitePa3 = new Pawn(WHITE, this, 3, 6);
        Piece whitePa4 = new Pawn(WHITE, this, 4, 6);
        Piece whitePa5 = new Pawn(WHITE, this, 5, 6);
        Piece whitePa6 = new Pawn(WHITE, this, 6, 6);
        Piece whitePa7 = new Pawn(WHITE, this, 7, 6);
        Piece[][] newBoard = {
            {blackRo0, blackPa0, null, null, null, null, whitePa0, whiteRo0},
            {blackKn0, blackPa1, null, null, null, null, whitePa1, whiteKn0},
            {blackBi0, blackPa2, null, null, null, null, whitePa2, whiteBi0},
            {blackQu0, blackPa3, null, null, null, null, whitePa3, whiteQu0},
            {_blackKi, blackPa4, null, null, null, null, whitePa4, _whiteKi},
            {blackBi1, blackPa5, null, null, null, null, whitePa5, whiteBi1},
            {blackKn1, blackPa6, null, null, null, null, whitePa6, whiteKn1},
            {blackRo1, blackPa7, null, null, null, null, whitePa7, whiteRo1} };
        _board = newBoard;
    }
    
    public List<Move> listValidMoves(){
    	List<Move> validMoves=new ArrayList<Move>();
    	for(int x=0; x<8; x++) {
    		for(int y=0; y<8; y++) {
    			if(_board[x][y]!=null) {
	    			if(_board[x][y].color().equals(_turn)) {
	    				validMoves.addAll(listValidMoves(_board[x][y],x,y));
	    			}
    			}
    		}
    	}
    	return validMoves;
    }
    
    public List<Move> listValidMoves(Piece p, int x, int y){
    	List<Move> validMoves=new ArrayList<Move>();
    	ArrayList<LocationPair> potentialLocations=new ArrayList<LocationPair>();
    	if(p.type()==PAWN) {
    		potentialLocations.add(new LocationPair(x,y+p.color().direction()));
    		LocationPair l=new LocationPair(x,y+2*p.color().direction());
    		if(l.onBoard()) potentialLocations.add(l);
    	}
    	if(p.type()==KNIGHT) {
    		LocationPair l=new LocationPair(x+2,y+1);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x+2,y-1);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x+1,y+2);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x+1,y-2);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x-1,y+2);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x-1,y-2);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x-2,y+1);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x-2,y-1);
    		if(l.onBoard()) potentialLocations.add(l);
    	}
    	if(p.type()==BISHOP) {
    		for(int xDir=-x; xDir<8-x; xDir++) {
    			if(xDir!=0) {
    				LocationPair l=new LocationPair(x+xDir,y+xDir);
    	    		if(l.onBoard()) potentialLocations.add(l);
    	    		l=new LocationPair(x+xDir,y-xDir);
    	    		if(l.onBoard()) potentialLocations.add(l);
    			}
    		}
    	}
    	if(p.type()==ROOK) {
    		for(int xDir=-x; xDir<8-x; xDir++) {
    			if(xDir!=0) {
					LocationPair l=new LocationPair(x+xDir,y);
		    		if(l.onBoard()) potentialLocations.add(l);
    			}
    		}
    		for(int yDir=-y; yDir<8-y; yDir++) {
    			if(yDir!=0) {
					LocationPair l=new LocationPair(x,y+yDir);
		    		if(l.onBoard()) potentialLocations.add(l);
    			}
    		}
    	}
    	if(p.type()==QUEEN) {
    		for(int xDir=-x; xDir<8-x; xDir++) {
    			if(xDir!=0) {
    				LocationPair l=new LocationPair(x+xDir,y+xDir);
    	    		if(l.onBoard()) potentialLocations.add(l);
    	    		l=new LocationPair(x+xDir,y-xDir);
    	    		if(l.onBoard()) potentialLocations.add(l);
    			}
    		}
    		for(int xDir=-x; xDir<8-x; xDir++) {
    			if(xDir!=0) {
					LocationPair l=new LocationPair(x+xDir,y);
		    		if(l.onBoard()) potentialLocations.add(l);
    			}
    		}
    		for(int yDir=-y; yDir<8-y; yDir++) {
    			if(yDir!=0) {
					LocationPair l=new LocationPair(x,y+yDir);
		    		if(l.onBoard()) potentialLocations.add(l);
    			}
    		}
    	}
    	if(p.type()==KING) {
    		LocationPair l=new LocationPair(x+1,y+1);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x+1,y);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x+1,y-1);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x,y+1);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x,y-1);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x-1,y+1);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x-1,y);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x-1,y-1);
    		if(l.onBoard()) potentialLocations.add(l);
    	}
    	for(LocationPair l : potentialLocations) {
			if(l.onBoard()) { 
				MoveValidityPair pair=p.outputPairAndMove(l.x(), l.y());
				if(pair.validity()) {
					this.undoMove();
					validMoves.add(pair.move());
				}
			}
		}
    	return validMoves;
    }

    /** Returns the x-location of the king of color COLOR. */
    public int kingX(PieceColor color) {
        if (color == WHITE) {
            return _whiteKi.getX();
        } else {
            return _blackKi.getX();
        }
    }

    /** Returns the y-location of the king of color COLOR. */
    public int kingY(PieceColor color) {
        if (color == WHITE) {
            return _whiteKi.getY();
        } else {
            return _blackKi.getY();
        }
    }
    
    public void printBoard() {
    	for(int row=0; row<8; row++) {
    		String r="";
    		for(int x=0; x<8; x++) {
    			if(_board[x][row]!=null)
    			r=r+_board[x][row].imageString()+" ";
    			else r=r+"---"+" ";
    		}
    		System.out.println(r);
    	}
    }
    
    public void printNonPhantomBoard() {
    	for(int row=0; row<8; row++) {
    		String r="";
    		for(int x=0; x<8; x++) {
    			if(_board[x][row]!=null) {
    				if(_board[x][row].getX()!=x || _board[x][row].getY()!=row) {
    					r=r+"---"+" ";
    				}
    				else r=r+_board[x][row].imageString()+" ";
    			}
    			else r=r+"---"+" ";
    		}
    		System.out.println(r);
    	}
    }
    
    public int checkPhantom(boolean print) { //outputs the number of phantom pieces on the board
    	int phantom=0;
    	int wphantom=0;
    	int bphantom=0;
    	for(int row=0; row<8; row++) {
    		for(int x=0; x<8; x++) {
    			if(_board[x][row]!=null) {
    				if(_board[x][row].getX()!=x || _board[x][row].getY()!=row) {
    					phantom++;
    					Piece p=_board[x][row];
    					if(_board[x][row].color().equals(BLACK)) bphantom++;
    					if(_board[x][row].color().equals(WHITE)) wphantom++;
    					if(print) System.out.println(p.color().name()+" "+p.type().name()+" is on: ("+p.getX()+","+p.getY()+"), when it should be on: ("+x+","+row+")");
    				}
    			}
    		}
    	}
    	System.out.println("# of Phantom Pieces: "+phantom+", Black Phantom: "+bphantom+", White Phantom: "+wphantom);
    	return phantom;
    }

    /** Sets the selected piece's x-location to X. */
    public void setSelectedX(int x) {
        _selectedX = x;
    }

    /** Sets the selected piece's y-location to Y. */
    public void setSelectedY(int y) {
        _selectedY = y;
    }

    /** Returns the selected piece's x-location. */
    public int selectedX() {
        return _selectedX;
    }

    /** Returns the selected piece's y-location. */
    public int selectedY() {
        return _selectedY;
    }

    /** Returns the board associated with this game. */
    public Piece[][] board() {
        return _board;
    }

    /** Returns the color to play on this turn. */
    public PieceColor turn() {
        return _turn;
    }

    /** The game board representing this game. */
    private Piece[][] _board;

    /** The GUI displayed to the player. */
    private ChessGUI _gui;

    /** The color to move next. */
    private PieceColor _turn;

    /** An ordered list of the moves made in the game. */
    private List<Move> _moves;

    /** Stores the black king. */
    private King _blackKi;

    /** Stores the white king. */
    private King _whiteKi;

    /** The x-location of the piece selected by the user for a move. */
    private int _selectedX;

    /** The y-location of the piece selected by the user for a move. */
    private int _selectedY;
    
    public String _original="not original";
}
