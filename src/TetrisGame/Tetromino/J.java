package TetrisGame.Tetromino;

import TetrisGame.Move;
import TetrisGame.Square;

public class J extends Base {
    @Override
    public boolean rotate(Move.Direction direction, Square[][] board) {
        return false;
    }

    @Override
    public boolean canRotate(Move.Direction direction, Square[][] board) {
        return false;
    }
}
