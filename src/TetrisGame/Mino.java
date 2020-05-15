package TetrisGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum Mino {
    I, J, L, O, S, T, Z, NONE;

    public static void draw(double gx, double gy, double squareSize, Mino type, GraphicsContext gc){
        Paint p;

        switch (type){
            case I: p = Color.web("#30C6EF"); break;
            case J: p = Color.web("#5866AF"); break;
            case L: p = Color.web("#EF7922"); break;
            case O: p = Color.web("#F7D409"); break;
            case S: p = Color.web("#41B541"); break;
            case T: p = Color.web("#AC4E9E"); break;
            case Z: p = Color.web("#EE2229"); break;
            default: p = Color.rgb(0,0,0,0); // transparent
        }

        gc.setFill(p);

        gc.fillRect(gx, gy, squareSize, squareSize);
    }
}
