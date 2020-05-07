package TetrisGame;

public class Move {
    public enum Direction{
        LEFT, RIGHT, NONE;

        public int getSign(Direction direction){
            return 0;
        }
    }

    public Direction translation;
    public Direction rotation;
    public boolean softDrop;
    public boolean hardDrop;
    public boolean hold;
}
