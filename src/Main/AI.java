package Main;

import TetrisGame.Board;
import TetrisGame.Game;
import TetrisGame.Mino;
import TetrisGame.Move;
import TetrisGame.Tetromino.Orientation;
import TetrisGame.Tetromino.Tetromino;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class AI implements MoveGetter {
    private Move move;

    private Game game;

    private Tetromino currentTetromino;
    private Orientation goalOrientation;
    private int goalX;

    private HashMap<Double, Pair<Orientation, Integer>> moves;

    private static double kHeight = 1;
    private static double kClears = 0;
    private static double kEdges = 0;
    private static double kWells = -1;
    private static double kHoles = -1;


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
                for (int y = game.getBoard().SPAWN_Y; y < game.getBoard().HEIGHT-2; y++) {
                    if(!currentTetromino.canMove(x, y, orientation, game.getBoard())){
                        y--;
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
        int height = -1; // bigger number is lower
        int clears = 0;
        int wells = 0;
        int edges = 0;
        int holes = 0;
        for (int y = 0; y < board[0].length; y++) {
            boolean fullRow = true;
            for (int x = 0; x < board.length; x++) {
                if(!isOpen(x, y, board)){

                    if(height == -1) {
                        height = y;
                       // System.out.print("@");
                    } else {
                        //System.out.print("#");
                    }
                } else {

                    fullRow = false;
                    boolean top = !isOpen(x, y-1, board);
                    boolean topLeft = !isOpen(x-1, y-1, board);
                    boolean left = !isOpen(x-1, y, board);
                    boolean right = !isOpen(x+1, y, board);
                    boolean topRight = !isOpen(x+1, y-1, board);
                    boolean bottom = !isOpen(x, y+1, board);
                   if(top){
                       holes ++;
                       //System.out.print("H");
                   } else if(left && right){
                       wells ++;
                       //System.out.print("W");
                   } else if((left || right) && bottom && !(topLeft || topRight)){
                       edges ++;
                       //System.out.print("E");
                   } else {
                       //System.out.print(" ");
                   }
                }
            }
            if(fullRow){
                clears ++;
                System.out.println("clear");
            }
            //System.out.print("\n");
        }

        //System.out.println(height + " " + clears + " " + edges + " " + wells + " " + holes);
        double score = (height * kHeight) + (clears * kClears) + (edges * kEdges) + (wells * kWells) + ((holes) * kHoles);
        return score;
    }

    private boolean isOpen(int x, int y, Mino[][] board){
        return (0 <= x && x < board.length) && (0 <= y && y < board[x].length) && board[x][y] == Mino.NONE;
    }

    @Override
    public Move getMove() {/*
        Mino[][] board = {
                {Mino.NONE, Mino.NONE, Mino.NONE, Mino.NONE, Mino.NONE, Mino.O, Mino.NONE},
                {Mino.NONE, Mino.NONE, Mino.NONE, Mino.O, Mino.O, Mino.O, Mino.O},
                {Mino.NONE, Mino.NONE, Mino.NONE, Mino.NONE, Mino.O, Mino.O, Mino.O},
                {Mino.NONE, Mino.NONE, Mino.NONE, Mino.NONE, Mino.O, Mino.O, Mino.NONE},
                {Mino.NONE, Mino.NONE, Mino.O, Mino.O, Mino.O, Mino.O, Mino.O},
                {Mino.NONE, Mino.NONE, Mino.NONE, Mino.NONE, Mino.NONE, Mino.O, Mino.O},
                {Mino.NONE, Mino.NONE, Mino.NONE, Mino.NONE, Mino.O, Mino.O, Mino.NONE}

        };
        scoreMove(board);*/
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

        if(currentTetromino.getOrientation() != goalOrientation && currentTetromino.getMinoType() != Mino.O){
            move.rotation = Move.Direction.RIGHT;
            move.hardDrop = false;
        } else {
            move.rotation = Move.Direction.NONE;
        }

        move.softDrop = false;
        move.hold = false;

        return move;
    }

    static void train(){
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream("Resources/trainingMinos.txt"));
        } catch(Exception e){

        }
        String str = scanner.nextLine();
        String[] minos = str.split(",");

        trainingMinos = new Mino[minos.length];
        for (int i = 0; i < minos.length; i++) {
            trainingMinos[i] = Mino.toMino(minos[i]);
        }

        //do attempts
        System.out.println(attempt(new double[]{7, 10, 1, -1, -3}));
    }

    private static int minoCounter;
    private static Mino[] trainingMinos;

    public static Mino getNextTrainingMino(){
        return trainingMinos[minoCounter++];
    }


    static int attempt(double[] params){
        minoCounter = 0;
        kHeight = params[0];
        kClears = params[1];
        kEdges = params[2];
        kWells = params[3];
        kHoles = params[4];

        Game game = new Game();
        AI ai = new AI(game);

        while(!game.gameIsOver() && minoCounter < trainingMinos.length){
            game.update(ai.getMove());
        }
        return minoCounter;
    }

    public static void main(String[] args) {
        train();
    }
}
