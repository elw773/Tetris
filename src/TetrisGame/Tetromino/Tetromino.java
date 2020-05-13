package TetrisGame.Tetromino;

import TetrisGame.Move;
import TetrisGame.Square;
import javafx.scene.canvas.GraphicsContext;

public abstract class Tetromino {
    private double x;
    private double y;
    private MovingSquare[] squares;

    /**
     * Moves the tetromino in the desired direction, if it can
     *
     * @param direction the desired direction
     * @param board the board to move on
     * @return true if succesful
     */
    public boolean translate(Move.Direction direction, Square[][] board){
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
    public boolean canTranslate(Move.Direction direction, Square[][] board){
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
    public boolean fall(Square[][] board){
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
    public boolean canFall(Square[][] board){
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
    public void lock(Square[][] board){
        for (MovingSquare square:squares) {
            square.lock(board);
        }
    }

    public abstract boolean rotate(Move.Direction direction, Square[][] board);

    public abstract boolean canRotate(Move.Direction direction, Square[][] board);

    public void drawGhostRelative(double boardGx, double boardGy, double squareSize, GraphicsContext gc, Square[][] board){

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
