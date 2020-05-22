package TetrisGame;

import TetrisGame.Tetromino.Orientation;
import TetrisGame.Tetromino.Tetromino;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.concurrent.ArrayBlockingQueue;

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

    private static final int LOCK_DELAY = 30;
    private int lockCounter;
    private int dropCounter;
    private Mino[][] board;
    private int score;
    private int level;
    private Tetromino hold;
    private Tetromino currentTetromino;
    private ArrayBlockingQueue<Tetromino> next;
    private boolean gameOver;
    private boolean held;
    private int clearedLines;

    public Game(){
        reset();
    }

    public void update(Move move){
        doMoves(move);
        boolean canFall = doFall();
        if(!canFall) {
            doLock();
        }
        score();

        if(currentTetromino.canFall(board)){
            lockCounter = 0;
            if(dropCounter > 40 || (move.softDrop)){
                currentTetromino.fall(board);
                dropCounter = 0;
            }
            dropCounter ++;
        } else {
            if(lockCounter > 40){
                currentTetromino.lock(board);
                for (int i = 0; i < PLAYABLE_HEIGHT; i++) {
                    if(isLineFull(i)){
                        System.out.println("Clearing " + i);
                        clearLine(i);
                    }
                }
                //TODO: if tetronimo locked above visible field
                lockCounter = 0;
                currentTetromino = next.remove();
                next.add(new Tetromino(Mino.getNextRandom()));
                if(!currentTetromino.move(SPAWN_X,SPAWN_Y, board)){
                    gameOver = true;
                }
            }
            lockCounter ++;
        }

        if(move.hardDrop){
            while(currentTetromino.fall(board)){ }
        }
        if(move.hold){
            Tetromino temp = currentTetromino;
            currentTetromino = hold;
            hold = temp;

            if(currentTetromino == null){
                currentTetromino = next.remove();
                next.add(new Tetromino(Mino.getNextRandom()));
                if(!currentTetromino.move(SPAWN_X,SPAWN_Y, board)){
                    gameOver = true;
                }
            }
            hold.setOrientation(Orientation.NORTH);
            currentTetromino.move(SPAWN_X, SPAWN_Y, board);
        }

        currentTetromino.translate(move.translation, board);
        currentTetromino.rotate(move.rotation, board);
    }

    private void score(){

    }


    private void doMoves(Move move){
        // soft drop
        if(move.softDrop) {
            fall();
        } else if(move.hardDrop){ // hard drop
            while(fall()){ }
        }
        // translate
        currentTetromino.translate(move.translation, board);
        // rotate
        currentTetromino.rotate(move.rotation, board);
        // hold
        if(move.hold && !held){
            held = true;
            Tetromino temp = currentTetromino;
            currentTetromino = hold;
            hold = temp;

            if(currentTetromino == null){
                nextTetromino();
            }

            hold.setOrientation(Orientation.NORTH);
            hold.move(SPAWN_X, SPAWN_Y, board);
        }

        // reset lock delay
        if(move.translation != Move.Direction.NONE && move.rotation != Move.Direction.NONE){
            lockCounter = 0;
        }
    }

    private boolean fall(){
        if(currentTetromino.fall(board)){
            lockCounter = 0;
            return true;
        } else {
            return false;
        }
    }

    private boolean isTSpin(){
        return false;
    }

    private void clearLines(){
        for (int y = currentTetromino.getY(); y < currentTetromino.getY() + currentTetromino.getHeight(); y++) {
            if(isLineFull(y)){
                clearLine(y);
                clearedLines ++;
            }
        }
    }

    private boolean doFall(){
        return currentTetromino.fall(board);
    }

    private void doLock(){
        if(lockCounter > LOCK_DELAY){
            gameOver = !currentTetromino.lock(board);

            clearLines();
            nextTetromino();
        }
    }

    private void nextTetromino(){

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
        next = new ArrayBlockingQueue<>(6);
        while(next.remainingCapacity() > 0){
            next.add(new Tetromino(Mino.getNextRandom()));
        }
        currentTetromino = new Tetromino(Mino.getNextRandom());
        currentTetromino.move(SPAWN_X,SPAWN_Y, board);
    }

    public boolean isLineFull(int y){
        for (int x = LOW_X; x <= HIGH_X; x++) {
            if(board[x][y] == Mino.NONE){
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
        return next.toArray(new Tetromino[0]);

    }

    public int getScore(){
        return 0;
    }

    public void drawBoard(double boardX, double boardY, double width, GraphicsContext gc){
        double squareSize = width / 10;
        double height = width * 2;
        for (int x = LOW_X; x <= HIGH_X; x++) {
            for (int y = PLAYABLE_Y; y <= HIGH_Y; y++) {
                Mino.draw(boardX + ((x-LOW_X)*squareSize), boardY + ((y-PLAYABLE_Y)*squareSize), squareSize, board[x][y], gc);
            }
        }
        currentTetromino.drawGhostRelative(boardX, boardY, squareSize, gc, board);

        currentTetromino.drawRelative(boardX, boardY, squareSize, gc);

        gc.clearRect(boardX, boardY-1000, squareSize*10, 1000);

        gc.setFill(Color.BLACK);
        gc.fillRect(boardX,boardY,squareSize * 10, 1);
        gc.fillRect(boardX,boardY,1, squareSize * 20);
        gc.fillRect(boardX,boardY + squareSize * 20,squareSize * 10, 1);
        gc.fillRect(boardX + squareSize * 10,boardY,1, squareSize * 20);


    }

    public void drawNext(double gx, double gy, double width, GraphicsContext gc){
        double squareSize = width / 6;
        double height = squareSize * 20;


        gc.setFill(Color.BLACK);
        gc.fillRect(gx, gy, width, height);
        gc.setFill(Color.WHITE);
        gc.fillRect(gx+1, gy+1, width-2, height-2);


        Tetromino[] next = getNext();
        for (int i = 0; i < next.length; i++) {
            next[i].drawAbsolute(gx + squareSize, gy + squareSize + (squareSize * i * 3), squareSize, gc);
        }
    }

    public void drawHold(double gx, double gy, double width, GraphicsContext gc){
        double squareSize = width / 6;
        double height = squareSize * 4;


        gc.setFill(Color.BLACK);
        gc.fillRect(gx, gy, width, height);
        gc.setFill(Color.WHITE);
        gc.fillRect(gx+1, gy+1, width-2, height-2);

        if(hold != null){
            hold.drawAbsolute(gx + squareSize, gy + squareSize, squareSize, gc);
        }
    }
}
