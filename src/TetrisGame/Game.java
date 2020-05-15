package TetrisGame;

import TetrisGame.Tetromino.Tetromino;

public class Game {
    private int lockCounter;
    private int dropCounter;
    private Mino[][] board;
    private int score;
    private Tetromino hold;
    private Tetromino currentTetromino;
    private Tetromino[] next;


    public void update(Move move){

    }

    public boolean gameIsOver(){
        return false;
    }

    public void reset() {
    }

    public boolean isLineFull(int line){
        return false;
    }

    public void clearLine(int line){
    }

    public Tetromino getHold(){
        return null;
    }

    public Mino[][] getBoard(){
        return null;
    }

    public Tetromino getCurrentTetromino(){
        return null;
    }

    public Tetromino[] getNext(){
        return null;
    }

    public int getScore(){
        return 0;
    }
}
