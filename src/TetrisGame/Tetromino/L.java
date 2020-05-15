package TetrisGame.Tetromino;

import TetrisGame.Move;
import TetrisGame.Mino;

public class L extends Tetromino {
    @Override
    public boolean rotate(Move.Direction direction, Mino[][] board) {
        return false;
    }

    @Override
    public boolean canRotate(Move.Direction direction, Mino[][] board) {
        return false;
    }
}
