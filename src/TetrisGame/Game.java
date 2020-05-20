package TetrisGame;

import Graphics.TetrisMenu;
import TetrisGame.Tetromino.Tetromino;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class Game {
    public static final int BOARD_WIDTH = 14;
    public static final int BOARD_HEIGHT = 30;
    public static final int PLAYABLE_WIDTH = 10;
    public static final int PLAYABLE_HEIGHT = 20;
    public static final int LOW_X = 2;
    public static final int HIGH_X = BOARD_WIDTH - 3;
    public static final int LOW_Y = 2;
    public static final int PLAYABLE_Y = 8;
    public static final int HIGH_Y = BOARD_HEIGHT - 3;
    public static final int SPAWN_X = LOW_X + 3;
    public static final int SPAWN_Y = PLAYABLE_Y - 2;

    private int lockCounter;
    private int dropCounter;
    private Mino[][] board;
    private int score;
    private Tetromino hold;
    private Tetromino currentTetromino;
    private Tetromino[] next;
    private boolean gameOver;

    public Game(){
        reset();
    }

    public void update(Move move){
        if(currentTetromino.canFall(board)){
            lockCounter = 0;
            if(dropCounter > 40 || (dropCounter > 1 && move.softDrop)){
                currentTetromino.fall(board);
                dropCounter = 0;
            }
            dropCounter ++;
        } else {
            if(lockCounter > 40){
                currentTetromino.lock(board);
                for (int i = 0; i < PLAYABLE_HEIGHT; i++) {
                    if(isLineFull(i)){
                        clearLine(i);
                    }
                }
                //TODO: if tetronimo locked above visible field
                lockCounter = 0;
                currentTetromino = new Tetromino(Mino.values()[new Random().nextInt(Mino.values().length-1)]);
                if(!currentTetromino.move(SPAWN_X,SPAWN_Y, board)){
                    gameOver = true;
                }
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

        gameOver = false;
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
        for (int x = LOW_X; x <= HIGH_X; x++) {
            if(board[x][line+PLAYABLE_Y] == Mino.NONE){
                return false;
            }
        }
        return true;
    }

    public void clearLine(int line){
        lowerLine(line-1);
    }

    public void lowerLine(int line){
        if(PLAYABLE_Y <= line && line <= PLAYABLE_HEIGHT) {
            for (int x = LOW_X; x <= HIGH_X; x++) {
                board[x][line + PLAYABLE_Y+1] = board[x][line + PLAYABLE_Y];
            }
            lowerLine(line-1);
        }
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
            for (int y = PLAYABLE_Y; y <= HIGH_Y; y++) {
                Mino.draw(boardX + ((x-LOW_X)*squareSize), boardY + ((y-PLAYABLE_Y)*squareSize), squareSize, board[x][y], gc);
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
