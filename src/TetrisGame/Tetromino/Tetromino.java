package TetrisGame.Tetromino;

import TetrisGame.Move;
import TetrisGame.Mino;
import javafx.scene.canvas.GraphicsContext;

import java.util.EnumMap;

public class Tetromino {
    private int x;
    private int y;
    private MovingSquare[] squares;

    private EnumMap<Orientation, int[]> xOffsets;
    private EnumMap<Orientation, int[]> yOffsets;

    public Tetromino(EnumMap<Orientation, int[]> xOffsets, EnumMap<Orientation, int[]> yOffsets){
        this.xOffsets = xOffsets;
        this.yOffsets = yOffsets;
    }

    /**
     * Moves the tetromino in the desired direction, if it can
     *
     * @param direction the desired direction
     * @param board the board to move on
     * @return true if succesful
     */
    public boolean translate(Move.Direction direction, Mino[][] board){
        for (MovingSquare square:squares) {
            if(!square.canTranslate(direction, board)){
                return false;
            }
        }
        for (MovingSquare square:squares) {
            square.translate(direction, board);
        }
        return true;
    }

    /**
     * Determines if the tetromino can be moved in the desired direction
     *
     * @param direction the desired direction
     * @param board the board to move on
     * @return true if it can translate
     */
    public boolean canTranslate(Move.Direction direction, Mino[][] board){
        for (MovingSquare square:squares) {
            if(!square.canTranslate(direction, board)){
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
        for (MovingSquare square:squares) {
            if(!square.canFall(board)){
                return false;
            }
        }
        for (MovingSquare square:squares) {
            square.fall(board);
        }
        return true;
    }

    /**
     * Determines if the tetromino can fall on the given board
     * @param board the board to fall down on
     * @return true if it can
     */
    public boolean canFall(Mino[][] board){
        for (MovingSquare square:squares) {
            if(!square.canFall(board)){
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
        for (MovingSquare square:squares) {
            square.lock(board);
        }
    }

    public abstract boolean rotate(Move.Direction direction, Mino[][] board);

    public abstract boolean canRotate(Move.Direction direction, Mino[][] board);

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
        for (MovingSquare square:squares) {
            square.drawAbsolute(gx, gy, squareSize, gc);
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
        for (MovingSquare square:squares) {
            square.drawRelative(boardGx, boardGy, squareSize, gc);
        }
    }
}
