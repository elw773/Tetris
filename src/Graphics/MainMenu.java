package Graphics;

import Main.Main;
import TetrisGame.Mino;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class MainMenu {
    private Button[] buttons;

    public MainMenu(){
        buttons = new Button[3];

        buttons[0] = new Button(200,350,300,100,10,40,"PLAY", Mino.getMid(Mino.S), ()->Main.getInstance().play());
        buttons[1] = new Button(200,500,300,100,10,40,"AI", Mino.getMid(Mino.I), ()->Main.getInstance().ai());
        buttons[2] = new Button(200, 650, 300, 100, 10, 40, "CONTROLS", Mino.getMid(Mino.L), ()->Main.getInstance().controls());

    }

    public void doFrame(GraphicsContext gc){
        drawTetrisLogo(162.5,50,375,gc);
        for(Button button:buttons){
            button.draw(gc);
            button.isClicled();
            button.hide();
        }

    }

    public void drawTetrisLogo(double x, double y, double width, GraphicsContext gc){
        gc.setFill(Mino.getDark(Mino.J));
        double squareSize = width/3;
        gc.fillRect(x,y,width,squareSize);
        gc.fillRect(x+squareSize, y+squareSize, squareSize, squareSize);


        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("arial", FontWeight.BOLD, squareSize));

        gc.setFill(Mino.getDark(Mino.Z));
        gc.fillText("T", x + (width/10), y + (squareSize * 7/8));

        gc.setFill(Mino.getDark(Mino.L));
        gc.fillText("E", x + (8*width/30), y + (squareSize * 7/8));

        gc.setFill(Mino.getDark(Mino.O));
        gc.fillText("T", x + (13*width/30), y + (squareSize * 7/8));

        gc.setFill(Mino.getDark(Mino.S));
        gc.fillText("R", x + (37*width/60), y + (squareSize * 7/8));

        gc.setFill(Mino.getDark(Mino.I));
        gc.fillText("I", x + (23*width/30), y + (squareSize * 7/8));

        gc.setFill(Mino.getDark(Mino.T));
        gc.fillText("S", x + (27*width/30), y + (squareSize * 7/8));
    }
}
