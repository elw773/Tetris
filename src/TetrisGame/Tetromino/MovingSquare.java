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
     * @param dx the horizontal movement
     * @param dy the vertical movement
     * @param board the board moving on
     * @return if the tetromino can translate
     */
    public boolean translate(int dx, int dy, Square[][] board){
        if(canTranslate(dx, dy, board)){
            x += dy;
            y += dx;
            return true;
        }
        return false;
    }

    /**
     * Determines whether the square can be translated in the desired direction on the given board
     *
     * @param dx the horizontal movement
     * @param dy the vertical movement
     * @param board the board moving on
     * @return if the tetromino can translate
     */
    public boolean canTranslate(int dx, int dy, Square[][] board){
        return (board[x+dx][y+dy] == Square.NONE);
    }

    /**
     * Locks this square to the given board
     * @param board the board to lock to
     */
    public void lock(Square[][] board){
        board[x][y] = this.type;
    }

    public int[][] rotate(Move.Direction direction, Square[][] board, double centreX, double centreY, int[][] wallKicks){
        return null;
    }

    /**
     * Draws the square at the given coordinates
     * @param gx
     * @param gy
     * @param squareSize
     * @param gc
     * @param ghost
     */
    public void drawAbsolute(double gx, double gy, double squareSize, GraphicsContext gc, boolean ghost){
        if(ghost){

        } else {
            Square.draw(gx, gy, squareSize, type, gc);
        }
    }


    /**
     * Draws the square on a board with given coordinates
     * @param boardGx
     * @param boardGy
     * @param squareSize
     * @param gc
     */
    public void drawRelative(double boardGx, double boardGy, double squareSize, GraphicsContext gc, boolean ghost){
        drawAbsolute(boardGx + (x*squareSize), boardGy + (y*squareSize), squareSize, gc, ghost);
    }
}
