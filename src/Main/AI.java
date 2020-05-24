package Main;

import TetrisGame.Game;
import TetrisGame.Mino;
import TetrisGame.Move;
import TetrisGame.Tetromino.Orientation;
import TetrisGame.Tetromino.Tetromino;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.util.*;

public class AI implements MoveGetter {
    private Move move;

    private Game game;

    private Tetromino currentTetromino;
    private Orientation goalOrientation;
    private int goalX;

    private HashMap<Double, Pair<Orientation, Integer>> moves;

    private static double kHeight = 0.5;
    private static double kClears = 0.75;
    private static double kEdges = 0.25;
    private static double kWells = -0.25;
    private static double kHoles = -1.0;


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
                for (int y = game.getBoard().SPAWN_Y; y < game.getBoard().HEIGHT; y++) {
                    if(!currentTetromino.canMove(x, y+1, orientation, game.getBoard()) && currentTetromino.canMove(x, y, orientation, game.getBoard())){
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

        double bestMove = Double.NEGATIVE_INFINITY;
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
        /*
        System.out.println();
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
        }*/

        double[] start = {0,0,0,0,0};
        double[] steps = {0.5,0.5,0.5,0.5,0.5};

        BestArray best = new BestArray(5);


        findBestInRange(start, steps, 2, best, 0);
        findBestInRangeDeeper(steps, 2, best, 5);

        for(Pair<Integer, double[]> attempt: best){
            System.out.print("Best: ");
            for(double d:attempt.getValue()){
                System.out.print(d + " ");
            }
            System.out.print(attempt.getKey() + "\n");
        }
    }

    static void findBestInRangeDeeper(double steps[],  int n, BestArray best, int depth){
        System.out.println("Deeper");
        if(depth > 0) {
            for (int i = 0; i < steps.length; i++) {
                steps[i] = steps[i]/(n);
            }
            double[][] starts = new double[best.size()][];
            for (int i = 0; i < best.size(); i++) {
                starts[i] = best.get(i).getValue();
            }

            for (int i = 0; i < starts.length; i++) {
                findBestInRange(starts[i], steps, n, best, 0);
            }

            findBestInRangeDeeper(steps, n, best, depth-1);
        }
    }

    static void findBestInRange(double[] start, double steps[], int n, BestArray best, int i){
        if(i < start.length){
            double original = start[i];
            findBestInRange(start, steps, n, best, i+1);
            for (int j = 0; j < n; j++) {
                start[i] += steps[i];
                if(start[i] < 1){
                    findBestInRange(start, steps, n, best, i + 1);
                } else {
                    break;
                }
            }
            start[i] = original;
            for (int j = 0; j < n; j++) {

                start[i] -= steps[i];

                if(start[i] > -1) {
                    findBestInRange(start, steps, n, best, i + 1);
                } else {
                    break;
                }
            }
            start[i] = original;
        } else {
            int score = attempt(start, 5);
            best.addNewAttempt(score, start);
        }
    }

    private static int MAX_MINOS = 2000;

    static int attempt(double[] params, int n){
        int totalScore = 0;
        for (int i = 0; i < n; i++) {
            totalScore += attempt(params);
        }
        return totalScore/n;
    }

    static int attempt(double[] params){
        kHeight = params[0];
        kClears = params[1];
        kEdges = params[2];
        kWells = params[3];
        kHoles = params[4];

        Game game = new Game();
        AI ai = new AI(game);

        Tetromino prevTetromino = game.getCurrentTetromino();
        int minoCounter = 0;
        while(!game.gameIsOver() && minoCounter < MAX_MINOS){
            game.update(ai.getMove());
            if(game.getCurrentTetromino() != prevTetromino){
                minoCounter ++;
                prevTetromino = game.getCurrentTetromino();

            }
        }
        //System.out.println("Score " + game.getScore() + " Level " + game.getLevel());
        return minoCounter;
    }

    public static void main(String[] args) {
        train();
    }

    private static class BestArray extends ArrayList<Pair<Integer, double[]>>{
        int capacity;
        BestArray(int capacity){
            super();
            this.capacity = capacity;
        }

        public void addNewAttempt(int score, double[] params){
            if(this.size() < capacity){
                this.add(new Pair<>(score, params.clone()));
            } else {
                if(score >= this.get(0).getKey()){
                    this.remove(0);
                    int i = 0;
                    while(i < this.size() && score > this.get(i).getKey()){
                        i++;
                    }
                    this.add(i, new Pair<>(score, params.clone()));

                    System.out.print("New: ");
                    for(double d:params){
                        System.out.print(d + " ");
                    }
                    System.out.print(score + "\n");
                }
            }
        }
    }
}
