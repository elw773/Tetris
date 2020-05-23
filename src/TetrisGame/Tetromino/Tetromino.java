package TetrisGame.Tetromino;

import TetrisGame.Game;
import TetrisGame.Move;
import TetrisGame.Mino;
import javafx.scene.canvas.GraphicsContext;

import java.util.EnumMap;

public class Tetromino {
    public static final int NUM_MINOS = 4;
    public static final int[] WALL_KICK_VALUES = {0, 1, 2, -1, -2};
    private static final EnumMap<Mino, EnumMap<Orientation, int[]>> X_OFFSETS;
    private static final EnumMap<Mino, EnumMap<Orientation, int[]>> Y_OFFSETS;


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
     *
     * @param x
     * @param y
     * @param board
     * @return
     */
    public boolean move(int x, int y, Mino[][] board){
        if(canMove(x, y, board)){
            this.x = x;
            this.y = y;
            return true;
        }
        return false;
    }

    /**
     *
     * @param x
     * @param y
     * @param board
     * @return
     */
    public boolean canMove(int x, int y, Mino[][] board){
        for (int i = 0; i < NUM_MINOS; i++) {

            if(notAvailable(x + getMinoXOffset(i), y + getMinoYOffset(i), board)){
                return false;
            }
        }
        return true;
    }

    private boolean notAvailable(int x, int y, Mino[][] board){
        return -1 >= x || x >= board.length || -1 >= y || y >= board[x].length || board[x][y] != Mino.NONE;
    }

    /**
     * Moves the tetromino in the desired direction, if it can
     *
     * @param direction the desired direction
     * @param board the board to move on
     * @return true if succesful
     */
    public boolean translate(Move.Direction direction, Mino[][] board){
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
    public boolean canTranslate(Move.Direction direction, Mino[][] board){
        for (int i = 0; i < NUM_MINOS; i++) {
            int minoX = getMinoX(i);
            int minoY = getMinoY(i);
            if(notAvailable(minoX + direction.getSign(), minoY, board)){
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
    public boolean fall(Mino[][] board){
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
    public boolean canFall(Mino[][] board){
        for (int i = 0; i < NUM_MINOS; i++) {
            int minoX = getMinoX(i);
            int minoY = getMinoY(i);
            if(notAvailable(minoX, minoY + 1, board)){
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
    public boolean lock(Mino[][] board){
        boolean valid = true;
        for (int i = 0; i < NUM_MINOS; i++) {
            int minoX = getMinoX(i);
            int minoY = getMinoY(i);
            board[minoX][minoY] = this.minoType;
            if(minoY < Game.PLAYABLE_Y){
                valid = false;
            }
        }
        return valid;
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

    public boolean rotate(Move.Direction direction, Mino[][] board){ //https://tetris.fandom.com/wiki/SRS
        if(minoType == Mino.O ||  direction == Move.Direction.NONE){
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

    public boolean canRotate(Move.Direction direction, Mino[][] board){
        return false;
    }

    private boolean canRotate(Orientation orientation, Mino[][] board, int xKick, int yKick){
        if(minoType == Mino.O){
            return true;
        }

        for (int i = 0; i < NUM_MINOS; i++) {
            int newX = this.x + xKick + X_OFFSETS.get(minoType).get(orientation)[i];
            int newY = this.y + yKick + Y_OFFSETS.get(minoType).get(orientation)[i];
            if(notAvailable(newX, newY, board)){
                return false;
            }
        }
        return true;
    }

    public Orientation getOrientation(){
        return orientation;
    }

    public void drawGhostRelative(double boardGx, double boardGy, double squareSize, GraphicsContext gc, Mino[][] board){
        int fallDist = 0;
        while(canMove(x, y+fallDist, board)){
            fallDist ++;
        }
        fallDist --; // we are one farther than we can go
        for (int i = 0; i < NUM_MINOS; i++) {
            double x = boardGx + ((getMinoXOffset(i) + this.x - Game.LOW_X) * squareSize);
            double y = boardGy + ((getMinoYOffset(i) + this.y + fallDist - Game.PLAYABLE_Y) * squareSize);
            Mino.drawGhost(x, y, squareSize, minoType, gc);
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
    public void drawRelative(double boardGx, double boardGy, double squareSize, GraphicsContext gc){

            drawAbsolute(boardGx + ((x - Game.LOW_X) * squareSize), boardGy + ((y - Game.PLAYABLE_Y) * squareSize), squareSize, gc);

    }
}
