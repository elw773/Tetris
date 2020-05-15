package TetrisGame;

import TetrisGame.Tetromino.Tetromino;
import TetrisGame.Tetromino.TetrominoFactory;

public class Game {
    private int lockCounter;
    private int dropCounter;
    private Mino[][] board;
    private int score;
    private Tetromino hold;
    private Tetromino currentTetromino;
    private Tetromino[] next;

    public Game(){
        reset();
    }

    public void update(Move move){
        if(currentTetromino.canFall(board)){
            lockCounter = 0;
            if(dropCounter > 40){
                currentTetromino.fall(board);
                dropCounter = 0;
            }
            dropCounter ++;
        } else {
            if(lockCounter > 25){
                currentTetromino.lock(board);
                lockCounter = 0;
                currentTetromino = TetrominoFactory.newTetromino(Mino.O);
            }
            lockCounter ++;
        }

        currentTetromino.translate(move.translation, board);
    }

    public boolean gameIsOver(){
        return false;
    }

    public void reset() {
        board = new Mino[12][26];

        for (int i = 0; i < board[0].length; i++) {
            board[0][i] = Mino.I;
            board[11][i] = Mino.I;
        }

        for (int i = 0; i < board.length; i++) {
            board[i][0] = Mino.I;
            board[i][25] = Mino.I;
        }

        hold = null;
        currentTetromino = null;
        next = new Tetromino[6];
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
