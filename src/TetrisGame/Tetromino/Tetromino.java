package TetrisGame.Tetromino;

import TetrisGame.Move;
import TetrisGame.Square;
import javafx.scene.canvas.GraphicsContext;

public abstract class Tetromino {
    private double x;
    private double y;

    public boolean translate(Move.Direction direction, Square[][] board){
        return false;
    }

    public boolean canTranslate(Move.Direction direction, Square[][] board){
        return false;
    }

    public boolean fall(Square[][] board){
        return false;
    }

    public boolean canFall(Square[][] board){
        return false;
    }

    public void lock(Square[][] board){

    }

    public abstract boolean rotate(Move.Direction direction, Square[][] board);

    public abstract boolean canRotate(Move.Direction direction, Square[][] board);

    public void drawGhostRelative(double boardGx, double boardGy, double squareSize, GraphicsContext gc, Square[][] board){

    }

    public void drawAbsolute(double gx, double gy, double squareSize, GraphicsContext gc){

    }

    public void drawRelative(double boardGx, double boardGy, double squareSize, GraphicsContext gc){

    }
}
