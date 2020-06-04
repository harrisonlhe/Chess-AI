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
    	_board=new Piece[8][8];
    	//_gui=null;
        //newGame();
        
    }
    
    public void clonePieces(Game g, Game nG) { //g is old game, this is new game
    	nG._turn=g.turn().abbrev().contentEquals("b") ? BLACK : WHITE;
        nG._selectedX=g._selectedX;
        nG._selectedY=g._selectedY;
    	for(int x=0; x<8; x++) {
        	for(int y=0; y<8; y++) {
        		if(g._board[x][y]!=null)
        		{
        			nG._board[x][y]=g._board[x][y].dclone(nG);
        		}
        		else {
        			nG._board[x][y]=null;
        		}
            }
        }
    	/** Stores the black king. */
        nG._blackKi=(King)this.board()[g.kingX(BLACK)][g.kingY(BLACK)];

        /** Stores the white king. */
        nG._whiteKi=(King)this.board()[g.kingX(WHITE)][g.kingY(WHITE)];
        
        
        for(int i=0; i<g._moves.size(); i++) {
        	Move m=g._moves.get(i);
        	nG._moves.add(m.changeBoard(nG));
        }
    }

    /** Clears the game and starts a new one. */
    public void newGame() {
        //initializeBoard();
    	initializeBoard();
        _moves.clear();
        _turn = WHITE;
        _selectedX = -1;
        _selectedY = -1;
    }
    
    public Game applyMoveCloning(Move move) {
    	Game nG=new Game(this); //this is old game
    	
    	int prevPieces=this.countPieces();
    	
    	nG.clonePieces(this, nG); //nG is new game
    	int newPieces=nG.countPieces();
    	
    	Move mNew=move.changeBoard(nG);
    	
    	nG.makeMove(mNew);
    	nG._turn=this.turn().opposite();
    	//System.out.println("created game clone");
    	
    	if(prevPieces<newPieces) {
    		//System.out.println("new piece appeared???");
    	}
    	return nG;
    }
    
    public int countPieces() {
    	int count=0;
    	for(int row=0; row<8; row++) {
    		for(int x=0; x<8; x++) {
    			if(_board[x][row]!=null) count++;
    		}
    	}
    	return count;
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
					
					if (_board[r][c].type().abbrev().equals("ki")) {
						if (_board[r][c].color().abbrev().equals("w")) {
							score += 1000;
						} else {
							score -= 1000;
						}
					}
				}
			}
		}
		return score;
    }
 
    public double complexEval() {
    	double score = 0;
    	int materialScore = this.simpleEval1();
    	score = materialScore;

    	//White Pawns
    	boolean wPawnAtC3 = false;
    	boolean wPawnAtD3 = false;
    	boolean wPawnAtE3 = false;
    	boolean wPawnAtF3 = false;
    	boolean wPawnAtD4 = false;
    	boolean wPawnAtE4 = false;			
    	if (_board[2][5] != null && _board[2][5] instanceof Pawn) {
    		if (_board[2][5].color().abbrev() == "w") {
    			wPawnAtC3 = true;
    		}
    	}
    	if (_board[3][5] != null && _board[3][5] instanceof Pawn) {
    		if (_board[3][5].color().abbrev() == "w") {
    			wPawnAtD3 = true;
    		}
    	}
    	if (_board[4][5] != null && _board[4][5] instanceof Pawn) {
    		if (_board[4][5].color().abbrev() == "w") {
    			wPawnAtE3 = true;
    		}
    	}
    	if (_board[5][5] != null && _board[4][5] instanceof Pawn) {
    		if (_board[5][5].color().abbrev() == "w") {
    			wPawnAtF3 = true;
    		}
    	}
    	if (_board[3][4] != null && _board[3][4] instanceof Pawn) {
    		if (_board[3][4].color().abbrev() == "w") {
    			wPawnAtD4 = true;
    		}
    	}
    	if (_board[4][4] != null && _board[4][4] instanceof Pawn) {
    		if (_board[4][4].color().abbrev() == "w") {
    			wPawnAtE4 = true;
    		}
    	}

    	if (wPawnAtC3 || wPawnAtD3 || wPawnAtE3 || wPawnAtF3) {
    		score += 0.3;
    	}
    	if (wPawnAtD4 || wPawnAtE4) {
    		score += 0.6;
    	}
    	//Black Pawns
    	boolean bPawnAtC6 = false;
    	boolean bPawnAtD6 = false;
    	boolean bPawnAtE6 = false;
    	boolean bPawnAtF6 = false;
    	boolean bPawnAtD5 = false;
    	boolean bPawnAtE5 = false;
    	if (_board[2][2] != null && _board[2][2] instanceof Pawn) {
    		if (_board[2][2].color().abbrev() == "b") {
    			bPawnAtC6 = true;
    		}
    	}
    	if (_board[3][2] != null && _board[3][2] instanceof Pawn) {
    		if (_board[3][2].color().abbrev() == "b") {
    			bPawnAtD6 = true;
    		}
    	}
    	if (_board[4][2] != null && _board[4][2] instanceof Pawn) {
    		if (_board[4][2].color().abbrev() == "b") {
    			bPawnAtE6 = true;
    		}
    	}
    	if (_board[5][2] != null && _board[5][2] instanceof Pawn) {
    		if (_board[5][2].color().abbrev() == "b") {
    			bPawnAtF6 = true;
    		}
    	}
    	if (_board[3][3] != null && _board[3][3] instanceof Pawn) {
    		if (_board[3][3].color().abbrev() == "b") {
    			bPawnAtD5 = true;
    		}
    	}
    	if (_board[4][3] != null && _board[4][3] instanceof Pawn) {
    		if (_board[4][3].color().abbrev() == "b") {
    			bPawnAtE5 = true;
    		}
    	}

    	if (bPawnAtC6 || bPawnAtD6 || bPawnAtE6 || bPawnAtF6) {
    		score -= 0.3;
    	}
    	if (bPawnAtD5 || bPawnAtE5) {
    		score -= 0.6;
    	}
    	//King safety
    	boolean wKingOnG1 = false;
    	boolean bKingOnG8 = false;
    	if (_board[6][7] != null && _board[6][7] instanceof King) {
    		if (_board[6][7].color().abbrev() == "w") {
    			wKingOnG1 = true;
    		}
    	}
    	if (_board[6][0] != null && _board[6][0] instanceof King) {
    		if (_board[6][0].color().abbrev() == "b") {
    			bKingOnG8 = true;
    		}
    	}
    	/*if (wKingOnG1) {
    		score += 0.1;
    	}
    	if (bKingOnG8) {
    		score -= 0.1;
    	}*/
    	//Rooks should be in the center of the board
    	boolean wRookOnD1 = false;
    	boolean wRookOnE1 = false;
    	boolean bRookOnD8 = false;
    	boolean bRookOnE8 = false;
    	if (_board[3][7] != null && _board[3][7] instanceof Rook) {
    		if (_board[3][7].color().abbrev() == "w") {
    			wRookOnD1 = true;
    		}
    	}
    	if (_board[4][7] != null && _board[4][7] instanceof Rook) {
    		if (_board[4][7].color().abbrev() == "w") {
    			wRookOnE1 = true;
    		}
    	}
    	if (_board[3][0] != null && _board[3][0] instanceof Rook) {
    		if (_board[3][0].color().abbrev() == "b") {
    			bRookOnD8 = true;
    		}
    	}
    	if (_board[4][0] != null && _board[4][0] instanceof Rook) {
    		if (_board[4][0].color().abbrev() == "b") {
    			bRookOnE8 = true;
    		}
    	}
    	if (wRookOnD1 || wRookOnE1) {
    		score += 0.6;
    	}
    	if (bRookOnD8 || bRookOnE8) {
    		score -= 0.6;
    	}
    	//Bishops should be fianchettoed
    	boolean wBishopOnG2 = false;
    	boolean wBishopOnB2 = false;
    	boolean bBishopOnG7 = false;
    	boolean bBishopOnB7 = false;
    	if (_board[6][6] != null && _board[6][6] instanceof Bishop) {
    		if (_board[6][6].color().abbrev() == "w") {
    			wBishopOnG2 = true;
    		}
    	}
    	if (_board[1][6] != null && _board[1][6] instanceof Bishop) {
    		if (_board[1][6].color().abbrev() == "w") {
    			wBishopOnB2 = true;
    		}
    	}
    	if (_board[6][1] != null && _board[6][1] instanceof Bishop) {
    		if (_board[6][1].color().abbrev() == "b") {
    			bBishopOnG7 = true;
    		}
    	}
    	if (_board[1][1] != null && _board[1][1] instanceof Bishop) {
    		if (_board[1][1].color().abbrev() == "b") {
    			bBishopOnB7 = true;
    		}
    	}
    	if (wBishopOnG2 || wBishopOnB2) {
    		score += 0.7;
    	}
    	if (bBishopOnG7 || bBishopOnB7) {
    		score -= 0.7;
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
    private void initializeBoard2() {
    	Piece[][] board = new Piece[8][8];
    	
    	Piece blackRo1 = new Rook(BLACK, this, 0,0);
    	board[0][0]=blackRo1;
    	Piece blackRo2 = new Rook(BLACK, this, 0,7);
    	board[0][7]=blackRo2;
    	Piece blackPa1 = new Pawn(BLACK, this, 2,2);
    	board[2][2]=blackPa1;
    	Piece blackQu1 = new Queen(BLACK, this, 2,7);
    	board[2][7]=blackQu1;
    	Piece blackBi1 = new Bishop(BLACK, this, 4, 2);
    	board[4][2]=blackBi1;
    	Piece blackPa2 = new Pawn(BLACK, this, 5,1);
    	board[5][1]=blackPa2;
    	_blackKi = new King(BLACK, this, 5, 0);
    	board[5][0]=_blackKi;
    	Piece whiteQu1 = new Queen(WHITE, this, 5,4);
    	board[5][4]=whiteQu1;
    	Piece whiteRo1 = new Rook(WHITE, this, 6,5);
    	board[6][5]=whiteRo1;
    	_whiteKi=new King(WHITE, this, 7,6);
    	board[7][6]=_whiteKi;
    	
            _board = board;
    	
    }
    private void initializeBoard3() {
    	Piece[][] board = new Piece[8][8];
    	
    	Piece blackRo1 = new Rook(BLACK, this, 0,4);
    	board[0][4]=blackRo1;
    	
    	Piece whiteQu1 = new Queen(WHITE, this, 4,4);
    	board[4][4]=whiteQu1;
    	
    	_blackKi = new King(BLACK, this, 0, 7);
    	board[0][7]=_blackKi;
    	
    	_whiteKi=new King(WHITE, this, 0,0);
    	board[0][0]=_whiteKi;
    	
            _board = board;
    	
    }
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
    		LocationPair l=new LocationPair(x+1,y+1);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x+1,y-1);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x,y+1);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x,y-1);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x-1,y+1);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x-1,y-1);
    		if(l.onBoard()) potentialLocations.add(l);
    		
    		l=new LocationPair(x,y+2);
    		if(l.onBoard()) potentialLocations.add(l);
    		l=new LocationPair(x,y-2);
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
