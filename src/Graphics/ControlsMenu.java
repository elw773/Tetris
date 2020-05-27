package Graphics;

import Main.Main;
import TetrisGame.Mino;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Manages the controls menu and its drawing
 */
public class ControlsMenu implements Menu {
    BorderedRectangle rectangles[];

    public ControlsMenu(){
        rectangles = new BorderedRectangle[]{
                new BorderedTextRectangle(200, 50, 300, 100, 10, 40, "CONTROLS", Mino.getMid(Mino.L)),

                new BorderedTextRectangle(250, 200, 50, 50, 5, 15, "ESC", Mino.getMid(Mino.Z)),
                new BorderedTextRectangle(550, 200, 50, 50, 5, 10, "RETURN", Mino.getMid(Mino.S)),
                new BorderedTextRectangle(75, 210, 150, 30, 5, 15, "Pause", Color.WHITE),
                new BorderedTextRectangle(375, 210, 150, 30, 5, 15, "Resume/Restart", Color.WHITE),

                new BorderedTextRectangle(250, 275, 50, 50, 5, 15, "Z", Mino.getMid(Mino.T)),
                new BorderedTextRectangle(550, 275, 50, 50, 5, 15, "UP", Mino.getMid(Mino.L)),
                new BorderedTextRectangle(75, 285, 150, 30, 5, 15, "Rotate CCW", Color.WHITE),
                new BorderedTextRectangle(375, 285, 150, 30, 5, 15, "Rotate CW", Color.WHITE),

                new BorderedTextRectangle(250, 350, 50, 50, 5, 10, "DOWN", Mino.getMid(Mino.O)),
                new BorderedTextRectangle(550, 350, 50, 50, 5, 10, "SPACE", Mino.getMid(Mino.J)),
                new BorderedTextRectangle(75, 360, 150, 30, 5, 15, "Soft Drop", Color.WHITE),
                new BorderedTextRectangle(375, 360, 150, 30, 5, 15, "Hard Drop", Color.WHITE),

                new BorderedTextRectangle(250, 425, 50, 50, 5, 10, "LEFT", Mino.getMid(Mino.T)),
                new BorderedTextRectangle(550, 425, 50, 50, 5, 10, "RIGHT", Mino.getMid(Mino.L)),
                new BorderedTextRectangle(75, 435, 150, 30, 5, 15, "Move Left", Color.WHITE),
                new BorderedTextRectangle(375, 435, 150, 30, 5, 15, "Move Right", Color.WHITE),

                new BorderedTextRectangle(250, 500, 50, 50, 5, 15, "C", Mino.getMid(Mino.I)),
                new BorderedTextRectangle(75, 510, 150, 30, 5, 15, "Hold", Color.WHITE)
        };
    }

    /**
     * Draws the controls menu
     * @param gc the graphics context to draw the menu on
     */
    @Override
    public void doFrame(GraphicsContext gc) {
        //draw the instructions


        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("arial", FontWeight.BOLD, 30));

        gc.setFill(Mino.getDark(Mino.Z));
        gc.fillText("T", 450, 100);
        for (int i = 0; i < rectangles.length; i++) {
            System.out.println(i);
        }
    }
}
