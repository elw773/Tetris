package TetrisGame;

import Graphics.TetrisMenu;
import TetrisGame.Tetromino.Orientation;
import TetrisGame.Tetromino.Tetromino;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.EnumMap;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Represents a tetris game
 */
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

    // these maps store the x and y coordinates of the square that is to the left of the pointer of a T tetromino
    private static EnumMap<Orientation, Integer> leftOfPointerX;
    private static EnumMap<Orientation, Integer> leftOfPointerY;
    static { // setup the maps to recognize t spins
        leftOfPointerX = new EnumMap<Orientation, Integer>(Orientation.class);
        leftOfPointerY = new EnumMap<Orientation, Integer>(Orientation.class);

        leftOfPointerX.put(Orientation.NORTH, 0);
        leftOfPointerY.put(Orientation.NORTH, 0);

        leftOfPointerX.put(Orientation.EAST, 2);
        leftOfPointerY.put(Orientation.EAST, 0);

        leftOfPointerX.put(Orientation.SOUTH, 2);
        leftOfPointerY.put(Orientation.SOUTH, 2);

        leftOfPointerX.put(Orientation.WEST, 0);
        leftOfPointerY.put(Orientation.WEST, 2);
    }

    public Game(){
        reset(1); // reset initialises the variables
    }

    /**
     * Increments the level if it is time to
     * pre- totalLinesCleared is equal to the number of lines cleared this game
     * post- increments level if the number of lines cleared is greater than 10*level
     */
    private void doLevelUp(){
        if(totalClearedLines > (level) * 10){
            level ++;
        }
    }

    /**
     * pre- level equals the current level
     * @return the drop rate of a tetromino for the given level (cells per frame)
     */
    private double getDropRate(){
        int framesPerCell; // these are the values according to tetris guidelines
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

    /**
     * Updates the game by one frame
     * @param move the moves of the player (or ai)
     */
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

    /**
     * Does the default scoring of soft drop and hard drop that should take place every frame
     * pre- dropCounter is equal to the number of squares that the current tetromino has falled this frame
     * @param move the move of the current frame
     */
    private void defaultScore(Move move){
        if(move.hardDrop){
            score += (dropCounter * 2);
        } else if (move.softDrop){
            score += dropCounter;
        }
        dropCounter = 0;
    }

    /**
     * Determines if a t spin or mini t spin took place
     * pre- lastMoveWasRotate is true if the tetromino was rotated and has not fallen or been translated since
     *      leftOfPointerX and Y are initialized
     *      the tetromino was locked this frame
     *      lines have not been cleared yet
     * post- tSpin will be true if a full t spin occured
     *       miniTSpin will be true if a mini t spin occured
     */
    private void checkTSpins(){

        if(lastMoveWasRotate && currentTetromino.getMinoType() == Mino.T){

            int x = currentTetromino.getX();
            int y = currentTetromino.getY();

            Orientation orientation = currentTetromino.getOrientation();

            int pointerCorners = 0; // the number of occupied squares next to the pointer

            if(!board.isOpen(x+ leftOfPointerX.get(orientation), y+ leftOfPointerY.get(orientation))){ // left of pointer
                pointerCorners += 1;
            }
            if(!board.isOpen(x+ leftOfPointerX.get(orientation.cw()), y+ leftOfPointerY.get(orientation.cw()))){ // right of pointer
                pointerCorners += 1;
            }

            int otherCorners = 0; // the number of occupied corners on the side opposite the pointer

            if(!board.isOpen(x+ leftOfPointerX.get(orientation.ccw()), y+ leftOfPointerY.get(orientation.ccw()))){
                otherCorners += 1;
            }
            if(!board.isOpen(x+ leftOfPointerX.get(orientation.ccw().ccw()), y+ leftOfPointerY.get(orientation.ccw().ccw()))){
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

    public int getLevel() {
        return level;
    }

    public int getLines(){
        return totalClearedLines;
    }

    /**
     * Calculates the new score after a piece has been locked
     * pre- t spins have been checked
     *      lines have been cleared
     *      cleared lines represents the number of lines cleared this frame
     * post- score will be updated
     */
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


    /**
     * Does the given moves for one frame
     * post- lastMoveWasRotate and lockCounter are updated
     * @param move the moves to do
     */
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

    /**
     * Makes the current tetromino fall if it can
     * post- if it does fall, lock counter is reset, drop counter increments, lastMoveWasRotate is set to false
     * @return true if it can fall
     */
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

    /**
     * Clears lines that are full
     * pre- a tetromino has been locked this frame (but a new tetromino is not set)
     * post- full lines will be removed from the board and lines above will fill the gap
     */
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

    /**
     * Makes the current tetromino fall if it needs to this frame
     *
     * The tetromino falls based on the incrementing dropGoal double, and falls until the dropActual int is equal to
     * the floor of the dropGoal
     *
     * pre- dropAcual represents the number of squares the current tetromino has fallen in total
     *
     * @return true if the tetromino fall
     */
    private boolean doFall(){
        if(currentTetromino.canFall(board)){
            dropGoal += getDropRate();
            while(dropActual < (int) dropGoal && fall()){
                dropActual ++;
            }
        }
        return currentTetromino.canFall(board);
    }

    /**
     * Locks the current tetromino if it should this frame
     *
     * Locks the tetromino if it has not moven for 30 frames
     *
     * post- lines are cleared, the new score is calculated, a new tetromino is spawned
     *       if the tetromino is locked in the buffer, the game is over
     *
     * @return true if the tetromino has locked
     */
    private boolean doLock(){
        if(lockCounter > LOCK_DELAY){
            gameOver = !currentTetromino.lock(board);
            lockCounter = 0;
            checkTSpins();
            clearLines();
            lockScore();

            nextTetromino();
            return true;
        } else {
            lockCounter ++;
            return false;
        }
    }

    /**
     * Spawns the next tetromino
     * pre- current tetromino has been locked
     * post- dropGoal, dropActual, held, lastMoveWasRotate are all reset
     *       if it cannot be spawned, the game is over
     */
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

    /**
     * Resets the current game
     * @param level the level to start the game at
     */
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

    /***
     * Determines if the line is full
     * @param y the line to check
     * @return true if there are no open squares in the line
     */
    public boolean isLineFull(int y){
        for (int x = 0; x < board.WIDTH; x++) {
            if(board.isOpen(x, y)){
                return false;
            }
        }
        return true;
    }

    /**
     * Recursively shifts down all lines above to clear this one
     * @param line the line to clear
     */
    public void clearLine(int line){
        lowerLine(line-1);
    }

    /**
     * Recursively shifts down this line and all lines above it
     * @param line the line to shift down
     */
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



    public static void printBoard(Board board){
        for (int i = 0; i < board.WIDTH+2; i++) {
            System.out.print("#");
        }
        for (int y = 0; y < board.HEIGHT; y++) {
            System.out.print("\n#");
            for (int x = 0; x < board.WIDTH; x++) {
                if(board.get(x,y) == Mino.NONE){
                    System.out.print(" ");
                } else {
                    System.out.print(board.get(x,y));
                }
            }
            System.out.print("#");
        }
        System.out.print("\n");
        for (int i = 0; i < board.WIDTH+2; i++) {
            System.out.print("#");
        }
        System.out.println();
    }




    public static void testTSpin(){
        Game game = new Game();
        Move move = new Move();

        int bottom = game.getBoard().HEIGHT-1;

        for (int x = 0; x < game.getBoard().WIDTH; x++) {
            for (int y = bottom-2; y <= bottom; y++) {
                game.getBoard().set(x,y,Mino.O);
            }
        }
        // clear out area
        game.getBoard().set(3,bottom-2,Mino.NONE);
        game.getBoard().set(4,bottom-2,Mino.NONE);
        game.getBoard().set(3,bottom-1,Mino.NONE);
        game.getBoard().set(3,bottom,Mino.NONE);
        game.getBoard().set(4,bottom-1,Mino.NONE);
        game.getBoard().set(2,bottom-1,Mino.NONE);

        //setup t
        game.currentTetromino = new Tetromino(Mino.T);

        game.currentTetromino.setOrientation(Orientation.EAST);
        game.currentTetromino.move(2, bottom-2,game.getBoard());

        game.currentTetromino.lock(game.getBoard());
        printBoard(game.getBoard());
        game.currentTetromino.unlock(game.getBoard());

        move.rotation = Move.Direction.RIGHT; // make it spin
        game.update(move);

        move.rotation = Move.Direction.NONE;
        for (int i = 0; i <= LOCK_DELAY; i++) { // make it lock
            game.update(move);
        }

        printBoard(game.getBoard());


    }

    public static void testMiniTSpin(){
        Game game = new Game();
        Move move = new Move();

        int bottom = game.getBoard().HEIGHT-1;

        for (int x = 0; x < game.getBoard().WIDTH; x++) {
            for (int y = bottom-2; y <= bottom; y++) {
                game.getBoard().set(x,y,Mino.O);
            }
        }
        // clear out area
        game.getBoard().set(3,bottom-2,Mino.NONE);
        game.getBoard().set(4,bottom-2,Mino.NONE);
        game.getBoard().set(3,bottom-1,Mino.NONE);
        game.getBoard().set(3,bottom,Mino.NONE);
        game.getBoard().set(4,bottom-1,Mino.NONE);
        game.getBoard().set(2,bottom-1,Mino.NONE);

        //setup t
        game.currentTetromino = new Tetromino(Mino.T);

        game.currentTetromino.setOrientation(Orientation.EAST);
        game.currentTetromino.move(2, bottom-2,game.getBoard());

        game.currentTetromino.lock(game.getBoard());
        printBoard(game.getBoard());
        game.currentTetromino.unlock(game.getBoard());

        move.rotation = Move.Direction.LEFT; // make it spin
        game.update(move);

        move.rotation = Move.Direction.NONE;
        for (int i = 0; i <= LOCK_DELAY; i++) { // make it lock
            game.update(move);
        }

        printBoard(game.getBoard());


    }

    private static void tryRotation(int x, int y, Tetromino tetromino, Move.Direction direction, Board board){
        System.out.println("Trying: " + x + " " + y + " " + direction);

        System.out.println(tetromino.move(x,y,board)); // bottom
        tetromino.lock(board);
        printBoard(board);
        tetromino.unlock(board);

        tetromino.rotate(direction, board);

        tetromino.lock(board);
        printBoard(board);
        tetromino.unlock(board);
    }

    public static void testTetrominos(){
        Board board = new Board(7,7,0);
        //wallKicks
        Tetromino tetromino = new Tetromino(Mino.I);
        tryRotation(1,5,tetromino, Move.Direction.RIGHT,board); // upwards
        tryRotation(-2,2,tetromino, Move.Direction.RIGHT,board); // right
        tryRotation(2,-2,tetromino, Move.Direction.RIGHT,board); // down
        tryRotation(4,2,tetromino, Move.Direction.RIGHT,board); // left

        //movement

        System.out.println("Falling");
        System.out.println(tetromino.move(0,4,board)); // one above bottom
        tetromino.lock(board);
        printBoard(board);
        tetromino.unlock(board);

        System.out.println(tetromino.fall(board)); // true
        tetromino.lock(board);
        printBoard(board);
        tetromino.unlock(board);
        System.out.println(tetromino.fall(board)); // false
        tetromino.lock(board);
        printBoard(board);
        tetromino.unlock(board);

        System.out.println("Translating");
        System.out.println("Falling");
        System.out.println(tetromino.move(2,4,board)); // one above bottom
        tetromino.lock(board);
        printBoard(board);
        tetromino.unlock(board);

        System.out.println(tetromino.translate(Move.Direction.LEFT, board)); // true
        tetromino.lock(board);
        printBoard(board);
        tetromino.unlock(board);
        System.out.println(tetromino.translate(Move.Direction.LEFT, board)); // false
        tetromino.lock(board);
        printBoard(board);
        tetromino.unlock(board);
        tetromino.lock(board);
        printBoard(board);
        tetromino.unlock(board);

        System.out.println(tetromino.fall(board)); // true
        tetromino.lock(board);
        printBoard(board);
        tetromino.unlock(board);
        System.out.println(tetromino.fall(board)); // false
        tetromino.lock(board);
        printBoard(board);
        tetromino.unlock(board);
    }


    public static void main(String[] args) {
        //testTSpin();
        testMiniTSpin();
        //testTetrominos();
    }
}
