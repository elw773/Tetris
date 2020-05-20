package TetrisGame.Tetromino;

import TetrisGame.Move;
import TetrisGame.Mino;
import javafx.scene.canvas.GraphicsContext;

import java.util.EnumMap;

public class Tetromino {
    public static final int NUM_MINOS = 4;
    public static final int[] WALL_KICK_VALUES = {0, 1, -1, 2, -2};
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
            if(board[x + getMinoXOffset(i)][y + getMinoYOffset(i)] != Mino.NONE){
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
            if(board[minoX + direction.getSign()][minoY] != Mino.NONE){
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
            if(board[minoX][minoY + 1] != Mino.NONE){
                return false;
            }
        }
        return true;
    }

    /**
     * Locks the tetromino onto the given board
     * @param board the board to lock onto
     */
    public void lock(Mino[][] board){
        for (int i = 0; i < NUM_MINOS; i++) {
            int minoX = getMinoX(i);
            int minoY = getMinoY(i);
            board[minoX][minoY] = this.minoType;
        }
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


    public boolean rotate(Move.Direction direction, Mino[][] board){ //https://tetris.fandom.com/wiki/SRS
        if(minoType == Mino.O || direction == Move.Direction.NONE) {
            return true;
        }

        for (int xKick:WALL_KICK_VALUES) {
            for(int yKick:WALL_KICK_VALUES){
                if(canRotate(orientation.rotate(direction), board, xKick, yKick)){
                    orientation = orientation.rotate(direction);
                    x += xKick;
                    y += yKick;
                    return true;
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
            if(board[newX][newY] != Mino.NONE){
                return false;
            }
        }
        return true;
    }

    public void drawGhostRelative(double boardGx, double boardGy, double squareSize, GraphicsContext gc, Mino[][] board){

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

            drawAbsolute(boardGx + ((x - 1) * squareSize), boardGy + ((y - 5) * squareSize), squareSize, gc);

    }
}
