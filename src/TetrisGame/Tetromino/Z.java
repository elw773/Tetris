package TetrisGame.Tetromino;

import TetrisGame.Move;
import TetrisGame.Square;

public class Z extends Tetromino {
    @Override
    public boolean rotate(Move.Direction direction, Square[][] board) {
        return false;
    }

    @Override
    public boolean canRotate(Move.Direction direction, Square[][] board) {
        return false;
    }
}
