package TetrisGame;

import Graphics.TetrisMenu;
import TetrisGame.Tetromino.Tetromino;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class Game {
    public static final int BOARD_WIDTH = 14;
    public static final int BOARD_HEIGHT = 28;
    public static final int PLAYABLE_WIDTH = 10;
    public static final int PLAYABLE_HEIGHT = 20;
    public static final int LOW_X = 2;
    public static final int HIGH_X = 11;
    public static final int LOW_Y = 6;
    public static final int HIGH_Y = 25;
    public static final int SPAWN_X = LOW_X + 3;
    public static final int SPAWN_Y = LOW_Y - 2;

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
                currentTetromino = new Tetromino(Mino.values()[new Random().nextInt(Mino.values().length-1)]);
                currentTetromino.move(4,1, board);
            }
            lockCounter ++;
        }

        currentTetromino.translate(move.translation, board);
        currentTetromino.rotate(move.rotation, board);
    }

    public boolean gameIsOver(){
        return false;
    }

    public void reset() {
        board = new Mino[BOARD_WIDTH][BOARD_HEIGHT];

        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                if(x < LOW_X || HIGH_X < x || y < LOW_Y || HIGH_Y < y){
                    board[x][y] = Mino.I;
                } else {
                    board[x][y] = Mino.NONE;
                }
            }
        }

        hold = null;
        currentTetromino = new Tetromino(Mino.values()[new Random().nextInt(Mino.values().length-1)]);
        currentTetromino.move(SPAWN_X,SPAWN_Y, board);
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
        for (int x = LOW_X; x <= HIGH_X; x++) {
            for (int y = LOW_Y; y <= HIGH_Y; y++) {
                Mino.draw(boardX + ((x-LOW_X)*squareSize), boardY + ((y-LOW_Y)*squareSize), squareSize, board[x][y], gc);
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
