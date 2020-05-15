package TetrisGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum Mino {
    I, J, L, O, S, T, Z, NONE;

    public static void draw(double gx, double gy, double squareSize, Mino type, GraphicsContext gc){
        Paint p;

        switch (type){
            case I: p = Color.web("#30C6EF");
            case J: p = Color.web("#5866AF");
            case L: p = Color.web("#EF7922");
            case O: p = Color.web("#F7D409");
            case S: p = Color.web("#41B541");
            case T: p = Color.web("#AC4E9E");
            case Z: p = Color.web("#EE2229");
            default: p = Color.rgb(0,0,0,1); // transparent
        }

        gc.setFill(p);

        gc.fillRect(gx, gy, squareSize, squareSize);
    }
}
