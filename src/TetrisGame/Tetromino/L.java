package TetrisGame.Tetromino;

import TetrisGame.Move;
import TetrisGame.Mino;

import java.util.EnumMap;

public class L extends Tetromino {
    private static int[] northXOffsets = {0,1,2,2};
    private static int[] northYOffsets = {1,1,1,0};

    private static EnumMap<Orientation, int[]> xOffsets;
    private static EnumMap<Orientation, int[]> yOffsets;


    public static void initialize(){
        xOffsets = new EnumMap<Orientation, int[]>(Orientation.class);
        yOffsets = new EnumMap<Orientation, int[]>(Orientation.class);
        xOffsets.put(Orientation.NORTH, northXOffsets);
        yOffsets.put(Orientation.NORTH, northYOffsets);
    }

    public L(){
        super(xOffsets, yOffsets, Mino.I);
    }
}