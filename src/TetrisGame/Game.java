package TetrisGame;

import TetrisGame.Tetromino.Orientation;
import TetrisGame.Tetromino.Tetromino;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.EnumMap;
import java.util.concurrent.ArrayBlockingQueue;

public class Game {
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 26;
    public static final int PLAYABLE_WIDTH = 10;
    public static final int PLAYABLE_HEIGHT = 20;
    public static final int LOW_X = 0;
    public static final int HIGH_X = BOARD_WIDTH - 1;
    public static final int LOW_Y = 0;
    public static final int PLAYABLE_Y = 6;
    public static final int HIGH_Y = BOARD_HEIGHT - 1;
    public static final int SPAWN_X = LOW_X + 3;
    public static final int SPAWN_Y = PLAYABLE_Y - 2;

    private static final int LOCK_DELAY = 30;
    private int lockCounter;
    private double dropRate = 0.03;
    private double dropGoal = 0;
    private int dropActual = 0;
    private int dropCounter;
    private Mino[][] board;
    private int score = 0;
    private int level = 1;
    private Tetromino hold;
    private Tetromino currentTetromino;
    private ArrayBlockingQueue<Tetromino> next;
    private boolean gameOver;
    private boolean held;
    private int clearedLines;
    private long totalGameTime = 0;
    private long numUpdates = 0;
    private boolean lastMoveWasRotate;

    private boolean tSpin;
    private boolean miniTSpin;
    private boolean backToBack;
    private int comboCount;
    private boolean combo;

    private static EnumMap<Orientation, Integer> tSpinCornerX;
    private static EnumMap<Orientation, Integer> tSpinCornerY;
    static {
        tSpinCornerX = new EnumMap<Orientation, Integer>(Orientation.class);
        tSpinCornerY = new EnumMap<Orientation, Integer>(Orientation.class);

        tSpinCornerX.put(Orientation.NORTH, 0);
        tSpinCornerY.put(Orientation.NORTH, 0);

        tSpinCornerX.put(Orientation.EAST, 2);
        tSpinCornerY.put(Orientation.EAST, 0);

        tSpinCornerX.put(Orientation.SOUTH, 2);
        tSpinCornerY.put(Orientation.SOUTH, 2);

        tSpinCornerX.put(Orientation.WEST, 0);
        tSpinCornerY.put(Orientation.WEST, 2);
    }

    public Game(){
        reset();
    }

    public void update(Move move){
        long start = System.currentTimeMillis();

        doMoves(move);
        boolean canFall = doFall();
        if(!canFall) {
            doLock();
        }
        defaultScore(move);
        numUpdates ++;
        totalGameTime += (System.currentTimeMillis() - start);
        double average = (double)totalGameTime/numUpdates;
        //System.out.println("Game: " + average);
        if((System.currentTimeMillis() - start) > 1){
          //  System.err.println("Game took " + (System.currentTimeMillis() - start));
        }

    }

    private void defaultScore(Move move){
        if(move.hardDrop){
            score += (dropCounter * 2);
        } else if (move.softDrop){
            score += dropCounter;
        }
        dropCounter = 0;
    }

    private void checkTSpins(){

        if(lastMoveWasRotate && currentTetromino.getMinoType() == Mino.T){

            int x = currentTetromino.getX();
            int y = currentTetromino.getY();

            Orientation orientation = currentTetromino.getOrientation();

            int pointerCorners = 0;

            if(board[x+tSpinCornerX.get(orientation)][y+tSpinCornerY.get(orientation)] != Mino.NONE){
                pointerCorners += 1;
            }
            if(board[x+tSpinCornerX.get(orientation.cw())][y+tSpinCornerY.get(orientation.cw())] != Mino.NONE){
                pointerCorners += 1;
            }

            int otherCorners = 0;

            if(board[x+tSpinCornerX.get(orientation.ccw())][y+tSpinCornerY.get(orientation.ccw())] != Mino.NONE){
                otherCorners += 1;
            }
            if(board[x+tSpinCornerX.get(orientation.ccw().ccw())][y+tSpinCornerY.get(orientation.ccw().ccw())] != Mino.NONE){
                otherCorners += 1;
            }

            if(otherCorners + pointerCorners >= 3){
                if(pointerCorners == 2){
                    tSpin = true;
                } else {
                    miniTSpin = true;
                }
            }
        }
    }

    private void lockScore(){
        checkTSpins();

        if(clearedLines == 0){
            combo = false;
            comboCount = 0;
        } else {
            comboCount += clearedLines;
        }

        int newPoints = 0;

        if(tSpin){
            switch (clearedLines){
                case 0: newPoints += 400; break;
                case 1: newPoints += 800; break;
                case 2: newPoints += 1200; break;
                case 3: newPoints += 1600; break;
            }
            System.out.println("T-spin");
        } else if(miniTSpin){
            switch (clearedLines){
                case 0: newPoints += 100; break;
                case 1: newPoints += 200; break;
                case 2: newPoints += 400; break;
            }
            System.out.println("Mini T-spin");
        } else {
            switch (clearedLines){
                case 1: newPoints += 100; break;
                case 2: newPoints += 300; break;
                case 3: newPoints += 500; break;
                case 4: newPoints += 800; break;
            }
        }

        boolean difficult = ((tSpin || miniTSpin) && clearedLines > 0) || clearedLines == 4;

        if(backToBack && difficult){
            newPoints = newPoints * 3;
            newPoints = newPoints / 2;
            System.out.println("BACK 2 BACK");
        } else if(combo){
            score += comboCount * 50 * level;
            System.out.println("Combo! " + comboCount);
        }
        score += newPoints * level;
        System.out.println("Score: " + score);


        combo = clearedLines > 0;
        clearedLines = 0;
        tSpin = false;
        miniTSpin = false;
    }


    private void doMoves(Move move){
        // soft drop
        if(move.softDrop) {
            dropGoal += 0.5;
        } else if(move.hardDrop){ // hard drop
            dropGoal += 20.0;
        }
        // translate
        if(move.translation != Move.Direction.NONE && currentTetromino.translate(move.translation, board)){
            lastMoveWasRotate = false;
            lockCounter = 0;
        }

        // rotate

        if(move.rotation != Move.Direction.NONE && currentTetromino.rotate(move.rotation, board) && currentTetromino.getMinoType() != Mino.O){
            lastMoveWasRotate = true;
            lockCounter = 0;
        }
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

    }

    private boolean fall(){
        if(currentTetromino.fall(board)){
            lockCounter = 0;
            dropCounter ++;
            lastMoveWasRotate = false;
            return true;
        } else {
            return false;
        }
    }


    private void clearLines(){
        //long start = System.currentTimeMillis();
        for (int y = currentTetromino.getY(); -1 < y && y < BOARD_HEIGHT && y < currentTetromino.getY() + currentTetromino.getHeight(); y++) {
          //  System.out.println("checking line: " + y);
            if(isLineFull(y)){
                clearLine(y);
                clearedLines ++;
            }
        }
        //System.out.println("Line clearing took " + (System.currentTimeMillis() - start));
    }

    private boolean doFall(){
        if(currentTetromino.canFall(board)){
            dropGoal += dropRate;
            while(dropActual < (int) dropGoal && fall()){
                dropActual ++;
            }
        }
        return currentTetromino.canFall(board);
    }

    private void doLock(){
        if(lockCounter > LOCK_DELAY){
            gameOver = !currentTetromino.lock(board);
            lockCounter = 0;
            lockScore();
            clearLines();


            nextTetromino();
        } else {
            lockCounter ++;
        }
    }

    private void nextTetromino(){
        currentTetromino = next.remove();
        next.add(new Tetromino(Mino.getNextRandom()));
        held = false;
        dropGoal = 0;
        dropActual = 0;
        lastMoveWasRotate = false;
        if(!currentTetromino.move(SPAWN_X,SPAWN_Y, board)){
            gameOver = true;
        }
    }

    public boolean gameIsOver(){
        return gameOver;
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
        held = false;
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
        if(PLAYABLE_Y <= line && line < HIGH_Y) {
            for (int x = LOW_X; x <= HIGH_X; x++) {
                board[x][line + 1] = board[x][line];
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
        return score;
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
