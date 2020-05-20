package TetrisGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum Mino {
    I, J, L, O, S, T, Z, NONE;

    public static void drawGhost(double gx, double gy, double squareSize, Mino type, GraphicsContext gc){
        gc.setFill(getMid(type));

        gc.fillRect(gx, gy, squareSize, squareSize);


        gc.clearRect(gx+3, gy+3, squareSize-6, squareSize-6);
    }

    public static void draw(double gx, double gy, double squareSize, Mino type, GraphicsContext gc){
        gc.setFill(getMid(type));

        gc.fillRect(gx, gy, squareSize, squareSize);

        gc.setFill(getDark(type));

        gc.fillRect(gx+1, gy+1, squareSize-2, squareSize-2);
    }

    private static Paint getDark(Mino type){
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

    private static Paint getMid(Mino type){
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

    private static Paint getLight(Mino type){
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
