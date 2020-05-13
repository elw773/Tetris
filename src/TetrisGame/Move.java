package TetrisGame;

public class Move {
    public enum Direction{
        LEFT, RIGHT, NONE;

        public int getSign(){
            switch (this){
                case LEFT: return -1;
                case RIGHT: return 1;
                default: return 0;
            }
        }
    }

    public Direction translation;
    public Direction rotation;
    public boolean softDrop;
    public boolean hardDrop;
    public boolean hold;
}
