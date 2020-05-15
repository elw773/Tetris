package TetrisGame.Tetromino;

import TetrisGame.Move;
import TetrisGame.Mino;
import javafx.scene.canvas.GraphicsContext;

import java.util.EnumMap;

public class Tetromino {
    public static final int NUM_MINOS = 4;

    private int x;
    private int y;
    private MovingSquare[] squares;
    private Orientation orientation;
    private Mino minoType;

    private EnumMap<Orientation, int[]> xOffsets;
    private EnumMap<Orientation, int[]> yOffsets;

    public Tetromino(EnumMap<Orientation, int[]> xOffsets, EnumMap<Orientation, int[]> yOffsets, Mino minoType){
        this.xOffsets = xOffsets;
        this.yOffsets = yOffsets;

        this.minoType = minoType;

        orientation = Orientation.NORTH;
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


    private int getMinoX(int n){
        return xOffsets.get(orientation)[n] + this.x;
    }

    private int getMinoY(int n){
        return yOffsets.get(orientation)[n] + this.y;
    }

    public boolean rotate(Move.Direction direction, Mino[][] board){
        return false;
    }

    public boolean canRotate(Move.Direction direction, Mino[][] board){
        return false;
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
            double x = gx + (xOffsets.get(orientation)[i] * squareSize);
            double y = gy + (yOffsets.get(orientation)[i] * squareSize);
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
        drawAbsolute(boardGx + (x*squareSize), boardGy + (y*squareSize), squareSize, gc);
    }
}
