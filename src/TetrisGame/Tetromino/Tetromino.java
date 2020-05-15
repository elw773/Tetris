package TetrisGame.Tetromino;

import TetrisGame.Move;
import TetrisGame.Mino;
import javafx.scene.canvas.GraphicsContext;

import java.util.EnumMap;

public class Tetromino {
    public static final int NUM_MINOS = 4;

    private int x;
    private int y;
    private Orientation orientation;
    private Mino minoType;

    private EnumMap<Orientation, int[]> xOffsets;
    private EnumMap<Orientation, int[]> yOffsets;
/*
    public static EnumMap<Orientation, int[]>[] createOffsets(int[] newXOffsets, int[] newYOffsets){
        EnumMap<Orientation, int[]>[] offsets = new EnumMap[2];
        offsets[0] = new EnumMap<Orientation, int[]>(Orientation.class);
        offsets[1] = new EnumMap<Orientation, int[]>(Orientation.class);


        int width = 0;
        for(int n:newXOffsets){
            if(n > width){
                width = n;
            }
        }

        int height = 0;
        for(int n:newYOffsets){
            if(n > height){
                height = n;
            }
        }

        double centreX = width/2.0;
        double centreY = height/2.0;

        double[] centreXOffset = new double[NUM_MINOS];
        double[] centreYOffset = new double[NUM_MINOS];

        for (int i = 0; i < NUM_MINOS; i++) {
            centreXOffset[i] = newXOffsets[i] - centreX;
            centreYOffset[i] = newYOffsets[i] - centreX;
        }

        for (Orientation orientation:Orientation.values()) {
            int[] xOffsets = new int[NUM_MINOS];
            int[] yOffsets = new int[NUM_MINOS];
            for (int i = 0; i < NUM_MINOS; i++) {
                xOffsets[i] = (int)(centreX + centreXOffset[i] + 0.5);
                yOffsets[i] = (int)(centreY + centreYOffset[i] + 0.5);
            }
            offsets[0].put(orientation, xOffsets);
            offsets[1].put(orientation, yOffsets);
            rotateOffsets90cw(centreXOffset, centreYOffset);
        }
        return offsets;
    }

    private static void rotateOffsets90cw(double[] xOffsets, double[] yOffsets){
        for (int i = 0; i < xOffsets.length; i++) {
            double newX = yOffsets[i] * -1;
            double newY = xOffsets[i];
            xOffsets[i] = newX;
            yOffsets[i] = newY;
        }
    }*/

    public Tetromino(EnumMap<Orientation, int[]> xOffsets, EnumMap<Orientation, int[]> yOffsets, Mino minoType){
        this.xOffsets = xOffsets;
        this.yOffsets = yOffsets;

        this.minoType = minoType;

        orientation = Orientation.NORTH;
    }

    /**
     *
     * @param x
     * @param y
     * @param board
     * @return
     */
    public boolean move(int x, int y, Mino[][] board){
        if(canMove(x, y, board)){
            this.x = x;
            this.y = y;
            return true;
        }
        return false;
    }

    /**
     *
     * @param x
     * @param y
     * @param board
     * @return
     */
    public boolean canMove(int x, int y, Mino[][] board){
        for (int i = 0; i < NUM_MINOS; i++) {
            if(board[x + xOffsets.get(orientation)[i]][y + yOffsets.get(orientation)[i]] != Mino.NONE){
                return false;
            }
        }
        return true;
    }

    /**
     * Moves the tetromino in the desired direction, if it can
     *
     * @param direction the desired direction
     * @param board the board to move on
     * @return true if succesful
     */
    public boolean translate(Move.Direction direction, Mino[][] board){
        if(canTranslate(direction, board)){
            this.x += direction.getSign();
            return true;
        }
        return false;
    }

    /**
     * Determines if the tetromino can be moved in the desired direction
     *
     * @param direction the desired direction
     * @param board the board to move on
     * @return true if it can translate
     */
    public boolean canTranslate(Move.Direction direction, Mino[][] board){
        for (int i = 0; i < NUM_MINOS; i++) {
            int minoX = getMinoX(i);
            int minoY = getMinoY(i);
            if(board[minoX + direction.getSign()][minoY] != Mino.NONE){
                return false;
            }
        }
        return true;
    }

    /**
     * Makes the tetromino fall down the board
     * @param board the board to fall down on
     * @return true if succesful
     */
    public boolean fall(Mino[][] board){
        if(canFall(board)){
            this.y ++;
            return true;
        }
        return false;
    }

    /**
     * Determines if the tetromino can fall on the given board
     * @param board the board to fall down on
     * @return true if it can
     */
    public boolean canFall(Mino[][] board){
        for (int i = 0; i < NUM_MINOS; i++) {
            int minoX = getMinoX(i);
            int minoY = getMinoY(i);
            if(board[minoX][minoY + 1] != Mino.NONE){
                return false;
            }
        }
        return true;
    }

    /**
     * Locks the tetromino onto the given board
     * @param board the board to lock onto
     */
    public void lock(Mino[][] board){
        for (int i = 0; i < NUM_MINOS; i++) {
            int minoX = getMinoX(i);
            int minoY = getMinoY(i);
            board[minoX][minoY] = this.minoType;
        }
    }


    /**
     * Gets the x of the nth mino
     * @param n the mino (0-3)
     * @return the x coordinate
     */
    private int getMinoX(int n){
        return xOffsets.get(orientation)[n] + this.x;
    }

    /**
     * Gets the y of the nth mino
     * @param n the mino (0-3)
     * @return the y coordinate
     */
    private int getMinoY(int n){
        return yOffsets.get(orientation)[n] + this.y;
    }

    public boolean rotate(Move.Direction direction, Mino[][] board){
        return false;
    }

    public boolean canRotate(Move.Direction direction, Mino[][] board){
        return false;
    }

    public void drawGhostRelative(double boardGx, double boardGy, double squareSize, GraphicsContext gc, Mino[][] board){

    }

    /**
     * Draws the square at the given coordinates
     * @param gx
     * @param gy
     * @param squareSize
     * @param gc
     */
    public void drawAbsolute(double gx, double gy, double squareSize, GraphicsContext gc){
        for (int i = 0; i < NUM_MINOS; i++) {
            double x = gx + (xOffsets.get(orientation)[i] * squareSize);
            double y = gy + (yOffsets.get(orientation)[i] * squareSize);
            Mino.draw(x, y, squareSize, minoType, gc);
        }
    }


    /**
     * Draws the square on a board with given coordinates
     * @param boardGx
     * @param boardGy
     * @param squareSize
     * @param gc
     */
    public void drawRelative(double boardGx, double boardGy, double squareSize, GraphicsContext gc){

            drawAbsolute(boardGx + ((x - 1) * squareSize), boardGy + ((y - 5) * squareSize), squareSize, gc);

    }
}
