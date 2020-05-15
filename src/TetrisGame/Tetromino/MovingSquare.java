package TetrisGame.Tetromino;

import TetrisGame.Move;
import TetrisGame.Mino;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 * Represents a square in a moving tetromino
 */
public class MovingSquare {
    private double dx;
    private double dy;
    private Mino type;

    public MovingSquare(Mino type){
        this.type = type;
    }


    /**
     * Determines whether the square can be translated in the desired direction on the given board
     *
     * @param dx the horizontal movement
     * @param dy the vertical movement
     * @param board the board moving on
     * @return if the tetromino can translate
     */
    public boolean canMove(double newCentreX, double newCentreY, Mino[][] board){
        return (board[Math.rou(newCentreX + dx)][y+dy] == Mino.NONE);
    }

    /**
     * Locks this square to the given board
     * @param board the board to lock to
     */
    public void lock(Mino[][] board){
        board[x][y] = this.type;
    }

    public void calcValidRotationKicks(ArrayList<int[]> wallKicks, Move.Direction direction, Mino[][] board, double centreX, double centreY){
        double dx = x-centreX;
        double dy = y-centreY;

        int newX =
        int newY = -1 * y+dx
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
            Mino.draw(gx, gy, squareSize, type, gc);
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
