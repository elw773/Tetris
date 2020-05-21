package Main;

import Main.Main;
import TetrisGame.Move;
import javafx.scene.input.KeyCode;

public class Player implements MoveGetter {
    private Move move = new Move();
    private int translationCounter;
    private int translationLimit;

    @Override
    public Move getMove() {
        InputManager inputManager = Main.getInstance().inputManager;
        if(inputManager.isKeyPressed(KeyCode.LEFT)){
            move.translation = Move.Direction.LEFT;
        } else if(inputManager.isKeyPressed(KeyCode.RIGHT)){
            move.translation = Move.Direction.RIGHT;
        } else {
            move.translation = Move.Direction.NONE;
            translationLimit = 5;
            translationCounter = 0;
        }

        if(move.translation != Move.Direction.NONE) {
            if(translationLimit != 5 || translationCounter != 0){
                if(translationCounter > translationLimit){
                    translationLimit = 2;
                    translationCounter = 0;
                } else {
                    move.translation = Move.Direction.NONE;
                }
            }
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
        move.hardDrop = inputManager.isKeyClicked(KeyCode.SPACE);
        move.hold = inputManager.isKeyClicked(KeyCode.C) || inputManager.isKeyClicked(KeyCode.SHIFT);

        return move;
    }
}
