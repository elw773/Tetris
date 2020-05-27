package TetrisGame.Tetromino;

import TetrisGame.Board;
import TetrisGame.Game;
import TetrisGame.Move;
import TetrisGame.Mino;
import javafx.scene.canvas.GraphicsContext;

import java.util.EnumMap;
import java.util.function.BinaryOperator;

/**
 * Represents the tetromios for the tetris game
 */
public class Tetromino {
    public static final int NUM_MINOS = 4;
    public static final int[] WALL_KICK_VALUES = {0, 1, 2, -1, -2};
    public static final EnumMap<Mino, EnumMap<Orientation, int[]>> X_OFFSETS;
    public static final EnumMap<Mino, EnumMap<Orientation, int[]>> Y_OFFSETS;

    /**
     * returns the bitmap of the desired tetromino
     * @param tetrominoType the mino of the desired tetromino
     * @return the bitmap of the tetromino
     */
    private static boolean[][] getBitMap(Mino tetrominoType){
        switch (tetrominoType){
            case I: return new boolean[][]{
                    {false, false, false, false},
                    { true,  true,  true,  true},
                    {false, false, false, false},
                    {false, false, false, false}
            };
            case J: return new boolean[][]{
                    { true, false, false},
                    { true,  true,  true},
                    {false, false, false}
            };
            case L: return new boolean[][]{
                    {false, false,  true},
                    { true,  true,  true},
                    {false, false, false}
            };
            case O: return new boolean[][]{
                    {true, true},
                    {true, true}
            };
            case S: return new boolean[][]{
                    {false,  true,  true},
                    { true,  true, false},
                    {false, false, false}
            };
            case T: return new boolean[][]{
                    {false,  true, false},
                    { true,  true,  true},
                    {false, false, false}
            };
            case Z: return new boolean[][]{
                    { true,  true, false},
                    {false,  true,  true},
                    {false, false, false}
            };
        }
        return null;
    }

    /**
     * static initialization block initializes the X_OFFSETS and Y_OFFSETS variables and populates them with the
     * offsets
     */
    static{ //https://www.tutorialspoint.com/a-static-initialization-block-in-java
        X_OFFSETS = new EnumMap<>(Mino.class);
        Y_OFFSETS = new EnumMap<>(Mino.class);
        for(Mino type:Mino.values()){
            if(type != Mino.NONE) {
                boolean[][] bitmap = getBitMap(type);

                EnumMap<Orientation, int[]> xOffsets = new EnumMap<>(Orientation.class);
                EnumMap<Orientation, int[]> yOffsets = new EnumMap<>(Orientation.class);

                for (Orientation orientation : Orientation.values()) {
                    xOffsets.put(orientation, new int[4]);
                    yOffsets.put(orientation, new int[4]);
                }

                int i = 0;
                int S = bitmap.length - 1;
                for (int x = 0; x < bitmap.length; x++) {
                    for (int y = 0; y < bitmap.length; y++) {
                        if (bitmap[y][x]) { // x and y are inverted because of the way the bitmaps are hardcoded
                            //north
                            xOffsets.get(Orientation.NORTH)[i] = x;
                            yOffsets.get(Orientation.NORTH)[i] = y;
                            //east
                            xOffsets.get(Orientation.EAST)[i] = S-y;
                            yOffsets.get(Orientation.EAST)[i] = x;
                            //south
                            xOffsets.get(Orientation.SOUTH)[i] = S-x;
                            yOffsets.get(Orientation.SOUTH)[i] = S-y;
                            //west
                            xOffsets.get(Orientation.WEST)[i] = y;
                            yOffsets.get(Orientation.WEST)[i] = S-x;

                            i++;
                            /*
                            Ascending:  Average: 0.0994 	 gameAvg: 0.0056
                            Descending: Average: 0.1139 	 gameAvg: 0.0042
                             */
                        }
                    }
                    X_OFFSETS.put(type, xOffsets);
                    Y_OFFSETS.put(type, yOffsets);
                }
            }
        }
    }


    private int x;
    private int y;
    private Orientation orientation;
    private Mino minoType;

    public Tetromino(Mino minoType){
        this.minoType = minoType;

        orientation = Orientation.NORTH;
    }

    /**
     * Moves the tetromino to a given location
     * @param x the x to move to
     * @param y the y to move to
     * @param board the board to move on
     * @return false if any square of the destination location is occupied or out of bounds
     */
    public boolean move(int x, int y, Board board){
        if(canMove(x, y, board)){
            this.x = x;
            this.y = y;
            return true;
        }
        return false;
    }

    /**
     * Determines if the tetromino can be moved to a given location
     * @param x the x to move to
     * @param y the y to move to
     * @param board the board to move on
     * @return false if any square of the destination location is occupied or out of bounds
     */
    public boolean canMove(int x, int y, Board board){
        for (int i = 0; i < NUM_MINOS; i++) {

            if(!board.isOpen(x + getMinoXOffset(i), y + getMinoYOffset(i))){
                return false;
            }
        }
        return true;
    }


    /**
     * Moves the tetromino in the desired direction, if it can
     *
     * @param direction the desired direction
     * @param board the board to move on
     * @return true if succesful
     */
    public boolean translate(Move.Direction direction, Board board){
        if(canTranslate(direction, board)){
            this.x += direction.getSign();
            return true;
        }
        return false;
    }

    /**
     * Determines if the tetromino can be moved in the desired direction
     *
     * @param direction the desired direction
     * @param board the board to move on
     * @return true if it can translate
     */
    public boolean canTranslate(Move.Direction direction, Board board){
        for (int i = 0; i < NUM_MINOS; i++) {
            int minoX = getMinoX(i);
            int minoY = getMinoY(i);
            if(!board.isOpen(minoX + direction.getSign(), minoY)){
                return false;
            }
        }
        return true;
    }

    /**
     * Makes the tetromino fall down the board
     * @param board the board to fall down on
     * @return true if succesful
     */
    public boolean fall(Board board){
        if(canFall(board)){
            this.y ++;
            return true;
        }
        return false;
    }

    /**
     * Determines if the tetromino can fall on the given board
     * @param board the board to fall down on
     * @return true if it can
     */
    public boolean canFall(Board board){
        for (int i = 0; i < NUM_MINOS; i++) {
            int minoX = getMinoX(i);
            int minoY = getMinoY(i);
            if(!board.isOpen(minoX, minoY + 1)){
                return false;
            }
        }
        return true;
    }

    /**
     * Locks the tetromino onto the given board
     * @param board the board to lock onto
     * @return false if the piece locked comletely above the visible portion of the screen
     */
    public boolean lock(Board board){
        boolean valid = false;
        for (int i = 0; i < NUM_MINOS; i++) {
            int minoX = getMinoX(i);
            int minoY = getMinoY(i);
            board.set(minoX, minoY, minoType);
            if(minoY >= board.FIRST_VISIBLE_Y){
                valid = true;
            }
        }
        return valid;
    }

    /**
     * FOR TESTING ONLY
     * unlocks the tetromino from the given board
     * @param board the board to unlock from
     */
    public void unlock(Board board){
        for (int i = 0; i < NUM_MINOS; i++) {
            int minoX = getMinoX(i);
            int minoY = getMinoY(i);
            board.set(minoX, minoY, Mino.NONE);
        }
    }

    public int getHeight(){
        return getBitMap(minoType).length;
    }

    public int getY(){
        return y;
    }

    public int getX(){
        return x;
    }


    /**
     * Gets the x of the nth mino
     * @param n the mino (0-3)
     * @return the x coordinate
     */
    private int getMinoX(int n){
        return getMinoXOffset(n) + this.x;
    }

    /**
     * Gets the y of the nth mino
     * @param n the mino (0-3)
     * @return the y coordinate
     */
    private int getMinoY(int n){
        return getMinoYOffset(n) + this.y;
    }

    /**
     * Gets the x offset of the nth mino
     * @param n the mino (0-3)
     * @return the x offset
     */
    private int getMinoXOffset(int n){
        return X_OFFSETS.get(minoType).get(orientation)[n];
    }

    /**
     * Gets the y offset of the nth mino
     * @param n the mino (0-3)
     * @return the y offset
     */
    private int getMinoYOffset(int n){
        return Y_OFFSETS.get(minoType).get(orientation)[n];
    }

    public void setOrientation(Orientation orientation){
        this.orientation = orientation;
    }

    public Mino getMinoType(){
        return minoType;
    }

    /**
     * Rotates the tetromino in a given direction, with wall kicks
     * @param direction the direction to rotate in (right = cw, left = ccw)
     * @param board the board to rotate on
     * @return false if the rotation cannot be accomplished, even with wall kicks
     */
    public boolean rotate(Move.Direction direction, Board board){ //https://tetris.fandom.com/wiki/SRS
        if(minoType == Mino.O ||  direction == Move.Direction.NONE){ // O does not rotate
            return true;
        }

        for (int yKick:WALL_KICK_VALUES) {
            for(int xKick:WALL_KICK_VALUES){
                if(canRotate(orientation.rotate(direction), board, xKick, yKick)){
                    orientation = orientation.rotate(direction);
                    x += xKick;
                    y += yKick;
                    return true;
                } else {
                    //System.out.println(minoType + " x: " + xKick + " y: " + yKick + " failed " + (x-Game.LOW_X) + " " + (y-Game.PLAYABLE_Y) + " " + orientation);
                }
            }
        }
        return false;

    }


    /**
     * Determines if the tetromino can be moved to a given location in a given orientation
     * @param x the x to move to
     * @param y the y to move to
     * @param orientation the orientation of the tetromino
     * @param board the board to move on
     * @return false if any square of the destination location is occupied or out of bounds
     */
    public boolean canMove(int x, int y, Orientation orientation, Board board){
        for (int i = 0; i < NUM_MINOS; i++) {
            int newX = x + X_OFFSETS.get(minoType).get(orientation)[i];
            int newY = y + Y_OFFSETS.get(minoType).get(orientation)[i];
            if(!board.isOpen(newX, newY)){
                return false;
            }
        }
        return true;
    }


    /**
     * Determines if the tetromino can be ata a given orientation with the given wall kicks
     * @param orientation the orientation
     * @param board the board the tetromino is on
     * @param xKick the horisontal wall kick
     * @param yKick the vertical wall kick
     * @return true if all squares in the destination are empty
     */
    private boolean canRotate(Orientation orientation, Board board, int xKick, int yKick){
        if(minoType == Mino.O){
            return true;
        }

        return canMove(x + xKick, y + yKick, orientation, board);
    }

    public Orientation getOrientation(){
        return orientation;
    }

    /**
     * Draws the ghost of the tetromino relative to a board
     *
     * the ghost shows where the tetromino will end up if it falls straight down
     *
     * @param boardGx the board's x
     * @param boardGy the board's y
     * @param squareSize the width of a square on the board
     * @param gc the graphics context to draw to
     * @param board the board
     */
    public void drawGhostRelative(double boardGx, double boardGy, double squareSize, GraphicsContext gc, Board board){
        int fallDist = 0;
        while(canMove(x, y+fallDist, board)){
            fallDist ++;
        }
        fallDist --; // we are one farther than we can go
        for (int i = 0; i < NUM_MINOS; i++) {
            double x = board.toGraphicX(boardGx, (getMinoXOffset(i) + this.x), squareSize);
            double y = board.toGraphicY(boardGy, (getMinoYOffset(i) + this.y + fallDist), squareSize);
            if((getMinoYOffset(i) + this.y + fallDist) >= board.FIRST_VISIBLE_Y) {
                Mino.drawGhost(x, y, squareSize, minoType, gc);
            }
        }
    }


    /**
     * Draws the square at the given coordinates
     * @param gx
     * @param gy
     * @param squareSize
     * @param gc
     */
    public void drawAbsolute(double gx, double gy, double squareSize, GraphicsContext gc){
        for (int i = 0; i < NUM_MINOS; i++) {
            double x = gx + (getMinoXOffset(i) * squareSize);
            double y = gy + (getMinoYOffset(i) * squareSize);
            Mino.draw(x, y, squareSize, minoType, gc);
        }
    }


    /**
     * Draws the square on a board with given coordinates
     * @param boardGx
     * @param boardGy
     * @param squareSize
     * @param gc
     */
    public void drawRelative(double boardGx, double boardGy, double squareSize, Board board, GraphicsContext gc){
            for (int i = 0; i < NUM_MINOS; i++) {
                double x = board.toGraphicX(boardGx, getMinoX(i), squareSize);
                double y = board.toGraphicY(boardGy, getMinoY(i), squareSize);

                if(getMinoY(i) >= board.FIRST_VISIBLE_Y) {
                    Mino.draw(x, y, squareSize, minoType, gc);
                }
            }

    }



}
