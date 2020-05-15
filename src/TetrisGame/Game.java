package TetrisGame;

import Graphics.TetrisMenu;
import TetrisGame.Tetromino.Tetromino;
import TetrisGame.Tetromino.TetrominoFactory;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

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
            if(lockCounter > 30){
                currentTetromino.lock(board);
                lockCounter = 0;
                currentTetromino = TetrominoFactory.newTetromino(Mino.values()[new Random().nextInt(Mino.values().length-1)]);
                currentTetromino.move(4,1, board);
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

        for (int x = 0; x < 12; x++) {
            for (int y = 0; y < 26; y++) {
                if(x == 0 || x == 11 || y == 0 || y == 25){
                    board[x][y] = Mino.I;
                } else {
                    board[x][y] = Mino.NONE;
                }
            }
        }

        hold = null;
        currentTetromino = TetrominoFactory.newTetromino(Mino.O);
        currentTetromino.move(4,1, board);
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

    public void drawBoard(double boardX, double boardY, double height, GraphicsContext gc){
        double squareSize = height / 20;
        for (int x = 1; x < 11; x++) {
            for (int y = 5; y < 25; y++) {
                Mino.draw(boardX + ((x-1)*squareSize), boardY + ((y-5)*squareSize), squareSize, board[x][y], gc);
            }
        }
        currentTetromino.drawRelative(boardX, boardY, squareSize, gc);

        gc.clearRect(boardX, boardY-1000, squareSize*10, 1000);

        gc.setFill(Color.BLACK);
        gc.fillRect(boardX,boardY,squareSize * 10, 1);
        gc.fillRect(boardX,boardY,1, squareSize * 20);
        gc.fillRect(boardX,boardY + squareSize * 20,squareSize * 10, 1);
        gc.fillRect(boardX + squareSize * 10,boardY,1, squareSize * 20);


    }
}
