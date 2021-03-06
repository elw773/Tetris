package TetrisGame;

import Main.AI;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Random;

/**
 * This enum represents the types of minos available in the game
 */
public enum Mino {
    I, J, L, O, S, T, Z, NONE;

    /**
     * Parses a string to a Mino
     * @param str the string to parse
     * @return the Mino that coresponds to the string, or Mino.NONE
     */
    public static Mino toMino(String str){
        switch (str){
            case "I": return I;
            case "J": return J;
            case "L": return L;
            case "O": return O;
            case "S": return S;
            case "T": return T;
            case "Z": return Z;
            default: return NONE;
        }
    }

    private static Mino[] bag;
    private static int i;

    /**
     * pre- bag is initialized and shuffled
     * post- if all minos in the bag have been used, re-shuffle it
     * @return the next random Mino
     */
    public static Mino getNextRandom(){
        if(i > 6){
            shuffleBag();
            i = 0;
        }
        return bag[i++];
    }
    private static Random random;

    /**
     * Shuffles the bag of minos
     */
    private static void shuffleBag(){
        //System.out.println("Shuffling");
        Mino temp;
        for (int j = bag.length - 1; j > 0; j--) {
            int k = random.nextInt(bag.length);
            temp = bag[k];
            bag[k] = bag[j];
            bag[j] = temp;
        }
    }

    /**
     * Initializes the bag of minos
     */
    static{
        random = new Random();
        bag = new Mino[7];
        for (int j = 0; j < Mino.values().length - 1; j++) { // skip the last value
            bag[j] = Mino.values()[j];
        }
        shuffleBag();
    }

    /**
     * Draws the ghost version of the square of the given mino type
     * @param gx the x to draw it at
     * @param gy the y to draw it at
     * @param squareSize the width to draw it with
     * @param type the type of the mino to draw
     * @param gc the graphics context to draw on
     */
    public static void drawGhost(double gx, double gy, double squareSize, Mino type, GraphicsContext gc){
        gc.setStroke(getMid(type));
        gc.setFill(Color.TRANSPARENT);
        gc.setLineWidth(2.5);

        gc.strokeRect(gx+2.5, gy+2.5, squareSize-5, squareSize-5);

        //gc.clearRect(gx+3, gy+3, squareSize-6, squareSize-6);
    }

    /**
     * Draws the square of the given mino type
     * @param gx the x to draw it at
     * @param gy the y to draw it at
     * @param squareSize the width to draw it with
     * @param type the type of the mino to draw
     * @param gc the graphics context to draw on
     */
    public static void draw(double gx, double gy, double squareSize, Mino type, GraphicsContext gc){
        gc.setFill(getMid(type));
        gc.fillRect(gx+2.5, gy+2.5, squareSize-5, squareSize-5);

        gc.setStroke(getDark(type));
        gc.setLineWidth(2.5);

        gc.strokeRect(gx+2.5, gy+2.5, squareSize-5, squareSize-5);

    }

    /**
     * Gets the dark color asociated with the given mino
     * @param type
     * @return the dark color
     */
    public static Color getDark(Mino type){
        switch (type){
            case I: return Color.web("#70d7c3");
            case J: return Color.web("#75bae8");
            case L: return Color.web("#eeac69");
            case O: return Color.web("#f6db5e");
            case S: return Color.web("#7bd09d");
            case T: return Color.web("#bd8fd1");
            case Z: return Color.web("#f18375");
            default: return Color.rgb(0,0,0,0); // transparent
        }
    }


    /**
     * Gets the mid color asociated with the given mino
     * @param type
     * @return the mid color
     */
    public static Color getMid(Mino type){
        switch (type){
            case I: return Color.web("#98e2d4");
            case J: return Color.web("#a1d0ef");
            case L: return Color.web("#f3c597");
            case O: return Color.web("#f9e68e");
            case S: return Color.web("#a0ddb9");
            case T: return Color.web("#d2b3e0");
            case Z: return Color.web("#f6aca3");
            default: return Color.rgb(0,0,0,0); // transparent
        }
    }

    /**
     * Gets the light color asociated with the given mino
     * @param type
     * @return the light color
     */
    public static Color getLight(Mino type){
        switch (type){
            case I: return Color.web("#c0eee4");
            case J: return Color.web("#cde6f7");
            case L: return Color.web("#f3c597");
            case O: return Color.web("#fbf0be");
            case S: return Color.web("#c6ebd5");
            case T: return Color.web("#e8d7ee");
            case Z: return Color.web("#fad6d1");
            default: return Color.rgb(0,0,0,0); // transparent
        }
    }
}
