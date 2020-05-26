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

    private static double kHeight = 0.36864000000000013;
    private static double kClears = 0.46335999999999994;
    private static double kEdges = 0.03807999999999998;
    private static double kWells = -0.16352;
    private static double kHoles = -0.8732799999999998;


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

        /*
        double[] start = {0,0,0,0,0};
        double[] steps = {0.5,0.5,0.5,0.5,0.5};

        BestArray best = new BestArray(5);


        findBestInRange(start, steps, 2, best, 0);
        findBestInRangeDeeper(steps, 0.25, 2, best, 5);*/
        double[] start = AI.start;
        double[] steps = AI.steps;

        BestArray best = new BestArray(bestCapacity);

        best.add(0, new Pair<Integer, double[]>(4883, new double[]{0.3814400000000001, 0.53632, 0.03295999999999998, -0.17504000000000003, -0.92448}));
        best.add(1, new Pair<Integer, double[]>(4920, new double[]{0.37452799999999997, 0.434176, 0.042175999999999984, -0.15660799999999997, -0.8640639999999997}));
        best.add(2, new Pair<Integer, double[]>(4951, new double[]{0.3855360000000001, 0.5383680000000001, 0.03091199999999998, -0.17299200000000003, -0.9224319999999999}));
        best.add(3, new Pair<Integer, double[]>(4975, new double[]{0.3481600000000001, 0.42752000000000007, 0.0739199999999999, -0.15327999999999997, -0.8835199999999999}));
        best.add(4, new Pair<Integer, double[]>(5000, new double[]{0.36864000000000013, 0.46335999999999994, 0.03807999999999998, -0.16352, -0.8732799999999998}));
        best.add(5, new Pair<Integer, double[]>(4883, new double[]{0.67578125, 0.669921875, 0.173828125, -0.181640625, -0.919921875}));


        for (int i = 0; i < best.size(); i++) {
            System.out.println(i);
            System.out.println("Average " + attempt(best.get(i).getValue(), 50) + "\n\n");
        }
        /*
        findBestInRange(start, steps, firstN, best, 0);
        findBestInRangeDeeper(steps, stepScale, secondN, best, depth);*/

        for(Pair<Integer, double[]> attempt: best){
            System.out.print("Best: ");
            for(double d:attempt.getValue()){
                System.out.print(d + " ");
            }
            System.out.print(attempt.getKey() + "\n");
        }
        System.out.println(numGames + " Games");
    }

    static double[] start = {0,0,0,0,0};
    static double[] steps = {0.2,0.2,0.2,0.2,0.2};
    static int firstN = 5;
    static int bestCapacity = 10;
    static double stepScale = 0.4;
    static int secondN = 2;
    static int depth = 7;
    static int nAttempts = 10;
    private static int MAX_MINOS = 10000;
    static long numGames = 0;


    static void findBestInRangeDeeper(double steps[],  double stepScale, int n, BestArray best, int depth){
        if(depth > 0) {

            for (int i = 0; i < steps.length; i++) {
                steps[i] = steps[i] * stepScale;
            }

            double[][] starts = new double[best.size()][];
            for (int i = 0; i < best.size(); i++) {
                starts[i] = best.get(i).getValue();
            }
            System.out.println(numGames + " Games \n\nDepth: " + depth);
            for(Pair<Integer, double[]> attempt: best){
                System.out.print("Best: ");
                for(double d:attempt.getValue()){
                    System.out.print(d + " ");
                }
                System.out.print(attempt.getKey() + "\n");
            }

            for (int i = 0; i < starts.length; i++) {


                findBestInRange(starts[i], steps, n, best, 0);
            }

            findBestInRangeDeeper(steps, stepScale, n, best, depth-1);
        }
    }

    static void findBestInRange(double[] start, double steps[], int n, BestArray best, int i){
        if(i == 0){
            System.out.print("\nSearching ");
            for(double d:start){
                System.out.print(d + " ");
            }
            System.out.print(" +-" + steps[0]*2 + " (steps of " + steps[0] + ")\n");
        }
        if(i < start.length){
            double original = start[i];
            for (int j = 0; j < n; j++) {
                start[i] += steps[i];
                if(start[i] <= 1){
                    findBestInRange(start, steps, n, best, i + 1);
                } else {
                    break;
                }
            }
            start[i] = original;
            for (int j = 0; j < n; j++) {

                start[i] -= steps[i];

                if(start[i] >= -1) {
                    findBestInRange(start, steps, n, best, i + 1);
                } else {
                    break;
                }
            }
            start[i] = original;
        } else {


            int score = attempt(start, nAttempts);
            best.addNewAttempt(score, start);
        }
    }



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
        numGames ++;
        System.out.println("Minos " + minoCounter + " Score " + game.getScore() + " Level " + game.getLevel());
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

                    System.out.print("Game " + numGames + " : ");
                    for(double d:params){
                        System.out.print(d + " ");
                    }
                    System.out.print(score + "\n");
                }
            }
        }
    }
}

/*
best.add(0, new Pair<Integer, double[]>(4883, new double[]{0.3814400000000001, 0.53632, 0.03295999999999998, -0.17504000000000003, -0.92448}));
best.add(1, new Pair<Integer, double[]>(4920, new double[]{0.37452799999999997, 0.434176, 0.042175999999999984, -0.15660799999999997, -0.8640639999999997}));
best.add(2, new Pair<Integer, double[]>(4951, new double[]{0.3855360000000001, 0.5383680000000001, 0.03091199999999998, -0.17299200000000003, -0.9224319999999999}));
best.add(3, new Pair<Integer, double[]>(4975, new double[]{0.3481600000000001, 0.42752000000000007, 0.0739199999999999, -0.15327999999999997, -0.8835199999999999}));
best.add(4, new Pair<Integer, double[]>(5000, new double[]{0.36864000000000013, 0.46335999999999994, 0.03807999999999998, -0.16352, -0.8732799999999998}));
best.add(5, new Pair<Integer, double[]>(4883, new double[]{0.67578125, 0.669921875, 0.173828125, -0.181640625, -0.919921875}));


for (int i = 0; i < best.size(); i++) {
    System.out.println(i);
    System.out.println("Average " + attempt(best.get(i).getValue(), 50) + "\n\n");
}

"C:\Program Files (x86)\Java\jdk1.8.0_181\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2019.1.1\lib\idea_rt.jar=56963:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2019.1.1\bin" -Dfile.encoding=UTF-8 -classpath "C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\charsets.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\deploy.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\ext\access-bridge-32.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\ext\cldrdata.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\ext\dnsns.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\ext\jaccess.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\ext\jfxrt.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\ext\localedata.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\ext\nashorn.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\ext\sunec.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\ext\sunjce_provider.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\ext\sunmscapi.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\ext\sunpkcs11.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\ext\zipfs.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\javaws.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\jce.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\jfr.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\jfxswt.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\jsse.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\management-agent.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\plugin.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\resources.jar;C:\Program Files (x86)\Java\jdk1.8.0_181\jre\lib\rt.jar;C:\Users\elw77\Github\Tetris\out\production\Tetris" Main.AI
0
Minos 4077 Score 20255676 Level 162
Minos 2334 Score 6590282 Level 92
Minos 2360 Score 6521146 Level 93
Minos 4197 Score 21112800 Level 167
Minos 1522 Score 2697548 Level 60
Minos 5794 Score 40765816 Level 231
Minos 788 Score 722654 Level 30
Minos 481 Score 262178 Level 18
Minos 1575 Score 2969148 Level 62
Minos 488 Score 279482 Level 18
Minos 4287 Score 22527802 Level 170
Minos 2326 Score 6282840 Level 92
Minos 8883 Score 96339678 Level 354
Minos 3881 Score 18357276 Level 154
Minos 476 Score 246282 Level 18
Minos 4226 Score 21455826 Level 168
Minos 1050 Score 1251070 Level 41
Minos 2648 Score 8348796 Level 105
Minos 1521 Score 2704840 Level 60
Minos 488 Score 250438 Level 18
Minos 989 Score 1107276 Level 38
Minos 1962 Score 4705358 Level 77
Minos 2832 Score 9647786 Level 112
Minos 5892 Score 41983166 Level 234
Minos 688 Score 532424 Level 26
Minos 2344 Score 6420532 Level 93
Minos 3168 Score 12048594 Level 126
Minos 2401 Score 7076806 Level 95
Minos 7487 Score 68690892 Level 298
Minos 5481 Score 35861576 Level 218
Minos 835 Score 753350 Level 32
Minos 1471 Score 2598520 Level 58
Minos 766 Score 666888 Level 29
Minos 516 Score 326190 Level 20
Minos 1308 Score 1990988 Level 51
Minos 1363 Score 2156486 Level 53
Minos 2547 Score 7692470 Level 101
Minos 2544 Score 7799806 Level 101
Minos 2924 Score 10414024 Level 116
Minos 237 Score 53904 Level 8
Minos 981 Score 1125344 Level 38
Minos 1428 Score 2425158 Level 56
Minos 2086 Score 5273376 Level 82
Minos 1518 Score 2662400 Level 60
Minos 2899 Score 9980178 Level 115
Minos 938 Score 1029584 Level 36
Minos 1586 Score 2981970 Level 62
Minos 2696 Score 8743314 Level 107
Minos 3441 Score 14043862 Level 136
Minos 5007 Score 30327344 Level 199
Average 2474


1
Minos 907 Score 931054 Level 35
Minos 2200 Score 5661420 Level 87
Minos 1217 Score 1644718 Level 47
Minos 3761 Score 17025378 Level 149
Minos 1032 Score 1238076 Level 40
Minos 7624 Score 69799812 Level 304
Minos 2033 Score 5006270 Level 80
Minos 1951 Score 4591768 Level 77
Minos 4594 Score 25522682 Level 183
Minos 5292 Score 33843616 Level 210
Minos 2727 Score 9268290 Level 108
Minos 2817 Score 9447766 Level 112
Minos 1521 Score 2730664 Level 60
Minos 4385 Score 23350266 Level 174
Minos 946 Score 1007378 Level 37
Minos 3302 Score 13057076 Level 131
Minos 584 Score 379376 Level 22
Minos 3975 Score 19249720 Level 158
Minos 2820 Score 9562572 Level 112
Minos 2509 Score 7651096 Level 99
Minos 718 Score 560496 Level 27
Minos 1072 Score 1375214 Level 42
Minos 5281 Score 33199092 Level 210
Minos 6169 Score 47070750 Level 246
Minos 3337 Score 13471078 Level 132
Minos 2125 Score 5407244 Level 84
Minos 1793 Score 3762638 Level 70
Minos 3306 Score 12990024 Level 131
Minos 9667 Score 114853734 Level 386
Minos 1053 Score 1290358 Level 41
Minos 3586 Score 15509810 Level 142
Minos 1673 Score 3326384 Level 66
Minos 2922 Score 10288224 Level 116
Minos 44 Score 1124 Level 1
Minos 1989 Score 4795882 Level 79
Minos 10000 Score 122895942 Level 400
Minos 4462 Score 24284262 Level 177
Minos 953 Score 1114528 Level 37
Minos 1561 Score 2843712 Level 61
Minos 1359 Score 2160896 Level 53
Minos 979 Score 1123564 Level 38
Minos 3192 Score 12692942 Level 127
Minos 2444 Score 7119072 Level 97
Minos 366 Score 150824 Level 14
Minos 1183 Score 1629930 Level 46
Minos 2384 Score 6874852 Level 94
Minos 420 Score 194712 Level 16
Minos 2071 Score 5109566 Level 82
Minos 3204 Score 12268674 Level 127
Minos 2370 Score 6658628 Level 94
Average 2757


2
Minos 2649 Score 8175880 Level 105
Minos 5301 Score 34300958 Level 211
Minos 270 Score 68538 Level 10
Minos 523 Score 316404 Level 20
Minos 2053 Score 4998978 Level 81
Minos 1104 Score 1450732 Level 43
Minos 650 Score 496114 Level 25
Minos 426 Score 214822 Level 16
Minos 2003 Score 4690892 Level 79
Minos 2337 Score 6594676 Level 92
Minos 902 Score 934744 Level 35
Minos 748 Score 634240 Level 29
Minos 1532 Score 2905148 Level 60
Minos 887 Score 913982 Level 34
Minos 972 Score 1063344 Level 38
Minos 5786 Score 40101078 Level 230
Minos 294 Score 89548 Level 11
Minos 2395 Score 6610134 Level 95
Minos 645 Score 468362 Level 25
Minos 3975 Score 18847226 Level 158
Minos 2412 Score 7129162 Level 95
Minos 7722 Score 72557346 Level 308
Minos 811 Score 758388 Level 31
Minos 3444 Score 14536752 Level 137
Minos 140 Score 15214 Level 4
Minos 1200 Score 1714068 Level 47
Minos 148 Score 19778 Level 5
Minos 1110 Score 1359010 Level 43
Minos 5637 Score 38372500 Level 224
Minos 1481 Score 2604218 Level 58
Minos 4347 Score 22792344 Level 173
Minos 1000 Score 1171126 Level 39
Minos 1475 Score 2538976 Level 58
Minos 2539 Score 7841242 Level 100
Minos 1895 Score 4314800 Level 75
Minos 1476 Score 2578880 Level 58
Minos 630 Score 423722 Level 24
Minos 2043 Score 4841294 Level 81
Minos 2418 Score 6878380 Level 96
Minos 718 Score 576606 Level 28
Minos 2619 Score 8172816 Level 104
Minos 1323 Score 2136820 Level 52
Minos 6511 Score 51474620 Level 259
Minos 610 Score 403370 Level 23
Minos 1745 Score 3642968 Level 69
Minos 909 Score 888476 Level 35
Minos 867 Score 884508 Level 34
Minos 3983 Score 19271926 Level 158
Minos 2044 Score 5008164 Level 81
Minos 6376 Score 49187280 Level 254
Average 2101


3
Minos 4011 Score 19545806 Level 159
Minos 725 Score 619576 Level 28
Minos 2086 Score 5244002 Level 82
Minos 3730 Score 16974904 Level 148
Minos 1272 Score 1810526 Level 50
Minos 7414 Score 67075722 Level 295
Minos 2305 Score 6473062 Level 91
Minos 843 Score 815884 Level 33
Minos 73 Score 4712 Level 2
Minos 2570 Score 7946082 Level 102
Minos 1300 Score 1938666 Level 51
Minos 3587 Score 15636158 Level 142
Minos 454 Score 225180 Level 17
Minos 1030 Score 1244206 Level 40
Minos 6224 Score 46694688 Level 248
Minos 2299 Score 6205900 Level 91
Minos 5835 Score 41286826 Level 232
Minos 2680 Score 8620778 Level 106
Minos 496 Score 256428 Level 19
Minos 3328 Score 13500498 Level 132
Minos 2944 Score 10347326 Level 117
Minos 1861 Score 4135006 Level 73
Minos 1937 Score 4591822 Level 76
Minos 1446 Score 2496984 Level 57
Minos 1072 Score 1376874 Level 42
Minos 1659 Score 3264238 Level 65
Minos 452 Score 242118 Level 17
Minos 4150 Score 20885252 Level 165
Minos 7460 Score 68186350 Level 297
Minos 6058 Score 44502566 Level 241
Minos 1777 Score 3930312 Level 70
Minos 2623 Score 8202196 Level 104
Minos 1032 Score 1259980 Level 40
Minos 853 Score 826966 Level 33
Minos 7420 Score 67226622 Level 296
Minos 312 Score 96616 Level 11
Minos 10000 Score 123272580 Level 400
Minos 1852 Score 4155414 Level 73
Minos 1431 Score 2442586 Level 56
Minos 6567 Score 52249332 Level 262
Minos 2119 Score 5349338 Level 84
Minos 887 Score 910888 Level 34
Minos 382 Score 175828 Level 14
Minos 1179 Score 1569748 Level 46
Minos 3300 Score 13081564 Level 131
Minos 610 Score 429842 Level 23
Minos 2729 Score 9162624 Level 108
Minos 10000 Score 123918732 Level 400
Minos 2469 Score 7258342 Level 98
Minos 1294 Score 1957542 Level 51
Average 2802


4
Minos 1205 Score 1699590 Level 47
Minos 1150 Score 1532682 Level 45
Minos 4278 Score 22105662 Level 170
Minos 633 Score 487162 Level 24
Minos 486 Score 253100 Level 18
Minos 1217 Score 1817582 Level 48
Minos 466 Score 249140 Level 17
Minos 2405 Score 6922284 Level 95
Minos 3708 Score 16723162 Level 147
Minos 1804 Score 3875382 Level 71
Minos 302 Score 95014 Level 11
Minos 4852 Score 27926960 Level 193
Minos 1429 Score 2419268 Level 56
Minos 3366 Score 13965616 Level 133
Minos 762 Score 658280 Level 29
Minos 3858 Score 18081738 Level 153
Minos 590 Score 411574 Level 22
Minos 713 Score 590400 Level 27
Minos 2645 Score 8363484 Level 105
Minos 647 Score 489896 Level 25
Minos 3244 Score 12631294 Level 129
Minos 6654 Score 53688272 Level 265
Minos 3589 Score 15644254 Level 142
Minos 3465 Score 14613612 Level 137
Minos 1439 Score 2423692 Level 56
Minos 1393 Score 2336476 Level 55
Minos 5255 Score 33132224 Level 209
Minos 3756 Score 17007482 Level 149
Minos 3269 Score 13236748 Level 130
Minos 9131 Score 99226496 Level 364
Minos 6898 Score 58606906 Level 275
Minos 4866 Score 29199310 Level 193
Minos 1802 Score 3870398 Level 71
Minos 586 Score 376824 Level 22
Minos 610 Score 408702 Level 23
Minos 1357 Score 2227196 Level 53
Minos 7697 Score 71592522 Level 307
Minos 1052 Score 1324634 Level 41
Minos 1789 Score 4015286 Level 71
Minos 2364 Score 6831372 Level 93
Minos 3128 Score 11504566 Level 124
Minos 4542 Score 25071830 Level 181
Minos 668 Score 492520 Level 25
Minos 239 Score 59740 Level 8
Minos 2923 Score 10217968 Level 116
Minos 3543 Score 14900456 Level 141
Minos 1471 Score 2583800 Level 58
Minos 935 Score 966114 Level 36
Minos 2421 Score 6994584 Level 96
Minos 425 Score 197522 Level 16
Average 2540


5
Minos 946 Score 1023100 Level 37
Minos 214 Score 41030 Level 7
Minos 499 Score 246106 Level 19
Minos 489 Score 261806 Level 18
Minos 928 Score 932928 Level 36
Minos 190 Score 32014 Level 6
Minos 926 Score 953132 Level 36
Minos 435 Score 192110 Level 16
Minos 281 Score 73956 Level 10
Minos 390 Score 174470 Level 14
Minos 488 Score 240592 Level 18
Minos 714 Score 567116 Level 27
Minos 425 Score 193944 Level 16
Minos 370 Score 133572 Level 14
Minos 1444 Score 2398920 Level 56
Minos 646 Score 436180 Level 25
Minos 369 Score 145574 Level 14
Minos 509 Score 264970 Level 19
Minos 462 Score 221246 Level 17
Minos 715 Score 559282 Level 27
Minos 481 Score 251758 Level 18
Minos 113 Score 9040 Level 3
Minos 652 Score 481992 Level 25
Minos 244 Score 52788 Level 8
Minos 292 Score 80996 Level 10
Minos 335 Score 112408 Level 12
Minos 216 Score 42476 Level 7
Minos 676 Score 481558 Level 26
Minos 343 Score 110824 Level 13
Minos 178 Score 26732 Level 6
Minos 885 Score 850906 Level 34
Minos 304 Score 100708 Level 11
Minos 767 Score 699456 Level 29
Minos 685 Score 501446 Level 26
Minos 545 Score 304290 Level 20
Minos 520 Score 293734 Level 19
Minos 655 Score 466962 Level 25
Minos 402 Score 182110 Level 15
Minos 417 Score 174310 Level 15
Minos 468 Score 237454 Level 18
Minos 1031 Score 1189414 Level 40
Minos 182 Score 29548 Level 6
Minos 481 Score 274342 Level 18
Minos 930 Score 996862 Level 36
Minos 194 Score 31072 Level 7
Minos 511 Score 275586 Level 19
Minos 665 Score 477274 Level 25
Minos 494 Score 264864 Level 19
Minos 537 Score 314780 Level 20
Minos 411 Score 192212 Level 15
Average 521


Best: 0.3814400000000001 0.53632 0.03295999999999998 -0.17504000000000003 -0.92448 4883
Best: 0.37452799999999997 0.434176 0.042175999999999984 -0.15660799999999997 -0.8640639999999997 4920
Best: 0.3855360000000001 0.5383680000000001 0.03091199999999998 -0.17299200000000003 -0.9224319999999999 4951
Best: 0.3481600000000001 0.42752000000000007 0.0739199999999999 -0.15327999999999997 -0.8835199999999999 4975
Best: 0.36864000000000013 0.46335999999999994 0.03807999999999998 -0.16352 -0.8732799999999998 5000
Best: 0.67578125 0.669921875 0.173828125 -0.181640625 -0.919921875 4883
300 Games

Process finished with exit code 0


 */
