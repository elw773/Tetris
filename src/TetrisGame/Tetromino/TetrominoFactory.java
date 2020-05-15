package TetrisGame.Tetromino;

import TetrisGame.Mino;

public class TetrominoFactory {
    public static Tetromino newTetromino(Mino type){
        switch (type){
            case I: return new I();
            case J: return new J();
            case L: return new L();
            case O: return new O();
            case S: return new S();
            case T: return new T();
            default: return new Z();
        }
    }
}
