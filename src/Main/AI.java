package Main;

import TetrisGame.Board;
import TetrisGame.Game;
import TetrisGame.Mino;
import TetrisGame.Move;
import TetrisGame.Tetromino.Orientation;
import TetrisGame.Tetromino.Tetromino;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AI implements MoveGetter {
    private Move move;

    private Game game;

    private Tetromino currentTetromino;
    private Orientation goalOrientation;
    private int goalX;

    private HashMap<Double, Pair<Orientation, Integer>> moves;

    public AI(Game game){
        this.game = game;
        move = new Move();
    }

    private void findBestMove(){
        Mino[][] boardCopy = game.getBoard().getBoard().clone();
        Mino currentMinoType = currentTetromino.getMinoType();

        moves = new HashMap<>();

        for(Orientation orientation: Orientation.values()){
            for (int x = -2; x < game.getBoard().WIDTH-2; x++) {
                for (int y = game.getBoard().HEIGHT-2; y <= game.getBoard().FIRST_VISIBLE_Y; y--) {
                    if(currentTetromino.canMove(x, y, orientation, game.getBoard())){
                        for (int i = 0; i < Tetromino.NUM_MINOS; i++) {
                            int minoX = Tetromino.X_OFFSETS.get(currentMinoType).get(orientation)[i] + x;
                            int minoY = Tetromino.Y_OFFSETS.get(currentMinoType).get(orientation)[i] + y;
                            boardCopy[minoX][minoY] = currentMinoType;
                        }
                        moves.put(scoreMove(boardCopy), new Pair<Orientation, Integer>(orientation, x));
                        for (int i = 0; i < Tetromino.NUM_MINOS; i++) {
                            int minoX = Tetromino.X_OFFSETS.get(currentMinoType).get(orientation)[i] + x;
                            int minoY = Tetromino.Y_OFFSETS.get(currentMinoType).get(orientation)[i] + y;
                            boardCopy[minoX][minoY] = Mino.NONE;
                        }
                        break;
                    }
                }
            }
        }

        double bestMove = -1000.0;
        for (Map.Entry<Double, Pair<Orientation, Integer>> move : moves.entrySet()){
            if(move.getKey() > bestMove){
                bestMove = move.getKey();
            }
        }
        Pair<Orientation, Integer> move = moves.get(bestMove);
        goalOrientation = move.getKey();
        goalX = move.getValue();
    }

    private double scoreMove(Mino[][] board){
        return 0;
    }

    @Override
    public Move getMove() {

        if(game.getCurrentTetromino() != currentTetromino){
            currentTetromino = game.getCurrentTetromino();
            findBestMove();
        }


        int dx = goalX - currentTetromino.getX();
        move.hardDrop = false;
        if(dx > 0){
            move.translation = Move.Direction.RIGHT;
        } else if(dx < 0){
            move.translation = Move.Direction.LEFT;
        } else {
            move.translation = Move.Direction.NONE;
            move.hardDrop = true;
        }

        if(currentTetromino.getOrientation() != goalOrientation){
            move.rotation = Move.Direction.RIGHT;
            move.hardDrop = false;
        } else {
            move.rotation = Move.Direction.NONE;
        }

        move.softDrop = false;
        move.hold = false;

        return move;
    }
}
