package TetrisGame;

import Main.AI;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Random;

public enum Mino {
    I, J, L, O, S, T, Z, NONE;

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
    public static Mino getNextRandom(){

        if(i > 6){
            shuffleBag();
            i = 0;
        }
        return bag[i++];
    }
    private static Random random;

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

    static{
        random = new Random();
        bag = new Mino[7];
        for (int j = 0; j < Mino.values().length - 1; j++) { // skip the last value
            bag[j] = Mino.values()[j];
        }
        shuffleBag();
    }

    public static void drawGhost(double gx, double gy, double squareSize, Mino type, GraphicsContext gc){
        gc.setFill(getMid(type));

        gc.fillRect(gx+0.5, gy+0.5, squareSize-1, squareSize-1);

        gc.clearRect(gx+3, gy+3, squareSize-6, squareSize-6);
    }

    public static void draw(double gx, double gy, double squareSize, Mino type, GraphicsContext gc){
        gc.setFill(getMid(type));

        gc.fillRect(gx+0.5, gy+0.5, squareSize-1, squareSize-1);

        gc.setFill(getDark(type));

        gc.fillRect(gx+2, gy+2, squareSize-4, squareSize-4);
    }

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
