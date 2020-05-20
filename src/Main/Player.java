package Main;

import Main.Main;
import TetrisGame.Move;
import javafx.scene.input.KeyCode;

public class Player implements MoveGetter {
    private Move move = new Move();
    private int translationCounter;

    @Override
    public Move getMove() {
        InputManager inputManager = Main.getInstance().inputManager;
        if(inputManager.isKeyPressed(KeyCode.LEFT) && translationCounter > 2){
            move.translation = Move.Direction.LEFT;
            translationCounter = 0;
        } else if(inputManager.isKeyPressed(KeyCode.RIGHT) && translationCounter > 2){
            move.translation = Move.Direction.RIGHT;
            translationCounter = 0;
        } else {
            move.translation = Move.Direction.NONE;
            translationCounter ++;
        }

        if(inputManager.isKeyClicked(KeyCode.UP) || inputManager.isKeyClicked(KeyCode.X)){
            move.rotation = Move.Direction.RIGHT;
        } else if(inputManager.isKeyClicked(KeyCode.CONTROL) || inputManager.isKeyClicked(KeyCode.Z)){
            move.rotation = Move.Direction.LEFT;
        } else {
            move.rotation = Move.Direction.NONE;
        }

        move.softDrop = inputManager.isKeyPressed(KeyCode.DOWN);

        return move;
    }
}
