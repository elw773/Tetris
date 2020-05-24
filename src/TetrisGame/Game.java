package TetrisGame;

import TetrisGame.Tetromino.Orientation;
import TetrisGame.Tetromino.Tetromino;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.EnumMap;
import java.util.concurrent.ArrayBlockingQueue;

public class Game {
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;
    public static final int BOARD_BUFFER = 6;

    private static final int LOCK_DELAY = 30;
    private int lockCounter;
    private double dropGoal = 0;
    private int dropActual = 0;
    private int dropCounter;
    private Board board;
    private int score = 0;
    private int level = 1;
    private Tetromino hold;
    private Tetromino currentTetromino;
    private ArrayBlockingQueue<Tetromino> next;
    private boolean gameOver;
    private boolean held;
    private int clearedLines;
    private int totalClearedLines;
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
        reset(1);
    }

    private void doLevelUp(){
        if(totalClearedLines > (level) * 10){
            level ++;
        }
    }

    private double getDropRate(){
        int framesPerCell;
        if(level == 0) {
            framesPerCell = 48;
        } else if(level == 1){
            framesPerCell = 43;
        } else  if(level == 2){
            framesPerCell = 38;
        } else  if(level == 3){
            framesPerCell = 33;
        } else  if(level == 4){
            framesPerCell = 28;
        } else  if(level == 5){
            framesPerCell = 23;
        } else  if(level == 6){
            framesPerCell = 18;
        } else  if(level == 7){
            framesPerCell = 13;
        } else  if(level == 8){
            framesPerCell = 8;
        } else  if(level == 9){
            framesPerCell = 6;
        } else  if(level <= 12){
            framesPerCell = 5;
        } else  if(level <= 15){
            framesPerCell = 4;
        } else  if(level <= 18){
            framesPerCell = 3;
        } else  if(level <= 28){
            framesPerCell = 2;
        } else {
            framesPerCell = 1;
        }
        return 1.0/framesPerCell;
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

            if(!board.isOpen(x+tSpinCornerX.get(orientation), y+tSpinCornerY.get(orientation))){
                pointerCorners += 1;
            }
            if(!board.isOpen(x+tSpinCornerX.get(orientation.cw()), y+tSpinCornerY.get(orientation.cw()))){
                pointerCorners += 1;
            }

            int otherCorners = 0;

            if(!board.isOpen(x+tSpinCornerX.get(orientation.ccw()), y+tSpinCornerY.get(orientation.ccw()))){
                otherCorners += 1;
            }
            if(!board.isOpen(x+tSpinCornerX.get(orientation.ccw().ccw()), y+tSpinCornerY.get(orientation.ccw().ccw()))){
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

        if(clearedLines == 0){
            combo = false;
            comboCount = 0;
        } else {
            comboCount += clearedLines;
        }

        int newPoints = 0;

        if(tSpin){
            switch (clearedLines){
                case 0: newPoints += 400; System.out.println("T-spin"); break;
                case 1: newPoints += 800; System.out.println("T-spin Single"); break;
                case 2: newPoints += 1200; System.out.println("T-spin Double"); break;
                case 3: newPoints += 1600; System.out.println("T-spin Triple"); break;
            }
        } else if(miniTSpin){
            switch (clearedLines){
                case 0: newPoints += 100; System.out.println("Mini T-spin"); break;
                case 1: newPoints += 200; System.out.println("Mini T-spin Single"); break;
                case 2: newPoints += 400; System.out.println("Mini T-spin Double"); break;
            }
        } else {
            switch (clearedLines){
                case 1: newPoints += 100; break;
                case 2: newPoints += 300; break;
                case 3: newPoints += 500; break;
                case 4: newPoints += 800; System.out.println("Tetris"); break;
            }
        }

        boolean difficult = ((tSpin || miniTSpin) && clearedLines > 0) || clearedLines == 4;

        if(backToBack && difficult){
            newPoints = newPoints * 3;
            newPoints = newPoints / 2;
            System.out.println("BACK 2 BACK");
        } else if(combo){
            score += comboCount * 50 * (level);
            System.out.println(comboCount + " Combo!");
        }
        score += newPoints * (level);
        System.out.println("Level: " + level + "\tScore: " + score + "\t Lines: " + totalClearedLines);
        doLevelUp();

        if(difficult){
            backToBack = true;
        } else if(clearedLines > 0){
            backToBack = false;
        }


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
            hold.move(board.SPAWN_X, board.SPAWN_Y, board);
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
        for (int y = currentTetromino.getY(); board.validY(y) && y < currentTetromino.getY() + currentTetromino.getHeight(); y++) {
            //System.out.println("checking line: " + y);
            if(isLineFull(y)){
                clearLine(y);
                clearedLines ++;
                totalClearedLines ++;
            }
        }
        //System.out.println("Line clearing took " + (System.currentTimeMillis() - start));
    }

    private boolean doFall(){
        if(currentTetromino.canFall(board)){
            dropGoal += getDropRate();
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
            checkTSpins();
            clearLines();
            lockScore();

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
        if(!currentTetromino.move(board.SPAWN_X,board.SPAWN_Y, board)){
            gameOver = true;
        }
    }

    public boolean gameIsOver(){
        return gameOver;
    }

    public void reset(int level) {
        // reset board
        board = new Board(BOARD_WIDTH, BOARD_HEIGHT, BOARD_BUFFER);

        //reset vars
        lockCounter = 0;
        dropGoal = 0;
        dropActual = 0;
        dropCounter = 0;
        score = 0;
        this.level = level;
        hold = null;
        gameOver = false;
        held = false;
        clearedLines = 0;
        totalClearedLines = 0;
        lastMoveWasRotate = false;
        tSpin = false;
        miniTSpin = false;
        backToBack = false;
        comboCount = 0;
        combo = false;

        // fill next
        next = new ArrayBlockingQueue<>(6);
        while(next.remainingCapacity() > 0){
            next.add(new Tetromino(Mino.getNextRandom()));
        }
        // start the game with a new tetromino
        nextTetromino();
    }

    public boolean isLineFull(int y){
        for (int x = 0; x < board.WIDTH; x++) {
            if(board.isOpen(x, y)){
                return false;
            }
        }
        return true;
    }

    public void clearLine(int line){
        lowerLine(line-1);
    }

    public void lowerLine(int line){
        if(board.validY(line)) {
            for (int x = 0; x < board.WIDTH; x++) {
                board.set(x, line+1, board.get(x,line));
            }
            lowerLine(line-1);
        }
    }

    public Tetromino getHold(){
        return hold;
    }

    public Board getBoard(){
        return board;
    }

    public Tetromino getCurrentTetromino(){
        return currentTetromino;
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
        for (int x = 0; x < board.WIDTH; x++) {
            for (int y = board.FIRST_VISIBLE_Y; y < board.HEIGHT; y++) {
                Mino.draw(board.toGraphicX(boardX, x, squareSize), board.toGraphicY(boardY, y, squareSize), squareSize, board.get(x, y), gc);
            }
        }
        currentTetromino.drawGhostRelative(boardX, boardY, squareSize, gc, board);

        currentTetromino.drawRelative(boardX, boardY, squareSize, board, gc);

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
