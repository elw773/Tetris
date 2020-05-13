package TetrisGame.Tetromino;

import TetrisGame.Move;
import TetrisGame.Square;
import javafx.scene.canvas.GraphicsContext;

/**
 * Represents a square in a moving tetromino
 */
public class MovingSquare {
    private int x;
    private int y;
    private Square type;

    public MovingSquare(Square type){
        this.type = type;
    }

    /**
     * Moves the square in the desired direction, if possible, on the given board
     *
     * @param direction the desired direction
     * @param board the board moving on
     * @return if the tetromino can translate
     */
    public boolean translate(Move.Direction direction, Square[][] board){
        if(canTranslate(direction, board)){
            x += direction.getSign();
            return true;
        }
        return false;
    }

    /**
     * Determines whether the square can be translated in the desired direction on the given board
     *
     * @param direction the desired direction
     * @param board the board moving on
     * @return if the tetromino can translate
     */
    public boolean canTranslate(Move.Direction direction, Square[][] board){
        return (board[x+direction.getSign()][y] == Square.NONE);
    }

    /**
     * Makes the piece move one down, if it can
     *
     * @param board the board for the square to move in
     * @return true if succesful
     */
    public boolean fall(Square[][] board){
        if(canFall(board)){
            y++;
            return true;
        }
        return false;
    }

    /**
     * @param board the board the square is falling on
     * @return true if it can move one down
     */
    public boolean canFall(Square[][] board){
        return board[x][y+1] == Square.NONE;
    }

    /**
     * Locks this square to the given board
     * @param board the board to lock to
     */
    public void lock(Square[][] board){
        board[x][y] = this.type;
    }

    public boolean rotate(Move.Direction direction, Square[][] board, double centreX, double centreY){
        return false;
    }

    public boolean canRotate(Move.Direction direction, Square[][] board, double centreX, double centreY){
        return false;
    }

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
        Square.draw(gx, gy, squareSize, type, gc);
    }


    /**
     * Draws the square on a board with given coordinates
     * @param boardGx
     * @param boardGy
     * @param squareSize
     * @param gc
     */
    public void drawRelative(double boardGx, double boardGy, double squareSize, GraphicsContext gc){
        Square.draw(boardGx + (x*squareSize), boardGy + (y*squareSize), squareSize, type, gc);
    }
}
