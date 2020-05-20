package Main;

import Main.Main;
import TetrisGame.Move;
import javafx.scene.input.KeyCode;

public class Player implements MoveGetter {
    Move move = new Move();

    @Override
    public Move getMove() {
        InputManager inputManager = Main.getInstance().inputManager;
        if(inputManager.isKeyClicked(KeyCode.LEFT)){
            move.translation = Move.Direction.LEFT;
        } else if(inputManager.isKeyClicked(KeyCode.RIGHT)){
            move.translation = Move.Direction.RIGHT;
        } else {
            move.translation = Move.Direction.NONE;
        }
        return move;
    }
}
