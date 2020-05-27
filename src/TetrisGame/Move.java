package TetrisGame;

import TetrisGame.Tetromino.Orientation;

/**
 * Represents a single frame move for the tetris game
 */
public class Move {
    /**
     * Represents the direction of a translation or rotaton
     */
    public enum Direction{
        LEFT, RIGHT, NONE;

        /**
         * the sign asociated with the direction
         * @return LEFT = -1, RIGHT = 1, NONE = 0
         */
        public int getSign(){
            switch (this){
                case LEFT: return -1;
                case RIGHT: return 1;
                default: return 0;
            }
        }
    }

    public Direction translation = Direction.NONE;
    public Direction rotation = Direction.NONE;
    public boolean softDrop;
    public boolean hardDrop;
    public boolean hold;
}
