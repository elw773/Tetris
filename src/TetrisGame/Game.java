package TetrisGame;

import TetrisGame.Tetromino.Tetromino;

import java.util.Queue;

public class Game {
    private int lockCounter;
    private int dropCounter;
    private Square[][] board;
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

    public boolean lineIsFull(int line){
        return false;
    }

    public void clearLine(int line){
    }

    public Tetromino getHold(){
        return null;
    }

    public Square[][] getBoard(){
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
